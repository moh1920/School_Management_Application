package test_Technique_stage.test_Technique_stage.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import test_Technique_stage.test_Technique_stage.entity.Student;

@Configuration
@RequiredArgsConstructor
public class StudentJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    // Note: inject reader/processor/writer as method parameters to avoid StepScope issues
    @Bean
    public Step studentStep(FlatFileItemReader<Student> studentReader,
                            StudentProcessor studentProcessor,
                            ItemWriter<Student> studentWriter) {
        return new StepBuilder("studentStep", jobRepository)
                .<Student, Student>chunk(10, transactionManager)
                .reader(studentReader)
                .processor(studentProcessor)
                .writer(studentWriter)
                .build();
    }

    @Bean
    public Job importStudentJob(Step studentStep) {
        return new JobBuilder("importStudentJob", jobRepository)
                .start(studentStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }
}
