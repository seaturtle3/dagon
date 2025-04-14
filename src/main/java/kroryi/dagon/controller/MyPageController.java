package kroryi.dagon.controller;


import jakarta.servlet.http.HttpSession;
import kroryi.dagon.DTO.PasswordFormDTO;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.entity.User;
import kroryi.dagon.service.FileStorageService;
import kroryi.dagon.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final FileStorageService fileStorageService;

    @GetMapping("")
    public String myPage(HttpSession session, Model model) {
        UsersDTO user = (UsersDTO) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "user/my_page";
    }

    // 회원 정보 수정
    @PostMapping("/update")
    public String updateUser(@RequestParam String uname,
                             @RequestParam String nickname,
                             @RequestParam String email,
                             @RequestParam String phone1,
                             @RequestParam String phone2,
                             @RequestParam String phone3,
                             @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
                             HttpSession session) {

        String uid = (String) session.getAttribute("uid");
        if (uid == null) return "redirect:/login";

        String fullPhone = phone1 + "-" + phone2 + "-" + phone3;

        //  기존 사용자 불러오기
        User user = myPageService.findByUid(uid);

        //  form으로부터 받은 값으로 설정
        user.setUname(uname);
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPhone(fullPhone);

        //  이미지 업로드 처리
        if (profileImage != null && !profileImage.isEmpty()) {
            String imagePath = fileStorageService.store(profileImage);
            user.setProfileImg(imagePath);
        }

        myPageService.updateUser(uid, user);

        //  세션 갱신
        session.setAttribute("loginUser", new UsersDTO(user));

        return "redirect:/mypage";
    }

    @PostMapping("/password")
    public String changePassword(@ModelAttribute PasswordFormDTO passwordForm,
                                 HttpSession session,
                                 Model model) {

        UsersDTO user = (UsersDTO) session.getAttribute("loginUser");
        if (user == null) return "redirect:/login";

        String result = myPageService.changePassword(user.getUid(), passwordForm);

        if ("success".equals(result)) {
            model.addAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
        } else {
            model.addAttribute("errorMessage", result);
        }

        model.addAttribute("user", user); // 다시 렌더링 시 필요
        return "user/my_page";
    }
}




