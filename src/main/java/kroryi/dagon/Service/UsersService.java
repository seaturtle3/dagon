package kroryi.dagon.Service;

import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.Entity.UsersEntity;
import kroryi.dagon.Repository.UsersRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Log4j2
@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    public boolean registerUser(UsersDTO dto) {
        if(usersRepository.existsById(dto.getId())) {
            return false;
        }
        UsersEntity user = new UsersEntity();
        user.setId(dto.getId());
        user.setPw(dto.getPw());
        user.setName(dto.getName());
        user.setNickname(dto.getNickname());
        user.setEmail(dto.getEmail());
        user.setPoints(dto.getPoints());
        user.setLevel(dto.getLevel());
        user.setCreated_at(new Date());
        user.setPhone(dto.getPhone());


        log.info("22222 -> {}",user.toString());

        usersRepository.save(user);
        return true;

    }

}
