package Boolean_Expression;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

/**
 * A flat, rounded-corner button with a hover/press color change,
 * used instead of the default chunky Swing button for a more modern look.
 */
public class RoundedButton extends JButton {

    private static final int ARC = 14;

    private final Color baseColor;
    private final Color hoverColor;
    private final Color pressColor;
    private Color currentColor;

    public RoundedButton(String text, Color baseColor, Color hoverColor, Color pressColor) {
        super(text);
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
        this.pressColor = pressColor;
        this.currentColor = baseColor;

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(getFont().deriveFont(Font.BOLD, 14f));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                currentColor = hoverColor;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                currentColor = baseColor;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                currentColor = pressColor;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentColor = getModel().isRollover() ? hoverColor : baseColor;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(currentColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), ARC, ARC);
        g2d.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return new Dimension(Math.max(d.width, 120), Math.max(d.height, 38));
    }
}
