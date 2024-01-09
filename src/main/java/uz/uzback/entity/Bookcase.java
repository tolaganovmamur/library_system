package uz.uzback.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.uzback.entity.template.AbcLongAuditingUserAndTime;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"bookcase_number","floor_id"})})
public class Bookcase extends AbcLongAuditingUserAndTime implements Serializable {
    @Column(name = "bookcase_number",nullable = false)
    private Integer bookcaseNumber;
    @ManyToOne
    @JoinColumn(name = "floor_id",nullable = false)
    private Floor floor;

    public Bookcase(Integer bookcaseNumber) {
        this.bookcaseNumber = bookcaseNumber;
    }

    public Bookcase(Integer bookcaseNumber, Floor floor) {
        this.bookcaseNumber = bookcaseNumber;
        this.floor = floor;
    }
}
