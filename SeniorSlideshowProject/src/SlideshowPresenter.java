import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import Animation.Animator;
import FileManager.FileManager;
import Slides.AudioState;
import Slides.SlideShowStateMachine;
import Slides.SlideState;
import Transitions.*;
import pkgImageTransitions.ImagePanel;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class SlideshowPresenter extends JFrame {

	private JPanel MainPanel;
	private JButton btnPlayPause;
	private JButton btnPrevious;
	private JButton btnNext;
	private JMenuItem mntmCreate;
	private SlideShowStateMachine slideStateMachine;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenu mnPresentModes;
	private JMenuItem mntmOpen;
	private JMenuItem mntmAuto;
	private JMenuItem mntmManual;
	private static SlideState currentSlide;
	private FileManager fMgr;
	private SoundTrack soundTrack;
	private static ImagePanel PresentationImagePanel;
	private boolean slidePlaying = false;
	private boolean clickedPlay = false;
	private boolean automatic;
	private SlideshowMaker creator;
	private Animator animator;


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
		automatic = true;
		MainPanel = new JPanel();
		MainPanel.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				resizeAllPanels();
				SlideShowStateMachine testSlide = SlideShowStateMachine.getInstance();
				if (testSlide.getCurrentSlide() != null){
					System.out.println("Updating layout due to resizing");
					//updateShow();
				}
			}
		});
		MainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(MainPanel);
		MainPanel.setLayout(null);
		
		slideStateMachine = SlideShowStateMachine.getInstance();
		soundTrack = new SoundTrack((String) null);
		fMgr = new FileManager();

		btnPlayPause = new JButton("Play");
		btnPlayPause.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e)
			{
                // InitializeShow();
				if(slidePlaying == true)
				{
					btnPlayPause.setText("Play"); // change Pause to Play when pause
					soundTrack.pauseB.doClick(); // Pause soundtrack
					clickedPlay = true;
					slidePlaying = false;
					slideStateMachine.setPausedState(true);
//					if (slideStateMachine.getNeedsReset()){
//						slideStateMachine.getPreviousSlide();
//					}
					updateShow();
				}
				else if(slidePlaying == false)
				{	
					btnPlayPause.setText("Pause"); //change Play to Pause when start
					slideStateMachine.setPausedState(false);
					soundTrack.pauseB.doClick(); // restore a paused soundtrack
					slidePlaying = true;
					if(clickedPlay == true)
					{
						clickedPlay = false;

					}
//					else{
//						soundTrack.startB.doClick(); // start soundtrack
//						slidePlaying = true;
//						if (slideStateMachine.getNeedsReset()){
//							slideStateMachine.getNextSlide();
//							slideStateMachine.setNeedsReset(false);;
//						}
//					}
					if(automatic) {
						startAutomaticSlideShow();
					}
				}
			}
		});
		btnPlayPause.setBounds(345, 530, 89, 23);
		MainPanel.add(btnPlayPause);
		btnPlayPause.setEnabled(true);


		menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 784, 21);
		MainPanel.add(menuBar);

		mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmOpen = new JMenuItem("Open File");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileTypeFilter(".ssp", "Slideshow Presentation File"));
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fc.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION){
					String selectedFilePath = fc.getSelectedFile().getPath();
					SlideShowStateMachine tempState = fMgr.readFile(selectedFilePath);
					slideStateMachine.clearSlideShow();
					SlideState slide = tempState.getFirstSlide();
					while(slide != null){
						System.out.println(slide.toString());
						slideStateMachine.addSlide(slide);
						slide = tempState.getNextSlide();
					}
					AudioState audio = tempState.getFirstAudio();
					while(audio != null){
						slideStateMachine.addAudio(audio);
						audio = tempState.getNextAudio();
					}
					currentSlide = slideStateMachine.getFirstSlide();
					if (currentSlide != null) {

						PresentationImagePanel.setImage(slideStateMachine.getSlideAtIndex(slideStateMachine.getDisplayIndex()).getIcon().getImage());
					}
					soundTrack.startB.setEnabled(slideStateMachine.getAudioListSize() != 0);
					soundTrack.jukeTable.tableChanged();
				}	
			}
		});
		mnFile.add(mntmOpen);

		JMenu mnModes = new JMenu("Modes\r\n");
		menuBar.add(mnModes);

		mnPresentModes = new JMenu("Presentation Modes");
		mnModes.add(mnPresentModes);

		mntmAuto = new JMenuItem("Automatic");
		mnPresentModes.add(mntmAuto);
		mntmAuto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPrevious.setEnabled(false);
				btnNext.setEnabled(false);
				btnPlayPause.setEnabled(true);
			}
		});

		mntmManual = new JMenuItem("Manual");
		mnPresentModes.add(mntmManual);
		mntmManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPlayPause.setEnabled(false);
				btnPrevious.setEnabled(true);
				btnNext.setEnabled(true);
			}
		});

		mntmCreate = new JMenuItem("Creation");
		mnModes.add(mntmCreate);
		mntmCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SlideShowStateMachine currentSlide = SlideShowStateMachine.getInstance();
				if(currentSlide != null){
					currentSlide.clearSlideShow();
				}
				if (creator == null) {
					creator = new SlideshowMaker();
				}
				creator.setVisible(true);
				setVisible(false);
			}
		});

		btnPrevious = new JButton("<<<<");
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentSlide = slideStateMachine.getPreviousSlide();
				updateShow();
				if(currentSlide != null)
				{
					btnPrevious.setEnabled(true);
				}
			}
		});
		btnPrevious.setBounds(50, -30, 89, 23);
		MainPanel.add(btnPrevious);
		btnPrevious.setEnabled(false);

		btnNext = new JButton(">>>>");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentSlide = slideStateMachine.getNextSlide();
				updateShow();
				if(currentSlide != null)
				{
					btnNext.setEnabled(true);
				}
			}
		});
		btnNext.setBounds(445, 530, 89, 23);
		MainPanel.add(btnNext);
		btnNext.setEnabled(false);
		resizeMainPanel();

		PresentationImagePanel = new ImagePanel();
		PresentationImagePanel.setBounds(0, menuBar.getHeight(), 765, 504);		
		MainPanel.add(PresentationImagePanel);	
		PresentationImagePanel.initializeBlankImage();
		PresentationImagePanel.repaint();
	}
	
	public SlideshowPresenter(SlideshowMaker creator) {
		this();
		this.creator = creator;		
	}

	private void resizeAllPanels(){
		resizeMainPanel();
		resizePresentationPanel();
	}

	private void resizePresentationPanel() {
		PresentationImagePanel.setBounds(0, menuBar.getHeight(), MainPanel.getWidth(), MainPanel.getHeight() -  (2 * menuBar.getHeight()) - btnPrevious.getHeight() );
		
	}

	private void resizeMainPanel() {
		int panelWidth = this.getWidth() - 35;
		int panelHeight = this.getHeight() - 35;
		MainPanel.setBounds(5, 5, panelWidth, panelHeight);

		btnPrevious.setBounds((panelWidth / 2) - 150, panelHeight - 40, 89, 23);
		btnPlayPause.setBounds((panelWidth / 2) - 43, panelHeight - 40, 100, 23);
		btnNext.setBounds((panelWidth / 2) + 75, panelHeight - 40, 89, 23);
		menuBar.setBounds(0, 0, MainPanel.getWidth(), 21);

	}

	
	private void updateShow() {
		if (!slidePlaying){
			currentSlide = slideStateMachine.getCurrentSlide();
			if(currentSlide != null)
			{
				PresentationImagePanel.setImage(slideStateMachine.getSlideAtIndex(slideStateMachine.getDisplayIndex()).getIcon().getImage());
			}
		}

	}

	private void startAutomaticSlideShow() {
		if (!slideStateMachine.getPausedState()){
			currentSlide = slideStateMachine.getCurrentSlide();
			PresentationImagePanel.setImage(slideStateMachine.getSlideAtIndex(slideStateMachine.getDisplayIndex()).getIcon().getImage());
			if (animator == null || slideStateMachine.getShowEnded()){
				slideStateMachine.setShowEnded(false);
				animator = new Animator(PresentationImagePanel);
				animator.start();
			}
		}

	}
}
