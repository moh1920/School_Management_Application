package test_Technique_stage.test_Technique_stage.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import test_Technique_stage.test_Technique_stage.entity.Level;
import test_Technique_stage.test_Technique_stage.DTOs.StudentRequest;
import test_Technique_stage.test_Technique_stage.DTOs.StudentResponse;
import test_Technique_stage.test_Technique_stage.entity.Student;
import test_Technique_stage.test_Technique_stage.service.StudentService;

import java.net.URI;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
@Validated
public class StudentController {

    private final StudentService studentService ;



    @GetMapping("getAllStudents")
    public ResponseEntity<?> getAllStudents(
            @RequestParam int page,
            @RequestParam int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<StudentResponse> studentResponses = studentService.getAllStudent(pageable);
            return ResponseEntity.status(HttpStatus.FOUND).body(studentResponses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur : " + e.getMessage());
        }

    }

    @GetMapping("getStudentById/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.FOUND).body(studentService.getStudentById(id)) ;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur : " + e.getMessage());
        }
    }
    @PostMapping("createStudent")
    public ResponseEntity<Void> createStudent(@Valid @RequestBody StudentRequest studentRequest) {
        Long createdId = studentService.addStudent(studentRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("updateStudent/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable("id") Long id,
                                              @Valid @RequestBody StudentRequest studentRequest) {
        try {
            studentService.updateStudent(id, studentRequest);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur : " + e.getMessage());
        }
    }

    @DeleteMapping("deleteStudent/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable("id") Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }
    @GetMapping("/search")
    public ResponseEntity<StudentResponse> getStudentByUsernameOrId(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "username", required = false) String username
    ) {
        try {
            return ResponseEntity.status(HttpStatus.FOUND).body(studentService.getStudentByUsernameOrId(id,username)) ;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<StudentResponse> filterStudentByLevel(
            @RequestParam("level") Level level
    ) {
        try {
            return ResponseEntity.status(HttpStatus.FOUND).body(studentService.filterStudentByLevel(level)) ;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }













}
