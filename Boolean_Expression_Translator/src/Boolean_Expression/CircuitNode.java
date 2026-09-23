package Boolean_Expression;

/**
 * A single node in the circuit tree.
 * A node is either:
 *  - a leaf: an input variable (isLeaf == true, value is a letter)
 *  - a gate: '~' (NOT, one child in "left"), '^' (AND, two children),
 *            '+' (OR, two children)
 *
 * x and y are filled in by CircuitPanel when it lays the tree out on screen;
 * they represent the point where this node's OUTPUT wire begins.
 */
public class CircuitNode {

    public char value;
    public CircuitNode left;
    public CircuitNode right;
    public boolean isLeaf;

    // Distance (in gate-levels) from the nearest leaf below this node.
    // A leaf has height 0. Used to decide the horizontal position of the node.
    public int height;

    // Screen coordinates of this node's output point, set during layout.
    public int x;
    public int y;

    public CircuitNode(char value) {
        this.value = value;
        this.isLeaf = Character.isLetter(value);
    }
}