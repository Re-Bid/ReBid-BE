package server.rebid.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.rebid.entity.ItemImage;
import server.rebid.repository.ItemImageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemImageCommandService {

    private final ItemImageRepository itemImageRepository;

    public void saveItemImages(List<ItemImage> itemImages) {
        itemImageRepository.saveAll(itemImages);
    }
}
