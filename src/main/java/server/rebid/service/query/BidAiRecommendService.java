package server.rebid.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;
import server.rebid.dto.response.AitemsResponse;
import server.rebid.entity.Bid;
import server.rebid.repository.BidRepository;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BidAiRecommendService {
    private final BidRepository bidRepository;

    @Value("${clova.accessKeyId}")
    private String accessKeyId;

    @Value("${clova.secretKey}")
    private String secretKey;

    @Value("${clova.serviceId}")
    private String serviceId;

    public List<Bid> getCategoryRecommend(String type, String targetId){
        RestTemplate restTemplate = new RestTemplate();

        // URL
        String baseUrl = "https://aitems.apigw.ntruss.com";
        String plusUrl =String.format("/api/v1/services/%s/infers/lookup?type=%s&targetId=%s", serviceId, type, targetId);
        String url = baseUrl + plusUrl;
        log.info("url : {}", url);

        // 헤더 설정
        String timeStampMillis = String.valueOf(Instant.now().toEpochMilli());
        String signature = makeSignature(plusUrl, timeStampMillis);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("x-ncp-iam-access-key", accessKeyId);
        headers.set("x-ncp-apigw-signature-v2", signature);
        headers.set("x-ncp-apigw-timestamp", timeStampMillis);
        log.info("headers: {}", headers);

        // 요청 보내기
        try {
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<AitemsResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    AitemsResponse.class
            );

            // 응답 데이터 로그로 출력
            log.info("Response Body: " + response.getBody());

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getValues().stream()
                        .map(Long::valueOf)
                        .map(bidRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();
            } else {
                throw new GeneralException(GlobalErrorCode.REST_TEMPLATE_FAIL1);
            }
        } catch (Exception e) {
            log.error("Error during REST call", e);
            throw new GeneralException(GlobalErrorCode.REST_TEMPLATE_FAIL2);
        }
    }

    private String makeSignature(String plusUrl, String timeStamp) {
        String space = " ";					// one space
        String newLine = "\n";					// new line
        String method = "GET";					// method

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(plusUrl)
                .append(newLine)
                .append(timeStamp)
                .append(newLine)
                .append(accessKeyId)
                .toString();

        try {
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            String encodeBase64String = Base64.encodeBase64String(rawHmac);

            return encodeBase64String;
        } catch (Exception e){
            log.error("Error during Secret KEY", e);
            throw new GeneralException(GlobalErrorCode.FEIGN_CLIENT_ERROR_500);
        }

    }
}
