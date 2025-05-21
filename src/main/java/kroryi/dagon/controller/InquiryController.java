package kroryi.dagon.controller;

import kroryi.dagon.enums.InquiryType;
import kroryi.dagon.enums.WriterType;
import kroryi.dagon.enums.ReceiverType;
import kroryi.dagon.service.InquiryService;
import kroryi.dagon.entity.Inquiry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @GetMapping("/inquiry")
    public String showInquiryForm(Model model) {
        model.addAttribute("writerTypes", WriterType.values());
        model.addAttribute("inquiryTypes", InquiryType.values());
        return "question/inquiry";
    }

    @GetMapping("/inquiry/list")
    public String showInquiries(Model model) {
        List<Inquiry> all = inquiryService.findAll();

        model.addAttribute("toPartners", all.stream()
                .filter(i -> ReceiverType.PARTNER.equals(i.getReceiverType()))
                .collect(Collectors.toList()));
        model.addAttribute("toAdmins", all.stream()
                .filter(i -> ReceiverType.ADMIN.equals(i.getReceiverType()))
                .collect(Collectors.toList()));

        return "question/list";
    }
}
