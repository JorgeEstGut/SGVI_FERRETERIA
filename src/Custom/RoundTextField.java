package Custom;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RoundTextField extends JTextField {

    private int arcWidth = 7;
    private int arcHeight = 7;

    public RoundTextField(int columns) {
        super(columns);
        setOpaque(false); // importante: para que se vea lo pintado
        setBorder(new EmptyBorder(5, 10, 5, 10)); // margen interno
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // fondo blanco redondeado
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);

        super.paintComponent(g);
        g2.dispose();
    }

    
}
