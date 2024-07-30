package server.rebid.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;
import server.rebid.dto.request.AdminBidRequest.ConfirmRealTimeBid;
import server.rebid.dto.request.AdminBidRequest.ConfirmReservationBid;
import server.rebid.entity.Bid;
import server.rebid.entity.enums.BidType;
import server.rebid.entity.enums.ConfirmStatus;
import server.rebid.entity.enums.MemberRole;
import server.rebid.repository.BidRepository;

@Service
@RequiredArgsConstructor
public class BidCommandService {

    private final BidRepository bidRepository;

    public Bid addBid(Bid bid) {
        return bidRepository.save(bid);
    }

    public Long rejectBid(Long bidId, MemberRole role, String rejectReason){
        isAdmin(role);
        Bid bid = checkBidPending(bidId);
//        if(!bid.getBidType().equals(BidType.REAL_TIME)){
//            throw new GeneralException(GlobalErrorCode.BID_NOT_REAL_TIME);
//        }
        bid.rejectBid(rejectReason);
        return bid.getId();
    }

    public Long confirmRealTime(Long bidId, MemberRole role, ConfirmRealTimeBid requestDTO) {
        isAdmin(role);
        Bid bid = checkBidPending(bidId);
//        if(!bid.getBidType().equals(BidType.REAL_TIME)){
//            throw new GeneralException(GlobalErrorCode.BID_NOT_REAL_TIME);
//        }
        bid.confirmRealTimeBid(requestDTO.getStartDate());
        return bid.getId();
    }

    public Long confirmReservationBid(Long bidId, MemberRole role, ConfirmReservationBid requestDTO) {
        isAdmin(role);
        Bid bid = checkBidPending(bidId);
//        if(!bid.getBidType().equals(BidType.RESERVATION)){
//            throw new GeneralException(GlobalErrorCode.BID_NOT_RESERVATION);
//        }
        bid.confirmReservationBid(requestDTO.getStartDate(), requestDTO.getEndDate());
        return bid.getId();
    }

    private void isAdmin(MemberRole role){
        if(!role.equals(MemberRole.ROLE_ADMIN)){
            throw new GeneralException(GlobalErrorCode.AUTHENTICATION_DENIED);
        }
    }

    private Bid checkBidPending(Long bidId){
        Bid bid = bidRepository.findById(bidId).orElseThrow(() -> new GeneralException(GlobalErrorCode.BID_NOT_FOUND));
        if(bid.getConfirmStatus().equals(ConfirmStatus.COMPLETE_CONFIRM)){
            throw new GeneralException(GlobalErrorCode.BID_ALREADY_CONFIRM);
        } else if(bid.getConfirmStatus().equals(ConfirmStatus.REJECT_CONFIRM)){
            throw new GeneralException(GlobalErrorCode.BID_ALREADY_REJECT);
        }
        return bid;
    }
}
