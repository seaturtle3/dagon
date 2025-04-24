package kroryi.dagon.service.board;

import kroryi.dagon.entity.FAQ;
import kroryi.dagon.repository.board.FAQRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FAQContentProvider extends BaseContentProvider<FAQ> {

    private final FAQRepository faqRepository;

    @Override
    public String getBoardName() {
        return "FAQ";
    }

    @Override
    public List<FAQ> getAllEntities() {
        return faqRepository.findAll();
    }

    @Override
    protected String extractContent(FAQ entity) {
        return entity.getAnswer();
    }
}
