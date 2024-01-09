package uz.uzback.service;

import org.springframework.http.HttpEntity;

public interface BookcaseService {
    HttpEntity<?> add(Integer bookcaseNumber, Long floorId);
    HttpEntity<?> update(Long id,Integer bookcaseNumber, Long floorId);
    HttpEntity<?> delete(Long id);
    HttpEntity<?> list(Long floorId);
}
