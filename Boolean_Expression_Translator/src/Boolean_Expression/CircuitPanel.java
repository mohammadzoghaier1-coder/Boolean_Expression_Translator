package Boolean_Expression;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;

import javax.swing.JPanel;

/**
 * Draws the full gate diagram for a boolean expression.
 *
 * Usage: circuitPanel.setExpression(CircuitBuilder.buildTree(postfixWithVariableLetters));
 *
 * Layout idea:
 *  - Every leaf (input variable) is placed in its own horizontal "lane",
 *    stacked top to bottom in the order they're encountered.
 *  - Every gate is placed one "level" to the right of its tallest child,
 *    vertically centered between its children's output points.
 *  - Wires are drawn as simple elbow (horizontal/vertical) connectors.
 */
public class CircuitPanel extends JPanel {

    private static final int MARGIN_LEFT = 90;
    private static final int MARGIN_TOP = 40;
    private static final int LEAF_Y_SPACING = 70;
    private static final int LEVEL_X_SPACING = 130;
    private static final int GATE_W = 45;
    private static final int GATE_H = 40;

    private static final Color WIRE_COLOR = new Color(0x64, 0x74, 0x8B);
    private static final Color GATE_FILL = new Color(0xEE, 0xF0, 0xFF);
    private static final Color GATE_STROKE = new Color(0x4F, 0x46, 0xE5);
    private static final Color LEAF_CHIP_FILL = new Color(0xF1, 0xF5, 0xF9);
    private static final Color LEAF_CHIP_STROKE = new Color(0xCB, 0xD5, 0xE1);
    private static final Color LEAF_TEXT = new Color(0x1E, 0x29, 0x3B);
    private static final Color OUTPUT_TEXT = new Color(0x4F, 0x46, 0xE5);

    private CircuitNode root;
    private int leafCount;
    private int leafCounter; // used only while laying out

    public CircuitPanel() {
        setBackground(Color.WHITE);
    }

    /** Give the panel a new expression tree to draw (pass null to clear it). */
    public void setExpression(CircuitNode root) {
        this.root = root;
        this.leafCount = CircuitBuilder.countLeaves(root);
        if (root != null) {
            // Compute heights up front so getPreferredSize() (called by
            // revalidate(), scroll panes, layout managers, etc.) has the
            // correct tree height BEFORE the first paintComponent() runs.
            // Without this, the very first layout pass sees root.height == 0
            // (its default value) and reports a too-small preferred size,
            // which clips large diagrams that exceed the fallback 500x300.
            CircuitBuilder.computeHeights(root);
        }
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        if (root == null) {
            return new Dimension(500, 300);
        }
        int width = MARGIN_LEFT + (root.height + 1) * LEVEL_X_SPACING + 120;
        int height = MARGIN_TOP * 2 + Math.max(leafCount, 1) * LEAF_Y_SPACING;
        return new Dimension(Math.max(width, 500), Math.max(height, 300));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (root == null) {
            g2d.setColor(Color.GRAY);
            g2d.drawString("Enter an expression and click \"Add Expression\" to see the circuit.", 20, 30);
            return;
        }

        CircuitBuilder.computeHeights(root);
        leafCounter = 0;
        assignPositions(root);

        drawWires(g2d, root);
        drawNodes(g2d, root);

        // final output stub past the root gate
        g2d.setColor(WIRE_COLOR);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(root.x, root.y, root.x + 35, root.y);
        g2d.setColor(OUTPUT_TEXT);
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 14f));
        g2d.drawString("Y", root.x + 42, root.y + 5);
    }

    // ---------- layout ----------

    private void assignPositions(CircuitNode node) {
        if (node == null) {
            return;
        }
        node.x = MARGIN_LEFT + node.height * LEVEL_X_SPACING;

        if (node.isLeaf) {
            node.y = MARGIN_TOP + leafCounter * LEAF_Y_SPACING;
            leafCounter++;
        } else if (node.right != null) {
            assignPositions(node.left);
            assignPositions(node.right);
            node.y = (node.left.y + node.right.y) / 2;
        } else {
            assignPositions(node.left);
            node.y = node.left.y;
        }
    }

    // ---------- wires ----------

    private void drawWires(Graphics2D g2d, CircuitNode node) {
        if (node == null || node.isLeaf) {
            return;
        }
        g2d.setColor(WIRE_COLOR);
        g2d.setStroke(new BasicStroke(2));

        if (node.right != null) {
            int inX = node.x - GATE_W;
            int topInY = node.y - GATE_H / 4;
            int botInY = node.y + GATE_H / 4;
            drawElbowWire(g2d, node.left.x, node.left.y, inX, topInY);
            drawElbowWire(g2d, node.right.x, node.right.y, inX, botInY);
            drawWires(g2d, node.left);
            drawWires(g2d, node.right);
        } else {
            int inX = node.x - GATE_W;
            drawElbowWire(g2d, node.left.x, node.left.y, inX, node.y);
            drawWires(g2d, node.left);
        }
    }

    private void drawElbowWire(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        int midX = (x1 + x2) / 2;
        g2d.drawLine(x1, y1, midX, y1);
        g2d.drawLine(midX, y1, midX, y2);
        g2d.drawLine(midX, y2, x2, y2);
    }

    // ---------- gates + leaves ----------

    private void drawNodes(Graphics2D g2d, CircuitNode node) {
        if (node == null) {
            return;
        }
        if (node.isLeaf) {
            drawLeaf(g2d, node);
        } else if (node.right != null) {
            int boxX = node.x - GATE_W;
            int boxY = node.y - GATE_H / 2;
            if (node.value == '^') {
                drawAndGate(g2d, boxX, boxY, GATE_W, GATE_H);
            } else {
                drawOrGate(g2d, boxX, boxY, GATE_W, GATE_H);
            }
            drawNodes(g2d, node.left);
            drawNodes(g2d, node.right);
        } else {
            drawNotGate(g2d, node.x - GATE_W, node.y - GATE_H / 2, GATE_W, GATE_H);
            drawNodes(g2d, node.left);
        }

        g2d.setColor(GATE_STROKE);
        g2d.fillOval(node.x - 3, node.y - 3, 6, 6);
    }

    private void drawLeaf(Graphics2D g2d, CircuitNode node) {
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 14f));
        FontMetrics fm = g2d.getFontMetrics();
        String label = String.valueOf(node.value);
        int textWidth = fm.stringWidth(label);

        // small rounded "chip" behind the variable letter
        int chipW = textWidth + 16;
        int chipH = 24;
        int chipX = node.x - chipW - 6;
        int chipY = node.y - chipH / 2;
        g2d.setColor(LEAF_CHIP_FILL);
        g2d.fillRoundRect(chipX, chipY, chipW, chipH, 10, 10);
        g2d.setColor(LEAF_CHIP_STROKE);
        g2d.setStroke(new BasicStroke(1.2f));
        g2d.drawRoundRect(chipX, chipY, chipW, chipH, 10, 10);

        g2d.setColor(LEAF_TEXT);
        g2d.drawString(label, chipX + 8, node.y + fm.getAscent() / 2 - 2);

        g2d.setColor(WIRE_COLOR);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(chipX + chipW, node.y, node.x, node.y);
    }

    /** AND gate: flat-backed "D" shape, point/output on the right. */
    private void drawAndGate(Graphics2D g2d, int x, int y, int w, int h) {
        double straightW = w - h / 2.0;
        Path2D.Double path = new Path2D.Double();
        path.moveTo(x, y);
        path.lineTo(x + straightW, y);
        path.append(new Arc2D.Double(x + straightW - h / 2.0, y, h, h, 90, -180, Arc2D.OPEN), true);
        path.lineTo(x, y + h);
        path.closePath();

        g2d.setColor(GATE_FILL);
        g2d.fill(path);
        g2d.setColor(GATE_STROKE);
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(path);
    }

    /** OR gate: curved back, pointed front, IEC/US shield shape. */
    private void drawOrGate(Graphics2D g2d, int x, int y, int w, int h) {
        Path2D.Double path = new Path2D.Double();
        path.moveTo(x, y);
        path.quadTo(x + w * 0.2, y + h * 0.5, x, y + h);
        path.quadTo(x + w * 0.6, y + h, x + w, y + h * 0.5);
        path.quadTo(x + w * 0.6, y, x, y);
        path.closePath();

        g2d.setColor(GATE_FILL);
        g2d.fill(path);
        g2d.setColor(GATE_STROKE);
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(path);
    }

    /** NOT gate: triangle with a small bubble on the output tip. */
    private void drawNotGate(Graphics2D g2d, int x, int y, int w, int h) {
        int bubble = 8;
        int[] xs = {x, x, x + w - bubble};
        int[] ys = {y, y + h, y + h / 2};

        g2d.setColor(GATE_FILL);
        g2d.fillPolygon(xs, ys, 3);
        g2d.setColor(GATE_STROKE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawPolygon(xs, ys, 3);

        g2d.setColor(GATE_FILL);
        g2d.fillOval(x + w - bubble, y + h / 2 - bubble / 2, bubble, bubble);
        g2d.setColor(GATE_STROKE);
        g2d.drawOval(x + w - bubble, y + h / 2 - bubble / 2, bubble, bubble);
    }
}