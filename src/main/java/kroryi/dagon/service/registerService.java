package kroryi.dagon.service;

import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.entity.Level;
import kroryi.dagon.entity.Role;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Log4j2
@Service
public class registerService {


    @Autowired
    private UserRepository userRepository;



    public void register(UsersDTO usersDTO) throws Exception {
        log.info("회원가입 요청: {}", usersDTO);



        if(userRepository.existsByUid(usersDTO.getId())){
            throw new Exception("이미 사용 중인 아아디입니다.");
        }
        if (userRepository.existsByUemail(usersDTO.getEmail())){
            throw new Exception("이미 사용 중인 이메일입니다.");
        }

        User user = new User();
        user.setUid(usersDTO.getId());
        user.setUpw(usersDTO.getPw());
        user.setUname(usersDTO.getName());
        user.setUemail(usersDTO.getEmail());
        user.setUphone(usersDTO.getPhone());
        user.setUcreated_at(LocalDateTime.now());
        user.setUlevel(Level.Silver);
        user.setUrole(Role.Normal_user);

        log.info("저장할 사용자: {}", user);

        userRepository.save(user);
        log.info("회원가입 완료: {}", user);
    }

}
