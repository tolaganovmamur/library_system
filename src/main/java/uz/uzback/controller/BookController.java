package uz.uzback.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.uzback.entity.Book;
import uz.uzback.payload.ReqBook;
import uz.uzback.service.BookService;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/add")
    public HttpEntity<?> add(@RequestBody ReqBook reqBook) {
        return bookService.add(reqBook);
    }

    @PostMapping("/update")
    public HttpEntity<?> update(@RequestBody ReqBook reqBook) {
        return bookService.update(reqBook);
    }

    @GetMapping("/getAll")
    public HttpEntity<?> get(HttpServletRequest request) {
        return bookService.get(request);
    }

    @PostMapping("/delete")
    public HttpEntity<?> delete(@RequestParam Long bookId) {
        return bookService.delete(bookId);
    }


}
