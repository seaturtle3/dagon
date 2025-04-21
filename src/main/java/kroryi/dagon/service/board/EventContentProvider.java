package kroryi.dagon.service.board;

import kroryi.dagon.entity.Event;
import kroryi.dagon.repository.board.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventContentProvider extends BaseContentProvider<Event> {

    private final EventRepository eventRepository;

    @Override
    public String getBoardName() {
        return "이벤트";
    }

    @Override
    public List<Event> getAllEntities() {
        return eventRepository.findAll();
    }

    @Override
    protected String extractContent(Event entity) {
        return entity.getContent();
    }
}
