package uz.uzback.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.uzback.entity.enums.BookingStatus;
import uz.uzback.entity.template.AbcLongAuditingUserAndTime;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Booking extends AbcLongAuditingUserAndTime implements Serializable {
    private Date bookedDate;
    private Date expiringDate;
    private Date backDate;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus = BookingStatus.BOOKED;
    @ManyToOne
    private Book book;
    @ManyToOne
    private User user;
}
