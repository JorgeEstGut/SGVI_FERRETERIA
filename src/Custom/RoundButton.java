package Custom;

import java.awt.*;
import javax.swing.*;

public class RoundButton extends JButton {
    private int arcWidth = 7;
    private int arcHeight = 7;

    public RoundButton(String text) {
        super(text);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

        // Texto
        super.paintComponent(g);
        g2.dispose();
    }
    
}
