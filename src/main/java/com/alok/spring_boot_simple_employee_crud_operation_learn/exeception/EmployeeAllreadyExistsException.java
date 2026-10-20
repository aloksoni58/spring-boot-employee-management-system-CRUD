package com.alok.spring_boot_simple_employee_crud_operation_learn.exeception;

public class EmployeeAllreadyExistsException extends RuntimeException{

	public EmployeeAllreadyExistsException(String msg) {
		super(msg);
	}

}
