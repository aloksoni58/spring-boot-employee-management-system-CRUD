package com.alok.spring_boot_simple_employee_crud_operation_learn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Employee {

	@Id
	private Integer id;
	private String name;
	private String email;
	private Long phone;

}
