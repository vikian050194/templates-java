package com.example.simple;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CalculatorTest {

    @Test
    public void TestCorrectResult() {
        // Arrange
        var calc = new Calculator();
        var expected = 42;

        // Act
        var actual = calc.add(40, 2);

        // Assert
        assertEquals(expected, actual);
    }
}
