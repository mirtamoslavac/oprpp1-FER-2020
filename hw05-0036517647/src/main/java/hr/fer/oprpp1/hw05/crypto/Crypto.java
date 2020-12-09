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

/**
 * The {@code Crypto} class enables performing ciphering or digesting activities.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class Crypto {
    /**
     * The size of the I/O buffer.
     */
    private static final int BUFFER_SIZE = 4096;
    /**
     * The name of the algorithm for creating a file digest.
     */
    private static final String DIGEST_ALGORITHM = "SHA-256";
    /**
     * Describes the operation (or set of operations) to be performed for the given file, in order to produce some output.
     */
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    /**
     * Program used to perform ciphering or digesting activities on the given file(s).
     *
     * @param args an array of command-line arguments.
     */
    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) throw new IllegalArgumentException("Invalid number of arguments! Expected 2 or 3, got " + args.length + "!");

        try(Scanner sc = new Scanner(System.in)) {
            switch (args[0]) {
                case "checksha" -> {
                    if (args.length > 2) throw new IllegalArgumentException("Too many arguments for digesting!");
                    if (!args[1].endsWith(".bin")) throw new IllegalArgumentException("Invalid file given! Expected a bin file, got \"" + args[1] + "\"!");

                    System.out.print("Please provide expected sha-256 digest for " + args[1] + ":\n> ");
                    String expectedDigest = sc.nextLine().trim();

                    String calculatedDigest = digest(args[1]);

                    System.out.print("Digesting completed. ");
                    if (expectedDigest.equalsIgnoreCase(calculatedDigest)) System.out.println("Digest of " + args[1] + " matches expected digest.");
                    else System.out.println("Digest of " + args[1] + " does not match the expected digest. Digest was: " + calculatedDigest);
                }

                case "encrypt", "decrypt" -> {
                    if (!args[1].endsWith(".pdf") && !args[1].endsWith(".bin"))
                        throw new IllegalArgumentException("Invalid file that is to be ciphered given! Expected a pdf or a bin file, got \"" + args[1] + "\"!");
                    if (!args[2].endsWith(".pdf")) throw new IllegalArgumentException("Invalid output file name as given! Expected a pdf file, got \"" + args[2] + "\"!");

                    System.out.print("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n> ");
                    String keyText = sc.nextLine().trim();
                    if (keyText.length() != 32) throw new IllegalArgumentException("Incorrect password length! Expected 32, got " + keyText.length() + "!");


                    System.out.print("Please provide initialization vector as hex-encoded text (32 hex-digits):\n> ");
                    String ivText = sc.nextLine().trim();
                    if (ivText.length() != 32) throw new IllegalArgumentException("Incorrect initialization vector length! Expected 32, got " + ivText.length() + "!");

                    boolean encrypt = args[0].equalsIgnoreCase("encrypt");
                    cipher(args[1], args[2], encrypt, keyText, ivText);

                    System.out.println((encrypt ? "Encryption" : "Decryption") + " completed. Generated file " + args[2] + " based on file " + args[1] + ".");
                }

                default -> throw new IllegalArgumentException("Illegal command " + args[0] + "!");
            }
        } catch (IllegalArgumentException | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | IOException e) {
            System.out.println(e.getClass().getCanonicalName() + ": " + e.getMessage());
        }
    }

    /**
     * Calculates the digest for the given file.
     *
     * @param fileName the name of the file that is to be digested.
     * @throws IOException when some I/O exception occurs while attempting to read from the file.
     * @throws NoSuchAlgorithmException when the requested cryptographic algorithm is not available.
     * @throws NullPointerException when the given {@code fileName} is {@code null}.
     * @return calculated digest for the given file.
     */
    private static String digest(String fileName) throws NoSuchAlgorithmException, IOException {
        Objects.requireNonNull(fileName, "The given file name cannot be null!");
        MessageDigest sha256 = MessageDigest.getInstance(DIGEST_ALGORITHM);

        try (InputStream is = new BufferedInputStream(Files.newInputStream(Path.of(fileName)))) {
            byte[] buf = new byte[BUFFER_SIZE];

            do {
                int k = is.read(buf);
                if (k == -1) break;

                sha256.update(buf, 0, k);
            } while (true);
        }

        return Util.bytetohex(sha256.digest());
    }

    /**
     * Encrypts or decrypts the given file {@code inputFileName}, depending on the given arguments, to a new file of the name {@code outputFileName}.
     *
     * @param inputFileName name of the file that is to be encrypted or decrypted.
     * @param outputFileName name of the file in which to store the result of the encryption or decryption.
     * @param encrypt {@code true} if encryption is wanted, {@code false} if decryption.
     * @param keyText a key needed for the encryption or decryption.
     * @param ivText the initialization vector needed for the encryption or decryption.
     * @throws BadPaddingException when a particular padding mechanism is expected for the given data, but the data is not padded properly.
     * @throws IllegalBlockSizeException when the length of data provided to a block cipher does not match the block size of the cipher.
     * @throws InvalidAlgorithmParameterException when invalid algorithm parameters are given.
     * @throws InvalidKeyException when an invalid key is given.
     * @throws IOException when some I/O exception occurs while attempting to read from or write to a file.
     * @throws NoSuchAlgorithmException when when the requested cryptographic algorithm is not available.
     * @throws NoSuchPaddingException when the requested padding mechanism is not available.
     * @throws NullPointerException when the requested given {@code inputFileName}, {@code outputFileName}, {@code keyText} or {@code ivText} are {@code null}.
     */
    private static void cipher(String inputFileName, String outputFileName, boolean encrypt, String keyText, String ivText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        Objects.requireNonNull(inputFileName);
        Objects.requireNonNull(outputFileName);
        SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(Objects.requireNonNull(keyText, "The given password cannot be null!")), "AES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(Objects.requireNonNull(ivText, "The given initialization vector cannot be null!")));

        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);

        try (InputStream is = new BufferedInputStream(Files.newInputStream(Path.of(inputFileName)));
             OutputStream os = new BufferedOutputStream(Files.newOutputStream(Path.of(outputFileName)))) {
            byte[] buf = new byte[BUFFER_SIZE];

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
