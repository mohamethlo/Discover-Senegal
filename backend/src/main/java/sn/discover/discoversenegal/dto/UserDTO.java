package sn.discover.discoversenegal.dto;

import lombok.*;
import sn.discover.discoversenegal.entities.UserRole;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private UserRole role;
    private LocalDateTime createdAt;
}
