package kroryi.dagon.service.pages.user;

import kroryi.dagon.DTO.PasswordFormDTO;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.entity.User;
import kroryi.dagon.enums.UserLevel;
import kroryi.dagon.repository.MyPageRepository;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.service.image.FileStorageService;
import kroryi.dagon.util.LevelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
@Service
@RequiredArgsConstructor
@Log4j2
public class MyPageService {

    private final UserRepository userRepository;
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

    public UsersDTO findUserInfoByUname(String uname) {
        User user = myPageRepository.findByUname(uname)
                .orElseThrow(() -> new NoSuchElementException("해당 이름의 사용자를 찾을 수 없습니다."));

        return new UsersDTO(user);
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


    public String changePasswords(String uid, PasswordFormDTO form) {
        User user = myPageRepository.findByUid(uid)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(form.getCurrentPassword(), user.getUpw())) {
            return "fail";
        }

        user.setUpw(passwordEncoder.encode(form.getNewPassword()));
        myPageRepository.save(user);

        return "success";
    }

// MyPageService.java 안에 아래 메서드를 추가
public Map<String, Object> getUserPointAndLevel(Long uno) {
    Integer point = userRepository.findPointsByUno(String.valueOf(uno)); // 기존 포인트
    UserLevel level = LevelUtil.calculateLevel(point); // 레벨 계산

    Map<String, Object> result = new HashMap<>();
    result.put("point", point);
    result.put("level", level.name());
    result.put("levelKorean", level.getKorean());

    int nextThreshold = LevelUtil.getNextLevelThreshold(level);
    if (nextThreshold != -1) {
        result.put("nextLevelPoint", nextThreshold);
        result.put("pointToNextLevel", nextThreshold - point);
    } else {
        result.put("nextLevelPoint", null);
        result.put("pointToNextLevel", 0);
    }

    return result;
}


    public boolean verifyPassword(Long uno, String inputPassword) {
        User user = userRepository.findByUno(uno)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return passwordEncoder.matches(inputPassword, user.getUpw());
    }

    public String deleteUserAccount(Long uno) {
        try {
            userRepository.deleteById(uno);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }
}


