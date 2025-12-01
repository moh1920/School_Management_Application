package test_Technique_stage.test_Technique_stage.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import test_Technique_stage.test_Technique_stage.entity.Level;
import test_Technique_stage.test_Technique_stage.DTOs.StudentRequest;
import test_Technique_stage.test_Technique_stage.DTOs.StudentResponse;
import test_Technique_stage.test_Technique_stage.entity.Student;
import test_Technique_stage.test_Technique_stage.service.StudentService;

import java.io.File;
import java.net.URI;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
@Validated
public class StudentController {

    private final StudentService studentService ;
    private final JobLauncher jobLauncher;
    private final Job importStudentJob;




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
    public ResponseEntity<?> createStudent(@Valid @RequestBody StudentRequest studentRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(studentService.addStudent(studentRequest));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
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
            return ResponseEntity.status(HttpStatus.OK).build();
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








    @PostMapping("/upload-students")
    public ResponseEntity<?> uploadStudents(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("Fichier vide");
        }

        File tmp = null;
        try {
            tmp = File.createTempFile("students-upload-", ".csv");
            file.transferTo(tmp);
            tmp.deleteOnExit(); // safety: ensures file removed on JVM exit

            JobParameters params = new JobParametersBuilder()
                    .addString("filePath", tmp.getAbsolutePath())
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(importStudentJob, params);

            return ResponseEntity.ok()
                    .body("Job lancé (id=" + execution.getId() + ") - Statut: " + execution.getStatus());
        } catch (Exception e) {
            // log exception (use a logger); do not expose stack traces in production
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur durant l'upload / lancement du job: " + e.getMessage());
        } finally {
            // try to delete now (best-effort) — be careful if job reads it after we delete synchronously
            // If job runs synchronously (default), it's safe to delete after launch; if async, don't delete here.
            if (tmp != null && tmp.exists()) {
                // only delete if job is not running in another thread and still needs it.
                // If you run jobs async, prefer storing file in an accessible location (S3, DB) instead.
                // tmp.delete(); // optionally attempt deletion
            }
        }
    }










}
