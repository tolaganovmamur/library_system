package uz.uzback.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.uzback.service.BookingService;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping()
    public HttpEntity<?> booking(@RequestParam Long bookId,@RequestParam Long userId) {
        return bookingService.booking(bookId,userId);
    }

    @GetMapping("/get")
    public HttpEntity<?> get(HttpServletRequest request) {
        return bookingService.get(request);
    }


    @PostMapping("/takeaway")
    public HttpEntity<?> takeaway(@RequestParam Long bookingId,@RequestParam Integer day,@RequestParam Long userId) {
        return bookingService.takeaway(bookingId,day,userId);
    }

    @PostMapping("/broughtBack")
    public HttpEntity<?> broughtBack(@RequestParam Long bookId,@RequestParam Long userId) {
        return bookingService.broughtBack(bookId,userId);
    }


}
