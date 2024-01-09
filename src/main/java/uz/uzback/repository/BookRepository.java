package uz.uzback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.uzback.entity.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    List<Book> findAllByBookcaseId(Long id);
    Optional<Book> findByBookcaseIdAndShelfNumberAndShelfSpaceNumber(Long id, Integer shelfNumber, Integer shelfSpaceNumber);
}