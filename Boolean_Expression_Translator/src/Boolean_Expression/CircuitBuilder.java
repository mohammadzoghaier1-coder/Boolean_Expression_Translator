package Boolean_Expression;

import java.util.Stack;

/**
 * Turns a postfix boolean expression (letters for variables, '~' NOT,
 * '^' AND, '+' OR) into a tree of CircuitNodes that CircuitPanel can draw.
 *
 * IMPORTANT: call this with the postfix string BEFORE it is passed through
 * convertInput(...) -- i.e. while it still has variable letters like A, B, C
 * in it instead of 0s and 1s. Otherwise the diagram will show "0"/"1" boxes
 * instead of variable names.
 */
public class CircuitBuilder {

    public static CircuitNode buildTree(String postfix) {
        if (postfix == null || postfix.isEmpty()) {
            return null;
        }

        Stack<CircuitNode> stack = new Stack<>();

        for (int i = 0; i < postfix.length(); i++) {
            char c = postfix.charAt(i);

            if (Character.isLetter(c)) {
                stack.push(new CircuitNode(c));
            } else if (c == '~' || c == '\'') {
                if (stack.isEmpty()) {
                    continue; // malformed expression, skip rather than crash
                }
                CircuitNode child = stack.pop();
                CircuitNode gate = new CircuitNode(c);
                gate.left = child;
                stack.push(gate);
            } else if (c == '^' || c == '+') {
                if (stack.size() < 2) {
                    continue; // malformed expression, skip rather than crash
                }
                CircuitNode right = stack.pop();
                CircuitNode left = stack.pop();
                CircuitNode gate = new CircuitNode(c);
                gate.left = left;
                gate.right = right;
                stack.push(gate);
            }
            // any other character is ignored
        }

        return stack.isEmpty() ? null : stack.pop();
    }

    /**
     * Fills in node.height for every node in the tree (post-order).
     * Leaves get height 0; every gate gets 1 + the tallest child.
     * Returns the height of the whole tree (i.e. of "node").
     */
    public static int computeHeights(CircuitNode node) {
        if (node == null) {
            return -1;
        }
        if (node.isLeaf) {
            node.height = 0;
            return 0;
        }
        int leftHeight = computeHeights(node.left);
        int rightHeight = (node.right != null) ? computeHeights(node.right) : -1;
        node.height = 1 + Math.max(leftHeight, rightHeight);
        return node.height;
    }

    public static int countLeaves(CircuitNode node) {
        if (node == null) {
            return 0;
        }
        if (node.isLeaf) {
            return 1;
        }
        return countLeaves(node.left) + countLeaves(node.right);
    }
}