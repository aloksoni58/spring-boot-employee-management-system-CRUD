package com.alok.spring_boot_simple_employee_crud_operation_learn.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import com.alok.spring_boot_simple_employee_crud_operation_learn.dto.EmployeeRequestDTO;
import com.alok.spring_boot_simple_employee_crud_operation_learn.dto.EmployeeResponseDTO;
import com.alok.spring_boot_simple_employee_crud_operation_learn.entity.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	 Employee toEmployee(EmployeeRequestDTO dto);
	
	 EmployeeResponseDTO toEmployeeResponseDTO(Employee employee);

	 List<Employee> toEmployees(List<EmployeeRequestDTO> dtos);
	 
	 List<EmployeeResponseDTO> toEmployeeResponseDTOs(List<Employee> employees);
	 
}
