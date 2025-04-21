package kroryi.dagon.service.board;

import kroryi.dagon.entity.Notice;
import kroryi.dagon.repository.board.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
