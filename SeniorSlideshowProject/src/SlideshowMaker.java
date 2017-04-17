import java.awt.EventQueue;
import FileManager.*;
import Slides.*;
import Transitions.*;
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
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
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

public class SlideshowMaker extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel MainPanel;
	private JPanel LayoutPanel;
	private JSlider layoutSlider;
	private JButton addImageBtn;
	private JButton removeImageBtn;
	private JPanel TransitionPanel;
	private SoundTrack soundTrack;
	private JPanel AudioPanel;
	private JMenu mnFile;
	private JMenuItem mntmNew;
	private JMenuItem mntmOpen;
	private JMenuItem mntmSave;
	private JMenuItem mntmPresent;
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
	private ImagePanel PreviewImagePanel;
	private SlideShowStateMachine slideStateMachine;
	private SlideState previousSlide;
	private SlideState currentSlide;
	private SlideState nextSlide;
	private FileManager fMgr;
	private JMenu mnPresentation;
	private ImageIcon previewIcon, iconRight, iconLeft;
	private int slideSize;
	private SlideshowPresenter presenter = null;
	private SlideshowMaker creator;

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
		setTitle("Slideshow Maker");									//Det the title of the Creator window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);					//Pressing X on the window will close it
		setBounds(100, 100, 800, 600);									//Set the bounds of the window

		JMenuBar menuBar = new JMenuBar();								//Create menu bar
		setJMenuBar(menuBar);											//Set the bounds of the menu bar to the window

		mnFile = new JMenu("File");										//Create File Menu
		menuBar.add(mnFile);											//Add to menu bar

		slideStateMachine = SlideShowStateMachine.getInstance();		//Get an instance of the SlideShowStateMachine
		fMgr = new FileManager();										//Get an instance of the FileManager

		mntmNew = new JMenuItem("New");									//Create New option
		mntmNew.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				SlideShowStateMachine state = SlideShowStateMachine.getInstance();				//Get an instance of SlideShowStateMachine
				state.clearSlideShow();															//Clear to a blank slide show
				layoutSlider.setEnabled(false);													//Disable the layout slider
				slideSize = 0;																	//Set slide show to 0
				layoutSlider.setValue(0);														//Initialize layout slider to value 0
				updateLayout();																	//Call updateLayout()
			}
		});
		mnFile.add(mntmNew);											//Add to File Menu

		mntmOpen = new JMenuItem("Open");								//Create Open option
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();											//Get an instance of JFileChooser
				fc.setFileFilter(new FileTypeFilter(".ssp", "Slideshow Presentation File"));	//Filter out only ".ssp" files
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);								//Select only those files
				int result = fc.showOpenDialog(null);											//No open dialog
				if (result == JFileChooser.APPROVE_OPTION){										//If file is approved, do...
					String selectedFilePath = fc.getSelectedFile().getPath();					//Get the path of the selected file
					SlideShowStateMachine tempState = fMgr.readFile(selectedFilePath);			//Set tempState to read the file
					slideStateMachine.clearSlideShow();											//Clear the current slide show in the window
					SlideState slide = tempState.getFirstSlide();								//Get the first slide of the tempState
					while(slide != null){														//While the slide is not null, do...
						slideStateMachine.addSlide(slide);										//Add slide to the array
						slide = tempState.getNextSlide();										//Go to next slide
					}
					AudioState audio = tempState.getFirstAudio();								//Get the first audio for tempState
					while(audio != null){														//While audio is not null, do...
						slideStateMachine.addAudio(audio);										//Add the audio to the array
						audio = tempState.getNextAudio();										//Get the next audio
					}
					soundTrack.startB.setEnabled(slideStateMachine.getAudioListSize() != 0);	//Enable the start function for audio
					soundTrack.jukeTable.tableChanged();										//Update the table
					layoutSlider.setValue(0);													//Set the layout slider to 0
					updateLayout();																//Call updateLayout()
				}
			}
		});
		mnFile.add(mntmOpen);											//Add to File Menu

		mntmSave = new JMenuItem("Save");								//Create Save option
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();											//Get an instance of JFileChooser
				fc.setFileFilter(new FileTypeFilter(".ssp", "Slideshow Presentation File"));	//Filter out only ".ssp"files
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);								//Select only those files
				int result = fc.showSaveDialog(null);											//No open dialog
				if (result == JFileChooser.APPROVE_OPTION){										//If file is approved, do...
					String selectedFilePath = fc.getSelectedFile().getPath();					//Get the path of the selected file
					if(selectedFilePath.endsWith(".ssp")) {										//If selected path ends with ".ssp", do...

					} else {
						selectedFilePath += ".ssp";												//Create a file with the suffix ".ssp"
					}
					fMgr.writeFile(slideStateMachine, selectedFilePath);						//Write information to the file path
				}
			}
		});
		mnFile.add(mntmSave);											//Add to File Menu

		mnPresentation = new JMenu("Presentation Mode");				//Create Presentation Menu
		menuBar.add(mnPresentation);									//Add to menu bar

		mntmPresent = new JMenuItem("Switch to Presentation Mode");		//Create Switch to Presentation Mode option
		mnPresentation.add(mntmPresent);								//Add to Presentation Menu
		mntmPresent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if (presenter == null) {								//If the presenter is null, do...
				presenter = new SlideshowPresenter(creator);			//Presenter is equal to the creator instance
				}
				presenter.setVisible(true);								//Open presenter window
				setVisible(false);										//Hide creator window
			}
		});


		MainPanel = new JPanel();										//Create Main Panel
		MainPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resizePanels();											//Call resizePanels() for enlarging or shirking the window
			}
		});
		MainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));				//Set the border for Main Panel
		MainPanel.setBounds(5, 5, 790, 590);							//Set the bounds for Main Panel
		setContentPane(MainPanel);										//Set components into the Main Panel

		LayoutPanel = new JPanel();																	//Create Layout Panel
		LayoutPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));		//Set the border for Layout Panel
		LayoutPanel.setBounds(10, 11, 764, 140);													//Set the bounds for Layout Panel

		TransitionPanel = new JPanel();																//Create Transition Panel
		TransitionPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));	//Set the border for Transition Panel
		TransitionPanel.setBounds(10, 162, 764, 238);												//Set the bounds for Transition Panel

		MainPanel.setLayout(null);										//Set the Main Panel layout to null
		MainPanel.add(LayoutPanel);										//Add to Main Panel
		LayoutPanel.setLayout(null);									//Set the Layout Panel layout to null

		addImageBtn = new JButton("+");									//Create + button
		addImageBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();															//Get an instance of JFileChooser
				fc.setFileFilter(new FileTypeFilter(".jpg", "image file"));										//Filter out only ".jpg" and "image file" files
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);									//Select only those files
				int result = fc.showDialog(null, "Add Image");													//Show dialog that the image is added
				if (result == JFileChooser.APPROVE_OPTION){														//If file is approved, do...
					String selectedFilePath = fc.getSelectedFile().getPath();									//Get the path of the file
					if (selectedFilePath.contains(".jpg")){														//If the file path has ".jpg" in it, do...
						ImageIcon icon = new ImageIcon(selectedFilePath);										//Set the image of the file to variable icon
						SlideState mySlide = new SlideState(icon);												//Set the variable mySlide to icon
						slideStateMachine.addSlide(mySlide);													//Add to slideStateMachine
					}
					else{
						makeListOfImages imageVector = new makeListOfImages();									//Create a list of vectors
						Vector imageFileVector = imageVector.listFilesAndFilesSubDirectories(selectedFilePath); //Add all images in the file path to the vector
						for (int i = 0; i < imageFileVector.size(); i++){										
							String filePath = (String) imageFileVector.elementAt(i);							//File path of image at index i
							ImageIcon icon = new ImageIcon(filePath);											//Set the icon to image of the file path
							SlideState mySlide = new SlideState(icon);											//Set the variable mySide to icon
							slideStateMachine.addSlide(mySlide);												//Add to slideStateMachine
						}
					}
				}
				updateLayout();																					//Call updateLayout()
			}
		});
		addImageBtn.setBounds(659, 110, 45, 20);						//Set the bounds for the button										
		LayoutPanel.add(addImageBtn);									//Add to Layout Panel

		removeImageBtn = new JButton("-");								//Create - button
		removeImageBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int currentIndex = layoutSlider.getValue();				//Set currentIndex to the value of the layout slider
				slideStateMachine.removeSlideAtIndex(currentIndex);		//Remove the image at that index
				updateLayout();											//Call updateLayout()

			}
		});
		removeImageBtn.setBounds(704, 110, 45, 20);						//Set the bounds for the button
		LayoutPanel.add(removeImageBtn);								//Add to Layout Panel

		lblSlidesRight = new JLabel("");								//Create lblSlidesRight
		lblSlidesRight.setHorizontalAlignment(SwingConstants.CENTER);	//Set alignment
		lblSlidesRight.setBounds(487, 37, 40, 30);						//Set the bounds for the label
		iconRight = new ImageIcon(SlideshowMaker.class.getResource("/ImageFiles/SlidesRight.jpg"));												//Get the image for the label to "/ImageFiles/SlidesRight.jpg"
		Image slidesRight = iconRight.getImage().getScaledInstance(lblSlidesRight.getWidth(), lblSlidesRight.getHeight(), Image.SCALE_SMOOTH);	//Set the icon to the image
		lblSlidesRight.setIcon(new ImageIcon(slidesRight, iconRight.getDescription()));															//Display icon
		LayoutPanel.add(lblSlidesRight);								//Add to Layout Panel

		lblNextImage = new JLabel("");									//Create lblNextImage
		lblNextImage.setHorizontalAlignment(SwingConstants.CENTER);		//Set alignment
		lblNextImage.setBounds(397, 22, 80, 60);						//Set the bounds for the label
		LayoutPanel.add(lblNextImage);									//Add to Layout Panel

		lblPrimaryImage = new JLabel("");								//Create lblPrimaryImage
		lblPrimaryImage.setHorizontalAlignment(SwingConstants.CENTER);	//Set alignment
		lblPrimaryImage.setBounds(267, 7, 120, 90);						//Set the bounds of the label
		LayoutPanel.add(lblPrimaryImage);								//Add to Layout Panel

		lblPreviousImage = new JLabel("");								//Create lblPreviousImage
		lblPreviousImage.setHorizontalAlignment(SwingConstants.CENTER);	//Set alignment
		lblPreviousImage.setBounds(177, 22, 80, 60);					//Set the bounds of the label
		LayoutPanel.add(lblPreviousImage);								//Add to Layout Panel

		lblSlidesLeft = new JLabel("");									//Create lblSlidesLeft
		lblSlidesLeft.setHorizontalAlignment(SwingConstants.CENTER);	//Set alignment
		lblSlidesLeft.setBounds(127, 37, 40, 30);						//Set the bounds for the label
		iconLeft = new ImageIcon(SlideshowMaker.class.getResource("/ImageFiles/SlidesLeft.jpg"));											//Get the image for the label to "/ImageFiles/SlidesLeft.jpg"
		Image slidesLeft = iconLeft.getImage().getScaledInstance(lblSlidesLeft.getWidth(), lblSlidesLeft.getHeight(), Image.SCALE_SMOOTH);	//Set the icon to the image
		lblSlidesLeft.setIcon(new ImageIcon(slidesLeft, iconLeft.getDescription()));														//Display icon
		LayoutPanel.add(lblSlidesLeft);									//Add to Layout Panel

		AudioPanel = new JPanel();										//Create Audio Panel
		AudioPanel.setBounds(10, 411, 764, 119);						//Set the bounds Audio Panel
		MainPanel.add(AudioPanel);										//Add to Main Panel 
		AudioPanel.setLayout(null);										//Set Audio Panel layout to null

		soundTrack = new SoundTrack((String) null);						//Set the soundTrack string to null
		soundTrack.setBounds(0, 0, 764, 110);							//Set the bounds for the soundTrack
		AudioPanel.add(soundTrack);										//Add to Audio Panel
		MainPanel.add(TransitionPanel);									//Add to Main Panel

		TransitionPanel.setLayout(null);								//Set the Transition Panel layout to null

		EditPanel = new JPanel();												//Create Edit Panel
		EditPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));	//Set the border for Edit Panel
		EditPanel.setBounds(542, 9, 124, 212);									//Set the bounds for Edit Panel
		TransitionPanel.add(EditPanel);											//Add to Transition Panel
		EditPanel.setLayout(null);												//Set the Edit Panel layout to null
		
		layoutSlider = new JSlider();									//Create Layout Slider
		layoutSlider.addChangeListener(new ChangeListener() {	
			public void stateChanged(ChangeEvent arg0) {
				updateLayout();											//Call updateLayout()
			}
		});
		layoutSlider.setEnabled(false);									//Disable Layout Slider
		layoutSlider.setBounds(15, 120, 642, 20);						//Set the bounds for the Layout Sldier
		layoutSlider.setValue(0);										//Set the slider to value 0
		layoutSlider.setMinimum(0);										//Set the slider minimum to 0
		layoutSlider.setSnapToTicks(true);								//Enable the snap to ticks for the slider
		slideSize = 0;													//Initialize slideSize to 0
		LayoutPanel.add(layoutSlider);									//Add to Layout Panel

		JButton PreviewTransition = new JButton(">");					//Create > button
		PreviewTransition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentSlide != null)								//If currentSlide is not null, do...
				{
					if (currentSlide.getTransition() == SlideState.Transition.NONE)		//If getTransition() == NONE
					{
						//do nothing
					}
					else if (currentSlide.getTransition() == SlideState.Transition.DOWN)//If getTransition() == Down, do...
																							//do a preview of transition and add to array
					{
						SwipeDown swipeDownTransition = new SwipeDown();
						BufferedImage imageToTransition = new BufferedImage(PreviewImagePanel.getWidth(), PreviewImagePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
						Graphics g = imageToTransition.createGraphics();
						Image image = currentSlide.getIcon().getImage();
						Image newImage = image.getScaledInstance(PreviewImagePanel.getWidth(), PreviewImagePanel.getHeight(), java.awt.Image.SCALE_SMOOTH);
						ImageIcon previewIcon = new ImageIcon(newImage);
						previewIcon.paintIcon(null, g, 0, 0);
						BufferedImage blankImage = new BufferedImage(PreviewImagePanel.getWidth(),PreviewImagePanel.getHeight(),BufferedImage.TYPE_INT_RGB);
						swipeDownTransition.DrawImageTransition(PreviewImagePanel, imageToTransition, blankImage, 3);
						updateLayout();
					}
					else if (currentSlide.getTransition() == SlideState.Transition.UP)	//If getTransition() == UP, do...
																							//do a preview of transition and add to array
					{
						SwipeUp swipeUpTransition = new SwipeUp();
						BufferedImage imageToTransition = new BufferedImage(PreviewImagePanel.getWidth(), PreviewImagePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
						Graphics g = imageToTransition.createGraphics();
						Image image = currentSlide.getIcon().getImage();
						Image newImage = image.getScaledInstance(PreviewImagePanel.getWidth(), PreviewImagePanel.getHeight(), java.awt.Image.SCALE_SMOOTH);
						ImageIcon previewIcon = new ImageIcon(newImage);
						previewIcon.paintIcon(null, g, 0, 0);
						BufferedImage blankImage = new BufferedImage(PreviewImagePanel.getWidth(),PreviewImagePanel.getHeight(),BufferedImage.TYPE_INT_RGB);
						swipeUpTransition.DrawImageTransition(PreviewImagePanel, imageToTransition, blankImage, 3);
						updateLayout();
					}
					else if (currentSlide.getTransition() == SlideState.Transition.LEFT)//If getTransition() == LEFT, do...
																							//do a preview of transition and add to array
					{
						SwipeLeft swipeLeftTransition = new SwipeLeft();
						BufferedImage imageToTransition = new BufferedImage(PreviewImagePanel.getWidth(), PreviewImagePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
						Graphics g = imageToTransition.createGraphics();
						Image image = currentSlide.getIcon().getImage();
						Image newImage = image.getScaledInstance(PreviewImagePanel.getWidth(), PreviewImagePanel.getHeight(), java.awt.Image.SCALE_SMOOTH);
						ImageIcon previewIcon = new ImageIcon(newImage);
						previewIcon.paintIcon(null, g, 0, 0);
						BufferedImage blankImage = new BufferedImage(PreviewImagePanel.getWidth(),PreviewImagePanel.getHeight(),BufferedImage.TYPE_INT_RGB);
						swipeLeftTransition.DrawImageTransition(PreviewImagePanel, imageToTransition, blankImage, 3);
						updateLayout();
					}
					else if (currentSlide.getTransition() == SlideState.Transition.RIGHT)//If getTransition() == RIGHT, do...
																							//do a preview of transition and add to array
					{
						SwipeRight swipeRightTransition = new SwipeRight();
						BufferedImage imageToTransition = new BufferedImage(PreviewImagePanel.getWidth(), PreviewImagePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
						Graphics g = imageToTransition.createGraphics();
						Image image = currentSlide.getIcon().getImage();
						Image newImage = image.getScaledInstance(PreviewImagePanel.getWidth(), PreviewImagePanel.getHeight(), java.awt.Image.SCALE_SMOOTH);
						ImageIcon previewIcon = new ImageIcon(newImage);
						previewIcon.paintIcon(null, g, 0, 0);
						BufferedImage blankImage = new BufferedImage(PreviewImagePanel.getWidth(),PreviewImagePanel.getHeight(),BufferedImage.TYPE_INT_RGB);
						swipeRightTransition.DrawImageTransition(PreviewImagePanel, imageToTransition, blankImage, 3);
						updateLayout();
					}
					else if (currentSlide.getTransition() == SlideState.Transition.CROSSFADE)//If getTransition() == CROSSFADE, do...
																							//do a preview of transition and add to array
					{
						CrossFade crossFadeTransition = new CrossFade();
						BufferedImage imageToTransition = new BufferedImage(PreviewImagePanel.getWidth(), PreviewImagePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
						Graphics g = imageToTransition.createGraphics();
						Image image = currentSlide.getIcon().getImage();
						Image newImage = image.getScaledInstance(PreviewImagePanel.getWidth(), PreviewImagePanel.getHeight(), java.awt.Image.SCALE_SMOOTH);
						ImageIcon previewIcon = new ImageIcon(newImage);
						previewIcon.paintIcon(null, g, 0, 0);
						BufferedImage blankImage = new BufferedImage(PreviewImagePanel.getWidth(),PreviewImagePanel.getHeight(),BufferedImage.TYPE_INT_RGB);
						crossFadeTransition.DrawImageTransition(PreviewImagePanel, imageToTransition, blankImage, 25);
						updateLayout();
					}
					else
					{
						System.out.println("error with transition type");				//Transition Error
					}	
				}
			}
		});
		PreviewTransition.setBounds(35, 166, 45, 23);					//Set the bounds for > button		
		EditPanel.add(PreviewTransition);								//Add to Edit Panel

		rdbtnNoTrans = new JRadioButton("None");								//Create NONE radio button
		rdbtnNoTrans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(currentSlide != null){
					currentSlide.setTransitionType(SlideState.Transition.NONE);	//Set transition type to NONE
				}

			}
		});
		rdbtnNoTrans.setHorizontalAlignment(SwingConstants.LEFT);				//Set alignment
		rdbtnNoTrans.setBounds(10, 9, 105, 23);									//Set the bounds for radio button
		rdbtnNoTrans.setSelected(true);											//Initialize to true 
		EditPanel.add(rdbtnNoTrans);											//Add to Edit Panel

		rdbtnSwipeUp = new JRadioButton("Swipe Up");							//Create UP radio button
		rdbtnSwipeUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currentSlide != null){
					currentSlide.setTransitionType(SlideState.Transition.UP);	//Set transition type to UP
				}
			}
		});
		rdbtnSwipeUp.setHorizontalAlignment(SwingConstants.LEFT);				//Set alignment
		rdbtnSwipeUp.setBounds(10, 35, 105, 23);								//Set the bounds for radio button
		EditPanel.add(rdbtnSwipeUp);											//Add to Edit Panel

		rdbtnSwipeDown = new JRadioButton("Swipe Down");						//Create DOWN radio button
		rdbtnSwipeDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currentSlide != null){
					currentSlide.setTransitionType(SlideState.Transition.DOWN);	//Set transition type to DOWN
				}
			}
		});
		rdbtnSwipeDown.setHorizontalAlignment(SwingConstants.LEFT);				//Set alignment
		rdbtnSwipeDown.setBounds(10, 61, 105, 23);								//Set the bounds for radio button
		EditPanel.add(rdbtnSwipeDown);											//Add to Edit Panel

		rdbtnSwipeLeft = new JRadioButton("Swipe Left");						//Create LEFT radio button
		rdbtnSwipeLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currentSlide != null){
					currentSlide.setTransitionType(SlideState.Transition.LEFT);	//Set transition type to LEFT
				}
			}
		});
		rdbtnSwipeLeft.setHorizontalAlignment(SwingConstants.LEFT);				//Set alignment
		rdbtnSwipeLeft.setBounds(10, 87, 105, 23);								//Set the bounds for radio button
		EditPanel.add(rdbtnSwipeLeft);											//Add to Edit Panel

		rdbtnSwipeRight = new JRadioButton("Swipe Right");						//Create RIGHT radio button
		rdbtnSwipeRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currentSlide != null){
					currentSlide.setTransitionType(SlideState.Transition.RIGHT);//Set transition type to RIGHT
				}
			}
		});
		rdbtnSwipeRight.setHorizontalAlignment(SwingConstants.LEFT);			//Set alignment
		rdbtnSwipeRight.setBounds(10, 113, 105, 23);							//Set the bounds for radio buttons
		EditPanel.add(rdbtnSwipeRight);											//Add to Edit Panel

		rdbtnCrossfade = new JRadioButton("Crossfade");							//Create CROSSFADE radio button
		rdbtnCrossfade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currentSlide != null){
					currentSlide.setTransitionType(SlideState.Transition.CROSSFADE);//Set transition type to CROSSFADE
				}
			}
		});
		rdbtnCrossfade.setHorizontalAlignment(SwingConstants.LEFT);				//Set alignment
		rdbtnCrossfade.setBounds(10, 136, 105, 23);								//Set the bounds for radio buttons
		EditPanel.add(rdbtnCrossfade);											//Add to Edit Panel

		//transGroup the radio buttons
		ButtonGroup transGroup = new ButtonGroup();
		transGroup.add(rdbtnNoTrans);
		transGroup.add(rdbtnSwipeUp);
		transGroup.add(rdbtnSwipeDown);
		transGroup.add(rdbtnSwipeLeft);
		transGroup.add(rdbtnSwipeRight);
		transGroup.add(rdbtnCrossfade);


		PreviewImagePanel = new ImagePanel();
		PreviewImagePanel.setBounds((TransitionPanel.getWidth()/2)-150, (TransitionPanel.getHeight()/2)-110, 300, 225);
		PreviewImagePanel.initializeBlankImage();
		TransitionPanel.add(PreviewImagePanel);
		
		resizePanels();
		creator = this;
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
		double heightwidthRatio = (double)panelHeight/(double)panelWidth;
		double heightRatio = (double)(panelWidth*heightwidthRatio)/(double)panelHeight;
		double widthRatio = (double)(mainPanelWidth - 10)/(double)panelWidth;
		panelWidth = mainPanelWidth - 10;
		panelHeight = (int)(panelWidth*heightwidthRatio);
		LayoutPanel.setBounds(10, 26, panelWidth, panelHeight);
		removeImageBtn.setBounds(panelWidth-60, panelHeight-30, 45, 20);
		addImageBtn.setBounds(panelWidth-110, panelHeight-30, 45, 20);
		layoutSlider.setBounds(15, panelHeight-23, panelWidth-160, 20);
		
		int primWidth = (int)(lblPrimaryImage.getWidth()*widthRatio);
		int primHeight = (int)(primWidth*0.75);
		int primX = panelWidth/2 - primWidth/2;
		int primY = 3;	
		lblPrimaryImage.setBounds(primX, primY, primWidth, primHeight);

		int nextWidth = (int)(lblNextImage.getWidth()*widthRatio);
	    int nextHeight = (int)(nextWidth*0.75);
	    int nextX = primX + primWidth + 10;
	    int nextY = primY + (primHeight/2) - (nextHeight/2);	    
		lblNextImage.setBounds(nextX, nextY, nextWidth, nextHeight);
		
		int prevWidth = (int)(lblPreviousImage.getWidth()*widthRatio);
	    int prevHeight = (int)(prevWidth*0.75);
	    int prevX = primX - prevWidth - 10;
	    int prevY = primY + (primHeight/2) - (prevHeight/2);	    
		lblPreviousImage.setBounds(prevX, prevY, prevWidth, prevHeight);
		
		int slidesRightWidth = (int)(lblSlidesRight.getWidth()*widthRatio);
	    int slidesRightHeight = (int)(slidesRightWidth*0.75);
	    int slidesRightX = nextX + nextWidth + 10;
	    int slidesRightY = nextY + (nextHeight/2) - (slidesRightHeight/2);	    
		lblSlidesRight.setBounds(slidesRightX, slidesRightY, slidesRightWidth, slidesRightHeight);
		
		int slidesLeftWidth = (int)(lblSlidesLeft.getWidth()*widthRatio);
	    int slidesLeftHeight = (int)(slidesLeftWidth*0.75);
	    int slidesLeftX = prevX - slidesLeftWidth - 10;
	    int slidesLeftY = prevY + (prevHeight/2) - (slidesLeftHeight/2);	    
		lblSlidesLeft.setBounds(slidesLeftX, slidesLeftY, slidesLeftWidth, slidesLeftHeight);
		
		updateLayout();
	}

	private void resizeTransitionPanel(){
		int mainPanelWidth = MainPanel.getWidth();
		int oldHeight = TransitionPanel.getHeight();
		int panelY = LayoutPanel.getY() + LayoutPanel.getHeight() + 11;
		int panelWidth = mainPanelWidth - 10;
		int panelHeight = MainPanel.getHeight() - AudioPanel.getHeight() - LayoutPanel.getHeight() - 60;
		double labelHeightRatio = (double)panelHeight/(double)oldHeight;
		int labelHeight = (int)(PreviewImagePanel.getHeight()*labelHeightRatio);
		int labelWidth = (int)(labelHeight * 1.333);
		TransitionPanel.setBounds(10, panelY, panelWidth, panelHeight);
		PreviewImagePanel.setBounds((panelWidth/2)-(labelWidth/2), (TransitionPanel.getHeight()/2)-(labelHeight/2), labelWidth, labelHeight);
		resizePreviewImage();
		EditPanel.setBounds(PreviewImagePanel.getX() + PreviewImagePanel.getWidth() + 10, PreviewImagePanel.getY() + PreviewImagePanel.getHeight() - 200, 118, 200);
	}

	private void resizeAudioPanel(){
		int mainPanelWidth = MainPanel.getWidth();
		int panelWidth = mainPanelWidth - 10;
		AudioPanel.setBounds(10, MainPanel.getHeight()-129, panelWidth, 119);
		soundTrack.setBounds(0, 0, panelWidth, 110);
	}

	private void updateLayout(){



		resizeImageIcon(lblSlidesLeft, iconLeft);
		resizeImageIcon(lblSlidesRight, iconRight);

		if(slideStateMachine.getSlideShowSize() != slideSize) {
			slideSize = slideStateMachine.getSlideShowSize();

			if (slideSize == 0) {
				layoutSlider.setValue(0);
				layoutSlider.setEnabled(false);			
			}			
			else if(!layoutSlider.isEnabled()) {
				layoutSlider.setEnabled(true);
			}			
			layoutSlider.setMaximum(slideSize - 1);
		}

		int currentIndex = layoutSlider.getValue();
		previousSlide = slideStateMachine.getSlideAtIndex(currentIndex -1);
		currentSlide = slideStateMachine.getSlideAtIndex(currentIndex);
		nextSlide = slideStateMachine.getSlideAtIndex(currentIndex + 1);

		if(previousSlide != null) {
			resizeImageIcon(lblPreviousImage, previousSlide.getIcon());
		} else {
			lblPreviousImage.setIcon(null);
		}

		if(currentSlide != null) {
			resizeImageIcon(lblPrimaryImage, currentSlide.getIcon());
			previewIcon = currentSlide.getIcon();
			resizePreviewImage();
			SlideState.Transition trans = currentSlide.getTransition();
			switch(trans){
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

		if(nextSlide != null) {
			resizeImageIcon(lblNextImage, nextSlide.getIcon());
		} else {
			lblNextImage.setIcon(null);
		}
	}


	private void resizeImageIcon(JLabel label, ImageIcon icon){
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

	private void resizePreviewImage(){
		if(previewIcon != null) {
			PreviewImagePanel.initializeBlankImage();
			PreviewImagePanel.setImage(previewIcon.getImage());
		}
	}
}
