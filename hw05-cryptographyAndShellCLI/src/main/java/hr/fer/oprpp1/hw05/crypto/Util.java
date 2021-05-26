package hr.fer.oprpp1.hw05.crypto;

import java.util.Objects;

/**
 * The {@code Util} class contains methods for the conversion between hexadecimal representation and bytes.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class Util {
    /**
     * All hexadecimal digits in order.
     */
    private static final String HEX_DIGITS = "0123456789abcdef";

    /**
     * Converts the given hexadecimal string {@code keyText} to bytes.
     *
     * @param keyText hexadecimal string that is to be converted.
     * @throws NullPointerException if the given {@code keyText} is {@code null}.
     * @return byte array containing the byte representation of the given hexadecimal string.
     */
    public static byte[] hextobyte(String keyText) {
        if(Objects.requireNonNull(keyText, "The given hex string cannot be null!").length() % 2 != 0) throw new IllegalArgumentException("The given hex number should have an even length!");
        if(keyText.length() == 0) return new byte[0];
        if(!keyText.matches("[0-9a-fA-F]+")) throw new IllegalArgumentException("Invalid hex number given!");

        keyText = keyText.toLowerCase();

        byte[] bytearray = new byte[keyText.length() / 2];

        for (int i = 0, textLength = keyText.length(); i < textLength; i += 2)
            bytearray[i / 2] = (byte) ((HEX_DIGITS.indexOf(keyText.charAt(i)) << 4) + HEX_DIGITS.indexOf(keyText.charAt(i + 1)));

        return bytearray;
    }

    /**
     * Converts the given bytes {@code bytearray} to a hexadecimal string.
     *
     * @param bytearray byte array containing bytes that are to be converted to a hexadecimal string.
     * @throws NullPointerException if the given {@code bytearray} is {@code null}.
     * @return hexadecimal representation of the given bytes.
     */
    public static String bytetohex(byte[] bytearray) {
        Objects.requireNonNull(bytearray, "The given byte array cannot be null!");
        StringBuilder sb = new StringBuilder();

        for (byte singleByte : bytearray)
            sb.append(HEX_DIGITS.charAt((singleByte & 0xF0) >> 4)).append((HEX_DIGITS.charAt(singleByte & 0x0F)));

        return sb.toString();
    }

}
