package hr.fer.oprpp1.hw02;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Tester class taken from this homework's PDF dedicated to demonstrating the workings of a {@link SmartScriptParser}.
 */
public class SmartScriptTester {
    /**
     * Method taken from this homework's PDF dedicated to demonstrating the workings of {@link SmartScriptParser}.
     * @param args an array of command-line arguments.
     */
    public static void main(String[] args) throws IOException {
        String docBody = Files.readString(Paths.get("..."));
        SmartScriptParser parser = new SmartScriptParser(docBody);
        DocumentNode document = parser.getDocumentNode();
        String originalDocumentBody = document.toString();

        SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
        DocumentNode document2 = parser2.getDocumentNode();
        // now document and document2 should be structurally identical trees
        boolean same = document.equals(document2); // ==> "same" must be true
    }
}
