package uz.uzback.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import uz.uzback.entity.Book;
import uz.uzback.payload.ReqBook;

public interface BookService {
     HttpEntity<?> add(ReqBook reqBook);
     HttpEntity<?> update(ReqBook reqBook);
     HttpEntity<?> get(HttpServletRequest request);
     HttpEntity<?> delete(Long bookId);
}
