package test_Technique_stage.test_Technique_stage.configuration;

import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import test_Technique_stage.test_Technique_stage.entity.Student;
import test_Technique_stage.test_Technique_stage.repositories.StudentRepo;

@Configuration
public class StudentWriterConfig {

    @Bean
    public ItemWriter<Student> studentWriter(StudentRepo repository) {
        // saveAll sur chunk
        return items -> repository.saveAll(items);
    }
}
