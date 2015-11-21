package com.chengsoft.tutorial;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {
	private String street1;
	private String city;
	private String state;
	private String zipcode;
}
