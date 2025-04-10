package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link kroryi.dagon.entity.QuestionCategory}
 */
@Value
public class QuestionCategoryDTO implements Serializable {
    Long id;
    String categoryType;
    String qcname;
}