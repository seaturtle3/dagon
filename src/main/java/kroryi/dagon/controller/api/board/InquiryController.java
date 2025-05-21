package kroryi.dagon.controller.api.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.InquiryDTO;
import kroryi.dagon.entity.Inquiry;
import kroryi.dagon.enums.InquiryType;
import kroryi.dagon.enums.ReceiverType;
import kroryi.dagon.enums.WriterType;
import kroryi.dagon.repository.InquiryRepository;
import kroryi.dagon.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "1:1 문의 API", description = "문의 작성 및 목록 조회 API")
public class InquiryController {

    private final InquiryService inquiryService;
    private final InquiryRepository inquiryRepository;

    @Operation(summary = "1:1 문의 작성", description = "사용자가 관리자 또는 파트너에게 1:1 문의를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "문의 등록 성공",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "입력값 오류"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @PostMapping("/inquiry")
    public Map<String, Object> submitInquiry(@RequestBody InquiryDTO inquiry) {
        Map<String, Object> response = new HashMap<>();
        try {
            Inquiry entity = new Inquiry();
            entity.setWriterType(WriterType.valueOf(inquiry.getUserType()));
            entity.setReceiverType(ReceiverType.valueOf(inquiry.getReceiverType()));
            entity.setInquiryType(InquiryType.valueOf(inquiry.getInquiryType()));
            entity.setWriter(inquiry.getWriter());
            entity.setContact(inquiry.getContact());
            entity.setTitle(inquiry.getTitle());
            entity.setContent(inquiry.getContent());
            inquiryRepository.save(entity);

            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    @Operation(summary = "문의 목록 조회", description = "전체 1:1 문의를 조회합니다. 관리자/파트너 구분")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Inquiry.class))))
    })
    @GetMapping("/inquiry/list")
    public Map<String, List<Inquiry>> getInquiries() {
        List<Inquiry> all = inquiryService.findAll();

        List<Inquiry> toPartners = all.stream()
                .filter(i -> ReceiverType.PARTNER.equals(i.getReceiverType()))
                .collect(Collectors.toList());

        List<Inquiry> toAdmins = all.stream()
                .filter(i -> ReceiverType.ADMIN.equals(i.getReceiverType()))
                .collect(Collectors.toList());

        Map<String, List<Inquiry>> result = new HashMap<>();
        result.put("toPartners", toPartners);
        result.put("toAdmins", toAdmins);

        return result;
    }

    public Map<String, Object> updateInquiry(@PathVariable Long id, @RequestBody InquiryDTO dto) {
        Map<String, Object> response = new HashMap<>();
        Optional<Inquiry> optionalInquiry = inquiryRepository.findById(id);

        if (optionalInquiry.isPresent()) {
            Inquiry inquiry = optionalInquiry.get();
            inquiry.setTitle(dto.getTitle());
            inquiry.setContent(dto.getContent());
            inquiry.setContact(dto.getContact());
            inquiry.setInquiryType(InquiryType.valueOf(dto.getInquiryType()));
            inquiry.setUpdatedAt(LocalDateTime.now());

            inquiryRepository.save(inquiry);
            response.put("success", true);
        } else {
            response.put("success", false);
            response.put("message", "해당 ID의 문의가 존재하지 않습니다.");
        }

        return response;
    }

    public Map<String, Object> deleteInquiry(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<Inquiry> optionalInquiry = inquiryRepository.findById(id);

        if (optionalInquiry.isPresent()) {
            inquiryRepository.deleteById(id);
            response.put("success", true);
        } else {
            response.put("success", false);
            response.put("message", "해당 ID의 문의가 존재하지 않습니다.");
        }

        return response;
    }
}
