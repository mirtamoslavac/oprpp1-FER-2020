package hr.fer.oprpp1.hw05.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Objects;
import java.util.Scanner;

public class Crypto {

    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) throw new IllegalArgumentException("Invalid number of arguments! Expected 2 or 3, got " + args.length);

        try(Scanner sc = new Scanner(System.in)) {
            switch (args[0]) {
                case "checksha" -> {
                    if (args.length > 2) throw new IllegalArgumentException("Too many arguments for digesting!");
                    if (!args[1].endsWith(".bin")) throw new IllegalArgumentException("Invalid file given! Expected a bin file, got " + args[1]);

                    System.out.print("Please provide expected sha-256 digest for " + args[1] + ":\n> ");
                    String expectedDigest = sc.nextLine().trim();

                    String calculatedDigest = digest(args[1]);

                    System.out.print("Digesting completed. ");
                    if (expectedDigest.equalsIgnoreCase(calculatedDigest)) System.out.println("Digest of " + args[1] + " matches expected digest.");
                    else System.out.println("Digest of " + args[1] + " does not match the expected digest. Digest was: " + calculatedDigest);

                }

                case "encrypt", "decrypt" -> {
                    if (!args[1].endsWith(".pdf") && !args[1].endsWith(".bin")) throw new IllegalArgumentException("Invalid file that is to be ciphered given! Expected a pdf " +
                            "or a bin file, got " + args[1]);
                    if (!args[2].endsWith(".pdf")) throw new IllegalArgumentException("Invalid output file name as given! Expected a pdf file, got " + args[2]);

                    System.out.print("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n> ");
                    String keyText = sc.nextLine().trim();
                    if (keyText.length() != 32) throw new IllegalArgumentException("Incorrect password length! Expected 32, got " + keyText.length());


                    System.out.print("Please provide initialization vector as hex-encoded text (32 hex-digits):\n> ");
                    String ivText = sc.nextLine().trim();
                    if (ivText.length() != 32) throw new IllegalArgumentException("Incorrect initialization vector length! Expected 32, got " + ivText.length());

                    boolean encrypt = args[0].equalsIgnoreCase("encrypt");
                    cipher(args[1], args[2], encrypt, keyText, ivText);

                    System.out.println((encrypt ? "Encryption" : "Decryption") + " completed. Generated file" + args[2] + " based on file " + args[1] + ".");
                }

                default -> throw new IllegalArgumentException("Illegal command " + args[0] + "!");
            }
        } catch (IllegalArgumentException | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | IOException e) {
            System.out.println(e.getClass().getCanonicalName() + ": " + e.getMessage());
        }
    }

    private static String digest(String fileName) throws NoSuchAlgorithmException, IOException {

        Objects.requireNonNull(fileName, "The given file name cannot be null!");
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

        try (InputStream is = new BufferedInputStream(Files.newInputStream(Path.of(fileName)))) {
            byte[] buf = new byte[4096];

            do {
                int k = is.read(buf);
                if (k == -1) break;

                sha256.update(buf, 0, k);
            } while (true);
        }

        return Util.bytetohex(sha256.digest());
    }

    private static void cipher(String inputFileName, String outputFileName, boolean encrypt, String keyText, String ivText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(Objects.requireNonNull(keyText, "The given password cannot be null!")), "AES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(Objects.requireNonNull(ivText, "The given initialization vector cannot be null!")));

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);

        try (InputStream is = new BufferedInputStream(Files.newInputStream(Path.of(inputFileName)));
             OutputStream os = new BufferedOutputStream(Files.newOutputStream(Path.of(outputFileName)))) {
            byte[] buf = new byte[4096];

            do {
                int k = is.read(buf);
                if (k == -1) {
                    os.write(cipher.doFinal());
                    break;
                }

                os.write(cipher.update(buf, 0, k));
            } while (true);
        }
    }


}
