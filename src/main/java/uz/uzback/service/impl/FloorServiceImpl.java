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
import uz.uzback.repository.BookcaseRepository;
import uz.uzback.repository.FloorRepository;
import uz.uzback.service.FloorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FloorServiceImpl implements FloorService {
    private final FloorRepository floorRepository;
    private final JdbcTemplate jdbcTemplate;
    private final BookcaseRepository bookcaseRepository;

    @Override
    public HttpEntity<?> add(Integer floorNumber) {
        floorRepository.findByFloorNumber(floorNumber).ifPresent(floor -> {
            throw new IncorrectDataException("Floor already exist!");
        });
        Floor floor = new Floor();
        floor.setFloorNumber(floorNumber);
        Floor save = floorRepository.save(floor);
        floorRepository.save(save);
        return ResponseEntity.ok(ApiResult.successResponse());
    }

    @Override
    public HttpEntity<?> update(Long id, Integer number) {
        Floor floor = floorRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Floor not found!"));
        if (floor.getFloorNumber() != number)
            floorRepository.findByFloorNumber(number).ifPresent(floor1 -> {
                throw new IncorrectDataException("Floor already exist!");
            });
        floor.setFloorNumber(number);
        floorRepository.save(floor);
        return ResponseEntity.ok(ApiResult.successResponse());
    }

    @Override
    public HttpEntity<?> delete(Long id) {
        Floor floor = floorRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Floor not found!"));
        if (floor.getBookcases().isEmpty()) {
            floorRepository.delete(floor);
            return ResponseEntity.ok(ApiResult.successResponse());
        }
        return ResponseEntity.ok(ApiResult.failResponse("Qavatga shkaflar biriktirilgan!"));
    }

    @Override
    public HttpEntity<?> list() {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select f.id,f.floor_number from floor f");
        return ResponseEntity.ok(ApiResult.successResponse(maps));
    }
}
