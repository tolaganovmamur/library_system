package uz.uzback.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.uzback.config.JwtService;
import uz.uzback.entity.Role;
import uz.uzback.entity.User;
import uz.uzback.entity.enums.RoleType;
import uz.uzback.exception.DataNotFoundException;
import uz.uzback.payload.ApiResult;
import uz.uzback.repository.RoleRepository;
import uz.uzback.repository.UserRepository;

import java.time.ZoneOffset;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public HttpEntity<?> signUp(SignUpDTO signUpDTO) {
        User user;
        if (userRepository.findByEmail(signUpDTO.getEmail()).isEmpty()) {
                Role role = roleRepository.findById(signUpDTO.getRoleId()).orElseThrow(() -> new DataNotFoundException("Role not found !!!"));
                user = User.builder()
                        .firstName(signUpDTO.getFirstName())
                        .lastName(signUpDTO.getLastName())
                        .email(signUpDTO.getEmail())
                        .password(passwordEncoder.encode(signUpDTO.getPassword()))
                        .role(role)
                        .enabled(true)
                        .build();
            } else {
                return ResponseEntity.ok(ApiResult.failResponse("Email is already exist!"));
            }
        userRepository.save(user);
        return ResponseEntity.ok(ApiResult.successResponse());
    }

    public HttpEntity<?> signIn(SignInDTO signInDTO) {
        User user = userRepository.findByEmail(signInDTO.getEmail()).orElseThrow(() -> new DataNotFoundException("User not found!!!"));
        if (passwordEncoder.matches(signInDTO.getPassword(), user.getPassword())) {
            var jwtToken = jwtService.generateToken(user);
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("token", jwtToken);
            objectNode.put("id", user.getId());
            objectNode.put("lastName", user.getLastName());
            objectNode.put("firstName", user.getFirstName());
            objectNode.put("roleId", user.getRole().getId());
            objectNode.put("name", user.getRole().getName().name());
            objectNode.put("isAdmin", RoleType.ROLE_ADMIN.equals(user.getRole().getName()));
            objectNode.put("createdAt", user.getCreatedAt().minusHours(5).toInstant(ZoneOffset.UTC).toEpochMilli());
            objectNode.put("accountNonLocked", user.isAccountNonLocked());
            objectNode.put("enabled", user.isEnabled());
            return ResponseEntity.ok(ApiResult.successResponse(objectNode));
        }
        return ResponseEntity.ok(ApiResult.failResponse("Paro'l noto'gri qaytadan urinib ko'rin"));
    }
}

