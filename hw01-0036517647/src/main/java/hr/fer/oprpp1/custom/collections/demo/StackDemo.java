package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.EmptyStackExceptionV2;
import hr.fer.oprpp1.custom.collections.ObjectStack;

/**
 * {@code StackDemo} is a command-line application which accepts a single command-line argument, which is an expression which should be evaluated.
 * The expression must be in postfix representation.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class StackDemo {
    /**
     * Performs an evaluation of the expression given as the single element of {@code args}.
     *
     * @param args an array containing command-line arguments.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Only one command-line argument is allowed, not " + args.length + "!");
        }

        String[] splitArguments = args[0].split("\\s+");

        ObjectStack stack = new ObjectStack();
        try {
            fillAndCalculate(splitArguments, stack);

            if (stack.size() != 1) {
                System.err.println("The final size stack does not equal one, but rather " + stack.size() + "! Too many operands!");
            } else {
                System.out.println("The given expression evaluates to " + stack.pop());
            }
        } catch (ArithmeticException | EmptyStackExceptionV2 | IllegalArgumentException e) {
            System.err.println(e.getClass().getCanonicalName() + ": " + e.getMessage());
        }



    }

    /**
     * Fills the given {@code stack} with operands and performs operations on those same operands with operators, all contained within {@code splitArguments}.
     *
     * @param splitArguments array containing operands and operators needed to evaluate the given expression.
     * @param stack empty {@link ObjectStack} instance that is to be filled with operands and used to store evaluation results.
     * @throws ArithmeticException when attempting to divide by zero.
     */
    private static void fillAndCalculate(String[] splitArguments, ObjectStack stack) throws ArithmeticException {
        for(String element : splitArguments) {
            if (element.matches("0|-?[1-9]\\d*")) {
                stack.push(element);
            } else if (element.matches("[+\\-/*%]")){
                try {
                    int elem2 = Integer.parseInt(stack.pop().toString());
                    int elem1 = Integer.parseInt(stack.pop().toString());

                    switch (element){
                        case "+"    -> stack.push(elem1 + elem2);
                        case "-"    -> stack.push(elem1 - elem2);
                        case "/"    -> {if (elem2 == 0) {throw new ArithmeticException("Cannot divide by zero!");} stack.push(elem1 / elem2);}
                        case "*"    -> stack.push(elem1 * elem2);
                        case "%"    -> {if (elem2 == 0) {throw new ArithmeticException("Cannot divide by zero!");} stack.push(elem1 % elem2);}
                    }
                } catch (EmptyStackExceptionV2 emptyStackException) {
                    throw new EmptyStackExceptionV2(emptyStackException.getMessage());
                }
            } else {
                throw new IllegalArgumentException("The expression given is not valid because of element \"" + element + "\"!");
            }
        }
    }

}
