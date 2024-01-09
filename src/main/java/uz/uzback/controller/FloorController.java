package uz.uzback.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.uzback.payload.ApiResult;
import uz.uzback.service.FloorService;

@RestController
@RequestMapping("/api/floor")
public class FloorController {
    private final FloorService floorService;

    public FloorController(FloorService floorService) {
        this.floorService = floorService;
    }

    @PostMapping("/add")
    public HttpEntity<?> add(@RequestParam Integer floorNumber) {
        floorService.add(floorNumber);
        return ResponseEntity.ok(ApiResult.successResponse());
    }

    @PostMapping("/update")
    public HttpEntity<?> update(@RequestParam Long id,@RequestParam Integer number) {
        return floorService.update(id,number);
    }

    @PostMapping("/delete")
    public HttpEntity<?> delete(@RequestParam Long id) {
        return floorService.delete(id);
    }

    @GetMapping("/list")
    public HttpEntity<?> list() {
        return floorService.list();
    }
}
