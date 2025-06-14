import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        UIManager.put("OptionPane.messageFont", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Panel.background", new Color(36, 36, 46)); // ciemne tło
        UIManager.put("OptionPane.background", new Color(36, 36, 46));
        UIManager.put("Button.background", new Color(69, 123, 157));
        UIManager.put("Button.foreground", Color.WHITE);




        LoginPanel loginPanel = new LoginPanel();
        loginPanel.setVisible(true);


    }
}