import java.awt.EventQueue;
import FileManager.*;
import Slides.*;
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
	private JMenu mnModes;
	private JMenuItem mntmNewMenuItem;
	private ImageIcon previewIcon;

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

		mntmOpen = new JMenuItem("Open");
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
						slideStateMachine.addSlide(slide);
						slide = tempState.getNextSlide();
					}
					AudioState audio = tempState.getFirstAudio();
					while(audio != null){
						slideStateMachine.addAudio(audio);
						audio = tempState.getNextAudio();
					}
					updateLayout();
				}	
			}
		});
		mnFile.add(mntmOpen);

		mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileTypeFilter(".ssp", "Slideshow Presentation File"));
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fc.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION){
					String selectedFilePath = fc.getSelectedFile().getPath();
					if(selectedFilePath.endsWith(".ssp")) {

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
		mntmPresent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				SlideshowPresenter presenter = new SlideshowPresenter();
				presenter.setVisible(true);
				setVisible(false);
			}
		});


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
		addImageBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileTypeFilter(".jpg", "image file"));
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int result = fc.showDialog(null, "Add Image");
				if (result == JFileChooser.APPROVE_OPTION){
					String selectedFilePath = fc.getSelectedFile().getPath();
					if (selectedFilePath.contains(".jpg")){
						ImageIcon icon = new ImageIcon(selectedFilePath);
						SlideState mySlide = new SlideState(icon);
						slideStateMachine.addSlide(mySlide);
					}
					else{
						makeListOfImages imageVector = new makeListOfImages();
						Vector imageFileVector = imageVector.listFilesAndFilesSubDirectories(selectedFilePath);
						for (int i = 0; i < imageFileVector.size(); i++){
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
		removeImageBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int currentIndex = layoutSlider.getValue();
				slideStateMachine.removeSlideAtIndex(currentIndex);
				updateLayout();

			}
		});
		removeImageBtn.setBounds(704, 110, 45, 20);
		LayoutPanel.add(removeImageBtn);

		layoutSlider = new JSlider();
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
		LayoutPanel.add(layoutSlider);	

		lblSlidesRight = new JLabel("");
		lblSlidesRight.setHorizontalAlignment(SwingConstants.CENTER);
		lblSlidesRight.setBounds(487, 37, 40, 30);
		ImageIcon iconRight = new ImageIcon(SlideshowMaker.class.getResource("/ImageFiles/SlidesRight.jpg"));
		Image slidesRight = iconRight.getImage().getScaledInstance(lblSlidesRight.getWidth(), lblSlidesRight.getHeight(), Image.SCALE_SMOOTH);
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
		PreviewTransition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TO DO : Preview transitions
			}
		});
		PreviewTransition.setBounds(35, 166, 45, 23);
		EditPanel.add(PreviewTransition);

		rdbtnNoTrans = new JRadioButton("None");
		rdbtnNoTrans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(currentSlide != null){
					currentSlide.setTransitionType(SlideState.Transition.NONE);
				}
				
			}
		});
		rdbtnNoTrans.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnNoTrans.setBounds(10, 9, 105, 23);
		rdbtnNoTrans.setSelected(true);
		EditPanel.add(rdbtnNoTrans);

		rdbtnSwipeUp = new JRadioButton("Swipe Up");
		rdbtnSwipeUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currentSlide != null){
					currentSlide.setTransitionType(SlideState.Transition.UP);
				}
			}
		});
		rdbtnSwipeUp.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnSwipeUp.setBounds(10, 35, 105, 23);
		EditPanel.add(rdbtnSwipeUp);

		rdbtnSwipeDown = new JRadioButton("Swipe Down");
		rdbtnSwipeDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currentSlide != null){
					currentSlide.setTransitionType(SlideState.Transition.DOWN);
				}
			}
		});
		rdbtnSwipeDown.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnSwipeDown.setBounds(10, 61, 105, 23);
		EditPanel.add(rdbtnSwipeDown);

		rdbtnSwipeLeft = new JRadioButton("Swipe Left");
		rdbtnSwipeLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currentSlide != null){
					currentSlide.setTransitionType(SlideState.Transition.LEFT);
				}
			}
		});
		rdbtnSwipeLeft.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnSwipeLeft.setBounds(10, 87, 105, 23);
		EditPanel.add(rdbtnSwipeLeft);

		rdbtnSwipeRight = new JRadioButton("Swipe Right");
		rdbtnSwipeRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currentSlide != null){
					currentSlide.setTransitionType(SlideState.Transition.RIGHT);
				}
			}
		});
		rdbtnSwipeRight.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnSwipeRight.setBounds(10, 113, 105, 23);
		EditPanel.add(rdbtnSwipeRight);

		rdbtnCrossfade = new JRadioButton("Crossfade");
		rdbtnCrossfade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currentSlide != null){
					currentSlide.setTransitionType(SlideState.Transition.CROSSFADE);
				}
			}
		});
		rdbtnCrossfade.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnCrossfade.setBounds(10, 136, 105, 23);
		EditPanel.add(rdbtnCrossfade);

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
		PreviewImagePanel.initializeImages();
		TransitionPanel.add(PreviewImagePanel);
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
		lblPrimaryImage.setBounds((panelWidth-225)/2, panelHeight-125, 120, 90);
		updateLayout();
		lblNextImage.setBounds((panelWidth+30)/2, panelHeight-115, 80, 60);
		updateLayout();
		lblPreviousImage.setBounds((panelWidth-400)/2, panelHeight-115, 80, 60);
		updateLayout();
		lblSlidesRight.setBounds((panelWidth+210)/2, panelHeight-115, 40, 30);
		lblSlidesLeft.setBounds((panelWidth-500)/2, panelHeight-115, 40, 30);
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
		int slideSize = slideStateMachine.getSlideShowSize();
		if(slideSize > 0 && !layoutSlider.isEnabled()) {
			layoutSlider.setEnabled(true);
			layoutSlider.setMaximum(slideSize - 1);
		}
		else if (slideSize == 0) {
			layoutSlider.setValue(0);
			layoutSlider.setEnabled(false);			
		}

		int currentIndex = layoutSlider.getValue();
		previousSlide = slideStateMachine.getSlideAtIndex(currentIndex -1);
		currentSlide = slideStateMachine.getSlideAtIndex(currentIndex);
		nextSlide = slideStateMachine.getSlideAtIndex(currentIndex + 1);

		if(previousSlide != null) {
			resizeImageIcon(lblPreviousImage, previousSlide.getIcon());
		} else if (layoutSlider.isEnabled()){
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
		} else if (layoutSlider.isEnabled()){
			lblPrimaryImage.setIcon(null);
		}

		if(nextSlide != null) {
			resizeImageIcon(lblNextImage, nextSlide.getIcon());
		} else if (layoutSlider.isEnabled()){
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
			BufferedImage bufferedImg = new BufferedImage(
					PreviewImagePanel.getWidth(),
					PreviewImagePanel.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			Graphics g = bufferedImg.createGraphics();
			g.drawImage(previewIcon.getImage(), 0, 0, PreviewImagePanel.getWidth(), PreviewImagePanel.getHeight(), null);
			PreviewImagePanel.initializeImages();
			PreviewImagePanel.setImage(bufferedImg);
		}
	}
}

