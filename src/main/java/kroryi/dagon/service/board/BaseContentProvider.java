package kroryi.dagon.service.board;

import java.util.List;
import java.util.Objects;

public abstract class BaseContentProvider<T> implements ImageContentProvider {
    public abstract String getBoardName();
    public abstract List<T> getAllEntities();
    protected abstract String extractContent(T entity);

    @Override
    public List<String> getAllContents() {
        return getAllEntities().stream()
                .map(this::extractContent)
                .filter(Objects::nonNull)
                .toList();
    }
}