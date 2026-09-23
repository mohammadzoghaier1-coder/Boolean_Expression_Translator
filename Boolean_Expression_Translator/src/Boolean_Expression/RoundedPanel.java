package Boolean_Expression;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * A JPanel that paints itself as a white "card": rounded corners and a
 * thin border, sitting on top of the window's plain background. It's a
 * drop-in JPanel replacement -- add() / remove() / setLayout() all still
 * work normally.
 */
public class RoundedPanel extends JPanel {

    private static final int ARC = 16;
    private final Color fill;
    private final Color border;

    public RoundedPanel(LayoutManager layout, Color fill, Color border) {
        super(layout);
        this.fill = fill;
        this.border = border;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(fill);
        g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC, ARC);
        g2d.setColor(border);
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC, ARC);
        g2d.dispose();
        super.paintComponent(g);
    }
}
