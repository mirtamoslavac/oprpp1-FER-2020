package hr.fer.oprpp1.lsystems.impl;

import hr.fer.zemris.lsystems.LSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LSystemBuilderImplTest {

    private LSystem lSystem1;
    private LSystem lSystem2;

    @BeforeEach
    void setUp() {
        this.lSystem1 = new LSystemBuilderImpl().setAxiom("F").registerProduction('F', "F+F--F+F").build();

        String[] data = new String[] {
                "axiom F",
                "production F F+F--F+F"
        };
        this.lSystem2 = new LSystemBuilderImpl().configureFromText(data).build();
    }

    @Test
    void testGenerateLevel0LSystem1() {
        assertEquals("F", this.lSystem1.generate(0));
    }

    @Test
    void testGenerateLevel0LSystem2() {
        assertEquals("F", this.lSystem2.generate(0));
    }

    @Test
    void testGenerateLevel1LSystem1() {
        assertEquals("F+F--F+F", this.lSystem1.generate(1));
    }

    @Test
    void testGenerateLevel1LSystem2() {
        assertEquals("F+F--F+F", this.lSystem2.generate(1));
    }

    @Test
    void testGenerateLevel2LSystem1() {
        assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", this.lSystem1.generate(2));
    }

    @Test
    void testGenerateLevel2LSystem2() {
        assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", this.lSystem2.generate(2));
    }

}
