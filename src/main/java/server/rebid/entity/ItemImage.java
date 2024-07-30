package server.rebid.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @JoinColumn(name = "bid_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    private Bid bid;
}
