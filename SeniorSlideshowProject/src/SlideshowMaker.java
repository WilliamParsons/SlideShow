import java.awt.EventQueue;
import FileManager.*;
import Slides.*;
import Transitions.*;
import javafx.scene.shape.Box;
import pkgImageTransitions.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSlider;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JMenuBar;
import javax.swing.border.BevelBorder;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.border.EtchedBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import radioBtnListener.*;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;

public class SlideshowMaker extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;

	//panel to hold the main panel
	private JPanel MainPanel;
	//panel for the slide layout
	private JPanel LayoutPanel;
	//slider to move between the slides in the slideshow
	private JSlider layoutSlider;
	//panel to hold the visual aid for current audio position
	private JPanel layoutTracker;
	//button to add and image
	private JButton addImageBtn;
	//button to remove an image
	private JButton removeImageBtn;
	//panel to hold the transition preview area
	private JPanel TransitionPanel;
	//soundtrack for the creation mode
	private SoundTrack soundTrack;
	//audio panel section for the audio features
	private JPanel AudioPanel;
	//menu option for file
	private JMenu mnFile;
	//menu option for new
	private JMenuItem mntmNew;
	//menu option to open a file
	private JMenuItem mntmOpen;
	//menu option to save the file state into a file
	private JMenuItem mntmSave;
	//menu option to switch to presentation mode
	private JMenuItem mntmPresent;
	//radio button to set the transition to none
	private JRadioButton rdbtnNoTrans;
	//radio button to set the transition to swipe up
	private JRadioButton rdbtnSwipeUp;
	//radio button to set the transition to swipe down
	private JRadioButton rdbtnSwipeDown;
	//radio button to set the transition to swipe left
	private JRadioButton rdbtnSwipeLeft;
	//radio button to set the transition to swipe right
	private JRadioButton rdbtnSwipeRight;
	//radio button to set the transition to crossfade
	private JRadioButton rdbtnCrossfade;
	//panel to hold radio buttons
	private JPanel EditPanel;
	//label to slide images to the right
	private JLabel lblSlidesRight;
	//label for the next image
	private JLabel lblNextImage;
	//label for the main image
	private JLabel lblPrimaryImage;
	//label for the previous image 
	private JLabel lblPreviousImage;
	//label for the to indicate left slide
	private JLabel lblSlidesLeft;
	//panel for the preview image
	private ImagePanel PreviewImagePanel;
	//instance of the state machine
	private SlideShowStateMachine slideStateMachine;
	//instance of the previous slide
	private SlideState previousSlide;
	//instance of the current slide
	private SlideState currentSlide;
	//instance for the next slide
	private SlideState nextSlide;
	//instance of the file manager used to do i/o
	private FileManager fMgr;
	//menu item for the presentation mode switch
	private JMenu mnModes;
	//image icons for the current left and right
	private ImageIcon previewIcon, iconRight, iconLeft;
	//integer representing the slideshow size
	private int slideSize;
	//instance of the presenter if there is one
	private SlideshowPresenter presenter = SlideshowPresenter.getInstance();
	//instance of a creator
	private SlideshowMaker creator;
	//instance of the colors to be used for the audio indicator
	private Color[] rainbowColor;

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

		slideStateMachine = SlideShowStateMachine.getInstance();
		fMgr = new FileManager();

		mntmNew = new JMenuItem("New");
		//add event listener to clear slide state when new is pressed
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SlideShowStateMachine state = SlideShowStateMachine.getInstance();
				state.clearSlideShow();
				layoutSlider.setEnabled(false);
				slideSize = 0;
				layoutSlider.setValue(0);
				updateLayout();
			}
		});
		mnFile.add(mntmNew);

		mntmOpen = new JMenuItem("Open");
		//add action listener to open file chooser to select slideshow file when pressed
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileTypeFilter(".ssp", "Slideshow Presentation File"));
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fc.showOpenDialog(null);
				//if the file chosen is of the approved type continue opening file
				if (result == JFileChooser.APPROVE_OPTION) {
					String selectedFilePath = fc.getSelectedFile().getPath();
					SlideShowStateMachine tempState = fMgr.readFile(selectedFilePath);
					slideStateMachine.clearSlideShow();
					SlideState slide = tempState.getFirstSlide();
					while (slide != null) {
						slideStateMachine.addSlide(slide);
						slide = tempState.getNextSlide();
					}
					AudioState audio = tempState.getFirstAudio();
					while (audio != null) {
						slideStateMachine.addAudio(audio);
						audio = tempState.getNextAudio();
					}
					soundTrack.startB.setEnabled(slideStateMachine.getAudioListSize() != 0);
					soundTrack.jukeTable.tableChanged();
					updateLayoutTracker();
					layoutSlider.setValue(0);
					updateLayout();
				}
			}
		});
		mnFile.add(mntmOpen);

		mntmSave = new JMenuItem("Save");
		//add action listener to save the current state as a slideshow file
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileTypeFilter(".ssp", "Slideshow Presentation File"));
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fc.showSaveDialog(null);
				//if the file name is correct continue saving the file
				if (result == JFileChooser.APPROVE_OPTION) {
					String selectedFilePath = fc.getSelectedFile().getPath();
					if (selectedFilePath.endsWith(".ssp")) {

					} else {
						selectedFilePath += ".ssp";
					}
					fMgr.writeFile(slideStateMachine, selectedFilePath);
				}
			}
		});
		mnFile.add(mntmSave);

		mnModes = new JMenu("Modes");
		menuBar.add(mnModes);

		mntmPresent = new JMenuItem("Presentation");
		mnModes.add(mntmPresent);
		//add action listener to the presentation menu option to switch to presentation mode
		mntmPresent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//if presenter already doesnt exist get its instance otherwise just set it to visible
				if (presenter == null) {
					presenter = SlideshowPresenter.getInstance();
				}
				presenter.setMaker(creator);
				presenter.setVisible(true);
				setVisible(false);
			}
		});

		MainPanel = new JPanel();
		//add action listener for when the user resizes things
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
		TransitionPanel.setBounds(10, 178, 764, 238);

		MainPanel.setLayout(null);
		MainPanel.add(LayoutPanel);
		LayoutPanel.setLayout(null);

		addImageBtn = new JButton("+");
		addImageBtn.setToolTipText("Add a single JPEG or directory of JPEG images to slideshow.");
		//add action listener to select image file using file chooser to add to slideshow
		addImageBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileTypeFilter(".jpg", "image file"));
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int result = fc.showDialog(null, "Add Image");
				if (result == JFileChooser.APPROVE_OPTION) {
					String selectedFilePath = fc.getSelectedFile().getPath();
					if (selectedFilePath.contains(".jpg")) {
						ImageIcon icon = new ImageIcon(selectedFilePath);
						SlideState mySlide = new SlideState(icon);
						slideStateMachine.addSlide(mySlide);
					} else {
						makeListOfImages imageVector = new makeListOfImages();
						Vector imageFileVector = imageVector.listFilesAndFilesSubDirectories(selectedFilePath);
						for (int i = 0; i < imageFileVector.size(); i++) {
							String filePath = (String) imageFileVector.elementAt(i);
							ImageIcon icon = new ImageIcon(filePath);
							SlideState mySlide = new SlideState(icon);
							slideStateMachine.addSlide(mySlide);
						}
					}
				}
				updateLayout();
			}
		});
		addImageBtn.setBounds(659, 110, 45, 20);
		LayoutPanel.add(addImageBtn);

		removeImageBtn = new JButton("-");
		removeImageBtn.setToolTipText("Remove currently selected image.");
		//add action listener to remove the current image in the preview window from the slideshow
		removeImageBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int currentIndex = layoutSlider.getValue();
				slideStateMachine.removeSlideAtIndex(currentIndex);
				updateLayout();

			}
		});
		removeImageBtn.setBounds(704, 110, 45, 20);
		LayoutPanel.add(removeImageBtn);

		lblSlidesRight = new JLabel("");
		lblSlidesRight.setHorizontalAlignment(SwingConstants.CENTER);
		lblSlidesRight.setBounds(487, 37, 40, 30);
		iconRight = new ImageIcon(SlideshowMaker.class.getResource("/ImageFiles/SlidesRight.jpg"));
		Image slidesRight = iconRight.getImage().getScaledInstance(lblSlidesRight.getWidth(),
				lblSlidesRight.getHeight(), Image.SCALE_SMOOTH);
		lblSlidesRight.setIcon(new ImageIcon(slidesRight, iconRight.getDescription()));
		LayoutPanel.add(lblSlidesRight);

		lblNextImage = new JLabel("");
		lblNextImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblNextImage.setBounds(397, 22, 80, 60);
		LayoutPanel.add(lblNextImage);

		lblPrimaryImage = new JLabel("");
		lblPrimaryImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrimaryImage.setBounds(267, 7, 120, 90);
		LayoutPanel.add(lblPrimaryImage);

		lblPreviousImage = new JLabel("");
		lblPreviousImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblPreviousImage.setBounds(177, 22, 80, 60);
		LayoutPanel.add(lblPreviousImage);

		lblSlidesLeft = new JLabel("");
		lblSlidesLeft.setHorizontalAlignment(SwingConstants.CENTER);
		lblSlidesLeft.setBounds(127, 37, 40, 30);
		iconLeft = new ImageIcon(SlideshowMaker.class.getResource("/ImageFiles/SlidesLeft.jpg"));
		Image slidesLeft = iconLeft.getImage().getScaledInstance(lblSlidesLeft.getWidth(), lblSlidesLeft.getHeight(),
				Image.SCALE_SMOOTH);
		lblSlidesLeft.setIcon(new ImageIcon(slidesLeft, iconLeft.getDescription()));
		LayoutPanel.add(lblSlidesLeft);

		AudioPanel = new JPanel();
		AudioPanel.setBounds(10, 411, 764, 119);
		MainPanel.add(AudioPanel);
		AudioPanel.setLayout(null);

		soundTrack = new SoundTrack((String) null, this);
		soundTrack.setBounds(0, 0, 764, 110);
		AudioPanel.add(soundTrack);
		MainPanel.add(TransitionPanel);

		TransitionPanel.setLayout(null);

		EditPanel = new JPanel();
		EditPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		EditPanel.setBounds(542, 9, 124, 212);
		TransitionPanel.add(EditPanel);
		EditPanel.setLayout(null);

		layoutSlider = new JSlider();
		//add action listener to update the slider as you move it
		layoutSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				updateLayout();
			}
		});
		layoutSlider.setEnabled(false);
		layoutSlider.setBounds(15, 120, 642, 20);
		layoutSlider.setValue(0);
		layoutSlider.setMinimum(0);
		layoutSlider.setSnapToTicks(true);
		slideSize = 0;
		LayoutPanel.add(layoutSlider);

		rainbowColor = new Color[6];
		// Add color to rainbowColor list
		rainbowColor[0] = Color.red;
		rainbowColor[1] = Color.orange;
		rainbowColor[2] = Color.yellow;
		rainbowColor[3] = Color.green;
		rainbowColor[4] = Color.cyan;
		rainbowColor[5] = Color.pink;
		
		layoutTracker = new JPanel();
		layoutTracker.setToolTipText("");
		layoutTracker.setBackground(Color.LIGHT_GRAY);
		layoutTracker.setBounds(15, 95, 595, 20);
		LayoutPanel.add(layoutTracker);
		layoutTracker.setLayout(null);

		JButton PreviewTransition = new JButton(">");
		PreviewTransition.setToolTipText("Preview the transition.");
		//add action listener to the transition button to preview the currently selected transition
		PreviewTransition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// if the slide is not null based on the transition selected by the radio button perform the appropriate transition
				if (currentSlide != null) {
					if (currentSlide.getTransition() == SlideState.Transition.NONE) {
						// do nothing
					} else if (currentSlide.getTransition() == SlideState.Transition.DOWN) {
						SwipeDown swipeDownTransition = new SwipeDown();
						BufferedImage imageToTransition = new BufferedImage(PreviewImagePanel.getWidth(),
								PreviewImagePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
						Graphics g = imageToTransition.createGraphics();
						Image image = currentSlide.getIcon().getImage();
						Image newImage = image.getScaledInstance(PreviewImagePanel.getWidth(),
								PreviewImagePanel.getHeight(), java.awt.Image.SCALE_SMOOTH);
						ImageIcon previewIcon = new ImageIcon(newImage);
						previewIcon.paintIcon(null, g, 0, 0);
						BufferedImage blankImage = new BufferedImage(PreviewImagePanel.getWidth(),
								PreviewImagePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
						swipeDownTransition.DrawImageTransition(PreviewImagePanel, imageToTransition, blankImage, 3);
						updateLayout();
					} else if (currentSlide.getTransition() == SlideState.Transition.UP) {
						SwipeUp swipeUpTransition = new SwipeUp();
						BufferedImage imageToTransition = new BufferedImage(PreviewImagePanel.getWidth(),
								PreviewImagePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
						Graphics g = imageToTransition.createGraphics();
						Image image = currentSlide.getIcon().getImage();
						Image newImage = image.getScaledInstance(PreviewImagePanel.getWidth(),
								PreviewImagePanel.getHeight(), java.awt.Image.SCALE_SMOOTH);
						ImageIcon previewIcon = new ImageIcon(newImage);
						previewIcon.paintIcon(null, g, 0, 0);
						BufferedImage blankImage = new BufferedImage(PreviewImagePanel.getWidth(),
								PreviewImagePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
						swipeUpTransition.DrawImageTransition(PreviewImagePanel, imageToTransition, blankImage, 3);
						updateLayout();
					} else if (currentSlide.getTransition() == SlideState.Transition.LEFT) {
						SwipeLeft swipeLeftTransition = new SwipeLeft();
						BufferedImage imageToTransition = new BufferedImage(PreviewImagePanel.getWidth(),
								PreviewImagePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
						Graphics g = imageToTransition.createGraphics();
						Image image = currentSlide.getIcon().getImage();
						Image newImage = image.getScaledInstance(PreviewImagePanel.getWidth(),
								PreviewImagePanel.getHeight(), java.awt.Image.SCALE_SMOOTH);
						ImageIcon previewIcon = new ImageIcon(newImage);
						previewIcon.paintIcon(null, g, 0, 0);
						BufferedImage blankImage = new BufferedImage(PreviewImagePanel.getWidth(),
								PreviewImagePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
						swipeLeftTransition.DrawImageTransition(PreviewImagePanel, imageToTransition, blankImage, 3);
						updateLayout();
					} else if (currentSlide.getTransition() == SlideState.Transition.RIGHT) {
						SwipeRight swipeRightTransition = new SwipeRight();
						BufferedImage imageToTransition = new BufferedImage(PreviewImagePanel.getWidth(),
								PreviewImagePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
						Graphics g = imageToTransition.createGraphics();
						Image image = currentSlide.getIcon().getImage();
						Image newImage = image.getScaledInstance(PreviewImagePanel.getWidth(),
								PreviewImagePanel.getHeight(), java.awt.Image.SCALE_SMOOTH);
						ImageIcon previewIcon = new ImageIcon(newImage);
						previewIcon.paintIcon(null, g, 0, 0);
						BufferedImage blankImage = new BufferedImage(PreviewImagePanel.getWidth(),
								PreviewImagePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
						swipeRightTransition.DrawImageTransition(PreviewImagePanel, imageToTransition, blankImage, 3);
						updateLayout();
					} else if (currentSlide.getTransition() == SlideState.Transition.CROSSFADE) {
						CrossFade crossFadeTransition = new CrossFade();
						BufferedImage imageToTransition = new BufferedImage(PreviewImagePanel.getWidth(),
								PreviewImagePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
						Graphics g = imageToTransition.createGraphics();
						Image image = currentSlide.getIcon().getImage();
						Image newImage = image.getScaledInstance(PreviewImagePanel.getWidth(),
								PreviewImagePanel.getHeight(), java.awt.Image.SCALE_SMOOTH);
						ImageIcon previewIcon = new ImageIcon(newImage);
						previewIcon.paintIcon(null, g, 0, 0);
						BufferedImage blankImage = new BufferedImage(PreviewImagePanel.getWidth(),
								PreviewImagePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
						crossFadeTransition.DrawImageTransition(PreviewImagePanel, imageToTransition, blankImage, 25);
						updateLayout();
					} else {
						System.out.println("error with transition type");
					}
				}
			}
		});
		PreviewTransition.setBounds(35, 166, 45, 23);
		EditPanel.add(PreviewTransition);

		rdbtnNoTrans = new JRadioButton("None");
		rdbtnNoTrans.addActionListener(new ActionListener() {
			//listener for the no transition radio button
			public void actionPerformed(ActionEvent arg0) {
				if (currentSlide != null) {
					currentSlide.setTransitionType(SlideState.Transition.NONE);
				}

			}
		});
		rdbtnNoTrans.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnNoTrans.setBounds(10, 9, 105, 23);
		rdbtnNoTrans.setSelected(true);
		EditPanel.add(rdbtnNoTrans);

		rdbtnSwipeUp = new JRadioButton("Swipe Up");
		//listener for the radio button that sets the swipe up transition
		rdbtnSwipeUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentSlide != null) {
					currentSlide.setTransitionType(SlideState.Transition.UP);
				}
			}
		});
		rdbtnSwipeUp.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnSwipeUp.setBounds(10, 35, 105, 23);
		EditPanel.add(rdbtnSwipeUp);

		rdbtnSwipeDown = new JRadioButton("Swipe Down");
		//listener for the radio button that sets the swipe down transition
		rdbtnSwipeDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentSlide != null) {
					currentSlide.setTransitionType(SlideState.Transition.DOWN);
				}
			}
		});
		rdbtnSwipeDown.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnSwipeDown.setBounds(10, 61, 105, 23);
		EditPanel.add(rdbtnSwipeDown);

		rdbtnSwipeLeft = new JRadioButton("Swipe Left");
		//listener for the radio button that sets the swipe left transition
		rdbtnSwipeLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentSlide != null) {
					currentSlide.setTransitionType(SlideState.Transition.LEFT);
				}
			}
		});
		rdbtnSwipeLeft.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnSwipeLeft.setBounds(10, 87, 105, 23);
		EditPanel.add(rdbtnSwipeLeft);

		rdbtnSwipeRight = new JRadioButton("Swipe Right");
		//listener for the radio button that sets the swipe right transition
		rdbtnSwipeRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentSlide != null) {
					currentSlide.setTransitionType(SlideState.Transition.RIGHT);
				}
			}
		});
		rdbtnSwipeRight.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnSwipeRight.setBounds(10, 113, 105, 23);
		EditPanel.add(rdbtnSwipeRight);

		rdbtnCrossfade = new JRadioButton("Crossfade");
		//listener for the radio button that sets the crossfade transition
		rdbtnCrossfade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentSlide != null) {
					currentSlide.setTransitionType(SlideState.Transition.CROSSFADE);
				}
			}
		});
		rdbtnCrossfade.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnCrossfade.setBounds(10, 136, 105, 23);
		EditPanel.add(rdbtnCrossfade);

		// transGroup the radio buttons
		ButtonGroup transGroup = new ButtonGroup();
		transGroup.add(rdbtnNoTrans);
		transGroup.add(rdbtnSwipeUp);
		transGroup.add(rdbtnSwipeDown);
		transGroup.add(rdbtnSwipeLeft);
		transGroup.add(rdbtnSwipeRight);
		transGroup.add(rdbtnCrossfade);

		PreviewImagePanel = new ImagePanel();
		PreviewImagePanel.setBounds((TransitionPanel.getWidth() / 2) - 150, (TransitionPanel.getHeight() / 2) - 110,
				300, 225);
		PreviewImagePanel.initializeBlankImage();
		TransitionPanel.add(PreviewImagePanel);

		resizePanels();
		creator = this;
	}

	//function to resize all the panels for the creator mode
	private void resizePanels() {
		resizeMainPanel();
		resizeLayoutPanel();
		resizeAudioPanel();
		resizeTransitionPanel();
	}

	//function to resize the main panel for the creator mode
	private void resizeMainPanel() {
		int panelWidth = this.getWidth() - 35;
		int panelHeight = this.getHeight() - 35;
		MainPanel.setBounds(5, 5, panelWidth, panelHeight);
	}

	//function to resize the layout panel for creator mode
	private void resizeLayoutPanel() {
		//int to hold the main panel width
		int mainPanelWidth = MainPanel.getWidth();
		//int to hold the layout panel's desired width
		int panelWidth = LayoutPanel.getWidth();
		//int holding the layout panel's desired height
		int panelHeight = LayoutPanel.getHeight();
		//int to get the right ratio of height to width
		double heightwidthRatio = (double) panelHeight / (double) panelWidth;
		//desired height ratio for layout panel
		double heightRatio = (double) (panelWidth * heightwidthRatio) / (double) panelHeight;
		//desired width ratio for layout panel objects
		double widthRatio = (double) (mainPanelWidth - 10) / (double) panelWidth;
		panelWidth = mainPanelWidth - 10;
		panelHeight = (int) (panelWidth * heightwidthRatio);
		LayoutPanel.setBounds(10, 26, panelWidth, panelHeight);
		removeImageBtn.setBounds(panelWidth - 60, panelHeight - 30, 45, 20);
		addImageBtn.setBounds(panelWidth - 110, panelHeight - 30, 45, 20);
		layoutSlider.setBounds(15, panelHeight - 23, panelWidth - 160, 20);
		layoutTracker.setBounds(15, panelHeight - 43, panelWidth - 160, 20);
		updateLayoutTracker();

		//int to hold needed width of primary image
		int primWidth = (int) (lblPrimaryImage.getWidth() * widthRatio);
		//int to hold height of primary image
		int primHeight = (int) (primWidth * 0.75);
		//int to hold primary images x position
		int primX = panelWidth / 2 - primWidth / 2;
		//int to hold primary images y position in panel
		int primY = 3;
		lblPrimaryImage.setBounds(primX, primY, primWidth, primHeight);

		//int to hold next image's width in panel
		int nextWidth = (int) (lblNextImage.getWidth() * widthRatio);
		//int to hold next image's height in panel
		int nextHeight = (int) (nextWidth * 0.75);
		//int to hold next images x position in panel
		int nextX = primX + primWidth + 10;
		//int to hold next images y position in panel
		int nextY = primY + (primHeight / 2) - (nextHeight / 2);
		lblNextImage.setBounds(nextX, nextY, nextWidth, nextHeight);

		//int to hold previous images width in panel
		int prevWidth = (int) (lblPreviousImage.getWidth() * widthRatio);
		//int to hold previous images height in panel
		int prevHeight = (int) (prevWidth * 0.75);
		//int to hold previous images x position in panel
		int prevX = primX - prevWidth - 10;
		//int to hold previous images y position in panel
		int prevY = primY + (primHeight / 2) - (prevHeight / 2);
		lblPreviousImage.setBounds(prevX, prevY, prevWidth, prevHeight);

		//int to hold the right slides img width in panel
		int slidesRightWidth = (int) (lblSlidesRight.getWidth() * widthRatio);
		//int to hold right slide's height
		int slidesRightHeight = (int) (slidesRightWidth * 0.75);
		//int to hold the right slide image's x position
		int slidesRightX = nextX + nextWidth + 10;
		//int to hold the left slide image's y position
		int slidesRightY = nextY + (nextHeight / 2) - (slidesRightHeight / 2);
		lblSlidesRight.setBounds(slidesRightX, slidesRightY, slidesRightWidth, slidesRightHeight);
		
		//int to hold the left slides img width in panel
		int slidesLeftWidth = (int) (lblSlidesLeft.getWidth() * widthRatio);
		//int to hold the left slides img height in panel
		int slidesLeftHeight = (int) (slidesLeftWidth * 0.75);
		//int to hold the left slides img x position in panel
		int slidesLeftX = prevX - slidesLeftWidth - 10;
		//int to hold the left slides img y position in panel
		int slidesLeftY = prevY + (prevHeight / 2) - (slidesLeftHeight / 2);
		lblSlidesLeft.setBounds(slidesLeftX, slidesLeftY, slidesLeftWidth, slidesLeftHeight);

		updateLayout();
	}

	//function to resize the transition panel objects
	private void resizeTransitionPanel() {
		//int to hold the main panel's width
		int mainPanelWidth = MainPanel.getWidth();
		//int to hold previous transition panel height
		int oldHeight = TransitionPanel.getHeight();
		//int to hold the panel's y position
		int panelY = LayoutPanel.getY() + LayoutPanel.getHeight() + 11;
		//int to hold the desired panel width
		int panelWidth = mainPanelWidth - 10;
		//int to hold th edesired panel height
		int panelHeight = MainPanel.getHeight() - AudioPanel.getHeight() - LayoutPanel.getHeight() - 60;
		//double to hold the ratio between height and width
		double labelHeightRatio = (double) panelHeight / (double) oldHeight;
		//desired label height using ratio
		int labelHeight = (int) (PreviewImagePanel.getHeight() * labelHeightRatio);
		//desired label width using ratio
		int labelWidth = (int) (labelHeight * 1.333);
		TransitionPanel.setBounds(10, panelY, panelWidth, panelHeight);
		PreviewImagePanel.setBounds((panelWidth / 2) - (labelWidth / 2),
				(TransitionPanel.getHeight() / 2) - (labelHeight / 2), labelWidth, labelHeight);
		resizePreviewImage();
		EditPanel.setBounds(PreviewImagePanel.getX() + PreviewImagePanel.getWidth() + 10,
				PreviewImagePanel.getY() + PreviewImagePanel.getHeight() - 200, 118, 200);
	}

	//function to resize the audio panel
	private void resizeAudioPanel() {
		//int to hold the main panel width
		int mainPanelWidth = MainPanel.getWidth();
		//int to hold the desired panel width
		int panelWidth = mainPanelWidth - 10;
		AudioPanel.setBounds(10, MainPanel.getHeight() - 129, panelWidth, 119);
		soundTrack.setBounds(0, 0, panelWidth, 110);
	}

	//function to update the visual audio representation
	private void updateLayoutTracker() {
		if (slideStateMachine.getAudioListSize() != 0) {
			layoutTracker.removeAll();
			//int to hold height of layout tracker
			int height = layoutTracker.getHeight();
			//int to handle x value of layout tracker 
			int x = 0;
			//int to handle the y position of the layout tracker
			int y = 0;
			//loop through the files in the soundtrack and fill the audio visual representation
			for (int i = 0; i < slideStateMachine.getAudioListSize(); i++) {
				AudioImagePanel panel = new AudioImagePanel(rainbowColor[i % rainbowColor.length],
						slideStateMachine.getAudioAtIndex(i).getFileName());
				int width = (int) (layoutTracker.getWidth() * slideStateMachine.getAudioAtIndex(i).getAudioTime()
						/ slideStateMachine.getTotalTime());
				panel.setSize(width, height);
				layoutTracker.add(panel);
				panel.setLocation(x, y);
				x += panel.getWidth() + 1;
			}
			repaint();
		}
	}

	//function to update the layout with resizes and repaints
	private void updateLayout() {

		resizeImageIcon(lblSlidesLeft, iconLeft);
		resizeImageIcon(lblSlidesRight, iconRight);

		//if the slideshow's current size is not the same as the stored slidesize change it to the current one
		if (slideStateMachine.getSlideShowSize() != slideSize) {
			slideSize = slideStateMachine.getSlideShowSize();

			//check if the slideshow has any items in it then change the layout sliders attributes based on the result
			if (slideSize == 0) {
				layoutSlider.setValue(0);
				layoutSlider.setEnabled(false);
			} else if (!layoutSlider.isEnabled()) {
				layoutSlider.setEnabled(true);
			}
			layoutSlider.setMaximum(slideSize - 1);
		}

		//int representing the current index value of the slideshowstateMachine
		int currentIndex = layoutSlider.getValue();
		previousSlide = slideStateMachine.getSlideAtIndex(currentIndex - 1);
		currentSlide = slideStateMachine.getSlideAtIndex(currentIndex);
		nextSlide = slideStateMachine.getSlideAtIndex(currentIndex + 1);

		//if there is a previous slide set the icon to it
		if (previousSlide != null) {
			resizeImageIcon(lblPreviousImage, previousSlide.getIcon());
		} else {
			lblPreviousImage.setIcon(null);
		}

		//if there is a current slide set its icon to it and set the radio button to the right setting
		if (currentSlide != null) {
			resizeImageIcon(lblPrimaryImage, currentSlide.getIcon());
			previewIcon = currentSlide.getIcon();
			resizePreviewImage();
			SlideState.Transition trans = currentSlide.getTransition();
			switch (trans) {
			case NONE:
				rdbtnNoTrans.setSelected(true);
				break;
			case LEFT:
				rdbtnSwipeLeft.setSelected(true);
				break;
			case RIGHT:
				rdbtnSwipeRight.setSelected(true);
				break;
			case UP:
				rdbtnSwipeUp.setSelected(true);
				break;
			case DOWN:
				rdbtnSwipeDown.setSelected(true);
				break;
			case CROSSFADE:
				rdbtnCrossfade.setSelected(true);
				break;
			default:
				rdbtnNoTrans.setSelected(true);
				break;
			}
		} else {
			lblPrimaryImage.setIcon(null);
		}

		//if the next slide exists set its icon to it
		if (nextSlide != null) {
			resizeImageIcon(lblNextImage, nextSlide.getIcon());
		} else {
			lblNextImage.setIcon(null);
		}
	}

	//function to resize the image icons
	private void resizeImageIcon(JLabel label, ImageIcon icon) {
		//if the slideshowstatemachine has slides in it resize the icons otherwise there is no point
		if (slideStateMachine.getSlideShowSize() > 0) {
			//create a buffere image to alter the icon size
			BufferedImage bufferedImg = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
					BufferedImage.TYPE_INT_RGB);
			Graphics g = bufferedImg.createGraphics();
			icon.paintIcon(null, g, 0, 0);
			g.dispose();

			//create a scaled image to hold their scaled version
			Image scaledImg;
			//get the appropriate image height
			int imageHeight = bufferedImg.getHeight();
			//get the appropriate image width
			int imageWidth = bufferedImg.getWidth();
			//get the ratio of height to width
			double heightRatio = (double) imageHeight / (double) imageWidth;
			//scale their height 
			int scaledHeight = label.getHeight();
			//scale their width
			int scaledWidth = label.getWidth();
			//print the image with whichever int is smaller for height, the heightwidth ratio or the scaled height
			if ((scaledWidth * heightRatio) > scaledHeight) {
				scaledImg = bufferedImg.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
			} else {
				scaledImg = bufferedImg.getScaledInstance(scaledWidth, (int) (scaledWidth * heightRatio),
						Image.SCALE_SMOOTH);
			}
			icon = new ImageIcon(scaledImg);
			label.setIcon(icon);
		}
	}

	//function to resize the preview image 
	private void resizePreviewImage() {
		// only resize it if there is a preview image to begin with
		if (previewIcon != null) {
			PreviewImagePanel.initializeBlankImage();
			PreviewImagePanel.setImage(previewIcon.getImage());
		}
	}
	

	//function to update the observer
	@Override
	public void update(Observable o, Object arg) {
		updateLayoutTracker(); // Whenever the soundtrack's table is updated, it
								// will tell the observer to update by invoking
								// this function
		System.out.print("SlideshowMaker: soundtrack table is changed\n");
	}

	//Function to notify sound track to update audio list
	public void notifyAudioUpdate() {
		soundTrack.startB.setEnabled(slideStateMachine.getAudioListSize() != 0);
		soundTrack.jukeTable.tableChanged();		
	}
}
