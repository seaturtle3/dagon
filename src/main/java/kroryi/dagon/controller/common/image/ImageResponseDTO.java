package kroryi.dagon.controller.common.image;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageResponseDTO {
    private byte[] data;
    private String contentType;
} 