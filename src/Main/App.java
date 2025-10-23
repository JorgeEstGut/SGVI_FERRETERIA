package Main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import View.LoginView;
import Model.TrabajadorDAO;
import Controller.LoginController;

public class App {

    public static void abrirLogin() {
        LoginView vista = new LoginView();
        TrabajadorDAO dao = new TrabajadorDAO();
        new LoginController(vista, dao);
        vista.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }

            abrirLogin();
        });
    }
}
