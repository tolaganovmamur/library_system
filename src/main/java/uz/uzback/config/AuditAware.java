package uz.uzback.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.uzback.entity.User;

import java.util.Optional;

public class AuditAware implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
            User principal = (User) authentication.getPrincipal();
            return Optional.of(principal.getId());
        }

        return Optional.empty();
    }
}
