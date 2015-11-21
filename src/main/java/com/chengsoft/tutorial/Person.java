package com.chengsoft.tutorial;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Person {
	private String name;
	private Integer age;
	private Address address;
	private List<Person> siblings;
}
