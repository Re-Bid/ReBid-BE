package server.rebid.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;
import server.rebid.entity.Category;
import server.rebid.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryQueryService {

    private final CategoryRepository categoryRepository;

    public Category findByName(String category) {
        return categoryRepository.findByName(category).orElseThrow(() -> new GeneralException(GlobalErrorCode.CATEGORY_NOT_FOUND));
    }
}
