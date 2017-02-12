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
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.AbstractListModel;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SlideshowMaker extends JFrame {

	private JPanel MainPanel;
	private JTextField LayoutOfThe;
	private JLabel lblImagepreview = new JLabel("");
	private ImageIcon previewImage = new ImageIcon("C:\\Users\\Bryce\\Documents\\GitHub\\SlideShow\\SeniorSlideshowProject\\src\\ImageFiles\\Test1.jpg");
	private boolean contextSwitch = false;
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
		setBounds(100, 100, 1175, 720);
		MainPanel = new JPanel();
		MainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(MainPanel);
		
		JList DirectoryList = new JList();
		
		JScrollBar ListScroll = new JScrollBar();
		
		JPanel LayoutPanel = new JPanel();
		
		JPanel TransitionPanel = new JPanel();
		
		SoundTrack soundTrack = new SoundTrack((String) null);
		GroupLayout gl_MainPanel = new GroupLayout(MainPanel);
		gl_MainPanel.setHorizontalGroup(
			gl_MainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_MainPanel.createSequentialGroup()
					.addComponent(DirectoryList, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(ListScroll, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_MainPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_MainPanel.createSequentialGroup()
							.addComponent(LayoutPanel, GroupLayout.DEFAULT_SIZE, 995, Short.MAX_VALUE)
							.addGap(6))
						.addGroup(Alignment.TRAILING, gl_MainPanel.createSequentialGroup()
							.addGroup(gl_MainPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(soundTrack, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 995, Short.MAX_VALUE)
								.addComponent(TransitionPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addContainerGap())))
		);
		gl_MainPanel.setVerticalGroup(
			gl_MainPanel.createParallelGroup(Alignment.TRAILING)
				.addComponent(DirectoryList, GroupLayout.DEFAULT_SIZE, 688, Short.MAX_VALUE)
				.addGroup(gl_MainPanel.createSequentialGroup()
					.addGroup(gl_MainPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_MainPanel.createSequentialGroup()
							.addComponent(LayoutPanel, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(TransitionPanel, GroupLayout.PREFERRED_SIZE, 302, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(soundTrack, GroupLayout.PREFERRED_SIZE, 211, GroupLayout.PREFERRED_SIZE))
						.addComponent(ListScroll, GroupLayout.DEFAULT_SIZE, 682, Short.MAX_VALUE))
					.addContainerGap())
				.addComponent(ListScroll, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 682, Short.MAX_VALUE)
		);
		
		JButton Beginning = new JButton("Beginning");
		
		JButton Previous = new JButton("Previous");
		
		JButton SelectTransition = new JButton("Select Transition");
		
		JButton Next = new JButton("Next");
		
		JButton End = new JButton("End");
		
		JList TransitionList = new JList();
		TransitionList.setFont(new Font("Tahoma", Font.PLAIN, 16));
		TransitionList.setModel(new AbstractListModel() {
			String[] values = new String[] {"<none>", "Left", "Right", "Up", "Down", "Fade"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		TransitionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JLabel lblTransitionSelection = new JLabel("Transition Selection");
		
		JButton PreviewTransition = new JButton("Preview");
		PreviewTransition.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(contextSwitch)
				{
					previewImage = new ImageIcon("C:\\Users\\Bryce\\Documents\\GitHub\\SlideShow\\SeniorSlideshowProject\\src\\ImageFiles\\Test1.jpg");
					contextSwitch = false;
				}
				else
				{
					previewImage = new ImageIcon("C:\\Users\\Bryce\\Documents\\GitHub\\SlideShow\\SeniorSlideshowProject\\src\\ImageFiles\\Test2.jpg");
					contextSwitch = true;
				}
				lblImagepreview.setIcon(previewImage);
			}
		});
		
		JButton btnRemove = new JButton("Remove");
		
		
		lblImagepreview.setIcon(previewImage);
		lblImagepreview.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton btnAdd = new JButton("Add");
		GroupLayout gl_TransitionPanel = new GroupLayout(TransitionPanel);
		gl_TransitionPanel.setHorizontalGroup(
			gl_TransitionPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_TransitionPanel.createSequentialGroup()
					.addGroup(gl_TransitionPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_TransitionPanel.createSequentialGroup()
							.addGap(10)
							.addComponent(lblTransitionSelection))
						.addGroup(gl_TransitionPanel.createSequentialGroup()
							.addGroup(gl_TransitionPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_TransitionPanel.createSequentialGroup()
									.addGap(2)
									.addComponent(Beginning, GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(Previous, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(PreviewTransition, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
									.addGap(18)
									.addComponent(Next, GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(End, GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
									.addGap(23))
								.addGroup(gl_TransitionPanel.createSequentialGroup()
									.addGap(29)
									.addComponent(lblImagepreview, GroupLayout.DEFAULT_SIZE, 805, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)))
							.addGroup(gl_TransitionPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnRemove, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_TransitionPanel.createSequentialGroup()
									.addComponent(TransitionList)
									.addGap(25))
								.addComponent(SelectTransition)
								.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		gl_TransitionPanel.setVerticalGroup(
			gl_TransitionPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_TransitionPanel.createSequentialGroup()
					.addGap(6)
					.addComponent(lblTransitionSelection, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_TransitionPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_TransitionPanel.createSequentialGroup()
							.addComponent(btnAdd)
							.addPreferredGap(ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
							.addComponent(btnRemove, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_TransitionPanel.createSequentialGroup()
							.addComponent(lblImagepreview, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGroup(gl_TransitionPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_TransitionPanel.createSequentialGroup()
							.addGap(32)
							.addComponent(TransitionList, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_TransitionPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(Next, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(PreviewTransition, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(End, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(SelectTransition, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGroup(gl_TransitionPanel.createSequentialGroup()
							.addGap(170)
							.addGroup(gl_TransitionPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(Previous, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(Beginning, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
					.addPreferredGap(ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
					.addComponent(LayoutOfThe, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(83)
					.addComponent(LayoutSlider, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		LayoutPanel.setLayout(gl_LayoutPanel);
		MainPanel.setLayout(gl_MainPanel);
	}
}
