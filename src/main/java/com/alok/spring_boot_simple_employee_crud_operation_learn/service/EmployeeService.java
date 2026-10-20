package com.alok.spring_boot_simple_employee_crud_operation_learn.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.alok.spring_boot_simple_employee_crud_operation_learn.dto.EmployeeRequestDTO;
import com.alok.spring_boot_simple_employee_crud_operation_learn.dto.EmployeeResponseDTO;
import com.alok.spring_boot_simple_employee_crud_operation_learn.entity.Employee;
import com.alok.spring_boot_simple_employee_crud_operation_learn.exeception.EmployeeAllreadyExistsException;
import com.alok.spring_boot_simple_employee_crud_operation_learn.mapper.EmployeeMapper;
import com.alok.spring_boot_simple_employee_crud_operation_learn.repository.EmployeeRepository;
import com.alok.spring_boot_simple_employee_crud_operation_learn.response_structure.MyResponseStructure;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper mapper;

	public MyResponseStructure<EmployeeResponseDTO> saveEmployee(EmployeeRequestDTO requestDTO) {
		Optional<Employee> dbEmployee = employeeRepository.findByEmail(requestDTO.email());
		if (!dbEmployee.isEmpty()) {
			throw new EmployeeAllreadyExistsException("Employee with this email allready exists!!!");
		}

		Employee employee = mapper.toEmployee(requestDTO);
		Employee newEmployee = employeeRepository.save(employee);
		EmployeeResponseDTO responseDTO = mapper.toEmployeeResponseDTO(newEmployee);
		MyResponseStructure<EmployeeResponseDTO> myResponseStructure = new MyResponseStructure<>();
		myResponseStructure.setStatusCode(201);
		myResponseStructure.setMsg("Employee registered successfully...");
		myResponseStructure.setData(responseDTO);
		return myResponseStructure;
	}

	public MyResponseStructure<List<EmployeeResponseDTO>> saveMultipleEmployee(List<EmployeeRequestDTO> requestDTOs) {

		if (requestDTOs.isEmpty()) {
			return null;
		}

		List<Employee> employees = mapper.toEmployees(requestDTOs);
		List<Employee> newEmployees = employeeRepository.saveAll(employees);
		List<EmployeeResponseDTO> responseDTOs = mapper.toEmployeeResponseDTOs(newEmployees);
		MyResponseStructure<List<EmployeeResponseDTO>> myResponseStructure = new MyResponseStructure<>();
		myResponseStructure.setStatusCode(201);
		myResponseStructure.setMsg("All employee registered successfully.");
		myResponseStructure.setData(responseDTOs);
		return myResponseStructure;
	}

	public MyResponseStructure<EmployeeResponseDTO> getEmployeeById(Integer id) {
		Optional<Employee> dbEmployee = employeeRepository.findById(id);
		MyResponseStructure<EmployeeResponseDTO> myResponseStructure = new MyResponseStructure<>();
		if (dbEmployee.isEmpty()) {
			myResponseStructure.setStatusCode(404);
			myResponseStructure.setMsg("Employee does not exists with id " + id);
			myResponseStructure.setData(null);
			return myResponseStructure;
		}
		EmployeeResponseDTO responseDTO = mapper.toEmployeeResponseDTO(dbEmployee.get());
		myResponseStructure.setStatusCode(302);
		myResponseStructure.setMsg("Employee found with id " + id);
		myResponseStructure.setData(responseDTO);
		return myResponseStructure;

	}

	public MyResponseStructure<EmployeeResponseDTO> getEmployeeByEmail(String email) {
		Optional<Employee> dbEmployee = employeeRepository.findByEmail(email);
		MyResponseStructure<EmployeeResponseDTO> myResponseStructure = new MyResponseStructure<>();
		if (dbEmployee.isEmpty()) {
			myResponseStructure.setStatusCode(404);
			myResponseStructure.setMsg("Employee does not exists with email : " + email);
			myResponseStructure.setData(null);
			return myResponseStructure;
		}
		EmployeeResponseDTO responseDTO = mapper.toEmployeeResponseDTO(dbEmployee.get());
		myResponseStructure.setStatusCode(302);
		myResponseStructure.setMsg("Employee found with email : " + email);
		myResponseStructure.setData(responseDTO);
		return myResponseStructure;

	}

	public MyResponseStructure<EmployeeResponseDTO> updateEmployeeById(Integer id, EmployeeRequestDTO requestDTO) {
		boolean isEmployee = employeeRepository.existsById(id);
		MyResponseStructure<EmployeeResponseDTO> myResponseStructure = new MyResponseStructure<>();
		if (!isEmployee) {
			myResponseStructure.setStatusCode(404);
			myResponseStructure.setMsg("Employee does not exists with id : " + id);
			myResponseStructure.setData(null);
			return myResponseStructure;
		}

		Employee employee = mapper.toEmployee(requestDTO);
		Employee newEmployee = employeeRepository.save(employee);
		EmployeeResponseDTO responseDTO = mapper.toEmployeeResponseDTO(newEmployee);
		myResponseStructure.setStatusCode(201);
		myResponseStructure.setMsg("Employee updated successfully...");
		myResponseStructure.setData(responseDTO);
		return myResponseStructure;
	}

	public MyResponseStructure<EmployeeResponseDTO> updateEmployeeByEmail(String email, EmployeeRequestDTO requestDTO) {
		boolean isEmployee = employeeRepository.existsByEmail(email);
		MyResponseStructure<EmployeeResponseDTO> myResponseStructure = new MyResponseStructure<>();
		if (!isEmployee) {
			myResponseStructure.setStatusCode(404);
			myResponseStructure.setMsg("Employee does not exists with email : " + email);
			myResponseStructure.setData(null);
			return myResponseStructure;
		}

		Employee employee = mapper.toEmployee(requestDTO);
		Employee newEmployee = employeeRepository.save(employee);
		EmployeeResponseDTO responseDTO = mapper.toEmployeeResponseDTO(newEmployee);
		myResponseStructure.setStatusCode(201);
		myResponseStructure.setMsg("Employee updated successfully...");
		myResponseStructure.setData(responseDTO);
		return myResponseStructure;
	}

	public MyResponseStructure<EmployeeResponseDTO> deleteEmployeeById(Integer id) {
		boolean isEmployee = employeeRepository.existsById(id);
		MyResponseStructure<EmployeeResponseDTO> myResponseStructure = new MyResponseStructure<>();
		if (!isEmployee) {
			myResponseStructure.setStatusCode(404);
			myResponseStructure.setMsg("Employee does not exists with id : " + id);
			myResponseStructure.setData(null);
			return myResponseStructure;
		}
		employeeRepository.deleteById(id);
		myResponseStructure.setStatusCode(202);
		myResponseStructure.setMsg("Employee delete with id : " + id);
		myResponseStructure.setData(null);
		return myResponseStructure;
	}

	public MyResponseStructure<EmployeeResponseDTO> deleteEmployeeByEmail(String email) {
		boolean isEmployee = employeeRepository.existsByEmail(email);
		MyResponseStructure<EmployeeResponseDTO> myResponseStructure = new MyResponseStructure<>();
		if (!isEmployee) {
			myResponseStructure.setStatusCode(404);
			myResponseStructure.setMsg("Employee does not exists with email : " + email);
			myResponseStructure.setData(null);
			return myResponseStructure;
		}
		employeeRepository.deleteByEmail(email);
		myResponseStructure.setStatusCode(202);
		myResponseStructure.setMsg("Employee delete with email : " + email);
		myResponseStructure.setData(null);
		return myResponseStructure;
	}

	public MyResponseStructure<List<EmployeeResponseDTO>> sortEmployeeInASC(String attribute) {
		List<Employee> employees = employeeRepository.findAll(Sort.by(Direction.ASC, attribute));
		MyResponseStructure<List<EmployeeResponseDTO>> myResponseStructure = new MyResponseStructure<>();
		if (employees.isEmpty()) {
			myResponseStructure.setStatusCode(HttpStatus.NO_CONTENT.value());
			myResponseStructure.setMsg("No employee content available!!!");
			myResponseStructure.setData(null);
			return myResponseStructure;
		}
		List<EmployeeResponseDTO> responseDTO = mapper.toEmployeeResponseDTOs(employees);
		myResponseStructure.setStatusCode(HttpStatus.FOUND.value());
		myResponseStructure.setMsg("Employees are sorted in ascending order...");
		myResponseStructure.setData(responseDTO);
		return myResponseStructure;
	}

	public MyResponseStructure<List<EmployeeResponseDTO>> sortEmployeeInDESC(String attribute) {
		List<Employee> employees = employeeRepository.findAll(Sort.by(Direction.DESC, attribute));
		MyResponseStructure<List<EmployeeResponseDTO>> myResponseStructure = new MyResponseStructure<>();
		if (employees.isEmpty()) {
			myResponseStructure.setStatusCode(HttpStatus.NO_CONTENT.value());
			myResponseStructure.setMsg("No employee content available!!!");
			myResponseStructure.setData(null);
			return myResponseStructure;
		}
		List<EmployeeResponseDTO> responseDTO = mapper.toEmployeeResponseDTOs(employees);
		myResponseStructure.setStatusCode(HttpStatus.FOUND.value());
		myResponseStructure.setMsg("Employees are sorted in descending order...");
		myResponseStructure.setData(responseDTO);
		return myResponseStructure;
	}

	public MyResponseStructure<Page<EmployeeResponseDTO>> getEmployeeByPageNumber(int pageNumber) {
		Page<Employee> page = employeeRepository.findAll(PageRequest.of(pageNumber, 4, Sort.by("id").ascending()));
		Page<EmployeeResponseDTO> responsePage = page.map(mapper::toEmployeeResponseDTO);
		MyResponseStructure<Page<EmployeeResponseDTO>> myResponseStructure = new MyResponseStructure<>();
		if (responsePage.isEmpty()) {
			myResponseStructure.setStatusCode(HttpStatus.NO_CONTENT.value());
			myResponseStructure.setMsg("No employee content available!!!");
			myResponseStructure.setData(responsePage);
			return myResponseStructure;
		}

		myResponseStructure.setStatusCode(HttpStatus.OK.value());
		myResponseStructure.setMsg("Employee content available of page " + pageNumber);
		myResponseStructure.setData(responsePage);
		return myResponseStructure;
	}
}
