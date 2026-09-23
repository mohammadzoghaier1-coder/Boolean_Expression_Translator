package Boolean_Expression;

import java.util.Stack;

public class PostfixEvaluater {

    // Method to evaluate value of a postfix expression
     static boolean evaluatePostfix(String exp) {
    // Create a stack
    Stack<Boolean> stack = new Stack<>();

    // Scan all characters one by one
    for (int i = 0; i < exp.length(); i++) {
        char c = exp.charAt(i);

        // If the scanned character is a operand (boolean value), push it to the stack.
        if (c == '0') {
            stack.push(false);
        } else if (c == '1') {
            stack.push(true);
        }
        // If the scanned character is an operator, pop one element from stack and apply the operator
        else {
            boolean val = stack.pop();

            switch (c) {
                case '~':
                case '\'':
                    stack.push(!val);
                    break;
                case '^':
                    stack.push(stack.pop() && val);
                    break;
                case '+':
                    stack.push(stack.pop() || val);
                    break;
            }
        }
    }

    return stack.pop();
}
}