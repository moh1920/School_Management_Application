package test_Technique_stage.test_Technique_stage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;
import test_Technique_stage.test_Technique_stage.DTOs.StudentRequest;
import test_Technique_stage.test_Technique_stage.DTOs.StudentResponse;
import test_Technique_stage.test_Technique_stage.entity.Level;
import test_Technique_stage.test_Technique_stage.entity.Student;
import test_Technique_stage.test_Technique_stage.exception.StudentException;
import test_Technique_stage.test_Technique_stage.mappers.Mapper;
import test_Technique_stage.test_Technique_stage.repositories.StudentRepo;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceJUnitTest {

    private StudentService studentService;
    private InMemoryStudentRepo studentRepo;
    private SimpleMapper mapper;

    @BeforeEach
    void setUp() {
        studentRepo = new InMemoryStudentRepo();
        mapper = new SimpleMapper();
        studentService = new StudentService(studentRepo, mapper);

        // seed one student
        Student s = new Student();
        s.setUsername("john");
        s.setLevel(Level.BEGINNER);
        studentRepo.save(s);
    }

    // -------------------------
    // getAllStudent
    // -------------------------
    @Test
    void getAllStudent_returnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<StudentResponse> page = studentService.getAllStudent(pageable);

        assertEquals(1, page.getTotalElements());
        assertEquals("john", page.getContent().get(0).username());
    }

    // -------------------------
    // getStudentById
    // -------------------------
    @Test
    void getStudentById_found() {
        Long id = studentRepo.findAll(PageRequest.of(0,10)).getContent().get(0).getId();
        StudentResponse res = studentService.getStudentById(id);
        assertNotNull(res);
        assertEquals("john", res.username());
    }

    @Test
    void getStudentById_notFound_throws() {
        assertThrows(StudentException.class, () -> studentService.getStudentById(999L));
    }

    // -------------------------
    // addStudent
    // -------------------------
    @Test
    void addStudent_savesAndReturnsId() {
        StudentRequest req = new StudentRequest(null, "alice", Level.ADVANCED);

        Long newId = studentService.addStudent(req);
        assertNotNull(newId);

        StudentResponse res = studentService.getStudentById(newId);
        assertEquals("alice", res.username());
        assertEquals(Level.ADVANCED, studentRepo.findById(newId).get().getLevel());
    }

    // -------------------------
    // updateStudent
    // -------------------------
    @Test
    void updateStudent_existing_updatesAndReturnsId() {
        Student existing = studentRepo.findAll(PageRequest.of(0,10)).getContent().get(0);
        Long id = existing.getId();

        StudentRequest req = new StudentRequest(null, "john-updated", Level.EXPERT);

        Long returnedId = studentService.updateStudent(id, req);
        assertEquals(id, returnedId);

        Student updated = studentRepo.findById(id).get();
        assertEquals("john-updated", updated.getUsername());
        assertEquals(Level.EXPERT, updated.getLevel());
    }

    @Test
    void updateStudent_notFound_throws() {
        StudentRequest req = new StudentRequest(null, "nobody", Level.ADVANCED);
        assertThrows(StudentException.class, () -> studentService.updateStudent(999L, req));
    }

    // -------------------------
    // deleteStudent
    // -------------------------
    @Test
    void deleteStudent_existing_deletes() {
        Student existing = studentRepo.findAll(PageRequest.of(0,10)).getContent().get(0);
        Long id = existing.getId();

        assertDoesNotThrow(() -> studentService.deleteStudent(id));
        assertFalse(studentRepo.findById(id).isPresent());
    }

    @Test
    void deleteStudent_notFound_throws() {
        assertThrows(StudentException.class, () -> studentService.deleteStudent(999L));
    }

    // -------------------------
    // getStudentByUsernameOrId
    // -------------------------
    @Test
    void getStudentByUsernameOrId_byUsername_returns() {
        StudentResponse res = studentService.getStudentByUsernameOrId(null, "john");
        assertEquals("john", res.username());
    }

    @Test
    void getStudentByUsernameOrId_byId_returns() {
        Long id = studentRepo.findAll(PageRequest.of(0,10)).getContent().get(0).getId();
        StudentResponse res = studentService.getStudentByUsernameOrId(id, null);
        assertEquals("john", res.username());
    }

    @Test
    void getStudentByUsernameOrId_notFound_throws() {
        assertThrows(StudentException.class, () -> studentService.getStudentByUsernameOrId(null, "unknown"));
    }

    // -------------------------
    // filterStudentByLevel
    // -------------------------
    @Test
    void filterStudentByLevel_found() {
        List<StudentResponse> res = studentService.filterStudentByLevel(Level.BEGINNER);
    }

    @Test
    void filterStudentByLevel_notFound_throws() {
        assertThrows(StudentException.class, () -> studentService.filterStudentByLevel(Level.EXPERT));
    }

    // -------------------------
    // In-memory repo and mapper (stubs)
    // -------------------------
    private static class InMemoryStudentRepo implements StudentRepo {

        private final Map<Long, Student> store = new LinkedHashMap<>();
        private final AtomicLong seq = new AtomicLong(1);

        // ----------------------------------------------------
        // METHODS ACTUALLY USED BY StudentService
        // ----------------------------------------------------

        @Override
        public Page<Student> findAll(Pageable pageable) {
            List<Student> list = new ArrayList<>(store.values());
            int start = Math.toIntExact(pageable.getOffset());
            int end = Math.min(start + pageable.getPageSize(), list.size());
            List<Student> content =
                    (start < list.size() && start <= end) ? list.subList(start, end) : Collections.emptyList();
            return new PageImpl<>(content, pageable, list.size());
        }

        @Override
        public Optional<Student> findById(Long id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public <S extends Student> S save(S entity) {
            if (entity.getId() == null) {
                entity.setId(seq.getAndIncrement());
            }
            store.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public void delete(Student entity) {
            if (entity != null && entity.getId() != null)
                store.remove(entity.getId());
        }

        @Override
        public Optional<Student> findByUsername(String username) {
            return store.values().stream()
                    .filter(s -> Objects.equals(s.getUsername(), username))
                    .findFirst();
        }

        @Override
        public Optional<Student> findByUsernameAndId(String username, Long id) {
            return store.values().stream()
                    .filter(s -> Objects.equals(s.getId(), id)
                            && Objects.equals(s.getUsername(), username))
                    .findFirst();
        }

        @Override
        public List<Student> findByLevel(Level level) {
            return store.values().stream().collect(Collectors.toList());

        }

        // ----------------------------------------------------
        // ALL OTHER JpaRepository METHODS → Unsupported
        // ----------------------------------------------------

        @Override public void flush() { throw new UnsupportedOperationException(); }
        @Override public <S extends Student> S saveAndFlush(S entity) { throw new UnsupportedOperationException(); }
        @Override public <S extends Student> List<S> saveAllAndFlush(Iterable<S> entities) { throw new UnsupportedOperationException(); }
        @Override public void deleteAllInBatch(Iterable<Student> entities) { throw new UnsupportedOperationException(); }
        @Override public void deleteAllByIdInBatch(Iterable<Long> longs) { throw new UnsupportedOperationException(); }
        @Override public void deleteAllInBatch() { throw new UnsupportedOperationException(); }
        @Override public Student getOne(Long aLong) { throw new UnsupportedOperationException(); }
        @Override public Student getById(Long aLong) { throw new UnsupportedOperationException(); }
        @Override public Student getReferenceById(Long aLong) { throw new UnsupportedOperationException(); }

        @Override
        public <S extends Student> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
        }

        @Override public <S extends Student> List<S> findAll(Example<S> example) { throw new UnsupportedOperationException(); }
        @Override public <S extends Student> List<S> findAll(Example<S> example, Sort sort) { throw new UnsupportedOperationException(); }
        @Override public <S extends Student> Page<S> findAll(Example<S> example, Pageable pageable) { throw new UnsupportedOperationException(); }
        @Override public <S extends Student> long count(Example<S> example) { throw new UnsupportedOperationException(); }
        @Override public <S extends Student> boolean exists(Example<S> example) { throw new UnsupportedOperationException(); }

        @Override
        public <S extends Student, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }

        @Override public List<Student> findAll() { throw new UnsupportedOperationException(); }
        @Override public List<Student> findAll(Sort sort) { throw new UnsupportedOperationException(); }
        @Override public List<Student> findAllById(Iterable<Long> longs) { throw new UnsupportedOperationException(); }
        @Override public <S extends Student> List<S> saveAll(Iterable<S> entities) { throw new UnsupportedOperationException(); }
        @Override public boolean existsById(Long aLong) { throw new UnsupportedOperationException(); }
        @Override public long count() { throw new UnsupportedOperationException(); }
        @Override public void deleteById(Long aLong) { throw new UnsupportedOperationException(); }
        @Override public void deleteAllById(Iterable<? extends Long> longs) { throw new UnsupportedOperationException(); }
        @Override public void deleteAll(Iterable<? extends Student> entities) { throw new UnsupportedOperationException(); }
        @Override public void deleteAll() { throw new UnsupportedOperationException(); }
    }


    private static class SimpleMapper extends Mapper {

        @Override
        public Student toStudent(StudentRequest studentRequest) {
            Student s = new Student();
            // studentRequest is a record: fields accessible by name (id(), username(), level())
            s.setId(studentRequest.id()); // may be null
            s.setUsername(studentRequest.username());
            s.setLevel(studentRequest.level());
            return s;
        }

        public static StudentResponse toStudentResponse(Student student) {
            // StudentResponse is a record: use its canonical constructor
            return new StudentResponse(student.getId(), student.getUsername(), student.getLevel());
        }

        // Note: your service uses a static method reference Mapper::toStudentResponse.
        // That static method is expected to exist on your project's Mapper type.
        // If it doesn't, adapt the service or Mapper interface.
    }
}
