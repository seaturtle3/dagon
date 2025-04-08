//package kroryi.dagon.service;
//
//import kroryi.dagon.DTO.UsersDTO;
//import kroryi.dagon.entity.User;
//import kroryi.dagon.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class UserService {
//
//    private final UserRepository userRepository;
//
//    public UsersDTO login(String uid, String upw) {
//        System.out.println("로그인 시도: " + uid);
//
//        Optional<User> optionalUser = userRepository.findByUid(uid);
//
//        if (optionalUser.isEmpty()) {
//            System.out.println("사용자를 찾을 수 없습니다");
//            return null;
//        }
//
//        User user = optionalUser.get();
//
//        // 비밀번호 검증
//        if (user.getUpw().equals(upw)) {
//            Long uno = user.getId();    // 유저 번호 가져오기
//            String uname = user.getUname();  // 유저 이름 가져오기
//            System.out.println("로그인 성공: " + uname + " (유저 번호: " + uno + ")");
//            // 로그인 성공 시 DTO로 변환하여 반환
//            return new UsersDTO(user);
//        }
//
//        System.out.println("비밀번호가 일치하지 않습니다");
//        return null;
//    }
//}