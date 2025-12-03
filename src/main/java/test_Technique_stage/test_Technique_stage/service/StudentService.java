package test_Technique_stage.test_Technique_stage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import test_Technique_stage.test_Technique_stage.entity.Level;
import test_Technique_stage.test_Technique_stage.entity.Student;
import test_Technique_stage.test_Technique_stage.DTOs.StudentRequest;
import test_Technique_stage.test_Technique_stage.DTOs.StudentResponse;
import test_Technique_stage.test_Technique_stage.exception.StudentException;
import test_Technique_stage.test_Technique_stage.mappers.Mapper;
import test_Technique_stage.test_Technique_stage.repositories.StudentRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepo studentRepo ;
    private final Mapper mapper ;



    public Page<StudentResponse> getAllStudent(Pageable pageable) {
        return studentRepo.findAll(pageable)
                .map(Mapper::toStudentResponse);
    }

    public StudentResponse getStudentById(Long id) {
        return Mapper.toStudentResponse(studentRepo.findById(id)
                .orElseThrow(() -> new StudentException(String.format("STUDENT NOT FOUND")))

        );
    }
    public Long addStudent(StudentRequest studentRequest){
        return studentRepo.save(mapper.toStudent(studentRequest)).getId();
    }

    public Long updateStudent(Long idStudent,StudentRequest studentRequest){
         var student = studentRepo.findById(idStudent)
                .orElseThrow(() -> new StudentException(String.format("STUDENT NOT FOUND  :", idStudent)));
//        student = mapper.toStudent(studentRequest);
        student.setUsername(studentRequest.username());
        student.setLevel(studentRequest.level());
        return studentRepo.save(student).getId();
    }

    public void deleteStudent(Long idStudent) {
        var student = studentRepo.findById(idStudent)
                .orElseThrow(() -> new StudentException(
                        String.format("STUDENT NOT FOUND : %d", idStudent)
                ));

        try {
            studentRepo.delete(student);
        } catch (Exception ex) {
            throw new StudentException(
                    String.format("FAILED TO DELETE STUDENT : %d", idStudent)
            );
        }
    }

    public StudentResponse getStudentByUsernameOrId(Long idStudent,String username){
        var student = new Student();
        if(idStudent != null){
             student = studentRepo.findById(idStudent)
                    .orElseThrow(() -> new StudentException(
                            String.format("STUDENT NOT FOUND : %d", idStudent)
                    ));
        } else if (username != null) {
             student = studentRepo.findByUsername(username)
                    .orElseThrow(() -> new StudentException(
                            String.format("STUDENT NOT FOUND : %s", username)
                    ));
        } else {

             student = studentRepo.findByUsernameAndId(username,idStudent)
                    .orElseThrow(() -> new StudentException(
                            String.format("STUDENT NOT FOUND : %s et %d", username , idStudent)
                    ));

        }
        return Mapper.toStudentResponse(student);
    }



    public List<StudentResponse> filterStudentByLevel(Level level) {

        studentRepo.findByLevel(level).stream()
                .forEach(student -> System.out.println(student.getLevel()));

        return studentRepo.findByLevel(level)
                .stream()
                .map(Mapper::toStudentResponse)
                .toList();
    }









}
