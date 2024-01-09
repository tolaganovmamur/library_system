package uz.uzback.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import uz.uzback.entity.Book;
import uz.uzback.entity.Booking;
import uz.uzback.entity.User;
import uz.uzback.entity.enums.BookStatus;
import uz.uzback.entity.enums.BookingStatus;
import uz.uzback.exception.DataNotFoundException;
import uz.uzback.payload.ApiResult;
import uz.uzback.repository.BookRepository;
import uz.uzback.repository.BookingRepository;
import uz.uzback.repository.UserRepository;
import uz.uzback.service.BookingService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookRepository bookRepository;
    private final BookingRepository bookingRepository;
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public HttpEntity<?> booking(Long bookId, Long userId) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new DataNotFoundException("Book not found"));
        if (!book.getBookStatus().equals(BookStatus.EXIST))
            return ResponseEntity.ok(ApiResult.failResponse("Book is not exist"));
        book.setBookStatus(BookStatus.BOOKED);
        Booking booking = new Booking();
        long expirationPeriodMillis = 3 * 24 * 60 * 60 * 1000L;


        Date currentDate = new Date();

        Date expirationDate = new Date(currentDate.getTime() + expirationPeriodMillis);
        booking.setBookedDate(currentDate);
        booking.setExpiringDate(expirationDate);
        booking.setBook(book);
        booking.setUser(user);
        Booking save1 = bookingRepository.save(booking);
        book.setBookingId(save1.getId());
        Book save = bookRepository.save(book);
        Runnable task = () -> {

            if (save.getBookStatus().equals(BookStatus.BOOKED)) {
                save.setBookStatus(BookStatus.EXIST);
                save.setBookingId(null);
                bookRepository.save(save);
                save1.setBookingStatus(BookingStatus.CANCELED);
                bookingRepository.save(save1);
            }

        };
        executorService.schedule(task, expirationPeriodMillis, TimeUnit.MILLISECONDS);
        return ResponseEntity.ok(ApiResult.successResponse());
    }

    @Override
    public HttpEntity<?> get(HttpServletRequest request) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        StringBuilder sql = new StringBuilder();
        if (request.getParameter("filter") != null && Boolean.parseBoolean(request.getParameter("filter"))) {
            sql.append(" where b.active = true ");
            if (request.getParameter("bookingStatus") != null) {
                sql.append(" and b.booking_status = '").append(request.getParameter("bookingStatus")).append("' ");
            }
            if (request.getParameter("bookId") != null) {
                sql.append(" and b.book_id = ").append(request.getParameter("bookId")).append(" ");
            }
            if (request.getParameter("userId") != null) {
                sql.append(" and b.user_id = ").append(request.getParameter("userId")).append(" ");
            }
            sql.append(" order by b.id desc ");

        } else {
            sql.append(" order by b.id desc ");
            long count = bookingRepository.count();
            objectNode.put("count", count);
            if (request.getParameter("page") != null && request.getParameter("limit") != null) {
                int page = Integer.parseInt(request.getParameter("page"));
                int limit = Integer.parseInt(request.getParameter("limit"));
                sql.append(" limit ").append(limit).append(" offset ").append((page - 1) * limit);
            }
        }
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select b.id,b.booked_date, b.expiring_date, b.back_date, b.booking_status, b.book_id,b.user_id from booking b left join book b1 on b1.id = b.book_id left join app_users u on u.id = b.user_id " + sql);
        objectNode.putPOJO("data", maps);
        return ResponseEntity.ok(ApiResult.successResponse(objectNode));
    }

    @Override
    public HttpEntity<?> takeaway(Long bookingId, Integer day, Long userId) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        long backDay = day * 24 * 60 * 60 * 1000L;
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new DataNotFoundException("Booking is not found!"));
        if (booking.getBookingStatus().equals(BookingStatus.BOOKED)) {
            if (!booking.getUser().getId().equals(userId))
                return ResponseEntity.ok(ApiResult.failResponse("You can't take away this book, because you are not owner of this booking!"));
            Book book = booking.getBook();
            book.setBookStatus(BookStatus.TAKENAWAY);
            bookRepository.save(book);
            Date currentDate = new Date();
            booking.setBackDate(new Date(currentDate.getTime() + backDay));
            booking.setBookingStatus(BookingStatus.TAKENAWAY);
            bookingRepository.save(booking);

            Runnable task = () -> {
                if (booking.getBookingStatus().equals(BookingStatus.TAKENAWAY)) {
                    booking.setBookingStatus(BookingStatus.NOTBROUGHTBACK);
                    bookingRepository.save(booking);
                }

            };
            executorService.schedule(task, backDay, TimeUnit.MILLISECONDS);
            return ResponseEntity.ok(ApiResult.successResponse());
        } else {
            return ResponseEntity.ok(ApiResult.failResponse("Booking is expired"));
        }
    }

    @Override
    public HttpEntity<?> broughtBack(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new DataNotFoundException("Book not found!"));
        Booking booking = bookingRepository.findById(book.getBookingId()).orElseGet(null);
        if (book.getBookStatus().equals(BookStatus.TAKENAWAY) && booking != null && booking.getBookingStatus().equals(BookingStatus.TAKENAWAY)) {
            if (!booking.getUser().getId().equals(userId))
                return ResponseEntity.ok(ApiResult.failResponse("You can't brought back this book, because you are not owner of this booking!"));
            book.setBookStatus(BookStatus.EXIST);
            book.setBookingId(null);
            bookRepository.save(book);
            booking.setBookingStatus(BookingStatus.BROUGHTBACK);
            bookingRepository.save(booking);
            return ResponseEntity.ok(ApiResult.successResponse());
        } else {
            return ResponseEntity.ok(ApiResult.failResponse("Book is not taken away!"));
        }
    }
}
