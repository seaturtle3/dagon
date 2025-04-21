package kroryi.dagon.DTO.multtae;

import lombok.Data;

import java.util.List;

@Data
public class MarineBaseResponse<T> {
    private Result<T> result;

    @Data
    public static class Result<T> {
        private List<T> data;
    }
}
