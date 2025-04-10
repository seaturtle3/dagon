package kroryi.dagon.DTO;

import lombok.Data;

import java.util.List;

@Data
public class TideInfoDTO {
    private Result result;

    @Data
    public static class Result{
        private List<TideItemDTO> data;
    }

}
