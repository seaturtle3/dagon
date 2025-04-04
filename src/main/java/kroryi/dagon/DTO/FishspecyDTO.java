package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link kroryi.dagon.entity.Fishspecy}
 */
@Value
public class FishspecyDTO implements Serializable {
    Long id;
    String finame;
    String fiiconUrl;
}