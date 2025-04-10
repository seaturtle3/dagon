package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link kroryi.dagon.entity.Admin}
 */
@Value
public class AdminDTO implements Serializable {
    Long aid;
    String apw;
    String aname;
}