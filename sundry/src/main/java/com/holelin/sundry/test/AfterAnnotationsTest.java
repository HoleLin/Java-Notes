package com.holelin.sundry.test;

import com.holelin.sundry.SundryApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SundryApplication.class)
public class AfterAnnotationsTest {

	@Test
	public void addNumber()
	{
		System.out.println("Running test -> ");
	}

	@AfterAll
	public static void cleanUp(){
		System.out.println("After All cleanUp() method called");
	}

	@AfterEach
	public void cleanUpEach(){
		System.out.println("After Each cleanUpEach() method called");
	}
}