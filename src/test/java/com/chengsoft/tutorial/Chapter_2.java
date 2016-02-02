package com.chengsoft.tutorial;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.FluentIterable;

public class Chapter_2 {
	
	Person tim = null;
	
	@Before
	public void before() {
		tim = Person.builder()
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
	}

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
	@Ignore
	public void testOptionCreate() {
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
	private Optional<Person> getEldestSibling(Person person) {
		// This is obviated with the use of Optional.flatMp()
//		if (!Optional.ofNullable(person.getSiblings()).isPresent()) {
//			return Optional.empty();
//		}
		// Create Optional from possible null list of siblings
		return Optional.ofNullable(person.getSiblings())
			// 
			.flatMap(x -> x.stream()
				.sorted(Comparator.comparing(Person::getAge).reversed())
				.findFirst());
	}
	
	@Test
	@Ignore
	public void testReduce() {
		List<String> words = Arrays.asList("Joe", "Tim", "Dan");
		// Reduce with variables
		words.stream()
		.mapToInt(String::length)
		.reduce((x, y) -> x+y)
		.ifPresent(System.out::println);
		
		// Reduce with method reference
		words.stream()
			.mapToInt(String::length)
			.reduce(Integer::sum)
			.ifPresent(System.out::println);
		
		// Use built-in sum() for int streams
		int result = words.stream()
		.mapToInt(String::length)
		.sum();
		System.out.println(result);
		
		// Reduce with identity (starting value), avoids Optional
		result = words.stream()
			.mapToInt(String::length)
			.reduce(5, (x,y) -> x+y);
		System.out.println(result);
		// Reduce using String object
		result = words.stream()
			.reduce(5, 
					(total, word) -> total+word.length(), 
					(total1, total2) -> total1 + total2);
		System.out.println(result);
		
		String combined = words.stream()
				// The identity determines the type for 'total'
			.reduce("", 
					(total, word) -> total+" "+word, 
					(total1, total2) -> total1 + total2);
		System.out.println(combined);
	}
	
	@Test
	@Ignore
	public void testCollector() {
		List<String> words = Arrays.asList("Joey", "Timmy", "Dan");
		HashSet<String> results = 
				words.stream()
				.filter(s -> s.length() > 3)
				// 1) Create the HashSet for each value
				// 2) Add the value to a HashSet
				// 3) Combine all the HashSets together
				.collect(HashSet::new, HashSet::add, HashSet::addAll);
		System.out.println(results);
		
		// String collector
		String result = words.stream()
		.filter(s -> s.length() > 3)
		.collect(Collectors.joining("+", "Sir ", " of US"));
		System.out.println(result);
		
		// Statistic collector
		DoubleSummaryStatistics stats = words.stream()
		.collect(Collectors.summarizingDouble(String::length));
		System.out.println(stats);
		
	}
	
	@Test
	@Ignore
	public void testMapCollecting() {
		// Map collector
		Map<String, Person> myMap = tim.getSiblings().stream()
				.collect(Collectors.toMap(Person::getName, Function.identity()));
		System.out.println(myMap);
		
		List<Person> people = Arrays.asList(
				Person.builder().name("John").age(25).build(),
				Person.builder().name("Tim").age(26).build(),
				Person.builder().name("Amy").age(27).build(),
				Person.builder().name("Will").age(25).build(),
				Person.builder().name("Seth").age(25).build());
		
		// Collect people by age with comma-separated names
		Map<Integer, String> peopleByAgeCommaMap = people.stream().collect(
				Collectors.toMap(
						Person::getAge, 
						Person::getName,
						// Resolve values that match to the same key
						// Here we just join the values with a comma
						(existingValue,newValue) -> existingValue+","+newValue));
		System.out.println(peopleByAgeCommaMap);
		
		// The same map as above using groupingBy() and mapping()
		Map<Integer, String> peopleByAgeGroupByMappingCommaDelimited = people.stream().collect(
				Collectors.groupingBy(Person::getAge, 
					Collectors.mapping(Person::getName, Collectors.joining(","))));
		System.out.println(peopleByAgeGroupByMappingCommaDelimited);
		
		// Collect people by age and put names in a Set
		Map<Integer, Set<String>> peopleByAgeMapWithSet = people.stream().collect(
				Collectors.toMap(
						Person::getAge, 
						// Return a collection with a single item
						p -> Collections.singleton(p.getName()),
						// Combine the two sets
						(existingValue,newValue) -> {
							Set<String> nameSet = new HashSet<>(existingValue);
							nameSet.addAll(newValue);
							return nameSet;
						}));
		System.out.println(peopleByAgeMapWithSet);
		
		// The same map as above using groupingBy() and mapping()
		Map<Integer, Set<String>> peopleByAgeMapWithSetGroupByMapping = people.stream().collect(
				Collectors.groupingBy(Person::getAge, 
						Collectors.mapping(Person::getName, Collectors.toSet())));
		System.out.println(peopleByAgeMapWithSetGroupByMapping);
		
		// Group by Person by age
		Map<Integer, List<Person>> peopleByAgeMapWithSetUsingGroupBy = people.stream().collect(
				Collectors.groupingBy(Person::getAge));
				// Use groupByConcurrent to return a concurrent map that can
				// be used in a parallel stream
//				Collectors.groupingByConcurrent(Person::getAge));
		System.out.println(peopleByAgeMapWithSetUsingGroupBy);
		
		// Partition just uses two lists and is faster
		Map<Boolean, List<Person>> peopleByAgeMapWithSetUsingPartioning = people.stream().collect(
				// Partition by people older than 25
				Collectors.partitioningBy(p -> p.getAge() > 25));
		System.out.println(peopleByAgeMapWithSetUsingPartioning);
		
	}
	
	private void printIntStream(IntStream stream) {
		System.out.println(Arrays.toString(stream.toArray()));
	}
	
	@Test
	@Ignore
	public void testPrimitiveStreams() {
		// Exclusive upper bound
		printIntStream(IntStream.range(0, 5));
		// Inclusive upper bound
		printIntStream(IntStream.rangeClosed(0, 5));
		
		// Map objects to ints
		printIntStream(Stream.of("apple", "boy", "cat").mapToInt(String::length));
		
		// Box primitives
		IntStream.range(0, 10).boxed();
	}
	
	@Test
	public void testParallelStreams() {
		// Must be stateless
		// Must be threadsafe
		// Use unordered to speed up certain operations
		
		// This will just get you the ANY element in the stream
		Stream.of("stream", "thread", "stateless").parallel().unordered().limit(1);
	}
}
