package hr.fer.oprpp1.hw01;

import static org.junit.jupiter.api.Assertions.*;
import static java.lang.Math.sqrt;
import static java.lang.Math.PI;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;

import org.junit.jupiter.api.Test;

public class ComplexNumberTest {

    private ComplexNumber testedComplexNumber;
    private static final double EPSILON = 1E-8;

    @Test
    public void testConstructorCalculatedMagnitudeAndPhase() {
        testedComplexNumber = new ComplexNumber(4, -4);
        assertEquals(sqrt(32), testedComplexNumber.getMagnitude(), EPSILON);
        assertEquals(7 / 4. * PI, testedComplexNumber.getAngle(), EPSILON);
    }

    @Test
    public void testConstructorCalculatedMagnitudeAndPhaseZeroes() {
        testedComplexNumber = new ComplexNumber(0, 0);
        assertEquals(0, testedComplexNumber.getMagnitude(), EPSILON);
        assertEquals(0, testedComplexNumber.getAngle(), EPSILON);
    }

    @Test
    public void testConstructorCalculatedMagnitudeAndPhaseNaN() {
        testedComplexNumber = new ComplexNumber(NaN, NaN);
        assertEquals(NaN, testedComplexNumber.getMagnitude());
        assertEquals(NaN, testedComplexNumber.getAngle());
    }

    @Test
    public void testConstructorCalculatedMagnitudeAndPhaseInfinity() {
        testedComplexNumber = new ComplexNumber(POSITIVE_INFINITY, NEGATIVE_INFINITY);
        assertEquals(POSITIVE_INFINITY, testedComplexNumber.getMagnitude());
        assertEquals(5.497787143782138, testedComplexNumber.getAngle(), EPSILON);
    }


    @Test
    public void testNewComplexNumberFromMagnitudeAndAngle() {
        ComplexNumber expectedComplexNumber = new ComplexNumber(sqrt(3),3);
        ComplexNumber testedComplexNumber = ComplexNumber.fromMagnitudeAndAngle(sqrt(12), 1 / 3. * PI);
        assertEquals(expectedComplexNumber.getReal(), testedComplexNumber.getReal(), EPSILON);
        assertEquals(testedComplexNumber.getImaginary(), testedComplexNumber.getImaginary(), EPSILON);
    }

    @Test
    public void testNewComplexNumberFromMagnitudeAndAngleNaN() {
        ComplexNumber expectedComplexNumber = new ComplexNumber(NaN,NaN);
        ComplexNumber testedComplexNumber = ComplexNumber.fromMagnitudeAndAngle(NaN, NaN);
        assertEquals(expectedComplexNumber.getReal(), testedComplexNumber.getReal());
        assertEquals(testedComplexNumber.getImaginary(), testedComplexNumber.getImaginary());
    }

    @Test
    public void testNewComplexNumberFromMagnitudeAndAngleInfinity() {
        ComplexNumber expectedComplexNumber = new ComplexNumber(POSITIVE_INFINITY,NEGATIVE_INFINITY);
        ComplexNumber testedComplexNumber = ComplexNumber.fromMagnitudeAndAngle(POSITIVE_INFINITY, 5.497787143782138);
        assertEquals(expectedComplexNumber.getReal(), testedComplexNumber.getReal());
        assertEquals(testedComplexNumber.getImaginary(), testedComplexNumber.getImaginary());
    }

    @Test
    public void testComplexNumberInFirstQuadrant() {
        ComplexNumber complexNumberQI = new ComplexNumber(1,1);
        assertTrue(0 <= complexNumberQI.getAngle() && complexNumberQI.getAngle() < PI / 2.);
        assertFalse(PI / 2. <= complexNumberQI.getAngle() && complexNumberQI.getAngle() < PI);
        assertFalse(PI  <= complexNumberQI.getAngle() && complexNumberQI.getAngle() < 3 * PI / 2.);
        assertFalse(3 * PI / 2.  <= complexNumberQI.getAngle() && complexNumberQI.getAngle() < 2 * PI);
    }

    @Test
    public void testComplexNumberInSecondQuadrant() {
        ComplexNumber complexNumberQII = new ComplexNumber(-1,1);
        assertFalse(0 <= complexNumberQII.getAngle() && complexNumberQII.getAngle() < PI / 2.);
        assertTrue(PI / 2. <= complexNumberQII.getAngle() && complexNumberQII.getAngle() < PI);
        assertFalse(PI  <= complexNumberQII.getAngle() && complexNumberQII.getAngle() < 3 * PI / 2.);
        assertFalse(3 * PI / 2.  <= complexNumberQII.getAngle() && complexNumberQII.getAngle() < 2 * PI);
    }

    @Test
    public void testComplexNumberInThirdQuadrant() {
        ComplexNumber complexNumberQIII = new ComplexNumber(-1,-1);
        assertFalse(0 <= complexNumberQIII.getAngle() && complexNumberQIII.getAngle() < PI / 2.);
        assertFalse(PI / 2. <= complexNumberQIII.getAngle() && complexNumberQIII.getAngle() < PI);
        assertTrue(PI  <= complexNumberQIII.getAngle() && complexNumberQIII.getAngle() < 3 * PI / 2.);
        assertFalse(3 * PI / 2.  <= complexNumberQIII.getAngle() && complexNumberQIII.getAngle() < 2 * PI);
    }

    @Test
    public void testComplexNumberInFourthQuadrant() {
        ComplexNumber complexNumberQIV = new ComplexNumber(1,-1);
        assertFalse(0 <= complexNumberQIV.getAngle() && complexNumberQIV.getAngle() < PI / 2.);
        assertFalse(PI / 2. <= complexNumberQIV.getAngle() && complexNumberQIV.getAngle() < PI);
        assertFalse(PI  <= complexNumberQIV.getAngle() && complexNumberQIV.getAngle() < 3 * PI / 2.);
        assertTrue(3 * PI / 2.  <= complexNumberQIV.getAngle() && complexNumberQIV.getAngle() < 2 * PI);
    }

    @Test
    public void testCreateNewComplexNumberFromReal() {
        testedComplexNumber = ComplexNumber.fromReal(327);
        assertEquals(327, testedComplexNumber.getReal(), EPSILON);
        assertEquals(0, testedComplexNumber.getImaginary(), EPSILON);
    }

    @Test
    public void testCreateNewComplexNumberFromRealNaN() {
        testedComplexNumber = ComplexNumber.fromReal(NaN);
        assertEquals(NaN, testedComplexNumber.getReal());
        assertEquals(0, testedComplexNumber.getImaginary(), EPSILON);
    }

    @Test
    public void testCreateNewComplexNumberFromRealInfinity() {
        testedComplexNumber = ComplexNumber.fromReal(-POSITIVE_INFINITY);
        assertEquals(NEGATIVE_INFINITY, testedComplexNumber.getReal());
        assertEquals(0, testedComplexNumber.getImaginary(), EPSILON);
    }

    @Test
    public void testCreateNewComplexNumberFromImaginary() {
        testedComplexNumber = ComplexNumber.fromImaginary(-17);
        assertEquals(0, testedComplexNumber.getReal(), EPSILON);
        assertEquals(-17, testedComplexNumber.getImaginary(), EPSILON);
    }

    @Test
    public void testCreateNewComplexNumberFromImaginaryNaN() {
        testedComplexNumber = ComplexNumber.fromImaginary(NaN);
        assertEquals(0, testedComplexNumber.getReal(), EPSILON);
        assertEquals(NaN, testedComplexNumber.getImaginary());
    }

    @Test
    public void testCreateNewComplexNumberFromImaginaryInfinity() {
        testedComplexNumber = ComplexNumber.fromImaginary(-POSITIVE_INFINITY);
        assertEquals(0, testedComplexNumber.getReal(), EPSILON);
        assertEquals(NEGATIVE_INFINITY, testedComplexNumber.getImaginary());
    }

    @Test
    public void testParsePositiveRealAndPositiveImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(1.1, 2.2).toString(), ComplexNumber.parse("1.1+2.2i").toString());
    }

    @Test
    public void testParsePositiveRealAndNegativeImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(1.1, -2.2).toString(), ComplexNumber.parse("1.1-2.2i").toString());
    }

    @Test
    public void testParseNegativeRealAndPositiveImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(-1.1, 2.2).toString(), ComplexNumber.parse("-1.1+2.2i").toString());
    }

    @Test
    public void testParseNegativeRealAndNegativeImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(-1.1, -2.2).toString(), ComplexNumber.parse("-1.1-2.2i").toString());
    }

    @Test
    public void testParseWholeRealAndWholeImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(123, 321).toString(), ComplexNumber.parse("123+321i").toString());
    }

    @Test
    public void testParseWholeRealAndOnlyDecimalImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(123, .321).toString(), ComplexNumber.parse("123+0.321i").toString());
    }

    @Test
    public void testParseWholeRealAndBothWholeAndDecimalImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(123, 123.321).toString(), ComplexNumber.parse("123+123.321i").toString());
    }

    @Test
    public void testParseOnlyDecimalRealAndWholeImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(.123, 321).toString(), ComplexNumber.parse(".123+321i").toString());
    }

    @Test
    public void testParseOnlyDecimalRealAndOnlyDecimalImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(.123, .321).toString(), ComplexNumber.parse(".123+.321i").toString());
    }

    @Test
    public void testParseOnlyDecimalRealAndBothWholeAndDecimalImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(.123, 123.321).toString(), ComplexNumber.parse(".123+123.321i").toString());
    }

    @Test
    public void testParseBothWholeAndDecimalRealAndWholeImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(456.123, 321).toString(), ComplexNumber.parse("456.123+321i").toString());
    }

    @Test
    public void testParseBothWholeAndDecimalRealAndOnlyDecimalImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(456.123, .321).toString(), ComplexNumber.parse("456.123+.321i").toString());
    }

    @Test
    public void testParseBothWholeAndDecimalRealAndBothWholeAndDecimalImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(456.123, 789.321).toString(), ComplexNumber.parse("456.123+789.321i").toString());
    }

    @Test
    public void testParseWholeRealAndPositiveImaginaryUnitImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(456, 1).toString(), ComplexNumber.parse("456+i").toString());
    }

    @Test
    public void testParseOnlyDecimalRealAndPositiveImaginaryUnitImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(-0.456, 1).toString(), ComplexNumber.parse("-0.456+i").toString());
    }

    @Test
    public void testParseBothWholeAndDecimalRealAndPositiveImaginaryUnitImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(456.8, 1).toString(), ComplexNumber.parse("456.8+i").toString());
    }

    @Test
    public void testParseWholeRealAndNegativeImaginaryUnitImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(456, -1).toString(), ComplexNumber.parse("456-i").toString());
    }

    @Test
    public void testParseOnlyDecimalRealAndNegativeImaginaryUnitImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(.456, -1).toString(), ComplexNumber.parse(".456-i").toString());
    }

    @Test
    public void testParseBothWholeAndDecimalRealAndNegativeImaginaryUnitImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(85.456, -1).toString(), ComplexNumber.parse("85.456-i").toString());
    }

    @Test
    public void testParseWholeRealToComplexNumberPositive() {
        assertEquals(ComplexNumber.fromReal(89).toString(), ComplexNumber.parse("89").toString());
    }

    @Test
    public void testParseWholeRealToComplexNumberPositiveSign() {
        assertEquals(ComplexNumber.fromReal(89).toString(), ComplexNumber.parse("+89").toString());
    }

    @Test
    public void testParseWholeRealToComplexNumberNegative() {
        assertEquals(ComplexNumber.fromReal(-89).toString(), ComplexNumber.parse("-89").toString());
    }

    @Test
    public void testParseOnlyDecimalRealToComplexNumberPositive() {
        assertEquals(ComplexNumber.fromReal(.03).toString(), ComplexNumber.parse(".03").toString());
    }

    @Test
    public void testParseOnlyDecimalRealToComplexNumberPositiveSign() {
        assertEquals(ComplexNumber.fromReal(.03).toString(), ComplexNumber.parse("+.03").toString());
    }

    @Test
    public void testParseOnlyDecimalRealToComplexNumberNegative() {
        assertEquals(ComplexNumber.fromReal(-.03).toString(), ComplexNumber.parse("-.03").toString());
    }

    @Test
    public void testParseBothWholeAndDecimalRealToComplexNumberPositive() {
        assertEquals(ComplexNumber.fromReal(0.03).toString(), ComplexNumber.parse("+0.03").toString());
        assertEquals(ComplexNumber.fromReal(0.03).toString(), ComplexNumber.parse("0.03").toString());

        assertEquals(ComplexNumber.fromReal(17.03).toString(), ComplexNumber.parse("+17.03").toString());
        assertEquals(ComplexNumber.fromReal(17.03).toString(), ComplexNumber.parse("17.03").toString());
    }

    @Test
    public void testParseBothWholeAndDecimalRealToComplexNumberPositiveSign() {
        assertEquals(ComplexNumber.fromReal(0.03).toString(), ComplexNumber.parse("+0.03").toString());

        assertEquals(ComplexNumber.fromReal(17.03).toString(), ComplexNumber.parse("+17.03").toString());
    }

    @Test
    public void testParseBothWholeAndDecimalRealToComplexNumberNegative() {
        assertEquals(ComplexNumber.fromReal(-0.03).toString(), ComplexNumber.parse("-0.03").toString());

        assertEquals(ComplexNumber.fromReal(-17.03).toString(), ComplexNumber.parse("-17.03").toString());
    }

    @Test
    public void testParseNaNRealToComplexNumberPositive() {
        assertEquals(ComplexNumber.fromReal(NaN).toString(), ComplexNumber.parse("NaN").toString());
    }

    @Test
    public void testParseNaNRealToComplexNumberPositiveSign() {
        assertEquals(ComplexNumber.fromReal(NaN).toString(), ComplexNumber.parse("+NaN").toString());
    }

    @Test
    public void testParseNaNRealToComplexNumberNegative() {
        assertEquals(ComplexNumber.fromReal(NaN).toString(), ComplexNumber.parse("-NaN").toString());
    }

    @Test
    public void testParseInfiniteRealToComplexNumberPositive() {
        assertEquals(ComplexNumber.fromReal(POSITIVE_INFINITY).toString(), ComplexNumber.parse("Infinity").toString());
    }

    @Test
    public void testParseInfiniteRealToComplexNumberPositiveSign() {
        assertEquals(ComplexNumber.fromReal(POSITIVE_INFINITY).toString(), ComplexNumber.parse("+Infinity").toString());
    }

    @Test
    public void testParseInfiniteRealToComplexNumberNegative() {
        assertEquals(ComplexNumber.fromReal(NEGATIVE_INFINITY).toString(), ComplexNumber.parse("-Infinity").toString());
    }

    @Test
    public void testParseWholeImaginaryToComplexNumberPositive() {
        assertEquals(ComplexNumber.fromImaginary(89).toString(), ComplexNumber.parse("89i").toString());
    }

    @Test
    public void testParseWholeImaginaryToComplexNumberPositiveSign() {
        assertEquals(ComplexNumber.fromImaginary(89).toString(), ComplexNumber.parse("+89i").toString());
    }

    @Test
    public void testParseWholeImaginaryToComplexNumberNegative() {
        assertEquals(ComplexNumber.fromImaginary(-89).toString(), ComplexNumber.parse("-89i").toString());
    }

    @Test
    public void testParseOnlyDecimalImaginaryToComplexNumberPositive() {
        assertEquals(ComplexNumber.fromImaginary(.03).toString(), ComplexNumber.parse(".03i").toString());
    }

    @Test
    public void testParseOnlyDecimalImaginaryToComplexNumberPositiveSign() {
        assertEquals(ComplexNumber.fromImaginary(.03).toString(), ComplexNumber.parse("+.03i").toString());
    }

    @Test
    public void testParseOnlyDecimalImaginaryToComplexNumberNegative() {
        assertEquals(ComplexNumber.fromImaginary(-.03).toString(), ComplexNumber.parse("-.03i").toString());
    }

    @Test
    public void testParseBothWholeAndDecimalImaginaryToComplexNumberPositive() {
        assertEquals(ComplexNumber.fromImaginary(0.03).toString(), ComplexNumber.parse("0.03i").toString());

        assertEquals(ComplexNumber.fromImaginary(17.03).toString(), ComplexNumber.parse("17.03i").toString());
    }

    @Test
    public void testParseBothWholeAndDecimalImaginaryToComplexNumberPositiveSign() {
        assertEquals(ComplexNumber.fromImaginary(0.03).toString(), ComplexNumber.parse("+0.03i").toString());

        assertEquals(ComplexNumber.fromImaginary(17.03).toString(), ComplexNumber.parse("+17.03i").toString());
    }

    @Test
    public void testParseBothWholeAndDecimalImaginaryToComplexNumberNegative() {
        assertEquals(ComplexNumber.fromImaginary(-0.03).toString(), ComplexNumber.parse("-0.03i").toString());

        assertEquals(ComplexNumber.fromImaginary(-17.03).toString(), ComplexNumber.parse("-17.03i").toString());
    }

    @Test
    public void testParseImaginaryUnitToComplexNumberPositive() {
        assertEquals(ComplexNumber.fromImaginary(1).toString(), ComplexNumber.parse("i").toString());
    }

    @Test
    public void testParseImaginaryUnitToComplexNumberPositiveSign() {
        assertEquals(ComplexNumber.fromImaginary(1).toString(), ComplexNumber.parse("+i").toString());
    }

    @Test
    public void testParseImaginaryUnitToComplexNumberNegative() {
        assertEquals(ComplexNumber.fromImaginary(-1).toString(), ComplexNumber.parse("-i").toString());
    }

    @Test
    public void testParseNaNToComplexNumberPositive() {
        assertEquals(ComplexNumber.fromImaginary(NaN).toString(), ComplexNumber.parse("NaNi").toString());
    }

    @Test
    public void testParseNaNImaginaryToComplexNumberPositiveSign() {
        assertEquals(ComplexNumber.fromImaginary(NaN).toString(), ComplexNumber.parse("+NaNi").toString());
    }

    @Test
    public void testParseNaNImaginaryToComplexNumberNegative() {
        assertEquals(ComplexNumber.fromImaginary(NaN).toString(), ComplexNumber.parse("-NaNi").toString());
    }

    @Test
    public void testParseInfinityImaginaryToComplexNumberPositive() {
        assertEquals(ComplexNumber.fromImaginary(POSITIVE_INFINITY).toString(), ComplexNumber.parse("Infinityi").toString());
    }

    @Test
    public void testParseInfinityImaginaryToComplexNumberPositiveSign() {
        assertEquals(ComplexNumber.fromImaginary(POSITIVE_INFINITY).toString(), ComplexNumber.parse("+Infinityi").toString());
    }

    @Test
    public void testParseInfinityImaginaryToComplexNumberNegative() {
        assertEquals(ComplexNumber.fromImaginary(NEGATIVE_INFINITY).toString(), ComplexNumber.parse("-Infinityi").toString());
    }

    @Test
    public void testParsePositiveNaNRealAndPositiveInfinityImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(NaN, POSITIVE_INFINITY).toString(), ComplexNumber.parse("NaN+Infinityi").toString());
    }

    @Test
    public void testParsePositiveNaNRealAndNegativeInfinityImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(NaN, NEGATIVE_INFINITY).toString(), ComplexNumber.parse("NaN-Infinityi").toString());
    }

    @Test
    public void testParsePositiveNaNRealAndPositiveNaNImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(NaN, NaN).toString(), ComplexNumber.parse("NaN+NaNi").toString());
    }

    @Test
    public void testParsePositiveNaNRealAndNegativeNaNImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(NaN, NaN).toString(), ComplexNumber.parse("NaN-NaNi").toString());
    }

    @Test
    public void testParseNegativeNaNRealAndPositiveInfinityImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(NaN, POSITIVE_INFINITY).toString(), ComplexNumber.parse("-NaN+Infinityi").toString());
    }

    @Test
    public void testParseNegativeNaNRealAndNegativeInfinityImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(NaN, NEGATIVE_INFINITY).toString(), ComplexNumber.parse("-NaN-Infinityi").toString());
    }

    @Test
    public void testParseNegativeNaNRealAndPositiveNaNImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(NaN, NaN).toString(), ComplexNumber.parse("-NaN+NaNi").toString());
    }

    @Test
    public void testParseNegativeNaNRealAndNegativeNaNImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(NaN, NaN).toString(), ComplexNumber.parse("-NaN-NaNi").toString());
    }

    @Test
    public void testParsePositiveInfinityRealAndPositiveInfinityImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(POSITIVE_INFINITY, POSITIVE_INFINITY).toString(), ComplexNumber.parse("Infinity+Infinityi").toString());
    }

    @Test
    public void testParsePositiveInfinityRealAndNegativeInfinityImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(POSITIVE_INFINITY, NEGATIVE_INFINITY).toString(), ComplexNumber.parse("Infinity-Infinityi").toString());
    }

    @Test
    public void testParsePositiveInfinityRealAndPositiveNaNImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(POSITIVE_INFINITY, NaN).toString(), ComplexNumber.parse("Infinity+NaNi").toString());
    }

    @Test
    public void testParsePositiveInfinityRealAndNegativeNaNImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(POSITIVE_INFINITY, NaN).toString(), ComplexNumber.parse("Infinity-NaNi").toString());
    }

    @Test
    public void testParseNegativeInfinityRealAndPositiveInfinityImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(NEGATIVE_INFINITY, POSITIVE_INFINITY).toString(), ComplexNumber.parse("-Infinity+Infinityi").toString());
    }

    @Test
    public void testParseNegativeInfinityRealAndNegativeInfinityImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(NEGATIVE_INFINITY, NEGATIVE_INFINITY).toString(), ComplexNumber.parse("-Infinity-Infinityi").toString());
    }

    @Test
    public void testParseNegativeInfinityRealAndPositiveNaNImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(NEGATIVE_INFINITY, NaN).toString(), ComplexNumber.parse("-Infinity+NaNi").toString());
    }

    @Test
    public void testParseNegativeInfinityRealAndNegativeNaNImaginaryToComplexNumber() {
        assertEquals(new ComplexNumber(NEGATIVE_INFINITY, NaN).toString(), ComplexNumber.parse("-Infinity-NaNi").toString());
    }

    @Test
    public void testParseNullToComplexNumberShouldThrow() {
        assertThrows(NullPointerException.class, () -> ComplexNumber.parse(null));
    }

    @Test
    public void testParseImaginaryUnitInFrontToComplexNumberShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("i456"));
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("-i8"));
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("i-"));
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("18+i3.15"));
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("iNaN"));
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("iInfinity"));
    }

    @Test
    public void testParseMultipleSignsToComplexNumberShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("-+2.71"));
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("--2.71"));
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("-2.71+-3.15i"));
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("Infinity+-NaN"));
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("-+Infinity"));
    }

    @Test
    public void testGetReal() {
        testedComplexNumber = new ComplexNumber(sqrt(-123.789), 456);
        assertEquals(sqrt(-123.789), testedComplexNumber.getReal(), EPSILON);
    }

    @Test
    public void testGetRealNaN() {
        testedComplexNumber = new ComplexNumber(NaN, 456);
        assertEquals(NaN, testedComplexNumber.getReal());
    }

    @Test
    public void testGetRealNaNInfinity() {
        testedComplexNumber = new ComplexNumber(-NEGATIVE_INFINITY, 456);
        assertEquals(POSITIVE_INFINITY, testedComplexNumber.getReal());
    }

    @Test
    public void testGetImaginary() {
        testedComplexNumber = new ComplexNumber(13, .048599111199999);
        assertEquals(.048599111199999, testedComplexNumber.getImaginary(), EPSILON);
    }

    @Test
    public void testGetImaginaryNaN() {
        testedComplexNumber = new ComplexNumber(13, -NaN);
        assertEquals(NaN, testedComplexNumber.getImaginary());
    }

    @Test
    public void testGetImaginaryInfinity() {
        testedComplexNumber = new ComplexNumber(13, POSITIVE_INFINITY);
        assertEquals(POSITIVE_INFINITY, testedComplexNumber.getImaginary());
    }

    @Test
    public void testGetMagnitude() {
        testedComplexNumber = new ComplexNumber(-sqrt(12), 2);
        assertEquals(4, testedComplexNumber.getMagnitude(), EPSILON);
    }

    @Test
    public void testGetMagnitudeZeroComplexNumber() {
        testedComplexNumber = new ComplexNumber(0, 0);
        assertEquals(0, testedComplexNumber.getMagnitude(), EPSILON);
    }

    @Test
    public void testGetMagnitudeNaN() {
        testedComplexNumber = new ComplexNumber(NaN, NaN);
        assertEquals(NaN, testedComplexNumber.getMagnitude());
    }

    @Test
    public void testGetMagnitudeInfinity() {
        testedComplexNumber = new ComplexNumber(NaN, POSITIVE_INFINITY);
        assertEquals(POSITIVE_INFINITY, testedComplexNumber.getMagnitude());
    }

    @Test
    public void testGetAnglePositive() {
        testedComplexNumber = new ComplexNumber(-1.456, sqrt(18));
        assertEquals(1.901384779, testedComplexNumber.getAngle(), EPSILON);
    }

    @Test
    public void testGetAngleNegative() {
        testedComplexNumber = new ComplexNumber(4, -4618);
        assertEquals(-1.569930151, testedComplexNumber.getAngle() - (2. * PI), EPSILON);
        assertEquals(-1.569930151 + 2 * PI, testedComplexNumber.getAngle(), EPSILON);
    }

    @Test
    public void testGetAngleZero() {
        testedComplexNumber = new ComplexNumber(1, 0);
        assertEquals(0, testedComplexNumber.getAngle(), EPSILON);
    }

    @Test
    public void testGetAngleNaN() {
        testedComplexNumber = new ComplexNumber(NaN, NaN);
        assertEquals(NaN, testedComplexNumber.getAngle());
    }

    @Test
    public void testGetAngleFromInfinity() {
        testedComplexNumber = new ComplexNumber(NEGATIVE_INFINITY, NEGATIVE_INFINITY);
        assertEquals(3.9269908169872414, testedComplexNumber.getAngle(), EPSILON);
    }

    @Test
    public void testAddTwoComplexNumbers() {
        testedComplexNumber = new ComplexNumber(-6, 3).add(new ComplexNumber(-1, 5));
        assertEquals(-7, testedComplexNumber.getReal(), EPSILON);
        assertEquals(8, testedComplexNumber.getImaginary(), EPSILON);
    }

    @Test
    public void testAdditionWithNaNAndInfinity() {
        assertEquals("NaN+NaNi", new ComplexNumber(NaN, POSITIVE_INFINITY).add(new ComplexNumber(NaN, NEGATIVE_INFINITY)).toString());
    }

    @Test
    public void testAddNullToComplexNumberShouldThrow() {
        assertThrows(NullPointerException.class, () -> new ComplexNumber(-6, 3).add(null));
    }

    @Test
    public void testSubtractTwoComplexNumbers() {
        testedComplexNumber = new ComplexNumber(-6, 3).sub(new ComplexNumber(-1, 5));
        assertEquals(-5, testedComplexNumber.getReal(), EPSILON);
        assertEquals(-2, testedComplexNumber.getImaginary(), EPSILON);
    }

    @Test
    public void testSubtractNullFromComplexNumberShouldThrow() {
        assertThrows(NullPointerException.class, () -> new ComplexNumber(-6, 3).sub(null));
    }

    @Test
    public void testSubtractionWithNaNAndInfinity() {
        assertEquals("NaN+Infinityi", new ComplexNumber(NaN, POSITIVE_INFINITY).sub(new ComplexNumber(NaN, NEGATIVE_INFINITY)).toString());
    }

    @Test
    public void testMultiplyTwoComplexNumbers() {
        testedComplexNumber = new ComplexNumber(-6, 3).mul(new ComplexNumber(-1, 5));
        assertEquals(-9, testedComplexNumber.getReal(), EPSILON);
        assertEquals(-33, testedComplexNumber.getImaginary(), EPSILON);
    }

    @Test
    public void testMultiplicationWithNaNAndInfinity() {
        assertEquals("NaN+NaNi", new ComplexNumber(NaN, POSITIVE_INFINITY).mul(new ComplexNumber(NEGATIVE_INFINITY, POSITIVE_INFINITY)).toString());
    }

    @Test
    public void testMultiplyComplexNumberByNullShouldThrow() {
        assertThrows(NullPointerException.class, () -> new ComplexNumber(-6, 3).mul(null));
    }

    @Test
    public void testDivideTwoComplexNumbers() {
        testedComplexNumber = new ComplexNumber(-6, 3).div(new ComplexNumber(-1, 5));
        assertEquals(21 / 26., testedComplexNumber.getReal(), EPSILON);
        assertEquals(27 / 26., testedComplexNumber.getImaginary(), EPSILON);
    }

    @Test
    public void testDivisionWithNaNAndInfinity() {
        assertEquals("NaN+NaNi", new ComplexNumber(NaN, POSITIVE_INFINITY).div(new ComplexNumber(NaN, NEGATIVE_INFINITY)).toString());
    }

    @Test
    public void testDivideComplexNumberByNullShouldThrow() {
        assertThrows(NullPointerException.class, () -> new ComplexNumber(-6, 3).div(null));
    }

    @Test
    public void testZeroComplexNumberAsDivisorShouldThrow() {
        assertThrows(ArithmeticException.class, () -> new ComplexNumber(-6, 3).div(new ComplexNumber(0, 0)));
    }

    @Test
    public void testComplexNumberRaisedTo3rdPower() {
        testedComplexNumber = new  ComplexNumber(3, 2).power(3);
        assertEquals(-9, testedComplexNumber.getReal(), EPSILON);
        assertEquals(46, testedComplexNumber.getImaginary(), EPSILON);
    }

    @Test
    public void testPowerWithNaNAndInfinity() {
        assertEquals("NaN+NaNi", new ComplexNumber(NaN, POSITIVE_INFINITY).power(3).toString());
    }

    @Test
    public void testComplexNumberRaisedToNegativePowerShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> new ComplexNumber(-6, 3).power(-3));
    }

    @Test
    public void test5thRootOfComplexNumber() {
        ComplexNumber[] fiveRootsOfComplexNumber = new ComplexNumber(-4, 4).root(5);

        assertEquals(1.260073511, fiveRootsOfComplexNumber[0].getReal(), EPSILON);
        assertEquals(0.642039522, fiveRootsOfComplexNumber[0].getImaginary(), EPSILON);
        assertEquals(-0.221231742, fiveRootsOfComplexNumber[1].getReal(), EPSILON);
        assertEquals(1.396802247, fiveRootsOfComplexNumber[1].getImaginary(), EPSILON);
        assertEquals(-1.396802247, fiveRootsOfComplexNumber[2].getReal(), EPSILON);
        assertEquals(0.221231742, fiveRootsOfComplexNumber[2].getImaginary(), EPSILON);
        assertEquals(-0.642039522, fiveRootsOfComplexNumber[3].getReal(), EPSILON);
        assertEquals(-1.260073511, fiveRootsOfComplexNumber[3].getImaginary(), EPSILON);
        assertEquals(0.999999999, fiveRootsOfComplexNumber[4].getReal(), EPSILON);
        assertEquals(-1.000000000, fiveRootsOfComplexNumber[4].getImaginary(), EPSILON);
        for (int k = 0, size = fiveRootsOfComplexNumber.length; k < size; k++) {
            assertEquals(sqrt(2), fiveRootsOfComplexNumber[k].getMagnitude(), EPSILON);
            assertEquals(3 / 20. * PI + 2 / 5. * PI * k, fiveRootsOfComplexNumber[k].getAngle(), EPSILON);
        }
    }

    @Test
    public void test2ndRootWithNaNAndInfinity() {
        ComplexNumber[] twoRootsOfComplexNumber =new ComplexNumber(NaN, POSITIVE_INFINITY).root(2);
        assertEquals("NaN+NaNi", twoRootsOfComplexNumber[0].toString());
        assertEquals("NaN+NaNi", twoRootsOfComplexNumber[1].toString());
    }

    @Test
    public void testZeroNthRootOfComplexNumberShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> new ComplexNumber(-6, 3).root(0));
    }

    @Test
    public void testNegativeNthRootOfComplexNumberShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> new ComplexNumber(-6, 3).root(-3));
    }

    @Test
    public void testToStringPositiveRealAndPositiveImaginaryToComplexNumber() {
        assertEquals("1.1+2.2i", new ComplexNumber(1.1, 2.2).toString());
    }

    @Test
    public void testToStringPositiveRealAndNegativeImaginaryToComplexNumber() {
        assertEquals("1.1-2.2i", new ComplexNumber(1.1, -2.2).toString());
    }

    @Test
    public void testToStringNegativeRealAndPositiveImaginaryToComplexNumber() {
        assertEquals("-1.1+2.2i", new ComplexNumber(-1.1, 2.2).toString());
    }

    @Test
    public void testToStringNegativeRealAndNegativeImaginaryToComplexNumber() {
        assertEquals("-1.1-2.2i", new ComplexNumber(-1.1, -2.2).toString());
    }

    @Test
    public void testToStringWholeRealAndWholeImaginaryToComplexNumber() {
        assertEquals("123+456i", new ComplexNumber(123, 456).toString());
    }

    @Test
    public void testToStringWholeRealAndOnlyDecimalImaginaryToComplexNumber() {
        assertEquals("123+0.456i", new ComplexNumber(123, .456).toString());
    }

    @Test
    public void testToStringWholeRealAndBothWholeAndDecimalImaginaryToComplexNumber() {
        assertEquals("123-456.789i", new ComplexNumber(123.0, -456.789).toString());
    }

    @Test
    public void testToStringOnlyDecimalRealAndWholeImaginaryToComplexNumber() {
        assertEquals("0.123+35i", new ComplexNumber(.123, 35).toString());
    }

    @Test
    public void testToStringOnlyDecimalRealAndOnlyDecimalImaginaryToComplexNumber() {
        assertEquals("0.321-0.75i", new ComplexNumber(.321, -0.75).toString());
    }

    @Test
    public void testToStringOnlyDecimalRealAndBothWholeAndDecimalImaginaryToComplexNumber() {
        assertEquals("-0.896+7.3i", new ComplexNumber(-.896, 7.3).toString());
    }

    @Test
    public void testToStringBothWholeAndDecimalRealAndWholeImaginaryToComplexNumber() {
        assertEquals("-98946.8-3i", new ComplexNumber(-98946.8, -3).toString());
    }

    @Test
    public void testToStringBothWholeAndDecimalRealAndOnlyDecimalImaginaryToComplexNumber() {
        assertEquals("-98946.8-0.8i", new ComplexNumber(-98946.8, -.8).toString());
    }

    @Test
    public void testToStringBothWholeAndDecimalRealAndBothWholeAndDecimalImaginaryToComplexNumber() {
        assertEquals("-98946.8+15.7i", new ComplexNumber(-98946.8, 15.7).toString());
    }

    @Test
    public void testToStringWholeRealAndPositiveImaginaryUnitImaginaryToComplexNumber() {
        assertEquals("123+i", new ComplexNumber(123, 1).toString());
    }

    @Test
    public void testToStringOnlyDecimalRealAndPositiveImaginaryUnitImaginaryToComplexNumber() {
        assertEquals("0.123+i", new ComplexNumber(.123, 1).toString());
    }

    @Test
    public void testToStringBothWholeAndDecimalRealAndPositiveImaginaryUnitImaginaryToComplexNumber() {
        assertEquals("-123.456+i", new ComplexNumber(-123.456, 1).toString());
    }

    @Test
    public void testToStringWholeRealAndNegativeImaginaryUnitImaginaryToComplexNumber() {
        assertEquals("123-i", new ComplexNumber(123, -1).toString());
    }

    @Test
    public void testToStringOnlyDecimalRealAndNegativeImaginaryUnitImaginaryToComplexNumber() {
        assertEquals("0.123-i", new ComplexNumber(.123, -1).toString());
    }

    @Test
    public void testToStringBothWholeAndDecimalRealAndNegativeImaginaryUnitImaginaryToComplexNumber() {
        assertEquals("-123.456-i", new ComplexNumber(-123.456, -1).toString());
    }

    @Test
    public void testToStringWholeAndDecimalRealToComplexNumberPositive() {
        assertEquals("1.1", ComplexNumber.fromReal(1.1).toString());
    }

    @Test
    public void testToStringWholeRealToComplexNumberPositive() {
        assertEquals("5", ComplexNumber.fromReal(5).toString());
    }

    @Test
    public void testToStringOnlyDecimalRealToComplexNumberPositive() {
        assertEquals("0.07", ComplexNumber.fromReal(.07).toString());
    }

    @Test
    public void testToStringRealToComplexNumberPositiveZero() {
        assertEquals("0", ComplexNumber.fromReal(0).toString());
    }

    @Test
    public void testToStringWholeAndDecimalRealToComplexNumberNegative() {
        assertEquals("-1.1", ComplexNumber.fromReal(-1.1).toString());
    }

    @Test
    public void testToStringWholeRealToComplexNumberNegative() {
        assertEquals("-5", ComplexNumber.fromReal(-5).toString());
    }

    @Test
    public void testToStringOnlyDecimalRealToComplexNumberNegative() {
        assertEquals("-0.07", ComplexNumber.fromReal(-.07).toString());
    }

    @Test
    public void testToStringRealToComplexNumberNegativeZero() {
        assertEquals("0", ComplexNumber.fromReal(-0).toString());
    }

    @Test
    public void testToStringWholeAndDecimalImaginaryToComplexNumberPositive() {
        assertEquals("1.1i", ComplexNumber.fromImaginary(1.1).toString());
    }

    @Test
    public void testToStringWholeImaginaryToComplexNumberPositive() {
        assertEquals("5i", ComplexNumber.fromImaginary(5).toString());
    }

    @Test
    public void testToStringOnlyDecimalImaginaryToComplexNumberPositive() {
        assertEquals("0.07i", ComplexNumber.fromImaginary(.07).toString());
    }

    @Test
    public void testToStringImaginaryToComplexNumberPositiveImaginaryUnit() {
        assertEquals("+i", ComplexNumber.fromImaginary(1).toString());
    }

    @Test
    public void testToStringWholeAndDecimalImaginaryToComplexNumberNegative() {
        assertEquals("-1.1i", ComplexNumber.fromImaginary(-1.1).toString());
    }

    @Test
    public void testToStringWholeImaginaryToComplexNumberNegative() {
        assertEquals("-5i", ComplexNumber.fromImaginary(-5).toString());
    }

    @Test
    public void testToStringOnlyDecimalImaginaryToComplexNumberNegative() {
        assertEquals("-0.07i", ComplexNumber.fromImaginary(-.07).toString());
    }

    @Test
    public void testToStringImaginaryToComplexNumberNegativeImaginaryUnit() {
        assertEquals("-i", ComplexNumber.fromImaginary(-1).toString());
    }

    @Test
    public void testToStringPositiveNaNRealAndPositiveInfinityImaginaryToComplexNumber() {
        assertEquals("NaN+Infinityi", new ComplexNumber(NaN, POSITIVE_INFINITY).toString());
    }

    @Test
    public void testToStringPositiveNaNRealAndNegativeInfinityImaginaryToComplexNumber() {
        assertEquals("NaN-Infinityi", new ComplexNumber(NaN, NEGATIVE_INFINITY).toString());
    }

    @Test
    public void testToStringPositiveNaNRealAndPositiveNaNImaginaryToComplexNumber() {
        assertEquals("NaN+NaNi", new ComplexNumber(NaN, NaN).toString());
    }

    @Test
    public void testToStringPositiveNaNRealAndNegativeNaNImaginaryToComplexNumber() {
        assertEquals("NaN+NaNi", new ComplexNumber(NaN, -NaN).toString());
    }

    @Test
    public void testToStringNegativeNaNRealAndPositiveInfinityImaginaryToComplexNumber() {
        assertEquals("NaN+Infinityi", new ComplexNumber(-NaN, POSITIVE_INFINITY).toString());
    }

    @Test
    public void testToStringNegativeNaNRealAndNegativeInfinityImaginaryToComplexNumber() {
        assertEquals("NaN-Infinityi", new ComplexNumber(-NaN, NEGATIVE_INFINITY).toString());
    }

    @Test
    public void testToStringNegativeNaNRealAndPositiveNaNImaginaryToComplexNumber() {
        assertEquals("NaN+NaNi", new ComplexNumber(-NaN, NaN).toString());
    }

    @Test
    public void testToStringNegativeNaNRealAndNegativeNaNImaginaryToComplexNumber() {
        assertEquals("NaN+NaNi", new ComplexNumber(-NaN, -NaN).toString());
    }

    @Test
    public void testToStringPositiveInfinityRealAndPositiveInfinityImaginaryToComplexNumber() {
        assertEquals("Infinity+Infinityi", new ComplexNumber(POSITIVE_INFINITY, POSITIVE_INFINITY).toString());
    }

    @Test
    public void testToStringPositiveInfinityRealAndNegativeInfinityImaginaryToComplexNumber() {
        assertEquals("Infinity-Infinityi", new ComplexNumber(POSITIVE_INFINITY, NEGATIVE_INFINITY).toString());
    }

    @Test
    public void testToStringPositiveInfinityRealAndPositiveNaNImaginaryToComplexNumber() {
        assertEquals("Infinity+NaNi", new ComplexNumber(POSITIVE_INFINITY, NaN).toString());
    }

    @Test
    public void testToStringPositiveInfinityRealAndNegativeNaNImaginaryToComplexNumber() {
        assertEquals("Infinity+NaNi", new ComplexNumber(POSITIVE_INFINITY, -NaN).toString());
    }

    @Test
    public void testToStringNegativeInfinityRealAndPositiveInfinityImaginaryToComplexNumber() {
        assertEquals("-Infinity+Infinityi", new ComplexNumber(NEGATIVE_INFINITY, POSITIVE_INFINITY).toString());
    }

    @Test
    public void testToStringNegativeInfinityRealAndNegativeInfinityImaginaryToComplexNumber() {
        assertEquals("-Infinity-Infinityi", new ComplexNumber(NEGATIVE_INFINITY, NEGATIVE_INFINITY).toString());
    }

    @Test
    public void testToStringNegativeInfinityRealAndPositiveNaNImaginaryToComplexNumber() {
        assertEquals("-Infinity+NaNi", new ComplexNumber(NEGATIVE_INFINITY, NaN).toString());
    }

    @Test
    public void testToStringNegativeInfinityRealAndNegativeNaNImaginaryToComplexNumber() {
        assertEquals("-Infinity+NaNi", new ComplexNumber(NEGATIVE_INFINITY, -NaN).toString());
    }




}
