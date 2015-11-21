package com.chengsoft.tutorial;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import org.junit.Ignore;
import org.junit.Test;

public class Chapter_1 {
	
	String[] words = new String[]{
			"banana",
			"apple",
			"0",
			"dog"};

	@Test
	@Ignore
	public void testComparatorLambda() {
		Arrays.sort(words,
				// Type can be inferred
//				(String first, String second) -> Integer.compare(first.length(), second.length()));
				   (first, second) -> Integer.compare(first.length(), second.length()));
		System.out.println(Arrays.asList(words));
		
		// the lambda expression stored in a functional interface
		BiFunction<String, String, Integer> comp
		   = (first, second) -> Integer.compare(first.length(), second.length());
	}
	
	@Test
	@Ignore
	public void testMethodReference() {
		
		// Equivalent to (x, y) -> x.compareToIgnoreCase(y)
		Arrays.sort(words, String::compareToIgnoreCase);
		
		// Equivalent to (x) -> copyList.add(x)
		List<String> copyList = new ArrayList<>();
		Arrays.stream(words).forEach(copyList::add);
		
		// Equivalent to (x) -> x.toUpperCase()
		// Equivalent to (x) -> new String[x]
		words = Arrays.stream(words).map(String::toUpperCase).toArray(String[]::new);
		
		// Equivalent to (x) -> System.out.println(x)
		Arrays.stream(words).forEach(System.out::println);
	}
	
	@Test
	@Ignore
	public void testThreadSafeFreeVariables() {
		// message should not be changed, because
		// stream operations can be multithreaded
		String message = "hello";
		Random rand = new Random();
		IntStream
			.generate(rand::nextInt)
			.limit(10)
			.forEach(x -> System.out.printf("%s %d\n", message, x));
	}
	
	interface Person {
		public long getId();
		public default String getName() {return "John Doe";}
		public static String findFirstMan() {
			return "adam";
		}
	}
	
	class Boss implements Person {
		@Override
		public long getId() {return 1l;}
		
	}
	
	@Test
	@Ignore
	public void testInterfaceDefaultMethod() {
		System.out.println(new Boss().getName());
	}
	
	@Test
	@Ignore
	public void testInterfaceStaticMethod() {
		System.out.println(Person.findFirstMan());
	}
	
	static class Employee implements Person {

		static final Random rand = new Random();
		long id = rand.nextLong();
		
		@Override
		public long getId() {
			return id;
		}
		public String toString() {
			return String.format("%d %s", id, getName());
		}
		
	}
	
	@Test
	@Ignore
	public void testKeyExtraction() {
		Employee[] emps = new Employee[]{new Employee(), new Employee(), new Employee()};
		System.out.println("Unsorted");
		Arrays.stream(emps).forEach(System.out::println);
		System.out.println("Sorted with key extractor");
		Arrays.sort(emps, Comparator.comparing(Employee::getId));
		Arrays.stream(emps).forEach(System.out::println);
	}
	
	@Test
	public void testFiles() {
		File file = new File("src/test/java");
		Arrays.stream(file.listFiles(f -> f.isDirectory()))
			.forEach(x -> System.out.println(x.getName()));
	}

}
