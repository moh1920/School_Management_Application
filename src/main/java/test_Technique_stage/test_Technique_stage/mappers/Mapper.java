package test_Technique_stage.test_Technique_stage.mappers;

import org.springframework.stereotype.Service;
import test_Technique_stage.test_Technique_stage.entity.Student;
import test_Technique_stage.test_Technique_stage.DTOs.StudentRequest;
import test_Technique_stage.test_Technique_stage.DTOs.StudentResponse;

@Service
public class Mapper {
    public  Student toStudent(StudentRequest request) {
        return Student.builder()
                .id(request.id())
                .username(request.username())
                .level(request.level())
                .build();
    }

    public static StudentResponse toStudentResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getUsername(),
                student.getLevel()
        );
    }

}
