package kroryi.dagon.service.community;

import kroryi.dagon.entity.Event;
import kroryi.dagon.repository.board.EventRepository;
import kroryi.dagon.service.BaseContentProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
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
