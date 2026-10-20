package com.alok.spring_boot_simple_employee_crud_operation_learn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record EmployeeRequestDTO(@Positive(message = "Id should be positive") Integer id,
		@NotBlank(message = "Name should not be blank") String name,
		@NotBlank(message = "Email should not be blank") String email,
		@Positive(message = "Phone should not be negative") Long phone) {

}
