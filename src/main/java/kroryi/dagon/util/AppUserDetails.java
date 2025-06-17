package kroryi.dagon.util;

import org.springframework.security.core.userdetails.UserDetails;

public interface AppUserDetails extends UserDetails {
    String getRole();
    Long getUno();
}
