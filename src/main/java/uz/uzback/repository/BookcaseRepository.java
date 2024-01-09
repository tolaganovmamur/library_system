package uz.uzback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.uzback.entity.Bookcase;

import java.util.Optional;

@Repository
public interface BookcaseRepository extends JpaRepository<Bookcase,Long> {
    Optional<Bookcase> findByBookcaseNumberAndFloorId(Integer bookcaseNumber, Long floorId);
}
