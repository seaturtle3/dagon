package kroryi.dagon.service;

import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Log4j2
@Service
public class RegisterService {


    @Autowired
    private UserRepository userRepository;


    public void register(UsersDTO usersDTO) throws Exception {
        log.info("회원가입 요청: {}", usersDTO);


        if (userRepository.existsByUid(usersDTO.getUid())) {
            throw new Exception("이미 사용 중인 아아디입니다.");
        }
        if (userRepository.existsByUemail(usersDTO.getUemail())) {
            throw new Exception("이미 사용 중인 이메일입니다.");
        }

        User user = new User();
        user.setUid(usersDTO.getUid());
        user.setUpw(usersDTO.getUpw());
        user.setUname(usersDTO.getUname());
        user.setUemail(usersDTO.getUemail());
        user.setUphone(usersDTO.getFullPhone());
        user.setUcreatedAt(LocalDateTime.now());
        user.setUlevel(User.Level.SILVER);
        user.setUrole(User.Role.NORMAL_USER);

        log.info("저장할 사용자: {}", user);

        userRepository.save(user);
        log.info("회원가입 완료: {}", user);
    }

}
