package Boolean_Expression;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * A JTextField that shows light gray hint text (e.g. "e.g. (A+B)^~C")
 * when it is empty and doesn't have focus. Purely a UX nicety so first-time
 * users know what format to type the expression in.
 */
public class PlaceholderTextField extends JTextField {

    private final String placeholder;

    public PlaceholderTextField(int columns, String placeholder) {
        super(columns);
        this.placeholder = placeholder;
        setFont(getFont().deriveFont(Font.PLAIN, 15f));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xCB, 0xD5, 0xE1), 1, true),
                new EmptyBorder(8, 10, 8, 10)));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getText().isEmpty() && !isFocusOwner()) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(160, 167, 179));
            g2d.setFont(getFont());
            g2d.drawString(placeholder, getInsets().left, getInsets().top + g2d.getFontMetrics().getAscent());
            g2d.dispose();
        }
    }
}
