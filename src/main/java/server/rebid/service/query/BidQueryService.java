package server.rebid.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;
import server.rebid.entity.Bid;
import server.rebid.entity.Category;
import server.rebid.entity.Member;
import server.rebid.entity.enums.ConfirmStatus;
import server.rebid.entity.enums.MemberRole;
import server.rebid.repository.BidRepository;
import server.rebid.repository.CategoryRepository;
import server.rebid.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BidQueryService {

    private final BidRepository bidRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
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
        return bidAiRecommendService.getAitemsRecommend(type, targetId);
    }

    public List<Bid> getPersonalRecommend(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(GlobalErrorCode.MEMBER_NOT_FOUND));
        String type = "personalRecommend";
        String targetId = memberId.toString();
        return bidAiRecommendService.getAitemsRecommend(type, targetId);
    }
}
