package server.rebid.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import server.rebid.common.CommonResponse;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;
import server.rebid.dto.request.ChatRequest;
import server.rebid.dto.response.ChatMemberResponse;
import server.rebid.dto.response.ChatResponse;
import server.rebid.entity.Bid;
import server.rebid.entity.Category;
import server.rebid.entity.enums.ConfirmStatus;
import server.rebid.entity.enums.MemberRole;
import server.rebid.repository.BidRepository;
import server.rebid.repository.CategoryRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BidQueryService {

    private final BidRepository bidRepository;
    private final CategoryRepository categoryRepository;
    private final BidAiRecommendService bidAiRecommendService;

    public List<Bid> findAll() {
        return bidRepository.findActiveBids(LocalDateTime.now());
    }

    public List<Bid> getImminentBids() {
        return bidRepository.findImminentBids(LocalDateTime.now());
    }

    public List<Bid> getBidsByCategory(String categoryName) {
        return bidRepository.findBidsByCategory(LocalDateTime.now(), categoryName);
    }

    public Bid findById(Long bidId) {
        return bidRepository.findById(bidId).orElseThrow(() -> new GeneralException(GlobalErrorCode.BID_NOT_FOUND));
    }

    public List<Bid> getBidsByStatus(Integer page, Integer size, String status, MemberRole role) {
        checkAdmin(role);
        ConfirmStatus confirmStatus;
        if(status.equals("pending")){
            confirmStatus = ConfirmStatus.PENDING_CONFIRM;
        } else if (status.equals("confirm")) {
            confirmStatus = ConfirmStatus.COMPLETE_CONFIRM;
        } else {
            throw new GeneralException(GlobalErrorCode.BID_KEYWORD_NOT_FOUND);
        }
        return bidRepository.getBidsByStatus(page, size, confirmStatus);
    }

    public Bid getBidForAdmin(Long bidId, MemberRole role) {
        checkAdmin(role);
        return bidRepository.findById(bidId).orElseThrow(() -> new GeneralException(GlobalErrorCode.BID_NOT_FOUND));
    }

    private void checkAdmin(MemberRole role){
        if(!role.equals(MemberRole.ROLE_ADMIN)){
            throw new GeneralException(GlobalErrorCode.AUTHENTICATION_DENIED);
        }
    }

    public List<Bid> getMemberSales(Long memberId) {
        return bidRepository.getMemberSales(memberId);
    }

    public List<Bid> getCategoryRecommend(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new GeneralException(GlobalErrorCode.CATEGORY_NOT_FOUND));
        String type = "pop";
        String targetId = category.getId().toString();
        return bidAiRecommendService.getCategoryRecommend(type, targetId);
    }
}
