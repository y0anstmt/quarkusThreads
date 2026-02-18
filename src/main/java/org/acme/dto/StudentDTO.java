package org.acme.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    @NotBlank(message = "Name is mandatory")
    private String name;
    
    @NotNull(message = "Age is mandatory")
    private Integer age;
    
    @NotBlank(message = "Email is mandatory")
    private String email;
    
    private List<UniversityDTO> university;
}
