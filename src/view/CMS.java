package view;

import javax.swing.SwingUtilities;

public class CMS {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}
