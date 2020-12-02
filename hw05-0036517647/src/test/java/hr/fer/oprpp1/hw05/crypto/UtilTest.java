package hr.fer.oprpp1.hw05.crypto;

import org.junit.jupiter.api.Test;

import static hr.fer.oprpp1.hw05.crypto.Util.bytetohex;
import static hr.fer.oprpp1.hw05.crypto.Util.hextobyte;
import static org.junit.jupiter.api.Assertions.*;

public class UtilTest {


    @Test
    void testHexToByte() {
        assertArrayEquals(new byte[] {1, -82, 34}, hextobyte("01aE22"));
        assertArrayEquals(new byte[] {0}, hextobyte("00"));
        assertArrayEquals(new byte[] {0, 0, 0, 0, 0}, hextobyte("0000000000"));
        assertArrayEquals(new byte[] {-1}, hextobyte("Ff"));
    }

    @Test
    void testHexToByteOddThrows() {
        assertThrows(IllegalArgumentException.class, () -> hextobyte("1"));
        assertThrows(IllegalArgumentException.class, () -> hextobyte("123"));
        assertThrows(IllegalArgumentException.class, () -> hextobyte("12345"));
    }

    @Test
    void testHexToByteNullThrows() {
        assertThrows(NullPointerException.class, () -> hextobyte(null));
    }

    @Test
    void testHexToByteEmpty() {
        assertArrayEquals(new byte[0], hextobyte(""));
    }

    @Test
    void testHexToByteInvalidDigitThrows() {
        assertThrows(IllegalArgumentException.class, () -> hextobyte("0g"));
        assertThrows(IllegalArgumentException.class, () -> hextobyte("0x0g"));
        assertThrows(IllegalArgumentException.class, () -> hextobyte("-1"));
        assertThrows(IllegalArgumentException.class, () -> hextobyte("0x01"));
        assertThrows(IllegalArgumentException.class, () -> hextobyte("0X01"));
    }

    @Test
    void testReversibilityHex() {
        assertEquals("01ae22", bytetohex(hextobyte("01aE22")));
        assertEquals("", bytetohex(hextobyte("")));
    }

    @Test
    void testByteToHex() {
        assertEquals("01ae22", bytetohex(new byte[] {1, -82, 34}));
        assertEquals("00", bytetohex(new byte[] {0}));
        assertEquals("0000000000", bytetohex(new byte[] {0, 0, 0, 0, 0}));
        assertEquals("ff", bytetohex(new byte[] {-1}));
    }

    @Test
    void testByteToHexNullThrows() {
        assertThrows(NullPointerException.class, () -> bytetohex(null));
    }

    @Test
    void testByteToHexEmpty() {
        assertEquals("", bytetohex(new byte[0]));
    }

    @Test
    void testReversibilityByte() {
        assertArrayEquals(new byte[] {1, -82, 34}, hextobyte(bytetohex(new byte[] {1, -82, 34})));
        assertArrayEquals(new byte[0], hextobyte(bytetohex(new byte[0])));
    }
}
