package kroryi.dagon.DTO.multtae;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TideInfoDTO {
    private Result result;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result{
        private List<TideItemDTO> data;
    }

}
