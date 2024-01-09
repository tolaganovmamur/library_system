package uz.uzback.entity.template;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class AbcLongAuditingUserAndTime extends AbcUserAuditing{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
