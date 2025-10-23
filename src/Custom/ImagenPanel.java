package Custom;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagenPanel extends JPanel {
    private Image imagen;

    public ImagenPanel(String rutaImagen) {
        URL url = getClass().getResource(rutaImagen);
        if (url != null) {
            this.imagen = new ImageIcon(url).getImage();
        } else {
            System.err.println("No se encontró la imagen: " + rutaImagen);
        }
        setLayout(new java.awt.BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagen != null) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }
}