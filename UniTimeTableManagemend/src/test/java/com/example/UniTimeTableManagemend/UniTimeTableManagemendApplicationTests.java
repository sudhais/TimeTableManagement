package com.example.UniTimeTableManagemend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class UniTimeTableManagemendApplicationTests {

	Calculator calculator = new Calculator();
	@Test
	void itShould() {
		int value = calculator.add(20,10);
		assertEquals(30,value);
	}

	class Calculator{

		int add(int a, int b){
			return a+b;
		}
	}

}
