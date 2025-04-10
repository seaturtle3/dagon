package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link kroryi.dagon.entity.UserAction}
 */
@Value
public class UserActionDTO implements Serializable {
    Long id;
    String boardType;
    String actionType;
    Integer views;
}