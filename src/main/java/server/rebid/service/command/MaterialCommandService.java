package server.rebid.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.rebid.dto.request.MaterialRequest;
import server.rebid.dto.request.MaterialRequest.AddMaterial;
import server.rebid.dto.response.MaterialResponse;
import server.rebid.entity.Bid;
import server.rebid.entity.ItemImage;
import server.rebid.entity.Material;
import server.rebid.mapper.BidMapper;
import server.rebid.repository.MaterialRepository;

import java.util.List;

import static server.rebid.dto.response.MaterialResponse.*;

@Service
@RequiredArgsConstructor
public class MaterialCommandService {
    private final MaterialRepository materialRepository;
    private final ItemImageCommandService itemImageCommandService;

    public MaterialId addMaterial(Long memberId, AddMaterial requestDTO) {
        Material material = Material.builder()
                .description(requestDTO.getDescription())
                .title(requestDTO.getTitle())
                .build();

        List<ItemImage> itemImages = BidMapper.toItemImages(requestDTO.getImageUrl());
        itemImages.forEach(image -> image.setMaterial(material));
        material.setItemImages(itemImages);
        itemImageCommandService.saveItemImages(itemImages);
        Long materialId = materialRepository.save(material).getId();
        return MaterialId.builder().materialId(materialId).build();
    }

//    private MaterialRepository materialRepository;
//
//    public MaterialResponse.addMaterial(CustomUserDetails user, AddMaterial request){
//
//    }
}
