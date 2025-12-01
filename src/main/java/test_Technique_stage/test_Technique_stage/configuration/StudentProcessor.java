package test_Technique_stage.test_Technique_stage.configuration;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import test_Technique_stage.test_Technique_stage.entity.Student;

@Component
public class StudentProcessor implements ItemProcessor<Student, Student> {

    @Override
    public Student process(Student student) {
        if (student == null) return null;
        if (student.getUsername() != null) {
            student.setUsername(student.getUsername().trim().toLowerCase());
        }
        return student;
    }
}
