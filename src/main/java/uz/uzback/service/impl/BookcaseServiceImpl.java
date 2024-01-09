package uz.uzback.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import uz.uzback.entity.Bookcase;
import uz.uzback.entity.Floor;
import uz.uzback.exception.DataNotFoundException;
import uz.uzback.exception.IncorrectDataException;
import uz.uzback.payload.ApiResult;
import uz.uzback.repository.BookRepository;
import uz.uzback.repository.BookcaseRepository;
import uz.uzback.repository.FloorRepository;
import uz.uzback.service.BookcaseService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookcaseServiceImpl implements BookcaseService {
    private final FloorRepository floorRepository;
    private final BookcaseRepository bookcaseRepository;
    private final JdbcTemplate jdbcTemplate;
    private final BookRepository bookRepository;

    @Override
    public HttpEntity<?> add(Integer bookcaseNumber, Long floorId) {
        Floor floor = floorRepository.findById(floorId).orElseThrow(() -> new DataNotFoundException("Floor not found!"));
        List<Bookcase> bookcases = floor.getBookcases();
        if (bookcases.size() >= 20) {
            return ResponseEntity.ok(ApiResult.failResponse("Qavatga shkaf qo'shish mumkin emas!"));
        }
        bookcaseRepository.findByBookcaseNumberAndFloorId(bookcaseNumber, floorId).ifPresent(bookcase -> {
            throw new IncorrectDataException("Bookcase already exists!");
        });
        Bookcase save = bookcaseRepository.save(new Bookcase(bookcaseNumber, floor));
        bookcases.add(save);
        floor.setBookcases(bookcases);
        floorRepository.save(floor);
        return ResponseEntity.ok(ApiResult.successResponse());
    }


    @Override
    public HttpEntity<?> update(Long id, Integer bookcaseNumber, Long floorId) {
        Bookcase bookcase = bookcaseRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Bookcase not found!"));
        Floor floor = floorRepository.findById(floorId).orElseThrow(() -> new DataNotFoundException("Floor not found!"));
        if (floor.getId() != bookcase.getFloor().getId()) {
            if (floor.getBookcases().size() >= 20) {
                return ResponseEntity.ok(ApiResult.failResponse("Qavatga shkaf qo'shish mumkin emas!"));
            }
            List<Bookcase> bookcases = floor.getBookcases();
            bookcases.add(bookcase);
            floor.setBookcases(bookcases);
            floorRepository.save(floor);
            List<Bookcase> bookcases1 = bookcase.getFloor().getBookcases();
            bookcases1.remove(bookcase);
            bookcase.getFloor().setBookcases(bookcases1);
            floorRepository.save(bookcase.getFloor());
            bookcase.setFloor(floor);
        }
        if (bookcase.getBookcaseNumber() != bookcaseNumber && bookcaseRepository.findByBookcaseNumberAndFloorId(bookcaseNumber, floorId).isPresent())
            throw new IncorrectDataException("Bookcase number is already exists in this floor!");
        bookcase.setBookcaseNumber(bookcaseNumber);
        bookcaseRepository.save(bookcase);
        return ResponseEntity.ok(ApiResult.successResponse());
    }

    @Override
    public HttpEntity<?> delete(Long id) {
        Bookcase bookcase = bookcaseRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Bookcase not found!"));
        if (bookRepository.findAllByBookcaseId(id).isEmpty()) {
            bookcaseRepository.delete(bookcase);
            return ResponseEntity.ok(ApiResult.successResponse());
        }
        return ResponseEntity.ok(ApiResult.failResponse("Bookcase is not empty!, Please delete all books in this bookcase!"));
    }

    @Override
    public HttpEntity<?> list(Long floorId) {
        floorRepository.findById(floorId).orElseThrow(() -> new DataNotFoundException("Floor not found!"));
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select b.id, b.bookcase_number , b.floor_id, f.floor_number from bookcase b left join floor f on f.id = b.floor_id where b.floor_id=? order by b.bookcase_number", floorId);
        return ResponseEntity.ok(ApiResult.successResponse(maps));
    }
}
