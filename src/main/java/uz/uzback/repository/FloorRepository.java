package uz.uzback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.uzback.entity.Floor;

import java.util.Optional;

@Repository
public interface FloorRepository extends JpaRepository<Floor,Long> {
    Optional<Floor> findByFloorNumber(Integer floorNumber);
}
