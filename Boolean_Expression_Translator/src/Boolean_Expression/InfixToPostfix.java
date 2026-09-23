package Boolean_Expression;

import java.util.Stack;

public class InfixToPostfix {

    // Function to return precedence of operators
    static int prec(char c) {
        if (c == '~' || c == '\'')
            return 3;
        else if (c == '^')
            return 2;
        else if (c == '+')
            return 1;
        else
            return -1;
    }

    // Function to return associativity of operators
    static char associativity(char c) {
        if (c == '^')
            return 'R';
        return 'L'; // Default to left-associative
    }

    // The main function to convert infix expression to postfix expression
    static String infixToPostfix(String s) {
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        String resultString = new String();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            // Ignore whitespace entirely -- otherwise it gets treated as a
            // pseudo-operator below (prec(' ') falls through to the default
            // -1 case) and corrupts the output order for any expression
            // typed with spaces, e.g. "A ^ B" instead of "A^B".
            if (Character.isWhitespace(c)) {
                continue;
            }

            // If the scanned character is an operand, add it to the output string.
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
                result.append(c);
            }
            // If the scanned character is an ‘(‘, push it to the stack.
            else if (c == '(') {
                stack.push(c);
            }
            // If the scanned character is an ‘)’, pop and add to the output string from the stack
            // until an ‘(‘ is encountered.
            else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    result.append(stack.pop());
                }
                if (!stack.isEmpty()) {
                    stack.pop(); // Pop '('
                }
            }
            // If an operator is scanned
            else {
                while (!stack.isEmpty() && (prec(c) < prec(stack.peek()) ||
                                             prec(c) == prec(stack.peek()) &&
                                                 associativity(c) == 'L')) {
                    result.append(stack.pop());
                }
                stack.push(c);
            }
        }

        // Pop all the remaining elements from the stack
        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }

        resultString = result.toString();
        System.out.println(result);
        return resultString;
    }


}