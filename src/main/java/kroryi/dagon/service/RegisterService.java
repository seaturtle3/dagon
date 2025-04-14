package kroryi.dagon.service;

import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.entity.User;
import kroryi.dagon.enums.UserRole;
import kroryi.dagon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@Log4j2
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // ✅ 추가



    public void register(UsersDTO usersDTO) throws Exception {
        log.info("회원가입 요청: {}", usersDTO);


        if (userRepository.existsByUid(usersDTO.getUid())) {
            throw new Exception("이미 사용 중인 아이디입니다.");
        }
        if (userRepository.existsByemail(usersDTO.getEmail())) {
            throw new Exception("이미 사용 중인 이메일입니다.");
        }

        User user = new User();
        user.setUid(usersDTO.getUid());

        user.setUpw(passwordEncoder.encode(usersDTO.getUpw()));

        user.setUname(usersDTO.getUname());
        user.setEmail(usersDTO.getEmail());
        user.setPhone(usersDTO.getFullPhone());

        // ✅ 기본 권한 부여 (문자열이라면 그대로, enum이면 .name() 사용)
        user.setRole(UserRole.valueOf("USER"));

        log.info("저장할 사용자: {}", user);
        log.info("비밀번호 암호화 완료: {}", user.getUpw());

        userRepository.save(user);
        log.info("회원가입 완료: {}", user);
        log.info("DB 저장 완료: {}", user);
    }

}
