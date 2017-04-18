import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SoundIndicator extends JComponent {
	private static final long serialVersionUID = 1L;
	private Point location;
	private int x;
	private int y;
	private int width;
	private int height;
	private JLabel label = new JLabel();
	private Dimension dim;
	public SoundIndicator(String name, int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.label.setText(name);
		this.label.setBackground(Color.BLACK);
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.yellow);
		g.fillRect(x, y, width, height);
	}
	
	public static void main(String args[]){
		JFrame frame = new JFrame("JFrame Color Example");
		frame.setSize(100, 100);
		SoundIndicator s = new SoundIndicator("1",0,0,50,50);
		frame.add(s);
		frame.setVisible(true);
	}
}
