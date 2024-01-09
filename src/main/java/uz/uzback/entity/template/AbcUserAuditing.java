package uz.uzback.entity.template;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbcUserAuditing extends AbcTimeStamp{
    @CreatedBy
    private Long createdBy;

    @LastModifiedBy
    private Long modifiedBy;

    private Boolean active = true;
}
