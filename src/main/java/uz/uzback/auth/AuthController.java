package uz.uzback.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-in")
    public HttpEntity<?> signIn(@RequestBody SignInDTO signInDTO){
        return authService.signIn(signInDTO);
    }
    @PostMapping("/register")
    public HttpEntity<?> signUp(@RequestBody SignUpDTO sign) {
        return authService.signUp(sign);
    }
}

