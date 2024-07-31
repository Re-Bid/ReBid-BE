package server.rebid.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;
import server.rebid.dto.request.MaterialRequest;
import server.rebid.dto.request.MaterialRequest.AddMaterial;
import server.rebid.dto.response.MaterialResponse;
import server.rebid.entity.Bid;
import server.rebid.entity.ItemImage;
import server.rebid.entity.Material;
import server.rebid.entity.Member;
import server.rebid.mapper.BidMapper;
import server.rebid.repository.MaterialRepository;
import server.rebid.repository.MemberRepository;

import java.util.List;

import static server.rebid.dto.response.MaterialResponse.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MaterialCommandService {
    private final MaterialRepository materialRepository;
    private final ItemImageCommandService itemImageCommandService;
    private final MemberRepository memberRepository;

    public MaterialId addMaterial(Long memberId, AddMaterial requestDTO) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(GlobalErrorCode.MEMBER_NOT_FOUND));
        Material material = Material.builder()
                .description(requestDTO.getDescription())
                .title(requestDTO.getTitle())
                .member(member)
                .build();

        List<ItemImage> itemImages = BidMapper.toItemImages(requestDTO.getImageUrl());
        itemImages.forEach(image -> image.setMaterial(material));
        material.setItemImages(itemImages);
        itemImageCommandService.saveItemImages(itemImages);
        Long materialId = materialRepository.save(material).getId();
        return MaterialId.builder().materialId(materialId).build();
    }

}
