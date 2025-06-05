package kroryi.dagon.service.auth;

import jakarta.transaction.Transactional;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.ReportRepository;
import kroryi.dagon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;

    public UsersDTO login(String uid, String upw) {
        System.out.println("로그인 시도: " + uid);

        Optional<User> optionalUser = userRepository.findByUid(uid);

        if (optionalUser.isEmpty()) {
            System.out.println("사용자를 찾을 수 없습니다");
            return null;
        }

        User user = optionalUser.get();



        // 비활성화된 계정 체크
        if (!user.isActive()) {
            System.out.println("계정이 비활성화되었습니다.");
            return null;  // 비활성화된 계정은 로그인 실패 처리
        }


        // 비밀번호 검증
        if (user.getUpw().equals(upw)) {
            Long uno = user.getUno();    // 유저 번호 가져오기
            String uname = user.getUname();  // 유저 이름 가져오기
            System.out.println("로그인 성공: " + uname + " (유저 번호: " + uno + ")");
            // 로그인 성공 시 DTO로 변환하여 반환
            return new UsersDTO(user);
        }

        System.out.println("비밀번호가 일치하지 않습니다");
        return null;
    }

    @Transactional
    public void deactivateUser(Long uno) {
        User user = userRepository.findById(uno)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        if (user.isActive()) {
            user.setIsActive(false);
            log.info("상태 변경 완료: uno = {}, isActive = {}", uno, user.isActive());
            // 저장 생략해도 됨
        }
    }
    @Transactional
    public void reactivateUser(Long uno) {
        User user = userRepository.findById(uno)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        if (!user.isActive()) {
            user.setIsActive(true);
            userRepository.save(user);
        } else {
            log.info("이미 활성화 상태입니다: uno = {}", uno);
        }
    }


    public User getUserByUno(Long uno) {
        return userRepository.findByUno(uno)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));
    }
}






