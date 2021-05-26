package hr.fer.oprpp1.gui.layouts;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

public class CalcLayoutTest {

    @Test
    void testPositionOutsideOfBoundsThrows() {
        JPanel p = new JPanel(new CalcLayout());

        assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("both wrong"), new RCPosition(0, 0)));
        assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("row < 1"), new RCPosition(0, 2)));
        assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("row > 5"), new RCPosition(6, 6)));
        assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("column < 1"), new RCPosition(2, -4)));
        assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("column > 7"), new RCPosition(1, 10)));
    }

    @Test
    void testDisplayThrows() {
        JPanel p = new JPanel(new CalcLayout());

        assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("column = 2"), new RCPosition(1, 2)));
        assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("column = 3"), new RCPosition(1, 3)));
        assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("column = 4"), new RCPosition(1, 4)));
        assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("column = 5"), new RCPosition(1, 5)));
    }

    @Test
    void testSetAlreadySetConstraintThrows() {
        JPanel p = new JPanel(new CalcLayout());

        p.add(new JLabel("x"), new RCPosition(5, 4));
        assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("already added"), new RCPosition(5, 4)));

        p.add(new JLabel("won't throw"), new RCPosition(1, 7));
    }

    @Test
    void testCheckPreferredSizeDimensions() {
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10,30));
        JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20,15));
        p.add(l1, new RCPosition(2,2));
        p.add(l2, new RCPosition(3,3));
        Dimension dim = p.getPreferredSize();

        assertEquals(new Dimension(152, 158), dim);
    }

    @Test
    void testCheckPreferredSizeDimensions2() {
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108, 15));
        JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16, 30));
        p.add(l1, new RCPosition(1, 1));
        p.add(l2, new RCPosition(3, 3));
        Dimension dim = p.getPreferredSize();

        assertEquals(new Dimension(152, 158), dim);
    }



}
