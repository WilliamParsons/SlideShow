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

	private JPanel MainPanel;
	private JPanel LayoutPanel;
	private JSlider layoutSlider;
	private JPanel layoutTracker;
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
	private JMenu mnModes;
	private JMenuItem mntmNewMenuItem;
	private ImageIcon previewIcon, iconRight, iconLeft;
	private int slideSize;
	private SlideshowPresenter presenter = SlideshowPresenter.getInstance();
	private SlideshowMaker creator;
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
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileTypeFilter(".ssp", "Slideshow Presentation File"));
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fc.showOpenDialog(null);
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
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileTypeFilter(".ssp", "Slideshow Presentation File"));
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fc.showSaveDialog(null);
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
		mntmPresent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (presenter == null) {
					presenter = SlideshowPresenter.getInstance();
				}
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
		TransitionPanel.setBounds(10, 178, 764, 238);

		MainPanel.setLayout(null);
		MainPanel.add(LayoutPanel);
		LayoutPanel.setLayout(null);

		addImageBtn = new JButton("+");
		addImageBtn.setToolTipText("Add a single JPEG or directory of JPEG images to slideshow.");
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
		PreviewTransition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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

	private void resizePanels() {
		resizeMainPanel();
		resizeLayoutPanel();
		resizeAudioPanel();
		resizeTransitionPanel();
	}

	private void resizeMainPanel() {
		int panelWidth = this.getWidth() - 35;
		int panelHeight = this.getHeight() - 35;
		MainPanel.setBounds(5, 5, panelWidth, panelHeight);
	}

	private void resizeLayoutPanel() {
		int mainPanelWidth = MainPanel.getWidth();
		int panelWidth = LayoutPanel.getWidth();
		int panelHeight = LayoutPanel.getHeight();
		double heightwidthRatio = (double) panelHeight / (double) panelWidth;
		double heightRatio = (double) (panelWidth * heightwidthRatio) / (double) panelHeight;
		double widthRatio = (double) (mainPanelWidth - 10) / (double) panelWidth;
		panelWidth = mainPanelWidth - 10;
		panelHeight = (int) (panelWidth * heightwidthRatio);
		LayoutPanel.setBounds(10, 26, panelWidth, panelHeight);
		removeImageBtn.setBounds(panelWidth - 60, panelHeight - 30, 45, 20);
		addImageBtn.setBounds(panelWidth - 110, panelHeight - 30, 45, 20);
		layoutSlider.setBounds(15, panelHeight - 23, panelWidth - 160, 20);
		layoutTracker.setBounds(15, panelHeight - 43, panelWidth - 160, 20);
		updateLayoutTracker();

		int primWidth = (int) (lblPrimaryImage.getWidth() * widthRatio);
		int primHeight = (int) (primWidth * 0.75);
		int primX = panelWidth / 2 - primWidth / 2;
		int primY = 3;
		lblPrimaryImage.setBounds(primX, primY, primWidth, primHeight);

		int nextWidth = (int) (lblNextImage.getWidth() * widthRatio);
		int nextHeight = (int) (nextWidth * 0.75);
		int nextX = primX + primWidth + 10;
		int nextY = primY + (primHeight / 2) - (nextHeight / 2);
		lblNextImage.setBounds(nextX, nextY, nextWidth, nextHeight);

		int prevWidth = (int) (lblPreviousImage.getWidth() * widthRatio);
		int prevHeight = (int) (prevWidth * 0.75);
		int prevX = primX - prevWidth - 10;
		int prevY = primY + (primHeight / 2) - (prevHeight / 2);
		lblPreviousImage.setBounds(prevX, prevY, prevWidth, prevHeight);

		int slidesRightWidth = (int) (lblSlidesRight.getWidth() * widthRatio);
		int slidesRightHeight = (int) (slidesRightWidth * 0.75);
		int slidesRightX = nextX + nextWidth + 10;
		int slidesRightY = nextY + (nextHeight / 2) - (slidesRightHeight / 2);
		lblSlidesRight.setBounds(slidesRightX, slidesRightY, slidesRightWidth, slidesRightHeight);

		int slidesLeftWidth = (int) (lblSlidesLeft.getWidth() * widthRatio);
		int slidesLeftHeight = (int) (slidesLeftWidth * 0.75);
		int slidesLeftX = prevX - slidesLeftWidth - 10;
		int slidesLeftY = prevY + (prevHeight / 2) - (slidesLeftHeight / 2);
		lblSlidesLeft.setBounds(slidesLeftX, slidesLeftY, slidesLeftWidth, slidesLeftHeight);

		updateLayout();
	}

	private void resizeTransitionPanel() {
		int mainPanelWidth = MainPanel.getWidth();
		int oldHeight = TransitionPanel.getHeight();
		int panelY = LayoutPanel.getY() + LayoutPanel.getHeight() + 11;
		int panelWidth = mainPanelWidth - 10;
		int panelHeight = MainPanel.getHeight() - AudioPanel.getHeight() - LayoutPanel.getHeight() - 60;
		double labelHeightRatio = (double) panelHeight / (double) oldHeight;
		int labelHeight = (int) (PreviewImagePanel.getHeight() * labelHeightRatio);
		int labelWidth = (int) (labelHeight * 1.333);
		TransitionPanel.setBounds(10, panelY, panelWidth, panelHeight);
		PreviewImagePanel.setBounds((panelWidth / 2) - (labelWidth / 2),
				(TransitionPanel.getHeight() / 2) - (labelHeight / 2), labelWidth, labelHeight);
		resizePreviewImage();
		EditPanel.setBounds(PreviewImagePanel.getX() + PreviewImagePanel.getWidth() + 10,
				PreviewImagePanel.getY() + PreviewImagePanel.getHeight() - 200, 118, 200);
	}

	private void resizeAudioPanel() {
		int mainPanelWidth = MainPanel.getWidth();
		int panelWidth = mainPanelWidth - 10;
		AudioPanel.setBounds(10, MainPanel.getHeight() - 129, panelWidth, 119);
		soundTrack.setBounds(0, 0, panelWidth, 110);
	}

	private void updateLayoutTracker() {
		if (slideStateMachine.getAudioListSize() != 0) {
			layoutTracker.removeAll();
			int height = layoutTracker.getHeight();
			int x = 0;
			int y = 0;
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

	private void updateLayout() {

		resizeImageIcon(lblSlidesLeft, iconLeft);
		resizeImageIcon(lblSlidesRight, iconRight);

		if (slideStateMachine.getSlideShowSize() != slideSize) {
			slideSize = slideStateMachine.getSlideShowSize();

			if (slideSize == 0) {
				layoutSlider.setValue(0);
				layoutSlider.setEnabled(false);
			} else if (!layoutSlider.isEnabled()) {
				layoutSlider.setEnabled(true);
			}
			layoutSlider.setMaximum(slideSize - 1);
		}

		int currentIndex = layoutSlider.getValue();
		previousSlide = slideStateMachine.getSlideAtIndex(currentIndex - 1);
		currentSlide = slideStateMachine.getSlideAtIndex(currentIndex);
		nextSlide = slideStateMachine.getSlideAtIndex(currentIndex + 1);

		if (previousSlide != null) {
			resizeImageIcon(lblPreviousImage, previousSlide.getIcon());
		} else {
			lblPreviousImage.setIcon(null);
		}

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

		if (nextSlide != null) {
			resizeImageIcon(lblNextImage, nextSlide.getIcon());
		} else {
			lblNextImage.setIcon(null);
		}
	}

	private void resizeImageIcon(JLabel label, ImageIcon icon) {
		if (slideStateMachine.getSlideShowSize() > 0) {
			BufferedImage bufferedImg = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
					BufferedImage.TYPE_INT_RGB);
			Graphics g = bufferedImg.createGraphics();
			icon.paintIcon(null, g, 0, 0);
			g.dispose();

			Image scaledImg;
			int imageHeight = bufferedImg.getHeight();
			int imageWidth = bufferedImg.getWidth();
			double heightRatio = (double) imageHeight / (double) imageWidth;
			int scaledHeight = label.getHeight();
			int scaledWidth = label.getWidth();
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

	private void resizePreviewImage() {
		if (previewIcon != null) {
			PreviewImagePanel.initializeBlankImage();
			PreviewImagePanel.setImage(previewIcon.getImage());
		}
	}
	

	@Override
	public void update(Observable o, Object arg) {
		updateLayoutTracker(); // Whenever the soundtrack's table is updated, it
								// will tell the observer to update by invoking
								// this function
		System.out.print("SlideshowMaker: soundtrack table is changed\n");
	}

	public SoundTrack getSoundTrack() {
		// TODO Auto-generated method stub
		return this.soundTrack;
	}
}
