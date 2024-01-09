package uz.uzback.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.uzback.entity.template.AbcLongAuditingUserAndTime;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor

public class Floor extends AbcLongAuditingUserAndTime implements Serializable {
    @Column(unique = true, nullable = false)
    private Integer floorNumber;

    @OneToMany(mappedBy = "floor")
    @Size(min = 1, max = 20)
    private List<Bookcase> bookcases;

    public Floor(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }
}
