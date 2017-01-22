import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JSpinner;
import javax.swing.JEditorPane;
import javax.swing.JSlider;
import java.awt.Canvas;
import javax.swing.JLayeredPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Choice;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import java.awt.Color;

public class SlideshowMaker extends JFrame {

	private JPanel MainPanel;
	private JTextField LayoutOfThe;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SlideshowMaker frame = new SlideshowMaker();
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
		setBounds(100, 100, 1175, 718);
		MainPanel = new JPanel();
		MainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(MainPanel);
		
		JList DirectoryList = new JList();
		
		JScrollBar ListScroll = new JScrollBar();
		
		JPanel LayoutPanel = new JPanel();
		
		JPanel TransitionPanel = new JPanel();
		
		JPanel PhotoPanel = new JPanel();
		
		JPanel AudioPanel = new JPanel();
		GroupLayout gl_MainPanel = new GroupLayout(MainPanel);
		gl_MainPanel.setHorizontalGroup(
			gl_MainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_MainPanel.createSequentialGroup()
					.addComponent(DirectoryList, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(ListScroll, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_MainPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(PhotoPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 975, Short.MAX_VALUE)
						.addComponent(TransitionPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 975, Short.MAX_VALUE)
						.addComponent(LayoutPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 975, Short.MAX_VALUE)
						.addComponent(AudioPanel, GroupLayout.DEFAULT_SIZE, 975, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_MainPanel.setVerticalGroup(
			gl_MainPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(DirectoryList, GroupLayout.DEFAULT_SIZE, 669, Short.MAX_VALUE)
				.addGroup(Alignment.TRAILING, gl_MainPanel.createSequentialGroup()
					.addGroup(gl_MainPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_MainPanel.createSequentialGroup()
							.addComponent(LayoutPanel, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(TransitionPanel, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(PhotoPanel, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(AudioPanel, GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE))
						.addComponent(ListScroll, GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		JLabel lblNewLabel = new JLabel("Audio Selection");
		
		JList list_1 = new JList();
		
		JButton Play = new JButton("Play");
		
		JButton Pause = new JButton("Pause");
		
		JButton Add = new JButton("Add");
		GroupLayout gl_AudioPanel = new GroupLayout(AudioPanel);
		gl_AudioPanel.setHorizontalGroup(
			gl_AudioPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_AudioPanel.createSequentialGroup()
					.addGroup(gl_AudioPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_AudioPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNewLabel))
						.addGroup(gl_AudioPanel.createSequentialGroup()
							.addGap(105)
							.addGroup(gl_AudioPanel.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_AudioPanel.createSequentialGroup()
									.addComponent(Pause)
									.addGap(306)
									.addComponent(Play)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(Add))
								.addComponent(list_1, GroupLayout.PREFERRED_SIZE, 787, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(83, Short.MAX_VALUE))
		);
		gl_AudioPanel.setVerticalGroup(
			gl_AudioPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_AudioPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addGap(18)
					.addComponent(list_1, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_AudioPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(Pause)
						.addComponent(Add)
						.addComponent(Play))
					.addContainerGap(19, Short.MAX_VALUE))
		);
		AudioPanel.setLayout(gl_AudioPanel);
		
		JLabel lblPhotoPreview = new JLabel("Photo Preview");
		
		JLabel lblThePhotoPreview = new JLabel("The photo preview for the selected pictures.");
		GroupLayout gl_PhotoPanel = new GroupLayout(PhotoPanel);
		gl_PhotoPanel.setHorizontalGroup(
			gl_PhotoPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_PhotoPanel.createSequentialGroup()
					.addGroup(gl_PhotoPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_PhotoPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblPhotoPreview))
						.addGroup(gl_PhotoPanel.createSequentialGroup()
							.addGap(369)
							.addComponent(lblThePhotoPreview)))
					.addContainerGap(392, Short.MAX_VALUE))
		);
		gl_PhotoPanel.setVerticalGroup(
			gl_PhotoPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_PhotoPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblPhotoPreview)
					.addGap(38)
					.addComponent(lblThePhotoPreview)
					.addContainerGap(67, Short.MAX_VALUE))
		);
		PhotoPanel.setLayout(gl_PhotoPanel);
		
		JButton Beginning = new JButton("Beginning");
		
		JButton Previous = new JButton("Previous");
		
		JButton SelectTransition = new JButton("Select Transition");
		
		JButton Next = new JButton("Next");
		
		JButton End = new JButton("End");
		
		JList TransitionList = new JList();
		TransitionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JLabel lblTransitionSelection = new JLabel("Transition Selection");
		GroupLayout gl_TransitionPanel = new GroupLayout(TransitionPanel);
		gl_TransitionPanel.setHorizontalGroup(
			gl_TransitionPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_TransitionPanel.createSequentialGroup()
					.addContainerGap(94, Short.MAX_VALUE)
					.addGroup(gl_TransitionPanel.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_TransitionPanel.createSequentialGroup()
							.addComponent(Beginning)
							.addGap(50)
							.addComponent(Previous)
							.addGap(144)
							.addComponent(SelectTransition)
							.addGap(134)
							.addComponent(Next)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(End))
						.addComponent(TransitionList, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 819, GroupLayout.PREFERRED_SIZE))
					.addGap(62))
				.addGroup(gl_TransitionPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTransitionSelection)
					.addContainerGap(872, Short.MAX_VALUE))
		);
		gl_TransitionPanel.setVerticalGroup(
			gl_TransitionPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_TransitionPanel.createSequentialGroup()
					.addGap(6)
					.addComponent(lblTransitionSelection)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(TransitionList, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_TransitionPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_TransitionPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(Beginning)
							.addComponent(Previous))
						.addGroup(gl_TransitionPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(End)
							.addComponent(Next)
							.addComponent(SelectTransition)))
					.addContainerGap())
		);
		TransitionPanel.setLayout(gl_TransitionPanel);
		
		JSlider LayoutSlider = new JSlider();
		LayoutSlider.setValue(0);
		
		LayoutOfThe = new JTextField();
		LayoutOfThe.setText("Layout of the Pictures");
		LayoutOfThe.setColumns(10);
		
		JLabel lblSlideshowPreview = new JLabel("Slideshow Preview");
		lblSlideshowPreview.setForeground(Color.BLACK);
		lblSlideshowPreview.setBackground(Color.LIGHT_GRAY);
		GroupLayout gl_LayoutPanel = new GroupLayout(LayoutPanel);
		gl_LayoutPanel.setHorizontalGroup(
			gl_LayoutPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_LayoutPanel.createSequentialGroup()
					.addGroup(gl_LayoutPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_LayoutPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(LayoutSlider, GroupLayout.DEFAULT_SIZE, 955, Short.MAX_VALUE))
						.addGroup(gl_LayoutPanel.createSequentialGroup()
							.addGap(414)
							.addComponent(LayoutOfThe, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_LayoutPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblSlideshowPreview)))
					.addContainerGap())
		);
		gl_LayoutPanel.setVerticalGroup(
			gl_LayoutPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_LayoutPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSlideshowPreview)
					.addGap(75)
					.addComponent(LayoutOfThe, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
					.addComponent(LayoutSlider, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		LayoutPanel.setLayout(gl_LayoutPanel);
		MainPanel.setLayout(gl_MainPanel);
	}
}
