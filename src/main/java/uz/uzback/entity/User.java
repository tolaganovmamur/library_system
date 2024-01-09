package uz.uzback.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.uzback.entity.template.AbcLongAuditingUserAndTime;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_users")
public class User extends AbcLongAuditingUserAndTime implements UserDetails {

    private String lastName;

    private String firstName;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private boolean enabled = true;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Role role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getName().toString()));
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public User(String firstName, String lastName, String email, String password, Role role, boolean enabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

}
