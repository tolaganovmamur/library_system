package uz.uzback.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.uzback.entity.enums.RoleType;
import uz.uzback.entity.template.AbcLongAuditingUserAndTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role extends AbcLongAuditingUserAndTime {

    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType name;
}
