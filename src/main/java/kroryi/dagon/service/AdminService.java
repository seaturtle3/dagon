package kroryi.dagon.service;


import jakarta.persistence.EntityNotFoundException;
import kroryi.dagon.DTO.AdminDTO;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.entity.Admin;
import kroryi.dagon.entity.User;
import kroryi.dagon.enums.UserLevel;
import kroryi.dagon.enums.UserRole;
import kroryi.dagon.repository.ApiAdminRepository;
import kroryi.dagon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ApiAdminRepository apiAdminRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public void registerAdmin(AdminDTO adminDTO) {
        if (apiAdminRepository.existsByAid(adminDTO.getAid())) {
            throw new IllegalArgumentException("이미 존재하는 관리자 ID입니다.");
        }

        Admin admin = new Admin();
        admin.setAid(adminDTO.getAid());
        admin.setApw(passwordEncoder.encode(adminDTO.getApw())); // 비밀번호 암호화
        admin.setAname(adminDTO.getAname());

        apiAdminRepository.save(admin);
    }

    // 전체 회원 조회 (페이징)
    public Page<UsersDTO> getAllUsers(Pageable pageable, String search) {
        Page<User> userPage;

        // 검색 조건이 있을 때, findByUsernameContainingOrEmailContaining을 사용
        if (search != null && !search.isEmpty()) {
            userPage = userRepository.findByUnameContainingOrEmailContaining(search, search, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        // User -> UsersDTO 변환
        List<UsersDTO> usersDTOList = userPage.getContent().stream()
                .map(user -> modelMapper.map(user, UsersDTO.class)) // User -> UsersDTO 변환
                .collect(Collectors.toList());

        // 변환된 리스트와 페이징 정보를 이용해 Page<UsersDTO> 반환
        return new PageImpl<>(usersDTOList, pageable, userPage.getTotalElements());
    }



    public UsersDTO getUserByUid(String uid) {
        System.out.println("Requested uid: " + uid); // uid 값 로그 출력
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        return new UsersDTO(user);
    }



    public UsersDTO updateUser(String uid, UsersDTO dto) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        user.setUname(dto.getUname());
        user.setNickname(dto.getNickname());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getFullPhone());
        user.setPoints(dto.getPoints());
        user.setLevel(UserLevel.values()[dto.getLevel()]);
        user.setRole(UserRole.valueOf(dto.getRole()));

        return new UsersDTO(userRepository.save(user));
    }

    public void deleteUser(String uid) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        userRepository.delete(user);
    }
}
