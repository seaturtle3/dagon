package kroryi.dagon.DTO.board;

import kroryi.dagon.entity.Event;
import kroryi.dagon.entity.EventImage;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class EventResponseDTO {
    private Long eventId;
    private String title;
    private String content;
    private String thumbnailUrl;
    private LocalDate startAt;
    private LocalDate endAt;
    private LocalDateTime createdAt;
    private LocalDateTime modifyAt;
    private String eventStatus;
    private Integer views;
    private Boolean isTop;
    private String adminName;
    private List<byte[]> imageDataList;
    private List<byte[]> thumbnailDataList;
    private List<String> imageUrlList;
    private List<Long> imageIdList;

    public static EventResponseDTO from(Event e) {
        // 썸네일 이미지만 필터링 (isThumbnail이 true인 것만)
        List<EventImage> thumbnailImages = e.getImages().stream()
                .filter(EventImage::getIsThumbnail)
                .collect(Collectors.toList());
        
        // 이미지 데이터 추출 (썸네일만)
        List<byte[]> imageDataList = thumbnailImages.stream()
                .map(EventImage::getImageData)
                .collect(Collectors.toList());
        
        List<byte[]> thumbnailDataList = thumbnailImages.stream()
                .map(EventImage::getThumbnailData)
                .collect(Collectors.toList());
        
        List<String> imageUrlList = thumbnailImages.stream()
                .map(EventImage::getImageUrl)
                .collect(Collectors.toList());

        List<Long> imageIdList = thumbnailImages.stream()
                .map(EventImage::getId)
                .collect(Collectors.toList());

        return EventResponseDTO.builder()
                .eventId(e.getEventId())
                .title(e.getTitle())
                .content(e.getContent())
                .thumbnailUrl(e.getThumbnailUrl())
                .startAt(e.getStartAt())
                .endAt(e.getEndAt())
                .createdAt(e.getCreatedAt())
                .modifyAt(e.getModifyAt())
                .eventStatus(
                        e.getEventStatus() != null ? e.getEventStatus().getKorean() : "미정"
                )
                .views(e.getViews())
                .isTop(e.getIsTop())
                .adminName(
                        e.getAdmin() != null ? e.getAdmin().getAname() : "미지정 관리자"
                )
                .imageDataList(imageDataList)
                .thumbnailDataList(thumbnailDataList)
                .imageUrlList(imageUrlList)
                .imageIdList(imageIdList)
                .build();
    }

    public void setImageDataList(List<byte[]> imageDataList) {
        this.imageDataList = imageDataList;
    }

    public void setThumbnailDataList(List<byte[]> thumbnailDataList) {
        this.thumbnailDataList = thumbnailDataList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public void setImageIdList(List<Long> imageIdList) {
        this.imageIdList = imageIdList;
    }
}
