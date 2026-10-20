package com.alok.spring_boot_simple_employee_crud_operation_learn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alok.spring_boot_simple_employee_crud_operation_learn.dto.EmployeeRequestDTO;
import com.alok.spring_boot_simple_employee_crud_operation_learn.dto.EmployeeResponseDTO;
import com.alok.spring_boot_simple_employee_crud_operation_learn.response_structure.MyResponseStructure;
import com.alok.spring_boot_simple_employee_crud_operation_learn.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/employee")
public class EmployeeController {

	private final EmployeeService employeeService;

	@PostMapping(value = "/saveEmployee")
	public ResponseEntity<MyResponseStructure<EmployeeResponseDTO>> saveEmployee(
			@RequestBody @Validated EmployeeRequestDTO requestDTO) {
		MyResponseStructure<EmployeeResponseDTO> responseStructure = employeeService.saveEmployee(requestDTO);
		return ResponseEntity.status(responseStructure.getStatusCode()).body(responseStructure);
	}

	@PostMapping(value = "/saveAllEmployee")
	public ResponseEntity<MyResponseStructure<List<EmployeeResponseDTO>>> saveMultipleEmployee(
			@RequestBody List<@Valid EmployeeRequestDTO> requestDTOs) {
		MyResponseStructure<List<EmployeeResponseDTO>> responseStructure = employeeService
				.saveMultipleEmployee(requestDTOs);
		return ResponseEntity.status(responseStructure.getStatusCode()).body(responseStructure);
	}

	@GetMapping(value = "/getEmployeeById/{id}")
	public ResponseEntity<MyResponseStructure<EmployeeResponseDTO>> getEmployeeById(@PathVariable Integer id) {
		MyResponseStructure<EmployeeResponseDTO> myResponseStructure = employeeService.getEmployeeById(id);
		return ResponseEntity.status(myResponseStructure.getStatusCode()).body(myResponseStructure);
	}

	@GetMapping(value = "/getEmployeeByEmail")
	public ResponseEntity<MyResponseStructure<EmployeeResponseDTO>> getEmployeeByEmail(@RequestParam String email) {
		MyResponseStructure<EmployeeResponseDTO> myResponseStructure = employeeService.getEmployeeByEmail(email);
		return ResponseEntity.status(myResponseStructure.getStatusCode()).body(myResponseStructure);
	}

	@PutMapping(value = "/updateEmployeeById/{id}")
	public ResponseEntity<MyResponseStructure<EmployeeResponseDTO>> updateEmplloyeeById(@PathVariable Integer id,
			@RequestBody @Validated EmployeeRequestDTO requestDTO) {
		MyResponseStructure<EmployeeResponseDTO> myResponseStructure = employeeService.updateEmployeeById(id,
				requestDTO);
		return ResponseEntity.status(myResponseStructure.getStatusCode()).body(myResponseStructure);
	}

	@PutMapping(value = "/updateEmployeeByEmail/{email}")
	public ResponseEntity<MyResponseStructure<EmployeeResponseDTO>> updateEmplloyeeByEmail(@PathVariable String email,
			@RequestBody @Validated EmployeeRequestDTO requestDTO) {
		MyResponseStructure<EmployeeResponseDTO> myResponseStructure = employeeService.updateEmployeeByEmail(email,
				requestDTO);
		return ResponseEntity.status(myResponseStructure.getStatusCode()).body(myResponseStructure);
	}

	@DeleteMapping(value = "/deleteEmployeeById/{id}")
	public ResponseEntity<MyResponseStructure<EmployeeResponseDTO>> deleteEmployeeById(@PathVariable Integer id) {
		MyResponseStructure<EmployeeResponseDTO> myResponseStructure = employeeService.deleteEmployeeById(id);
		return ResponseEntity.status(myResponseStructure.getStatusCode()).body(myResponseStructure);
	}

	@DeleteMapping(value = "/deleteEmployeeByEmail/{email}")
	public ResponseEntity<MyResponseStructure<EmployeeResponseDTO>> deleteEmployeeByEmail(@PathVariable String email) {
		MyResponseStructure<EmployeeResponseDTO> myResponseStructure = employeeService.deleteEmployeeByEmail(email);
		return ResponseEntity.status(myResponseStructure.getStatusCode()).body(myResponseStructure);
	}

	@GetMapping(value = "/sortInAsc/{attribute}")
	public ResponseEntity<MyResponseStructure<List<EmployeeResponseDTO>>> sortEmployeeInASC(
			@PathVariable(value = "attribute") String attribute) {
		MyResponseStructure<List<EmployeeResponseDTO>> myResponseStructure = employeeService
				.sortEmployeeInASC(attribute);
		return ResponseEntity.status(myResponseStructure.getStatusCode()).body(myResponseStructure);
	}

	@GetMapping(value = "/sortInDesc/{attribute}")
	public ResponseEntity<MyResponseStructure<List<EmployeeResponseDTO>>> sortEmployeeInDESC(
			@PathVariable String attribute) {
		MyResponseStructure<List<EmployeeResponseDTO>> myResponseStructure = employeeService
				.sortEmployeeInDESC(attribute);
		return ResponseEntity.status(myResponseStructure.getStatusCode()).body(myResponseStructure);
	}

	@GetMapping(value = "/getEmployeeBypageNumber/{pageNumber}")
	public ResponseEntity<MyResponseStructure<Page<EmployeeResponseDTO>>> getEmployeeByPageNumber(
			@PathVariable int pageNumber) {
		MyResponseStructure<Page<EmployeeResponseDTO>> page = employeeService.getEmployeeByPageNumber(pageNumber);
		return ResponseEntity.status(page.getStatusCode()).body(page);
	}
}
