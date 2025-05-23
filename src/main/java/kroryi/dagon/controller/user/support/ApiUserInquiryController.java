package kroryi.dagon.controller.user.support;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.InquiryDTO;
import kroryi.dagon.entity.Inquiry;
import kroryi.dagon.enums.InquiryType;
import kroryi.dagon.enums.ReceiverType;
import kroryi.dagon.enums.WriterType;
import kroryi.dagon.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Tag(name = "User-Inquiry", description = "1:1 문의 API (사용자)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiry")
public class ApiUserInquiryController {

    private final InquiryRepository inquiryRepository;

    @Operation(summary = "문의 등록")
    @PostMapping
    public Map<String, Object> submitInquiry(@RequestBody InquiryDTO dto) {
        Map<String, Object> response = new HashMap<>();
        try {
            Inquiry entity = new Inquiry();
            entity.setWriterType(WriterType.valueOf(dto.getUserType()));
            entity.setReceiverType(ReceiverType.valueOf(dto.getReceiverType()));
            entity.setInquiryType(InquiryType.valueOf(dto.getInquiryType()));
            entity.setWriter(dto.getWriter());
            entity.setContact(dto.getContact());
            entity.setTitle(dto.getTitle());
            entity.setContent(dto.getContent());
            entity.setCreatedAt(LocalDateTime.now());
            entity.setUpdatedAt(LocalDateTime.now());

            inquiryRepository.save(entity);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
        }
        return response;
    }

    @Operation(summary = "문의 수정")
    @PutMapping("/{id}")
    public Map<String, Object> updateInquiry(@PathVariable Long id, @RequestBody InquiryDTO dto) {
        Map<String, Object> response = new HashMap<>();
        Optional<Inquiry> opt = inquiryRepository.findById(id);
        if (opt.isPresent()) {
            Inquiry inquiry = opt.get();
            inquiry.setTitle(dto.getTitle());
            inquiry.setContent(dto.getContent());
            inquiry.setContact(dto.getContact());
            inquiry.setInquiryType(InquiryType.valueOf(dto.getInquiryType()));
            inquiry.setUpdatedAt(LocalDateTime.now());

            inquiryRepository.save(inquiry);
            response.put("success", true);
        } else {
            response.put("success", false);
            response.put("message", "존재하지 않는 ID입니다.");
        }
        return response;
    }

    @Operation(summary = "문의 삭제")
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteInquiry(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        if (inquiryRepository.existsById(id)) {
            inquiryRepository.deleteById(id);
            response.put("success", true);
        } else {
            response.put("success", false);
            response.put("message", "존재하지 않는 ID입니다.");
        }
        return response;
    }
}
