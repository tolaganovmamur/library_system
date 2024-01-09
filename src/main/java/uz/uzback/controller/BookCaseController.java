package uz.uzback.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.uzback.service.BookcaseService;

@RestController
@RequestMapping("/api/bookcase")
@RequiredArgsConstructor
public class BookCaseController {
    private final BookcaseService bookCaseService;

    @PostMapping("/add")
    public HttpEntity<?> add(@RequestParam Integer bookcaseNumber, @RequestParam Long floorId) {
        return bookCaseService.add(bookcaseNumber,floorId);
    }

    @PostMapping("/update")
    public HttpEntity<?> update(@RequestParam Long id,@RequestParam Integer bookcaseNumber, @RequestParam Long floorId) {
        return bookCaseService.update(id,bookcaseNumber,floorId);
    }

    @PostMapping("/delete")
    public HttpEntity<?> delete(@RequestParam Long id) {
        return bookCaseService.delete(id);
    }

    @GetMapping("/list")
    public HttpEntity<?> list(@RequestParam Long floorId) {
        return bookCaseService.list(floorId);
    }


}
