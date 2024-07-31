package server.rebid.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;
import server.rebid.dto.response.MaterialResponse;
import server.rebid.dto.response.MaterialResponse.GetTotalMaterial;
import server.rebid.entity.Comment;
import server.rebid.entity.Material;
import server.rebid.mapper.MaterialMapper;
import server.rebid.repository.CommentCustomRepository;
import server.rebid.repository.MaterialQueryRepository;
import server.rebid.repository.MaterialRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MaterialQueryService {
    private final MaterialRepository materialRepository;
    private final MaterialQueryRepository materialQueryRepository;
    private final CommentCustomRepository commentCustomRepository;

    public GetTotalMaterial getTotalMaterial() {
        List<Material> materials = materialQueryRepository.getTotalMaterial();
        return MaterialMapper.toGetTotalMaterial(materials);
    }

    public MaterialResponse.GetMaterial getOneMaterial(Long materialId) {
        Material material = materialRepository.findById(materialId).orElseThrow(() -> new GeneralException(GlobalErrorCode.MATERIAL_NOT_FOUND));
        List<Comment> comments = commentCustomRepository.getComments(materialId);
        return MaterialMapper.toGetMaterial(material, comments);
    }
}
