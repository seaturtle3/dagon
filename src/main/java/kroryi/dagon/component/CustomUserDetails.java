package kroryi.dagon.component;

import kroryi.dagon.entity.User;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;

@Getter
@ToString
public class CustomUserDetails implements UserDetails {

    private final Long uno;
    private final String uname;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String role; // ✅ 역할 정보 추가

    public CustomUserDetails(Long uno, String uname, String password, Collection<? extends GrantedAuthority> authorities,String role) {
        this.uno = uno;
        this.uname = uname;
        this.password = password;
        this.authorities = authorities;
        this.role = role;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return uname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}