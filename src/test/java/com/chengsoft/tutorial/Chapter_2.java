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
import java.util.List;
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
	public void test() {
		
	}

}
