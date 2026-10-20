package com.alok.spring_boot_simple_employee_crud_operation_learn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alok.spring_boot_simple_employee_crud_operation_learn.entity.Employee;

import jakarta.transaction.Transactional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	Optional<Employee> findByEmail(String email);
	boolean existsByEmail(String email);
	@Transactional
	void deleteByEmail(String email);
}
