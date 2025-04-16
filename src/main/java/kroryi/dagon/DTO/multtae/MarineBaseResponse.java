package kroryi.dagon.DTO.multtae;

import lombok.Data;
import java.util.List;

@Data
public class MarineBaseResponse<T> {
    private Result<T> result;

    @Data
    public static class Result<T> {
        private Meta meta;
        private List<T> data;
    }

    @Data
    public static class Meta {
        private String obs_post_id;
        private String obs_post_name;
        private String obs_lat;
        private String obs_lon;
        private String obs_last_req_cnt;
    }
}
