package Custom;

import java.awt.Image;
import javax.swing.ImageIcon;

public class IconScaler {

    public static ImageIcon getScaledIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(IconScaler.class.getResource(path));
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
