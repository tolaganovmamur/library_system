package uz.uzback.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.uzback.entity.enums.BookStatus;
import uz.uzback.entity.template.AbcLongAuditingUserAndTime;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"bookcase_id","shelf_number","shelf_space_number"})})
public class Book extends AbcLongAuditingUserAndTime implements Serializable {
    private String name;
    private String author;
    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus = BookStatus.EXIST;
    @Column(name = "shelf_number",nullable = false)
    private Integer shelfNumber;
    @Column(name = "shelf_space_number",nullable = false)
    private Integer shelfSpaceNumber;


    @ManyToOne
    @JoinColumn(name = "bookcase_id",nullable = false)
    private Bookcase bookcase;


    private Long bookingId;
    public Book(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public Book(String name, String author, Integer shelfNumber, Integer shelfSpaceNumber, Bookcase bookcase) {
        this.name = name;
        this.author = author;
        this.shelfNumber = shelfNumber;
        this.shelfSpaceNumber = shelfSpaceNumber;
        this.bookcase = bookcase;
    }
}
