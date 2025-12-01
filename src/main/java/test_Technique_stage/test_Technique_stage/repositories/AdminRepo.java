package test_Technique_stage.test_Technique_stage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import test_Technique_stage.test_Technique_stage.entity.Admin;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<Admin,Long> {
    Optional<Admin> findByUsername(String username);
    boolean existsByUsername(String username);
}
