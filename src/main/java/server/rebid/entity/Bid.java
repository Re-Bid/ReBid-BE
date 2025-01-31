package server.rebid.entity;

import jakarta.persistence.*;
import lombok.*;
import server.rebid.entity.enums.BidStatus;
import server.rebid.entity.enums.BidType;
import server.rebid.entity.enums.ConfirmStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bid extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sell_member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String itemName;  // 상품 이름

    private String itemIntro; // 상품 소개

    private String itemDescription; // 상품 설명

    @Builder.Default
    @OneToMany(mappedBy = "bid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemImage> itemImages = new ArrayList<>();  // 상품 이미지 URL

    @Builder.Default
    @OneToMany(mappedBy = "bid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BidHistory> bidHistories = new ArrayList<>();  // 입찰 내역

    @Column(nullable = false)
    private Integer startingPrice; // 시작 가격

    @Column(unique = true, nullable = false)
    private Long bidCode;  // 제품 코드 (우리가 고유한 정수 값으로 넣어줘야 함)

    @Enumerated(EnumType.STRING)
    private ConfirmStatus confirmStatus;

    private String cancelReason;

    @Enumerated(EnumType.STRING)
    private BidStatus bidStatus;  // 입찰 상태 (입찰 완료, 유찰)

    private LocalDateTime startDate;  // 입찰 시작 시간 (관리자가 입력)

    private LocalDateTime endDate;  // 입찰 완료 시간 (관리자가 입력, 실시간일 경우는 입찰 완료 시간 없음)


    public void rejectBid(String rejectReason){
        this.cancelReason = rejectReason;
        this.confirmStatus = ConfirmStatus.REJECT_CONFIRM;
    }

    public void confirmRealTimeBid(LocalDateTime startDate){
        this.startDate = startDate;
        this.confirmStatus = ConfirmStatus.COMPLETE_CONFIRM;
    }

    public void confirmReservationBid(LocalDateTime startDate, LocalDateTime endDate){
        this.startDate = startDate;
        this.endDate = endDate;
        this.confirmStatus = ConfirmStatus.COMPLETE_CONFIRM;
    }
}
