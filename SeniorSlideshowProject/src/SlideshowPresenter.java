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

//class that represents the slideshow's presentation mode
public class SlideshowPresenter extends JFrame {

	//the main panel within the window
	private JPanel MainPanel;
	//button to represent the play/pause feature
	private JButton btnPlayPause;
	//button to go back a slide in manual mode
	private JButton btnPrevious;
	//button to go forward a slide in manual mode
	private JButton btnNext;
	//menu item to go to creation mode
	private JMenuItem mntmCreate;
	//instance of the slideShowStateMachine instance
	private SlideShowStateMachine slideStateMachine;
	//menu bar for all of the basic menu options
	private JMenuBar menuBar;
	//file option within the menu bar
	private JMenu mnFile;
	//presentation mode options in menu
	private JMenu mnPresentModes;
	//open file option in presentation mode
	private JMenuItem mntmOpen;
	//automatic presentation mode option
	private JMenuItem mntmAuto;
	//manual presentation mode option
	private JMenuItem mntmManual;
	//instance representing the currentSlide
	private static SlideState currentSlide;
	//file manager to be used for loading files correctly
	private FileManager fMgr;
	//instance of the soundtrack for the slideshow
	private SoundTrack soundTrack;
	//panel within which the slideshow is presented
	private static ImagePanel PresentationImagePanel;
	//boolean to determine when the slideshow is playing
	private boolean slidePlaying = false;
	//boolean to determine when someone has clicked play or pause
	private boolean clickedPlay = false;
	//boolean to determine whether or not you are in automatic mode
	private boolean automatic;
	//instance of the slideshow creator
	private SlideshowMaker creator;
	//instance of the animator class
	private Animator animator;
	//singleton representation of this presenter class
	private static SlideshowPresenter presenter = new SlideshowPresenter();


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					presenter = new SlideshowPresenter();
					presenter.setMinimumSize(new Dimension(800, 600));
					presenter.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	private SlideshowPresenter() {
		
		setTitle("Slideshow Presentation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		automatic = true;
		MainPanel = new JPanel();
		//create a listener so that when the panel is resized it will be adjusted appropriately
		MainPanel.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				resizeAllPanels();
				if (slideStateMachine.getShowEnded()){
					updateShow();
				}
			}
		});
		MainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(MainPanel);
		MainPanel.setLayout(null);
		
		slideStateMachine = SlideShowStateMachine.getInstance();
		soundTrack = new SoundTrack((String) null);
		soundTrack.startB.setEnabled(true);
		fMgr = new FileManager();

		btnPlayPause = new JButton("Play");
		//create an action listener which on pressing the play/pause button will switch it to the other button type and take appropriate action
		btnPlayPause.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e)
			{
				if (slideStateMachine.getSlideTime() == 0){
					slideStateMachine.setSoundtracklessTransition();
				}
                // check if the slideshow is playing or not; if it is change it to the play button and pause the slideshow otherwise change it to pause and play the slideshow
				if(slidePlaying == true)
				{
					btnPlayPause.setText("Play"); // change Pause to Play when pause
					soundTrack.pauseB.doClick(); // Pause soundtrack
					clickedPlay = true;
					slidePlaying = false;
					slideStateMachine.setPausedState(true);
					updateShow();
				}
				else if(slidePlaying == false)
				{	
					btnPlayPause.setText("Pause"); //change Play to Pause when start
					slideStateMachine.setPausedState(false);
					slidePlaying = true;
					//check to see whether or not the soundtrack has been properly initialized and then initialize it as needed
					if(clickedPlay == true)
					{
						soundTrack.pauseB.doClick(); // restore a paused soundtrack
						clickedPlay = false;

					}
					else{
						soundTrack.startB.doClick(); // start soundtrack
					}
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
		//create an action listener for the open file menu option that opens a slideshow file and populates the slideShowStateMachine appropriately
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileTypeFilter(".ssp", "Slideshow Presentation File"));
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fc.showOpenDialog(null);
				//check to see if the file chosen was an approved file and if it was then begin to gather the data from it
				if (result == JFileChooser.APPROVE_OPTION){
					String selectedFilePath = fc.getSelectedFile().getPath();
					SlideShowStateMachine tempState = fMgr.readFile(selectedFilePath);
					slideStateMachine.clearSlideShow();
					SlideState slide = tempState.getFirstSlide();
					//loop through the read data adding each slide to the slideshowstatemachine
					while(slide != null){
						System.out.println(slide.toString());
						slideStateMachine.addSlide(slide);
						slide = tempState.getNextSlide();
					}
					AudioState audio = tempState.getFirstAudio();
					//loop through the read data adding each audio file to the slideShowStateMachine
					while(audio != null){
						slideStateMachine.addAudio(audio);
						audio = tempState.getNextAudio();
					}
					currentSlide = slideStateMachine.getFirstSlide();
					//if the currentslide is null then initialize it to the correct image after opening the file
					if (currentSlide != null) {
						PresentationImagePanel.initializeBlankImage();
						PresentationImagePanel.repaint();
						PresentationImagePanel.setImage(slideStateMachine.getSlideAtIndex(slideStateMachine.getDisplayIndex()).getIcon().getImage());
					}
					soundTrack.startB.setEnabled(slideStateMachine.getAudioListSize() != 0);
					soundTrack.jukeTable.tableChanged();
				}	
			}
		});
		mnFile.add(mntmOpen);

		JMenu mnModes = new JMenu("Presentation Modes\r\n");
		menuBar.add(mnModes);

		mntmAuto = new JMenuItem("Automatic");
		mnModes.add(mntmAuto);
		//add an action listener for the automatic menu option to change the presentation mode to automatic mode
		mntmAuto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//reset the state of the slideShow to the first slide and draw it then set the buttons to the right states
				slideStateMachine.resetDisplayIndex();
				PresentationImagePanel.initializeBlankImage();
				PresentationImagePanel.repaint();
				updateShow();
				btnPrevious.setEnabled(false);
				btnNext.setEnabled(false);
				btnPlayPause.setEnabled(true);
			}
		});

		mntmManual = new JMenuItem("Manual");
		mnModes.add(mntmManual);
		//add an action listener for the manual menu option to change the presentation mode to manual
		mntmManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//paint the images to the appropriate sizes
				if(soundTrack.startB.getText() == "Stop") {
					soundTrack.startB.doClick();
				}
				slidePlaying = false;
				clickedPlay = false;
				slideStateMachine.getFirstAudio();
				PresentationImagePanel.initializeBlankImage();
				PresentationImagePanel.repaint();
				updateShow();
				btnPlayPause.setEnabled(false);
				btnPrevious.setEnabled(true);
				btnNext.setEnabled(true);
				animator = null;
			}
		});

		JMenu mnGoCreation = new JMenu("Go to Creation Mode");
		menuBar.add(mnGoCreation);
		mntmCreate = new JMenuItem("Creation");
		mnGoCreation.add(mntmCreate);
		//add action listener for creation mode menu option that changes the program from presentation mode to creation mode
		mntmCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//if creation mode has not been made yet go to creation mode
				if (creator == null) {
					creator = new SlideshowMaker();
				} else {
					//Otherwise notify to reinitialize audio list
					creator.notifyAudioUpdate();
				}
				creator.setVisible(true);
				setVisible(false);
			}
		});

		btnPrevious = new JButton("<<<<");
		//create an action listener for the previous slide button
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				slideStateMachine.decrementDisplayIndex();
				updateShow();
				//move the slideshow to the previous slide if the there is one
				if(currentSlide != null)
				{
					btnPrevious.setEnabled(true);
				}
				animator.reintializeSlides();
				slideStateMachine.getFirstAudio();
			}
		});
		btnPrevious.setBounds(50, -30, 89, 23);
		MainPanel.add(btnPrevious);
		btnPrevious.setEnabled(false);

		btnNext = new JButton(">>>>");
		//add action listener for the next slide button 
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				slideStateMachine.incrementDisplayIndex();
				updateShow();
				//if there is a next slide then allow the button to be pressed
				if(currentSlide != null)
				{
					btnNext.setEnabled(true);
				}
				animator.reintializeSlides();
				slideStateMachine.getFirstAudio();
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
	//special constructor used by creation mode to pass instance of the creator so that we can go back to creation mode easily
	public SlideshowPresenter(SlideshowMaker creator) {
		this();
		this.creator = creator;		
	}

	//function to resize the slideshow presenter when changes are made 
	private void resizeAllPanels(){
		resizeMainPanel();
		resizePresentationPanel();
		
	}

	//function to reset the image after resizing 
	private void resizePresentationPanel() {
		PresentationImagePanel.setBounds(0, menuBar.getHeight(), MainPanel.getWidth(), MainPanel.getHeight() -  (2 * menuBar.getHeight()) - btnPrevious.getHeight() );
//		PresentationImagePanel.initializeBlankImage();
//		PresentationImagePanel.repaint();
	}

	//function to resize the main panel and the buttons and menu options within
	private void resizeMainPanel() {
		//integer representing the new desired width of the main panel
		int panelWidth = this.getWidth() - 35;
		//int representing the new desired height of the main panel
		int panelHeight = this.getHeight() - 35;
		MainPanel.setBounds(5, 5, panelWidth, panelHeight);

		btnPrevious.setBounds((panelWidth / 2) - 150, panelHeight - 40, 89, 23);
		btnPlayPause.setBounds((panelWidth / 2) - 43, panelHeight - 40, 100, 23);
		btnNext.setBounds((panelWidth / 2) + 75, panelHeight - 40, 89, 23);
		menuBar.setBounds(0, 0, MainPanel.getWidth(), 21);

	}

	//function to update the image within the presentation mode if the slideshow is not currently playing
	private void updateShow() {
		if (!slidePlaying){
			currentSlide = slideStateMachine.getCurrentSlide();
			if(currentSlide != null)
			{
				PresentationImagePanel.setImage(slideStateMachine.getSlideAtIndex(slideStateMachine.getDisplayIndex()).getIcon().getImage());
			}
		}

	}

	//function to start the animator thread and restart animator threads when the previous slideshow ended
	private void startAutomaticSlideShow() {
		//check to make sure the slideshow is not currently paused
		if (!slideStateMachine.getPausedState()){
			//check to see if the slideshow hasnt ended yet if so set the image before allowing the thread to continue
			if (!slideStateMachine.getShowEnded()){
				PresentationImagePanel.setImage(slideStateMachine.getSlideAtIndex(slideStateMachine.getDisplayIndex()).getIcon().getImage());
			}
			//if the animator is not existent yet or the last slideshow animator has ended start a new instance of the animator
			if (animator == null || slideStateMachine.getShowEnded()){
				slideStateMachine.setShowEnded(false);
				//reset the display index if the slideshow had ended so that the first image is the first image of the presentation
				if (slideStateMachine.getDisplayIndex() == (slideStateMachine.getSlideShowSize() - 1)){
					slideStateMachine.resetDisplayIndex();
					initializeShow();
				}
				animator = new Animator(PresentationImagePanel);
				animator.start();			
				}
		}

	}
	//function to return the singleton instance of the presenter
	public static SlideshowPresenter getInstance(){
		return presenter;
	}
	
	//function to reset the play button and reset the general state of the presenter when the animation thread ends
	public void resetPlayButton(){
		btnPlayPause.setText("Play");
		if(soundTrack.startB.getText() == "Stop") {
			soundTrack.startB.doClick();
		}
		clickedPlay = false;
		slidePlaying = false;
		slideStateMachine.getFirstAudio();
		PresentationImagePanel.initializeBlankImage();
		PresentationImagePanel.repaint();
		updateShow();
		slideStateMachine.resetDisplayIndex();
	}
	//function to be used to initialize image of the presenter from the animator
	public void initializeShow(){
		PresentationImagePanel.setImage(slideStateMachine.getSlideAtIndex(0).getIcon().getImage());
	}

	public void setMaker(SlideshowMaker slideshowMaker) {
		creator = slideshowMaker;		
	}
}
