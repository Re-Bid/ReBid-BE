package server.rebid.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import server.rebid.common.CommonResponse;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;
import server.rebid.dto.request.ChatRequest;
import server.rebid.dto.request.ChatType;
import server.rebid.dto.response.ChatMemberResponse;
import server.rebid.dto.response.ChatResponse;
import server.rebid.entity.Bid;
import server.rebid.entity.BidHistory;
import server.rebid.repository.BidHistoryRepository;
import org.springframework.http.HttpHeaders;
import server.rebid.repository.BidRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidHistoryCommandService {

    private final BidHistoryRepository bidHistoryRepository;
    private final BidRepository bidRepository;
    private final RestTemplate restTemplate;

    @Value("${clova.clovastudio}")
    private String clovaStudio;

    @Value("{clova.apigw}")
    private String APIGW;

    public BidHistory addBidHistory(BidHistory bidHistory) {
        return bidHistoryRepository.save(bidHistory);
    }

    public CommonResponse<ChatMemberResponse> aiRecommendNextPrice(Long bidId) {
        Bid bid = bidRepository.findById(bidId).orElseThrow(() -> new GeneralException(GlobalErrorCode.BID_NOT_FOUND));
        List<BidHistory> bidHistories = bidHistoryRepository.getBidHistory(bidId);
        String maxPrice = bidHistories.size() > 0 ? bidHistories.get(bidHistories.size()-1).getPrice().toString() : bid.getStartingPrice().toString();
        String priceHistory = bidHistories.size() > 0 ? bidHistories.stream()
                .map(bidHistory -> String.valueOf(bidHistory.getPrice()))
                .collect(Collectors.joining(" -> ")): "없음";

        ChatType role = ChatType.valueOf("system");
        String message = "안녕! 너는 온라인 경매사야. 채팅의 시작 메시지는 \"안녕하세요! 저는 당신의 경매를 도와줄 [안녕맨] 입니다!\"로 시작해줘. 너는 사용자에게 제품을 사기 위해 다음 경매 가격을 추천해주는 역할을 가지고 있어." +
                "너는 \"경매 제품은 " + bid.getItemName() +" 이고, 현재 경매 최고가는 " +  maxPrice + "입니다\"로 말을 하고 경매를 시작해." +
                "너에게는 지금까지 제품의 경매 가격 내역이 주어질꺼야. 만약 경매 내역이 없으면 \"경매 내역이 없습니다. 경매에 참여해보세요!\"라고 작성해줘." +
                "만약 경매 가격 내역이 주어지면, 가격의 상승폭을 바탕으로 사용자에 다음 경매 가격으로 얼마가 좋을지 추천해줘."
                + "현재 경매 가격 내역 : " + priceHistory;

        ChatRequest.ChatMessage chatRequest = ChatRequest.ChatMessage.builder()
                .role(role)
                .content(message)
                .build();
        ChatRequest request = ChatRequest.builder()
                .messages((List<ChatRequest.ChatMessage>) Collections.singleton(chatRequest))
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("X-NCP-CLOVASTUDIO-API-KEY", clovaStudio);
        headers.add("X-NCP-APIGW-API-KEY", APIGW);

        String url = "https://clovastudio.stream.ntruss.com/testapp/v1/chat-completions/HCX-003";

        // 요청 보내기
        HttpEntity<ChatRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ChatResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                ChatResponse.class
        );

        ChatMemberResponse postResponse = ChatMemberResponse.builder()
                .message(Objects.requireNonNull(response.getBody()).getResult().getMessage().getContent())
                .build();
        if(response.getStatusCode().is2xxSuccessful()){
            return CommonResponse.onSuccess(postResponse);
        } else{
                throw new GeneralException(GlobalErrorCode.SERVER_ERROR);
        }
    }
}
