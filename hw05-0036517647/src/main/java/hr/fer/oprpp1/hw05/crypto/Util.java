package hr.fer.oprpp1.hw05.crypto;

import java.util.Objects;

public class Util {

    private static final String hexDigits = "0123456789abcdef";

    public static byte[] hextobyte(String keyText) {
        if(Objects.requireNonNull(keyText, "The given hex string cannot be null!").length() % 2 != 0) throw new IllegalArgumentException("The given hex number should have an even length!");
        if(keyText.length() == 0) return new byte[0];
        if(!keyText.matches("[0-9a-fA-F]+")) throw new IllegalArgumentException("Invalid hex number given!");

        keyText = keyText.toLowerCase();

        byte[] bytearray = new byte[keyText.length() / 2];

        for (int i = 0, textLength = keyText.length(); i < textLength; i += 2)
            bytearray[i / 2] = (byte) ((hexDigits.indexOf(keyText.charAt(i)) << 4) + hexDigits.indexOf(keyText.charAt(i + 1)));

        return bytearray;
    }

    public static String bytetohex(byte[] bytearray) {
        Objects.requireNonNull(bytearray, "The given byte array cannot be null!");
        StringBuilder sb = new StringBuilder();

        for (byte singleByte : bytearray)
            sb.append(hexDigits.charAt((singleByte & 0xF0) >> 4)).append((hexDigits.charAt(singleByte & 0x0F)));

        return sb.toString();
    }

}
