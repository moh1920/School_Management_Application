package test_Technique_stage.test_Technique_stage.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import test_Technique_stage.test_Technique_stage.entity.Student;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class StudentBatchConfig {

    @Bean
    @StepScope
    public FlatFileItemReader<Student> studentReader(
            @Value("#{jobParameters['filePath']}") String filePath) {

        FlatFileItemReader<Student> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setLinesToSkip(1);

        DefaultLineMapper<Student> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("username", "level");
        tokenizer.setDelimiter(";");
        tokenizer.setStrict(false);

        BeanWrapperFieldSetMapper<Student> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(Student.class);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(mapper);
        reader.setLineMapper(lineMapper);

        return reader;
    }
}
