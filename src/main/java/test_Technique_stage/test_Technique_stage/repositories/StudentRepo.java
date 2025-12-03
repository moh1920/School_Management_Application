package test_Technique_stage.test_Technique_stage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import test_Technique_stage.test_Technique_stage.entity.Level;
import test_Technique_stage.test_Technique_stage.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student,Long> {


    Optional<Student> findByUsername(String username);
    Optional<Student> findByUsernameAndId(String username,Long id);
    List<Student> findByLevel(Level level);


}
