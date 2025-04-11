package kroryi.dagon.service;

import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 주입

    public UsersDTO authenticate(String uid, String upw) {
        Optional<User> optionalUser = userRepository.findByUid(uid);

        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();
        if (passwordEncoder.matches(upw, user.getUpw())) {
            return new UsersDTO(user);
        }
        return null;
    }
}