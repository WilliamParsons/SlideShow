import java.awt.*;
import javax.swing.*;

public class AudioImagePanel extends JPanel {
    private String filename;
    private Color color;

    AudioImagePanel(Color color, String filename) {
        this.color = color;
        this.filename = filename;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.BLACK);
        FontMetrics fm = g.getFontMetrics();
        int totalWidth = fm.stringWidth(filename);
        int x = (getWidth() - totalWidth) / 2;
        int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
        g.drawString(filename, x, y);

    }

    public static void main(String[] arguments) {

        AudioImagePanel panel = new AudioImagePanel(Color.red, "test");

        // create a basic JFrame
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("JFrame Color Example");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add panel to main frame
        frame.add(panel);

        frame.setVisible(true);

    }
}