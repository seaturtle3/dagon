package kroryi.dagon.service;

import kroryi.dagon.DTO.PasswordFormDTO;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.MyPageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
@Service
@RequiredArgsConstructor
@Log4j2
public class MyPageService {

    private final MyPageRepository myPageRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    // 내 정보 조회
    public UsersDTO findUserInfo(Long uno) {
        User user = myPageRepository.findByUno(uno)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다."));

        return new UsersDTO(user);  // User 엔티티를 UsersDTO로 변환하여 반환
    }

    public User findByUno(Long uno) {
        return myPageRepository.findByUno(uno)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다."));
    }

    // 내 정보 수정
    public void updateUser(Long uno, User formData) {
        User user = myPageRepository.findByUno(uno)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다."));

        user.setUname(formData.getUname());
        user.setNickname(formData.getNickname());
        user.setPhone(formData.getPhone());
        user.setEmail(formData.getEmail());

        // 이미지 경로만 저장 (파일은 이미 저장된 상태)
        if (formData.getProfileImg() != null) {
            user.setProfileImg(formData.getProfileImg());
        }

        myPageRepository.save(user);  // 업데이트된 사용자 정보 저장
    }
    // 비밀번호 변경
    public String changePassword(Long uno, PasswordFormDTO form) {
        log.info("입력된 현재 비밀번호: " + form.getCurrentPassword());

        User user = myPageRepository.findByUno(uno)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(form.getCurrentPassword(), user.getUpw())) {
            return "fail"; // 비밀번호가 틀림
        }

        // 새 비밀번호로 업데이트
        user.setUpw(passwordEncoder.encode(form.getNewPassword()));
        myPageRepository.save(user);

        return "success";
    }


}


