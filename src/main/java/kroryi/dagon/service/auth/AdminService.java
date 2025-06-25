package kroryi.dagon.service.auth;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kroryi.dagon.DTO.AdminDTO;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.entity.Admin;
import kroryi.dagon.entity.Partner;
import kroryi.dagon.entity.User;
import kroryi.dagon.enums.LoginType;
import kroryi.dagon.enums.UserLevel;
import kroryi.dagon.enums.UserRole;
import kroryi.dagon.repository.ApiAdminRepository;
import kroryi.dagon.repository.PartnerRepository;
import kroryi.dagon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class AdminService {

    private final ApiAdminRepository apiAdminRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PartnerRepository partnerRepository;

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

    /**
     * SUPER_ADMIN 권한을 가진 관리자 생성
     */
    public void registerSuperAdmin(AdminDTO adminDTO) {
        if (apiAdminRepository.existsByAid(adminDTO.getAid())) {
            throw new IllegalArgumentException("이미 존재하는 관리자 ID입니다.");
        }

        Admin admin = new Admin();
        admin.setAid(adminDTO.getAid());
        admin.setApw(passwordEncoder.encode(adminDTO.getApw())); // 비밀번호 암호화
        admin.setAname(adminDTO.getAname());
        admin.setRole(kroryi.dagon.enums.AdminRole.SUPER_ADMIN); // SUPER_ADMIN 권한 설정

        apiAdminRepository.save(admin);
        log.info("SUPER_ADMIN 계정 생성 완료: {}", adminDTO.getAid());
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


    public UsersDTO updateUser(String uno, UsersDTO dto) {
        User user = (User) userRepository.findByUno(Long.valueOf(uno))
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        System.out.println("체크박스 상태: " + dto.isActive());

        // 필요한 필드들 업데이트
        user.setUname(dto.getUname());
        user.setNickname(dto.getNickname());
        user.setEmail(dto.getEmail());
        
        // 전화번호 처리 - fullPhone이 있으면 사용, 없으면 개별 번호 조합
        if (dto.getFullPhone() != null && !dto.getFullPhone().isEmpty()) {
            user.setPhone(dto.getFullPhone());
        } else if (dto.getPhone1() != null && dto.getPhone2() != null && dto.getPhone3() != null) {
            String fullPhone = dto.getPhone1() + "-" + dto.getPhone2() + "-" + dto.getPhone3();
            user.setPhone(fullPhone);
        }
        
        user.setPoints(dto.getPoints());
        user.setLevel(UserLevel.values()[dto.getLevel()]);
        user.setLevelPoint(Integer.valueOf(dto.getLevelPoint())); // 새 필드 처리
        user.setLoginType(LoginType.valueOf(dto.getLoginType())); // 새 필드 처리
        user.setRole(UserRole.valueOf(dto.getRole()));
        
        // 프로필 이미지 업데이트 - null이 아닐 때만 업데이트
        if (dto.getProfile_image() != null && !dto.getProfile_image().isEmpty()) {
            user.setProfileImg(dto.getProfile_image());
        }
        
        user.setIsActive(dto.isActive());

        return new UsersDTO(userRepository.save(user)); // 수정된 User 객체 저장
    }

    @Transactional
    public void deleteUser(String uno) {
        User user = (User) userRepository.findByUno(Long.valueOf(uno))
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        Partner partner = user.getPartner();
        if (partner != null) {
            partner.setUser(null);  // 관계 해제
            partnerRepository.save(partner);  // partner가 null이 아닌 경우에만 저장
        }

        // 다른 연관 관계도 clear
        user.getPartnerApplications().clear();
        user.getFishingReports().clear();
        user.getFishingDiaries().clear();
        user.getFreeBoards().clear();
        user.getUserActions().clear();
        user.getNotifications().clear();
        user.getReportsMade().clear();
        user.getReportsReceived().clear();
        log.info("delete user: ---> {}", user);

        userRepository.delete(user);  // 유저 삭제
    }
}
