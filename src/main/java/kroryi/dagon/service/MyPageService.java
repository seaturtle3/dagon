package kroryi.dagon.service;


import kroryi.dagon.DTO.PasswordFormDTO;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.MyPageRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MyPageRepository myPageRepository;
    private final PasswordEncoder passwordEncoder;


    public User findByUid(String uid) {
        // Java 내장 예외
        return myPageRepository.findByUid(uid)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다."));

    }

    public void updateUser(String uid, User formData) {
        User user = findByUid(uid);
        user.setUname(formData.getUname());
        user.setNickname(formData.getNickname());
        user.setPhone(formData.getPhone());
        user.setEmail(formData.getEmail());
        if (formData.getProfileImg() != null) {
            user.setProfileImg(formData.getProfileImg());
        }
        myPageRepository.save(user);
    }


    public String changePassword(String uid, PasswordFormDTO form) {
        User user = findByUid(uid);

        if (!passwordEncoder.matches(form.getCurrentPassword(), user.getUpw())) {
            return "현재 비밀번호가 올바르지 않습니다.";
        }

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            return "새 비밀번호가 일치하지 않습니다.";
        }

        user.setUpw(passwordEncoder.encode(form.getNewPassword()));
        myPageRepository.save(user);

        return "success";
    }
}



