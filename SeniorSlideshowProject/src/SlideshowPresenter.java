import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Slides.SlideShowStateMachine;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class SlideshowPresenter extends JFrame {

	private JPanel MainPanel;
	private JButton btnPlayPause;
	private JMenuItem mntmCreate;
	private SlideShowStateMachine slideStateMachine;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SlideshowPresenter frame = new SlideshowPresenter();
					frame.setMinimumSize(new Dimension(800, 600));
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SlideshowPresenter() {
		setTitle("Slideshow Presentation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		MainPanel = new JPanel();
		MainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(MainPanel);
		MainPanel.setLayout(null);
		
		slideStateMachine = SlideShowStateMachine.getInstance();

		btnPlayPause = new JButton("Play/Pause");
		btnPlayPause.setBounds(345, 527, 89, 23);
		MainPanel.add(btnPlayPause);

		JLabel lblPreview = new JLabel("Preview");
		lblPreview.setBounds(10, 34, 764, 482);
		MainPanel.add(lblPreview);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 784, 21);
		MainPanel.add(menuBar);

		JMenu mnModes = new JMenu("Modes\r\n");
		menuBar.add(mnModes);

		mntmCreate = new JMenuItem("Creatation");
		mnModes.add(mntmCreate);
		mntmCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SlideshowMaker maker = new SlideshowMaker();
				maker.setVisible(true);
				setVisible(false);
			}
		});
	}

	private void resizeMainPanel() {
		int panelWidth = this.getWidth() - 35;
		int panelHeight = this.getHeight() - 35;
		MainPanel.setBounds(5, 5, panelWidth, panelHeight);
		btnPlayPause.setBounds(panelWidth-455, panelHeight-73, 89, 23);
	}
	
	private void paintImage(JLabel label, ImageIcon icon){
		if(slideStateMachine.getSlideShowSize() > 0) {
			BufferedImage bufferedImg = new BufferedImage(
				    icon.getIconWidth(),
				    icon.getIconHeight(),
				    BufferedImage.TYPE_INT_RGB);
			Graphics g = bufferedImg.createGraphics();
			icon.paintIcon(null, g, 0,0);
			g.dispose();
				
			Image scaledImg;
			int imageHeight = bufferedImg.getHeight();
			int imageWidth = bufferedImg.getWidth();
			double heightRatio = (double)imageHeight/(double)imageWidth;
			int scaledHeight = label.getHeight();
			int scaledWidth = label.getWidth();
			if((scaledWidth*heightRatio) > scaledHeight){
				scaledImg = bufferedImg.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
			}
			else {
				scaledImg = bufferedImg.getScaledInstance(scaledWidth, (int)(scaledWidth*heightRatio), Image.SCALE_SMOOTH);
			}
			icon = new ImageIcon(scaledImg);
			label.setIcon(icon);
		}
	}
}