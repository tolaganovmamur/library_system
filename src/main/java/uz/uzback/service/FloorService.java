package uz.uzback.service;

import org.springframework.http.HttpEntity;

public interface FloorService {
    HttpEntity<?> add(Integer floorNumber);
    HttpEntity<?> update(Long id,Integer number);
    HttpEntity<?> delete(Long id);
    HttpEntity<?> list();
}
