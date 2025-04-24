package kroryi.dagon.controller;

import kroryi.dagon.DTO.InquiryDTO;
import kroryi.dagon.entity.Inquiry;
import kroryi.dagon.enums.InquiryType;
import kroryi.dagon.enums.ReceiverType;
import kroryi.dagon.enums.WriterType;
import kroryi.dagon.repository.InquiryRepository;
import kroryi.dagon.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;
    private final InquiryRepository inquiryRepository;


    @GetMapping("/inquiry")
    public String showInquiryForm(Model model) {
        // InquiryDTO나 관련 데이터를 모델에 추가할 수 있습니다.
        model.addAttribute("writerTypes", WriterType.values());
        model.addAttribute("inquiryTypes", InquiryType.values());

        // inquiry.html을 반환하여 해당 페이지를 렌더링
        return "question/inquiry"; // 여기서 "inquiry"는 inquiry.html을 의미합니다.
    }


    @PostMapping("/inquiry")
    @ResponseBody
    public Map<String, Object> submitInquiry(@RequestBody InquiryDTO inquiry) {
        Map<String, Object> response = new HashMap<>();
        try {
            // DTO → Entity
            Inquiry entity = new Inquiry();
            entity.setWriterType(WriterType.valueOf(inquiry.getUserType()));
            entity.setReceiverType(ReceiverType.valueOf(inquiry.getReceiverType()));
            entity.setInquiryType(InquiryType.valueOf(inquiry.getInquiryType()));
            entity.setWriter(inquiry.getWriter());
            entity.setContact(inquiry.getContact());
            entity.setTitle(inquiry.getTitle());
            entity.setContent(inquiry.getContent());

            // 저장
            inquiryRepository.save(entity);

            response.put("success", true);
        } catch (Exception e) {
            e.printStackTrace(); // 에러 확인
            response.put("success", false);
        }


        return response;
    }

//    @PostMapping("/inquiry")
//    public String submitInquiry(@Valid @ModelAttribute Inquiry inquiry, BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            model.addAttribute("inquiryTypes", InquiryType.values());
//            model.addAttribute("writerTypes", WriterType.values());
//            return "question/inquiry";
//        }
//        inquiryRepository.save(inquiry);
//        return "redirect:/inquiry-list";
//    }
//
//    @PostMapping("/inquiry")
//    @ResponseBody
//    public Map<String, Object> submitInquiry(@RequestBody InquiryDTO inquiry) {
//        // 문의 저장 처리 로직...
//        Map<String, Object> response = new HashMap<>();
//        try {
//            // 예: DB 저장 로직
//            System.out.println("문의 등록: " + inquiry.getTitle());
//
//            response.put("success", true);
//        } catch (Exception e) {
//            response.put("success", false);
//        }
//
//        return response;
//    }

    @GetMapping("/inquiry/list")
    public String showInquiries(Model model) {
        List<Inquiry> all = inquiryService.findAll();

        // 안전한 Enum 비교
        List<Inquiry> toPartners = all.stream()
                .filter(i -> ReceiverType.PARTNER.equals(i.getReceiverType()))
                .collect(Collectors.toList());

        List<Inquiry> toAdmins = all.stream()
                .filter(i -> ReceiverType.ADMIN.equals(i.getReceiverType()))
                .collect(Collectors.toList());

        // 로그로 값 확인
        System.out.println("=== 전체 문의 수: " + all.size() + " ===");
        toPartners.forEach(i -> System.out.println("→ [PARTNER] " + i.getTitle()));
        toAdmins.forEach(i -> System.out.println("→ [ADMIN] " + i.getTitle()));

        model.addAttribute("toPartners", toPartners);
        model.addAttribute("toAdmins", toAdmins);
        return "question/list";
    }
}
