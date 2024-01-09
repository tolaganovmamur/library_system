package uz.uzback.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;

public interface BookingService {
    HttpEntity<?> booking(Long bookId,Long userId);
    HttpEntity<?> get(HttpServletRequest request);
    HttpEntity<?> takeaway(Long bookingId,Integer day,Long userId);
    HttpEntity<?> broughtBack(Long bookId,Long userId);

}
