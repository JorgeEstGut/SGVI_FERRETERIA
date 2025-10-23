package Custom;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RoundComboBox<E> extends JComboBox<E> {

    private int arcWidth = 10;
    private int arcHeight = 10;
    private Color backgroundColor = new Color(244, 244, 244);
    private Color borderColor = new Color(200, 200, 200);

    public RoundComboBox() {
        super();
        setOpaque(false);
        setBorder(new EmptyBorder(5, 10, 5, 10));
        setFocusable(false);
        setBackground(backgroundColor);
        setForeground(Color.BLACK);
        setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setHorizontalAlignment(CENTER);
                if (isSelected) {
                    c.setBackground(new Color(255, 235, 59)); // amarillo claro
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo redondeado
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);

        // Borde
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);

        super.paintComponent(g);
        g2.dispose();
    }
}
