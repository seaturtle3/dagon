package kroryi.dagon.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class InquiryDTO {
    private String userType;
    private String receiverType;
    private String inquiryType;
    private String writer;
    private String contact;
    private String title;
    private String content;

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