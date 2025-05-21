package kroryi.dagon.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InquiryDTO {

    // 작성자 유형 (MEMBER, PARTNER 등)
    @Schema(description = "작성자 유형 (MEMBER, PARTNER)", example = "MEMBER")
    private String userType;

    // 받는 대상 (ADMIN, PARTNER 등)
    @Schema(description = "받는 사람 유형 (ADMIN, PARTNER)", example = "ADMIN")
    private String receiverType;

    // 문의 유형 (상품문의, 시스템문의 등)
    @Schema(description = "문의 유형 (PRODUCT, GENERAL)", example = "GENERAL")
    private String inquiryType;

    // 작성자 이름
    @Schema(description = "작성자 이름", example = "홍길동")
    private String writer;

    // 연락처
    @Schema(description = "연락처", example = "010-1234-5678")
    private String contact;

    // 문의 제목
    @Schema(description = "문의 제목", example = "상품 관련 문의")
    private String title;

    // 문의 내용
    @Schema(description = "문의 내용", example = "상품 가격에 대해 문의드립니다.")
    private String content;

    // 클라이언트가 보낼 필요 없음, 서버에서 관리
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

//    // Getters and setters
//    public String getWriterType() { return userType; }
//    public void setWriterType(String writerType) { this.userType = writerType; }
//
//    public String getInquiryType() { return inquiryType; }
//    public void setInquiryType(String inquiryType) { this.inquiryType = inquiryType; }
//
//    public String getWriter() { return writer; }
//    public void setWriter(String writer) { this.writer = writer; }
//
//    public String getContact() { return contact; }
//    public void setContact(String contact) { this.contact = contact; }
//
//    public String getTitle() { return title; }
//    public void setTitle(String title) { this.title = title; }
//
//    public String getContent() { return content; }
//    public void setContent(String content) { this.content = content; }
}