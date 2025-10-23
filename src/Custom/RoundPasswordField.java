package Custom;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RoundPasswordField extends JPasswordField {

    private int radius;

    public RoundPasswordField(int radius) {
        this.radius = radius;
        setOpaque(false); // importante para que se vea el redondeo
        setBorder(new EmptyBorder(5, 10, 5, 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibuja fondo redondeado
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.dispose();

        // 🔹 Ahora SÍ pintamos los caracteres de la contraseña
        super.paintComponent(g);
    }
}
