package uz.uzback.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.uzback.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
