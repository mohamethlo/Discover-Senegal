package sn.discover.discoversenegal.security.auth;

import lombok.Data;
import sn.discover.discoversenegal.entities.UserRole;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private UserRole role;
}
