package kroryi.dagon.service.support;

import kroryi.dagon.entity.Notice;
import kroryi.dagon.repository.board.NoticeRepository;
import kroryi.dagon.service.BaseContentProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NoticeContentProvider extends BaseContentProvider<Notice> {
    private final NoticeRepository noticeRepository;

    @Override
    public String getBoardName(){
        return "공지사항";
    }

    @Override
    public List<Notice> getAllEntities() {
        return noticeRepository.findAll();
    }

    @Override
    protected String extractContent(Notice entity) {
        return entity.getContent();
    }
}
