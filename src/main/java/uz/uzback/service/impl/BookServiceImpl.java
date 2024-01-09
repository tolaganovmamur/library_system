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
import uz.uzback.entity.Bookcase;
import uz.uzback.entity.Booking;
import uz.uzback.entity.enums.BookStatus;
import uz.uzback.exception.DataNotFoundException;
import uz.uzback.payload.ApiResult;
import uz.uzback.payload.ReqBook;
import uz.uzback.repository.BookRepository;
import uz.uzback.repository.BookcaseRepository;
import uz.uzback.repository.BookingRepository;
import uz.uzback.service.BookService;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.util.ClassUtils.isPresent;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookingRepository bookingRepository;
    private final BookcaseRepository bookcaseRepository;
    private final ObjectMapper objectMapper;
    private final JdbcTemplate jdbcTemplate;


    @Override
    public HttpEntity<?> add(ReqBook reqBook) {
        Bookcase bookcase = bookcaseRepository.findById(reqBook.getBookcaseId()).orElseThrow(() -> new DataNotFoundException("Bookcase not found!"));
        if (reqBook.getShelfNumber() > 10)
            return ResponseEntity.ok(ApiResult.failResponse("Shelf number must be less than 10!"));
        if (reqBook.getShelfSpaceNumber() > 20)
            return ResponseEntity.ok(ApiResult.failResponse("Shelf space number must be less than 20!"));
        if (bookRepository.findByBookcaseIdAndShelfNumberAndShelfSpaceNumber(reqBook.getBookcaseId(), reqBook.getShelfNumber(), reqBook.getShelfSpaceNumber()).isPresent())
            return ResponseEntity.ok(ApiResult.failResponse("Book already exist!"));
        bookRepository.save(new Book(reqBook.getName(), reqBook.getAuthor(), reqBook.getShelfNumber(), reqBook.getShelfSpaceNumber(), bookcase));
        return ResponseEntity.ok(ApiResult.successResponse());
    }

    @Override
    public HttpEntity<?> update(ReqBook reqBook) {
        Book book = bookRepository.findById(reqBook.getId()).orElseThrow(() -> new DataNotFoundException("Book not found!"));
        Bookcase bookcase = bookcaseRepository.findById(reqBook.getBookcaseId()).orElseThrow(() -> new DataNotFoundException("Bookcase not found!"));
        if (reqBook.getShelfNumber() > 10)
            return ResponseEntity.ok(ApiResult.failResponse("Shelf number must be less than 10!"));
        if (reqBook.getShelfSpaceNumber() > 20)
            return ResponseEntity.ok(ApiResult.failResponse("Shelf space number must be less than 20!"));
        Optional<Book> byBookcaseIdAndShelfNumberAndShelfSpaceNumber = bookRepository.findByBookcaseIdAndShelfNumberAndShelfSpaceNumber(reqBook.getBookcaseId(), reqBook.getShelfNumber(), reqBook.getShelfSpaceNumber());
        if (byBookcaseIdAndShelfNumberAndShelfSpaceNumber.isPresent() && byBookcaseIdAndShelfNumberAndShelfSpaceNumber.get().getId() != book.getId())
            return ResponseEntity.ok(ApiResult.failResponse("Book already exist!"));
        book.setAuthor(reqBook.getAuthor());
        book.setName(reqBook.getName());
        book.setShelfNumber(reqBook.getShelfNumber());
        book.setShelfSpaceNumber(reqBook.getShelfSpaceNumber());
        book.setBookcase(bookcase);
        bookRepository.save(book);
        return ResponseEntity.ok(ApiResult.successResponse());
    }

    @Override
    public HttpEntity<?> get(HttpServletRequest request) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        StringBuilder sql = new StringBuilder();
        if (request.getParameter("filter") != null && Boolean.parseBoolean(request.getParameter("filter"))) {
            sql.append(" where b.active = true ");
            if (request.getParameter("name") != null) {
                sql.append(" and upper(b.name) like '%").append(request.getParameter("name").toUpperCase()).append("%' ");
            }
            if (request.getParameter("author") != null) {
                sql.append(" and upper(b.author) like '%").append(request.getParameter("author").toUpperCase()).append("%' ");
            }
            if (request.getParameter("bookcaseId") != null) {
                sql.append(" and b.bookcase.id = ").append(request.getParameter("bookcaseId")).append(" ");
            }
            if (request.getParameter("shelfNumber") != null) {
                sql.append(" and b.shelf_number = ").append(request.getParameter("shelfNumber")).append(" ");
            }
            if (request.getParameter("shelfSpaceNumber") != null) {
                sql.append(" and b.shelf_space_number = ").append(request.getParameter("shelfSpaceNumber")).append(" ");
            }
            if (request.getParameter("book_status") != null) {
                sql.append(" and b.book_status = ").append(request.getParameter("book_status")).append(" ");
            }
            sql.append(" order by b.id desc ");
        } else {
            sql.append(" order by b.id desc ");
            long count = bookRepository.count();
            objectNode.put("count", count);
            if (request.getParameter("page") != null && request.getParameter("limit") != null) {
                int page = Integer.parseInt(request.getParameter("page"));
                int limit = Integer.parseInt(request.getParameter("limit"));
                sql.append(" limit ").append(limit).append(" offset ").append((page - 1) * limit);
            }
        }
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select b.id, b.name, b.author, b.shelf_number, b.shelf_space_number, b.book_status, b.bookcase_id, b.active,b.booking_id from book b " + sql);
        objectNode.putPOJO("data", maps);
        return ResponseEntity.ok(ApiResult.successResponse(objectNode));
    }

    @Override
    public HttpEntity<?> delete(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new DataNotFoundException("Book not found!"));
        if (book.getBookStatus().equals(BookStatus.BOOKED))
            return ResponseEntity.ok(ApiResult.failResponse("Book is booked!"));
        if (book.getBookStatus().equals(BookStatus.TAKENAWAY))
            return ResponseEntity.ok(ApiResult.failResponse("Book is taken away!"));
        List<Booking> allByBookId = bookingRepository.findAllByBookId(bookId);
        if (!allByBookId.isEmpty()) bookingRepository.deleteAll(allByBookId);
        bookRepository.delete(book);
        return ResponseEntity.ok(ApiResult.successResponse());
    }


}
