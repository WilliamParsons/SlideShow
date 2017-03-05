import java.awt.EventQueue;
import FileManager.*;
import Slides.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSlider;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.imageio.ImageIO;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JMenuBar;
import javax.swing.border.BevelBorder;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.border.EtchedBorder;

public class SlideshowMaker extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private SlideShowStateMachine stateMachine;
	private JPanel MainPanel;
	private JPanel LayoutPanel;
	private JSlider layoutSlider;
	private JButton addImageBtn;
	private JButton removeImageBtn;
	private JPanel TransitionPanel;
	private SoundTrack soundTrack;
	private JLabel lblImagepreview;
	private File imageFile;
	private ImageIcon previewImage;
	private JPanel AudioPanel;
	private JMenu mnFile;
	private JMenuItem mntmOpen;
	private JMenuItem mntmSave;
	private JRadioButton rdbtnNoTrans;
	private JRadioButton rdbtnSwipeUp;
	private JRadioButton rdbtnSwipeDown;
	private JRadioButton rdbtnSwipeLeft;
	private JRadioButton rdbtnSwipeRight;
	private JRadioButton rdbtnCrossfade;
	private JPanel EditPanel;
	private JLabel lblSlidesRight;
	private JLabel lblNextImage;
	private JLabel lblPrimaryImage;
	private JLabel lblPreviousImage;
	private JLabel lblSlidesLeft;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SlideshowMaker frame = new SlideshowMaker();
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
	public SlideshowMaker() {
		setTitle("Slideshow Maker");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);
		
		mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		MainPanel = new JPanel();
		MainPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resizePanels();
			}
		});
		MainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		MainPanel.setBounds(5, 5, 790, 590);
		setContentPane(MainPanel);
		
		LayoutPanel = new JPanel();
		LayoutPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		LayoutPanel.setBounds(10, 11, 764, 140);
		
		TransitionPanel = new JPanel();
		TransitionPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		TransitionPanel.setBounds(10, 162, 764, 238);

		MainPanel.setLayout(null);
		MainPanel.add(LayoutPanel);
		LayoutPanel.setLayout(null);
		
		addImageBtn = new JButton("+");
		addImageBtn.setBounds(659, 110, 45, 20);
		LayoutPanel.add(addImageBtn);
		AddImageListener imageListener = new AddImageListener();
		addImageBtn.addActionListener(imageListener);
		
		removeImageBtn = new JButton("-");
		removeImageBtn.setBounds(704, 110, 45, 20);
		LayoutPanel.add(removeImageBtn);
		
		layoutSlider = new JSlider();
		layoutSlider.setBounds(15, 110, 642, 20);
		layoutSlider.setValue(0);
		LayoutPanel.add(layoutSlider);
		
		lblSlidesRight = new JLabel("");
		lblSlidesRight.setHorizontalAlignment(SwingConstants.CENTER);
		lblSlidesRight.setBounds(487, 37, 40, 30);
		ImageIcon iconRight = new ImageIcon(SlideshowMaker.class.getResource("/ImageFiles/SlidesRight.jpg"));
		Image slidesRight = iconRight.getImage().getScaledInstance(lblSlidesRight.getWidth(), lblSlidesRight.getHeight(), Image.SCALE_SMOOTH);
		lblSlidesRight.setIcon(new ImageIcon(slidesRight, iconRight.getDescription()));
		LayoutPanel.add(lblSlidesRight);
		
		lblNextImage = new JLabel("Next Image");
		lblNextImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblNextImage.setBounds(397, 22, 80, 60);
		LayoutPanel.add(lblNextImage);
		
		lblPrimaryImage = new JLabel("Primary Image");
		lblPrimaryImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrimaryImage.setBounds(267, 7, 120, 90);
		LayoutPanel.add(lblPrimaryImage);
		
		lblPreviousImage = new JLabel("Previous Image");
		lblPreviousImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblPreviousImage.setBounds(177, 22, 80, 60);
		LayoutPanel.add(lblPreviousImage);
		
		lblSlidesLeft = new JLabel("");
		lblSlidesLeft.setHorizontalAlignment(SwingConstants.CENTER);
		lblSlidesLeft.setBounds(127, 37, 40, 30);
		ImageIcon iconLeft = new ImageIcon(SlideshowMaker.class.getResource("/ImageFiles/SlidesLeft.jpg"));
		Image slidesLeft = iconLeft.getImage().getScaledInstance(lblSlidesLeft.getWidth(), lblSlidesLeft.getHeight(), Image.SCALE_SMOOTH);
		lblSlidesLeft.setIcon(new ImageIcon(slidesLeft, iconLeft.getDescription()));
		LayoutPanel.add(lblSlidesLeft);
		
		AudioPanel = new JPanel();
		AudioPanel.setBounds(10, 411, 764, 119);
		MainPanel.add(AudioPanel);
		AudioPanel.setLayout(null);
		
		soundTrack = new SoundTrack((String) null);
		soundTrack.setBounds(0, 0, 764, 110);
		AudioPanel.add(soundTrack);
		MainPanel.add(TransitionPanel);
		
		TransitionPanel.setLayout(null);
		
		EditPanel = new JPanel();
		EditPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		EditPanel.setBounds(542, 9, 124, 212);
		TransitionPanel.add(EditPanel);
		EditPanel.setLayout(null);
		
		JButton PreviewTransition = new JButton(">");
		PreviewTransition.setBounds(35, 166, 45, 23);
		EditPanel.add(PreviewTransition);
		
		rdbtnNoTrans = new JRadioButton("None");
		rdbtnNoTrans.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnNoTrans.setBounds(10, 9, 105, 23);
		EditPanel.add(rdbtnNoTrans);
		
		rdbtnSwipeUp = new JRadioButton("Swipe Up");
		rdbtnSwipeUp.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnSwipeUp.setBounds(10, 35, 105, 23);
		EditPanel.add(rdbtnSwipeUp);
		
		rdbtnSwipeDown = new JRadioButton("Swipe Down");
		rdbtnSwipeDown.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnSwipeDown.setBounds(10, 61, 105, 23);
		EditPanel.add(rdbtnSwipeDown);
		
		rdbtnSwipeLeft = new JRadioButton("Swipe Left");
		rdbtnSwipeLeft.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnSwipeLeft.setBounds(10, 87, 105, 23);
		EditPanel.add(rdbtnSwipeLeft);
		
		rdbtnSwipeRight = new JRadioButton("Swipe Right");
		rdbtnSwipeRight.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnSwipeRight.setBounds(10, 113, 105, 23);
		EditPanel.add(rdbtnSwipeRight);
		
		rdbtnCrossfade = new JRadioButton("Crossfade");
		rdbtnCrossfade.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnCrossfade.setBounds(10, 136, 105, 23);
		EditPanel.add(rdbtnCrossfade);
		lblImagepreview = new JLabel("");
		lblImagepreview.setBounds((TransitionPanel.getWidth()/2)-150, (TransitionPanel.getHeight()/2)-110, 300, 225);
		TransitionPanel.add(lblImagepreview);
		imageFile = new File(SlideshowMaker.class.getResource("/ImageFiles/Test1.jpg").getPath());
		resizePanels();
	}
	
	private void resizePanels(){
		resizeMainPanel();
		resizeLayoutPanel();
		resizeAudioPanel();
		resizeTransitionPanel();
	}
	
	private void resizeMainPanel(){
	    int panelWidth = this.getWidth()-35;
	    int panelHeight = this.getHeight()-35;
	    MainPanel.setBounds(5, 5, panelWidth, panelHeight);
	}
	
	private void resizeLayoutPanel(){
	    int mainPanelWidth = MainPanel.getWidth();
	    int panelWidth = LayoutPanel.getWidth();
	    int panelHeight = LayoutPanel.getHeight();
	    double heightRatio = (double)panelHeight/(double)panelWidth;
	    panelWidth = mainPanelWidth - 10;
	    panelHeight = (int)(panelWidth*heightRatio);
	    LayoutPanel.setBounds(10, 26, panelWidth, panelHeight);
	    removeImageBtn.setBounds(panelWidth-60, panelHeight-30, 45, 20);
	    addImageBtn.setBounds(panelWidth-110, panelHeight-30, 45, 20);
	    layoutSlider.setBounds(15, panelHeight-30, panelWidth-160, 20);
	}
	
	private void resizeTransitionPanel(){
		int mainPanelWidth = MainPanel.getWidth();
	    int oldHeight = TransitionPanel.getHeight();
	    int panelY = LayoutPanel.getY() + LayoutPanel.getHeight() + 11;
	    int panelWidth = mainPanelWidth - 10;
	    int panelHeight = MainPanel.getHeight() - AudioPanel.getHeight() - LayoutPanel.getHeight() - 60;
	    double labelHeightRatio = (double)panelHeight/(double)oldHeight;
	    int labelHeight = (int)(lblImagepreview.getHeight()*labelHeightRatio);
	    int labelWidth = (int)(labelHeight * 1.333);
	    TransitionPanel.setBounds(10, panelY, panelWidth, panelHeight);
    	lblImagepreview.setBounds((panelWidth/2)-(labelWidth/2), (TransitionPanel.getHeight()/2)-(labelHeight/2), labelWidth, labelHeight);
		resizePreviewImage();
    	EditPanel.setBounds(lblImagepreview.getX() + lblImagepreview.getWidth() + 10, lblImagepreview.getY() + lblImagepreview.getHeight() - 200, 118, 200);
	}
	
	private void resizeAudioPanel(){
		int mainPanelWidth = MainPanel.getWidth();
		int panelWidth = mainPanelWidth - 10;
	    AudioPanel.setBounds(10, MainPanel.getHeight()-129, panelWidth, 119);
	    soundTrack.setBounds(0, 0, panelWidth, 110);
	}
	
	private void resizePreviewImage(){
		BufferedImage bufferedImg = null;
		try {
		    bufferedImg = ImageIO.read(imageFile);
		    Image scaledImg;
		    int imageHeight = bufferedImg.getHeight();
		    int imageWidth = bufferedImg.getWidth();
		    double heightRatio = (double)imageHeight/(double)imageWidth;
		    int scaledHeight = lblImagepreview.getHeight();
		    int scaledWidth = lblImagepreview.getWidth();
		    if((scaledWidth*heightRatio) > scaledHeight){
		    	scaledImg = bufferedImg.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
		    }
		    else {
		    	scaledImg = bufferedImg.getScaledInstance(scaledWidth, (int)(scaledWidth*heightRatio), Image.SCALE_SMOOTH);
		    }
		    previewImage = new ImageIcon(scaledImg);
		    
		    lblImagepreview.setIcon(previewImage);
		} catch (IOException e) {
		    e.printStackTrace();
		}		
	}
	
}
