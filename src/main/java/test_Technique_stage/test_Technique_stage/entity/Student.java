package test_Technique_stage.test_Technique_stage.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 15, message = "Username must be between 3 and 15 characters")
    @Column(nullable = false, unique = true)
    private String username ;
    @Enumerated(EnumType.STRING)
    private Level level ;
}
