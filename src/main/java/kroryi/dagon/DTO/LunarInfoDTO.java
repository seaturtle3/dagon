package kroryi.dagon.DTO;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.List;

@Data@XmlRootElement(name ="response")
@XmlAccessorType(XmlAccessType.FIELD)
public class LunarInfoDTO {

    @XmlElement(name = "body")
    private Body body;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Body {
        @XmlElementWrapper(name = "items")
        @XmlElement(name = "item")
        private List<LunarItemDTO> items;
    }
}
