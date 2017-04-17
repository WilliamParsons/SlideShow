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

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRadioButton;

public class SlideshowPresenter extends JFrame {

	private JPanel MainPanel;
	private SlideShowStateMachine slideStateMachine;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenu mnCreation;
	private JMenuItem mntmOpen;
	private JMenuItem mntmCreate;
	private JButton btnPlayPause;
	private JButton btnPrevious;
	private JButton btnNext;
	private JRadioButton rdbtnAutomatic;
	private JRadioButton rdbtnManual;
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
		setTitle("Slideshow Presentation");									//Set the title of the Presentation Window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);						//Pressing X on the window will close it
		setBounds(100, 100, 800, 600);										//Set the bounds of the widnow
		automatic = true;													//Always start Presentation Mode in Automatic Mode
		MainPanel = new JPanel();											//Create the window
		MainPanel.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				resizeMainPanel();											//Resize the window when adjusting
				resizeAllPanels();
				SlideShowStateMachine testSlide = SlideShowStateMachine.getInstance();
				if (testSlide.getCurrentSlide() != null){
					updateShow();
				}
			}
		});
		MainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(MainPanel);											//Add all components to the Main Panel
		MainPanel.setLayout(null);
		slideStateMachine = SlideShowStateMachine.getInstance();			//Get an instance of SlideShowStateMachine for this run
		soundTrack = new SoundTrack((String) null);							//Get an instance of SoundTrack for this run
		fMgr = new FileManager();											//Get an instance of FileManager for this run
		slideStateMachine = SlideShowStateMachine.getInstance();
		soundTrack = new SoundTrack((String) null);
		fMgr = new FileManager();
		btnPlayPause = new JButton("Play");									//Create a "Play" button
		btnPlayPause.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e)
			{
				if(slidePlaying == true)									//If "slidePlaying" is true, do...
				{
					btnPlayPause.setText("Play"); 							//Change Pause to Play when pause
					soundTrack.pauseB.doClick(); 							//Pause soundtrack
					clickedPlay = true;										//Set the state "clickedPlay" to true
					slidePlaying = false;									//Set the state "slidePlaying" to false
				if(slidePlaying == true)
				{
					btnPlayPause.setText("Play"); 							// change Pause to Play when pause
					soundTrack.pauseB.doClick(); 							// Pause soundtrack
					clickedPlay = true;
					slidePlaying = false;
					slideStateMachine.setPausedState(true);
					if (slideStateMachine.getNeedsReset()){
						slideStateMachine.getPreviousSlide();
					}
				}
				else if(slidePlaying == false)								//If "slidePlaying" is false, do...
				{	
					btnPlayPause.setText("Pause"); 							//Change Play to Pause when start
					if(clickedPlay == true)									//If "clickedPlay" is true, do...
					btnPlayPause.setText("Pause"); 							//change Play to Pause when start
					slideStateMachine.setPausedState(false);
					if(clickedPlay == true)
					{
						soundTrack.pauseB.doClick(); 						// restore a paused soundtrack
						clickedPlay = false;								//Set clickedPay to false
						slidePlaying = true;								//Set slidePlaying to true
						if (slideStateMachine.getNeedsReset()){				//If slideStateMachine calls getNeedsReset(), do...
							slideStateMachine.getNextSlide();				//call getNextSlide()
							slideStateMachine.setNeedsReset(false);			//Set setNeedsReset to false
						}
					}else{
						soundTrack.startB.doClick(); 						// start soundtrack
						slidePlaying = true;								//Set slidePlaying to true
						if (slideStateMachine.getNeedsReset()){				//If slideStateMachine calls getNeedsReset(), do...
							slideStateMachine.getNextSlide();				//Call getNextSlide()
							slideStateMachine.setNeedsReset(false);			//Set setNeedsReset to false
						}
					}
					if(automatic) {											//If "automatic", do...
						startAutomaticSlideShow();							//Call function startAutomaticSlideShow()
					}
				}
			}
		}
	});
		btnPlayPause.setBounds(345, 530, 89, 23);							//Bounds of the "Play" button
		MainPanel.add(btnPlayPause);										//Add to the Main Panel
		btnPlayPause.setEnabled(true);										//Set enabled button to true


		menuBar = new JMenuBar();											//New menu bar
		menuBar.setBounds(0, 0, 784, 21);									//Bounds of the menu bar
		MainPanel.add(menuBar);												//Add to the Main Panel

		mnFile = new JMenu("File");											//File Menu	
		menuBar.add(mnFile);												//Add to menu bar

		mntmOpen = new JMenuItem("Open File");								//Open Option
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();																//Get an instance of JFileChooser
				fc.setFileFilter(new FileTypeFilter(".ssp", "Slideshow Presentation File"));						//Open files with the ".ssp" tag
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);													//Select those file only
				int result = fc.showOpenDialog(null);																//The file path
				if (result == JFileChooser.APPROVE_OPTION){															//If file is approved, do...
					String selectedFilePath = fc.getSelectedFile().getPath();										//Get the path of the file
					SlideShowStateMachine tempState = fMgr.readFile(selectedFilePath);								//Set the tempState to read file
					slideStateMachine.clearSlideShow();																//Clear the current slide show
					SlideState slide = tempState.getFirstSlide();													//Get the first slide of the tempState
					while(slide != null){																			//While the slide is not null, do...
						slideStateMachine.addSlide(slide);															//Add the slide to the array in the slideStateMachine
						slide = tempState.getNextSlide();															//Get the next slide in the tempState
					}
					AudioState audio = tempState.getFirstAudio();													//Get the first audio of the tempState
					while(audio != null){																			//While the audio is not null, do...
						slideStateMachine.addAudio(audio);															//Add the audio to the array in the slideStateMachine
						audio = tempState.getNextAudio();															//Get the next audio in the tempState
					}
					currentSlide = slideStateMachine.getFirstSlide();												//Set the currentSlide to the first slide in the slideStateMachine
					if (currentSlide != null) {																		//If the currentSlide is not null, do...

						Graphics imagePanelGraphics = PresentationImagePanel.getGraphics();																							//Get the graphics of the slide
						imagePanelGraphics.drawImage(currentSlide.getIcon().getImage(), 0, 10, PresentationImagePanel.getWidth(), PresentationImagePanel.getHeight(), null);		//Draw the graphic into the PresentationImagePanel
						PresentationImagePanel.setImage(currentSlide.getIcon().getImage());
					}
					soundTrack.startB.setEnabled(slideStateMachine.getAudioListSize() != 0);						
					soundTrack.jukeTable.tableChanged();
				}	
			}		
		});
		mnFile.add(mntmOpen);												//Add Open to File Menu

		mnCreation = new JMenu("Creation Mode");							//Creation Menu
		menuBar.add(mnCreation);											//Add to Menu Bar

		mntmCreate = new JMenuItem("Switch to Creation Mode");				//Swtich to Creation Mode opinion
		mnCreation.add(mntmCreate);											//Add to Creation Menu
		mntmCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (creator == null) {										//If creator is null, do...
				creator = new SlideshowMaker();								//Get an instance of SlideshowMaker
				SlideShowStateMachine currentSlide = SlideShowStateMachine.getInstance();
				if(currentSlide != null){
					currentSlide.clearSlideShow();
				}
				if (creator == null) {
					creator = new SlideshowMaker();
				}
				creator.setVisible(true);									//Set the Maker window to true (aka. view the Creation Window) 
				setVisible(false);											//Set the Presenter window to false (aka. hide the Presentation Window)
			}
		}
	});

		btnPrevious = new JButton("<<<<");
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentSlide = slideStateMachine.getPreviousSlide();		//Set the currentSlide to the PreviousSlide of the slideStateMachine
				updateShow();												//Call updateShow()
				if(currentSlide != null)									//If currentSlide is not null, do...
					btnPrevious.setEnabled(true);							//Enabled the Previous Button to true
			}
		});
		btnPrevious.setBounds(50, -30, 89, 23);								//Set the bounds of the Previous Button
		MainPanel.add(btnPrevious);											//Add to the Main Panel
		btnPrevious.setEnabled(false);										//Set the Previous Button to false initially 

		btnNext = new JButton(">>>>");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentSlide = slideStateMachine.getNextSlide();			//Set the currentSlide to the NextSlide of the slideStateMachine
				updateShow();												//Call updateShow()
				if(currentSlide != null)									//If currentSlide is not null, do...
					btnNext.setEnabled(true);								//Enabled the NExt Button to true
			}
		});
		btnNext.setBounds(445, 530, 89, 23);								//Set the bounds of the Next Button
		MainPanel.add(btnNext);												//Add to the Main Panel
		btnNext.setEnabled(false);											//Set the Next Button to false initally 

		PresentationImagePanel = new ImagePanel();							
		PresentationImagePanel.setBounds(0, 11, 765, 504);					//Set the bounds of the PresentationImagePanel
		MainPanel.add(PresentationImagePanel);								//Add image panel to the Main Panel
		
		ButtonGroup viewGroup = new ButtonGroup();							//Create a ButtonGroup for the viewing radio buttons
		
		rdbtnAutomatic = new JRadioButton("Automatic Viewing");				//Create a radio button for Automatic Viewing
		rdbtnAutomatic.setBounds(0, 0, 113, 23);							//Set the bounds for the Automatic Viewing radio button
		MainPanel.add(rdbtnAutomatic);										//Add to the Main Panel
		rdbtnAutomatic.setSelected(true);									//Set to initially be true when window is opened
		rdbtnAutomatic.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){
				btnPrevious.setEnabled(false);								//Enabled the Previous Button to false
				btnNext.setEnabled(false);									//Enabled the Next Button to false
				btnPlayPause.setEnabled(true);								//Enabled the Play button to true
			}
		});
		viewGroup.add(rdbtnAutomatic);										//Add radio button to the ButtonGroup
		
		rdbtnManual = new JRadioButton("Manual Viewing");					//Create a radio button for Manual Viewing
		rdbtnManual.setBounds(0, 0, 99, 23);								//Set the bounds for the Manual Viewing radio button
		MainPanel.add(rdbtnManual);											//Add to the Main Panel
		rdbtnManual.addActionListener(new ActionListener()			
		{
			public void actionPerformed(ActionEvent e)
			{
				getFirstImage();											//Call getFirstImage()
				btnPlayPause.setEnabled(false);								//Enabled the Play button to false
				btnPrevious.setEnabled(true);								//Enabled the Previous Button to true
				btnNext.setEnabled(true);									//Enabled the Next Button to true
			}
		});		
		viewGroup.add(rdbtnManual);											//Add radio button to the ButtonGroup
		
		PresentationImagePanel.initializeBlankImage();						//Initialize to PresentationImagePanel to a blank image
		PresentationImagePanel.repaint();									//Repaint PresentationImagePanel
		currentSlide = slideStateMachine.getFirstSlide();					//Get the first slide from slideStateMachine
		if(currentSlide != null) {
			//PresentationImagePanel.setImage(currentSlide.getIcon().getImage());
		}
		PresentationImagePanel = new ImagePanel();
		PresentationImagePanel.setBounds(0, 11, 765, 504);		
		MainPanel.add(PresentationImagePanel);	
		PresentationImagePanel.initializeBlankImage();
		PresentationImagePanel.repaint();
}
	
	public SlideshowPresenter(SlideshowMaker creator) {						//Function to sync Presenter with slide show in Creator
		this();
		this.creator = creator;		
	}
	
	private void resizeAllPanels(){											//Function to resize all panels in the window
		resizeMainPanel();													
		resizePresentationPanel();
	}

	private void resizePresentationPanel() {								//Function to resize Presentation Panel
		PresentationImagePanel.setBounds(0, 10, MainPanel.getWidth(), MainPanel.getHeight() -  (2 * menuBar.getHeight()) - btnPrevious.getHeight() );
		
	}

	private void resizeMainPanel() {										//Function to resize Main Panel
		int panelWidth = this.getWidth() - 35;
		int panelHeight = this.getHeight() - 35;
		MainPanel.setBounds(5, 5, panelWidth, panelHeight);

		btnPrevious.setBounds((panelWidth / 2) - 150, panelHeight - 40, 89, 23);
		btnPlayPause.setBounds((panelWidth / 2) - 43, panelHeight - 40, 100, 23);
		btnNext.setBounds((panelWidth / 2) + 75, panelHeight - 40, 89, 23);
		rdbtnAutomatic.setBounds((panelWidth / 2) - 330, panelHeight - 40, 140, 33);
		rdbtnManual.setBounds((panelWidth / 2) + 225, panelHeight - 40, 131, 33);
	}
	
	private void updateShow() {												//Function to update image in viewing for Manual Mode
		if (!slidePlaying){
			currentSlide = slideStateMachine.getCurrentSlide();
			if(currentSlide != null)
			{
				PresentationImagePanel.setImage(currentSlide.getIcon().getImage());
			}
		}

	}
	
	private void getFirstImage() {											//Function to get the first slide in the SlideShow in Manual Mode
		currentSlide = slideStateMachine.getFirstSlide();
		Graphics imagePanelGraphics = PresentationImagePanel.getGraphics();
		imagePanelGraphics.drawImage(currentSlide.getIcon().getImage(), 0, 10, PresentationImagePanel.getWidth(), PresentationImagePanel.getHeight(), null);
	}

	private void startAutomaticSlideShow() {								//Function to Play and Pause for Automatic Mode
		if (!slideStateMachine.getPausedState()){
			currentSlide = slideStateMachine.getCurrentSlide();
			PresentationImagePanel.setImage(currentSlide.getIcon().getImage());
			if (animator == null || slideStateMachine.getShowEnded()){
				slideStateMachine.setShowEnded(false);
				animator = new Animator(PresentationImagePanel);
				animator.start();
			}
		}

	}
}
