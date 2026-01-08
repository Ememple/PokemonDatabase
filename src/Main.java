import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PokemonGUI gui = new PokemonGUI();
            gui.setVisible(true);
        });
    }
}