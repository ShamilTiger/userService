package ru.ilyin.userservice.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO для создания пользователя")
public class UserRequestDto {


    @NotBlank(message = "Name is required")
    @Schema(description = "Full name", example = "Иван Иванов", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Email", example = "user@mail.ru", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be greater than 0")
    @Schema(description = "Age", example = "30", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer age;


}
