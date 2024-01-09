package uz.uzback.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Long roleId;
}
