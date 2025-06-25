package kroryi.dagon.service.community.fishingCenter;

import kroryi.dagon.entity.fishingCenter.TempImage;
import kroryi.dagon.repository.board.TempImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TempImageService {
    private final TempImageRepository tempImageRepository;

    @Transactional
    public Long save(byte[] data, String contentType) {
        TempImage tempImage = new TempImage();
        tempImage.setData(data);
        tempImage.setContentType(contentType);
        tempImageRepository.save(tempImage);
        return tempImage.getId();
    }

    public TempImage findById(Long id) {
        return tempImageRepository.findById(id).orElse(null);
    }

    @Transactional
    public void delete(Long id) {
        tempImageRepository.deleteById(id);
    }
} 