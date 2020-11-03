package hr.fer.oprpp1.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class Vector2DTest {

    private Vector2D tested2DVector;
    private static final double EPSILON = 1E-8;

    @Test
    public void testConstructor() {
        tested2DVector = new Vector2D(4, -4);
        assertEquals(4, tested2DVector.getX(), EPSILON);
        assertEquals(-4, tested2DVector.getY(), EPSILON);
    }

    @Test
    public void testGetX() {
        tested2DVector = new Vector2D(4, -4);
        assertEquals(4, tested2DVector.getX(), EPSILON);
    }

    @Test
    public void testGetY() {
        tested2DVector = new Vector2D(4, -4);
        assertEquals(-4, tested2DVector.getY(), EPSILON);
    }

    @Test
    public void testAdd() {
        tested2DVector = new Vector2D(4, -4);
        double oldX = tested2DVector.getX();
        double oldY = tested2DVector.getY();

        Vector2D offset2DVector = new Vector2D(4, -4);
        tested2DVector.add(offset2DVector);

        assertEquals(oldX + offset2DVector.getX(), tested2DVector.getX(), EPSILON);
        assertEquals(oldY + offset2DVector.getY(), tested2DVector.getY(), EPSILON);

        assertEquals(4, offset2DVector.getX(), EPSILON);
        assertEquals(-4, offset2DVector.getY(), EPSILON);
    }

    @Test
    public void testAddNullThrows() {
        tested2DVector = new Vector2D(4, -4);

        assertThrows(NullPointerException.class, () -> tested2DVector.add(null));
    }

    @Test
    public void testAdded() {
        tested2DVector = new Vector2D(4, -4);
        double oldX = tested2DVector.getX();
        double oldY = tested2DVector.getY();

        Vector2D offset2DVector = new Vector2D(4, -4);
        Vector2D new2DVector = tested2DVector.added(offset2DVector);

        assertEquals(oldX, tested2DVector.getX(), EPSILON);
        assertEquals(oldY, tested2DVector.getY(), EPSILON);

        assertEquals(4, offset2DVector.getX(), EPSILON);
        assertEquals(-4, offset2DVector.getY(), EPSILON);

        assertEquals(oldX + offset2DVector.getX(), new2DVector.getX(), EPSILON);
        assertEquals(oldY + offset2DVector.getY(), new2DVector.getY(), EPSILON);
    }

    @Test
    public void testAddedNullThrows() {
        tested2DVector = new Vector2D(4, -4);

        assertThrows(NullPointerException.class, () -> tested2DVector.added(null));
    }

    @Test
    public void testRotate() {
        tested2DVector = new Vector2D(4, -4);

        tested2DVector.rotate(1.8);

        assertEquals(2.986582145, tested2DVector.getX(), EPSILON);
        assertEquals(4.804198902, tested2DVector.getY(), EPSILON);
    }

    @Test
    public void testRotateFor0Degrees() {
        tested2DVector = new Vector2D(4, -4);
        double oldX = tested2DVector.getX();
        double oldY = tested2DVector.getY();

        tested2DVector.rotate(0);

        assertEquals(oldX, tested2DVector.getX(), EPSILON);
        assertEquals(oldY, tested2DVector.getY(), EPSILON);
    }

    @Test
    public void testRotateFor360Degrees() {
        tested2DVector = new Vector2D(4, -4);
        double oldX = tested2DVector.getX();
        double oldY = tested2DVector.getY();

        tested2DVector.rotate(2 * Math.PI);

        assertEquals(oldX, tested2DVector.getX(), EPSILON);
        assertEquals(oldY, tested2DVector.getY(), EPSILON);
    }

    @Test
    public void testRotated() {
        tested2DVector = new Vector2D(4, -4);
        double oldX = tested2DVector.getX();
        double oldY = tested2DVector.getY();

        Vector2D new2DVector = tested2DVector.rotated(1.8);

        assertEquals(oldX, tested2DVector.getX(), EPSILON);
        assertEquals(oldY, tested2DVector.getY(), EPSILON);

        assertEquals(2.986582145, new2DVector.getX(), EPSILON);
        assertEquals(4.804198902, new2DVector.getY(), EPSILON);
    }

    @Test
    public void testRotatedFor0Degrees() {
        tested2DVector = new Vector2D(4, -4);

        assertEquals(tested2DVector.rotated(0), tested2DVector);
    }

    @Test
    public void testRotatedFor360Degrees() {
        tested2DVector = new Vector2D(4, -4);
        double oldX = tested2DVector.getX();
        double oldY = tested2DVector.getY();

        Vector2D new2DVector = tested2DVector.rotated(2 * Math.PI);

        assertEquals(oldX, tested2DVector.getX(), EPSILON);
        assertEquals(oldY, tested2DVector.getY(), EPSILON);

        assertEquals(oldX, new2DVector.getX(), EPSILON);
        assertEquals(oldY, new2DVector.getY(), EPSILON);
    }

    @Test
    public void testScalePositive() {
        tested2DVector = new Vector2D(4, -4);

        tested2DVector.scale(1.8);

        assertEquals(7.2, tested2DVector.getX(), EPSILON);
        assertEquals(-7.2, tested2DVector.getY(), EPSILON);
    }

    @Test
    public void testScaleBy0() {
        tested2DVector = new Vector2D(4, -4);

        tested2DVector.scale(0);

        assertEquals(0, tested2DVector.getX(), EPSILON);
        assertEquals(0, tested2DVector.getY(), EPSILON);
    }

    @Test
    public void testScaleBy1() {
        tested2DVector = new Vector2D(4, -4);
        double oldX = tested2DVector.getX();
        double oldY = tested2DVector.getY();

        tested2DVector.scale(1);

        assertEquals(oldX, tested2DVector.getX(), EPSILON);
        assertEquals(oldY, tested2DVector.getY(), EPSILON);
    }

    @Test
    public void testScaleNegative() {
        tested2DVector = new Vector2D(4, -4);

        tested2DVector.scale(-1.8);

        assertEquals(-7.2, tested2DVector.getX(), EPSILON);
        assertEquals(7.2, tested2DVector.getY(), EPSILON);
    }

    @Test
    public void testScaledPositive() {
        tested2DVector = new Vector2D(4, -4);
        double oldX = tested2DVector.getX();
        double oldY = tested2DVector.getY();

        Vector2D new2DVector = tested2DVector.scaled(1.8);

        assertEquals(oldX, tested2DVector.getX(), EPSILON);
        assertEquals(oldY, tested2DVector.getY(), EPSILON);

        assertEquals(7.2, new2DVector.getX(), EPSILON);
        assertEquals(-7.2, new2DVector.getY(), EPSILON);
    }

    @Test
    public void testScaledBy0() {
        tested2DVector = new Vector2D(4, -4);
        double oldX = tested2DVector.getX();
        double oldY = tested2DVector.getY();

        Vector2D new2DVector = tested2DVector.scaled(0);

        assertEquals(oldX, tested2DVector.getX(), EPSILON);
        assertEquals(oldY, tested2DVector.getY(), EPSILON);

        assertEquals(0, new2DVector.getX(), EPSILON);
        assertEquals(0, new2DVector.getY(), EPSILON);
    }

    @Test
    public void testScaledBy1() {
        tested2DVector = new Vector2D(4, -4);
        double oldX = tested2DVector.getX();
        double oldY = tested2DVector.getY();

        Vector2D new2DVector = tested2DVector.scaled(1);

        assertEquals(oldX, tested2DVector.getX(), EPSILON);
        assertEquals(oldY, tested2DVector.getY(), EPSILON);

        assertEquals(oldX, new2DVector.getX(), EPSILON);
        assertEquals(oldY, new2DVector.getY(), EPSILON);
    }

    @Test
    public void testScaledNegative() {
        tested2DVector = new Vector2D(4, -4);
        double oldX = tested2DVector.getX();
        double oldY = tested2DVector.getY();

        Vector2D new2DVector = tested2DVector.scaled(-1.8);

        assertEquals(oldX, tested2DVector.getX(), EPSILON);
        assertEquals(oldY, tested2DVector.getY(), EPSILON);

        assertEquals(-7.2, new2DVector.getX(), EPSILON);
        assertEquals(7.2, new2DVector.getY(), EPSILON);
    }

    @Test
    public void testCopy() {
        tested2DVector = new Vector2D(4, -4);
        Vector2D new2DVector = tested2DVector.copy();

        assertEquals(tested2DVector, new2DVector);
    }

    @Test
    public void testEqualsTrue() {
        tested2DVector = new Vector2D(4, -4);

        assertEquals(tested2DVector, new Vector2D(4, -4));
    }

    @Test
    public void testEqualsFalse() {
        tested2DVector = new Vector2D(4, -4);

        assertNotEquals(tested2DVector,  new Vector2D(4, 4));
    }

}
