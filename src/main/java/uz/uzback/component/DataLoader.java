package uz.uzback.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.uzback.entity.Role;
import uz.uzback.entity.User;
import uz.uzback.entity.enums.RoleType;
import uz.uzback.repository.RoleRepository;
import uz.uzback.repository.UserRepository;


@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${spring.sql.init.mode}")
    private String modeType;


    @Override
    public void run(String... args) throws Exception {

        if (modeType.equals("always")) {


            Role admin = roleRepository.save(new Role(
                    RoleType.ROLE_ADMIN
            ));
            Role moderator = roleRepository.save(new Role(
                    RoleType.ROLE_MODERATOR
            ));
            Role user = roleRepository.save(new Role(
                    RoleType.ROLE_USER
            ));

            userRepository.save(new User(
                    "admin",
                    "admin",
                    "admin",
                    passwordEncoder.encode("1234"),
                    admin,
                    true
            ));  userRepository.save(new User(
                    "moderator",
                    "moderator",
                    "moderator",
                    passwordEncoder.encode("1234"),
                    moderator,
                    true
            ));  userRepository.save(new User(
                    "user",
                    "user",
                    "user",
                    passwordEncoder.encode("1234"),
                    user,
                    true
            ));

            System.out.println("Run successfully");
        }
    }
}
