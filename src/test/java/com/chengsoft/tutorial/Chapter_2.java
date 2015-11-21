package com.chengsoft.tutorial;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;

public class Chapter_2 {

	@Test
	@Ignore
	public void testStreamGenerator() {
		// Infinite stream of random doubles
		Stream<Double> randoms = Stream.generate(Math::random);
		long randomNumbersLessThan0_5 = 
				randoms
			.limit(100)
			.filter(x -> x < 0.5)
			.count();
		System.out.println(randomNumbersLessThan0_5);
	}
	
	@Test
	@Ignore
	public void testStreamIterate() {
		Stream<BigInteger> integers
		   = Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.ONE));
		integers
			.limit(10)
			.forEach(System.out::println);
	}
	
	@Test
	@Ignore
	public void testAutoCloseFileAndStream() {
		Path path = Paths.get("src/test/resources/test.txt");
		try (Stream<String> lines = Files.lines(path)) {
			// Do something with lines			
			System.out.println(lines.count());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Ignore
	public void testFlatMap() {
		List<String> boys = Arrays.asList("kevin", "adam", "tim");
		List<String> girls = Arrays.asList("tina", "karen", "crystal");
		List<List<String>> both = new ArrayList<>();
		both.add(boys);
		both.add(girls);
		both.stream()
			// Get the stream of each list and create a new stream
			// with the streams concatenated into one
			.flatMap(Collection::stream)
			// Get the first 2 letters of each name
			.map(n -> n.substring(0, 2))
			.sorted()
			.forEach(System.out::println);
	}
	
	@Test
	@Ignore
	public void testPeek() {
		String myString = "boat";
		String[] arr = Stream.of(myString.split("a"))
			.map(String::toUpperCase)
			.peek(System.out::println)
			.toArray(String[]::new);
	}
	
	@Test
	@Ignore
	public void testStateful() {
		// Distinct list of words
		Stream.of("apple", "banana", "apple", "banana")
			.distinct()
			.forEach(System.out::println);
		System.out.println("---");
		// Sort from longest to shortest word
		Stream.of("short", "very long", "tiny")
			.sorted(Comparator.comparing(String::length).reversed())
			.forEach(System.out::println);
	}
	
	@Test
	@Ignore
	public void testReductions() {
		// Returns the alphabetically largest word
		Optional<String> maxWord 
			= Stream.of("go", "aaaaaaaaaaaaaaa", "microsoft").max(String::compareToIgnoreCase);
		if (maxWord.isPresent())
			System.out.println(maxWord.get());
		System.out.println("---");
		// Find first word starting with 'g'
		List<String> wordList = Arrays.asList("of", "are", "because");
		System.out.printf("wordList: %s\n", wordList);
		Optional<String> findFirst
			= wordList.stream()
				.filter(s -> s.startsWith("g"))
				.findFirst();
		if (!findFirst.isPresent())
			System.out.println("Could not find word starting with 'g'");
		
		// Great for parallel processing
		findFirst
		= wordList.stream().parallel().filter(s -> s.startsWith("a")).findFirst();
		// Any match doesn't need filter
		boolean anyMatch = wordList.stream().anyMatch(s -> s.contains("a"));
		System.out.println("Any word contain 'a': " + anyMatch);
	}

	@Test
	@Ignore
	public void testOptionalConsume() {
		List<String> words = Arrays.asList("Joe", "Tim", "Dan");
		// Return default String if not present
		String result = words.stream()
			.filter(s -> s.equalsIgnoreCase("Jim"))
			.findFirst()
			.orElse("not found");
		System.out.println(result);
		System.out.println("---");
		try {
			// Throw exception if not present
			words.stream()
					.filter(s -> s.equalsIgnoreCase("Jim"))
					.findFirst()
					.orElseThrow(() -> new RuntimeException("can't find Jim!"));
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			System.err.println("---");
		}
		// Construct person if name exists
		words.stream()
			.filter(s -> s.equalsIgnoreCase("Tim"))
			.findFirst()
			.map(p -> Person.builder().name(p).build())
			.ifPresent(System.out::println);
	}
	
	@Test
	public void testOptionCreate() {
		Person tim = Person.builder()
			.name("Tim")
			.age(28)
			.siblings(Arrays.asList(
				Person.builder()
					.name("Jonathan")
					.age(31)
					.build(),
				Person.builder()
					.name("Bill")
					.age(10)
					.build(),
				Person.builder()
					.name("Bertha")
					.age(34)
					.build()))
			.build();
		// First time with existing siblings
		System.out.printf("Tim's Eldest Sibling: ");
		getEldestSibling(tim)
			.ifPresent(System.out::println);
		// Remove siblings
		tim.setSiblings(null);
		System.out.printf("Tim's Eldest Sibling (after removal): ");
		getEldestSibling(tim)
		.ifPresent(System.out::println);
		
	}
	public Optional<Person> getEldestSibling(Person person) {
		if (!Optional.ofNullable(person.getSiblings()).isPresent()) {
			return Optional.empty();
		}
		return person.getSiblings().stream()
			.sorted(Comparator.comparing(Person::getAge).reversed())
			.findFirst();
	}
}
