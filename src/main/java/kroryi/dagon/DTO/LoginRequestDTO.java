
package kroryi.dagon.DTO;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String uid; // 클라이언트에서 아이디를 'uid'로 보낼 것으로 가정
    private String upw; // 클라이언트에서 비밀번호를 'upw'로 보낼 것으로 가정
}