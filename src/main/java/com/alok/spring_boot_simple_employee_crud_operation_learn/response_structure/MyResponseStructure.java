package com.alok.spring_boot_simple_employee_crud_operation_learn.response_structure;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({"statusCode", "msg", "data"})
public class MyResponseStructure<T> {

	private int statusCode;
	private String msg;
	private T data;

}
