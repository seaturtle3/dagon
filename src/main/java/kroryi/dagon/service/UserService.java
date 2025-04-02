package kroryi.dagon.service;

import kroryi.dagon.entity.User;
import kroryi.dagon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean login(String uid, String upw) {
        System.out.println("로그인 시도: " + uid);

        Optional<User> optionalUser = userRepository.findByUid(uid);

        if (optionalUser.isEmpty()) {
            System.out.println("사용자를 찾을 수 없습니다");
            return false;
        }
        User user = optionalUser.get();
        return user.getUpw().equals(upw);
    }

}
