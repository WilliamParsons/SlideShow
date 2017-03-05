
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import Slides.SlideShowStateMachine;
import Slides.SlideState;


public class AddImageListener implements ActionListener {

	private static final int FILES_AND_DIRECTORIES = 2;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileTypeFilter(".jpg", "image file"));
		fc.setFileSelectionMode(FILES_AND_DIRECTORIES);
		SlideShowStateMachine mySlideShow = SlideShowStateMachine.getInstance();
		int result = fc.showDialog(null, "Add Image");
		if (result == JFileChooser.APPROVE_OPTION){
			String selectedFilePath = fc.getSelectedFile().getPath();
			if (selectedFilePath.contains(".jpg")){
				ImageIcon icon = new ImageIcon(selectedFilePath);
				SlideState mySlide = new SlideState(icon);
				mySlideShow.addSlide(mySlide);
			}
			else{
				makeListOfImages imageVector = new makeListOfImages();
				Vector imageFileVector = imageVector.listFilesAndFilesSubDirectories(selectedFilePath);
				for (int i = 0; i < imageFileVector.size(); i++){
					String filePath = (String) imageFileVector.elementAt(i);
					ImageIcon icon = new ImageIcon(filePath);
					SlideState mySlide = new SlideState(icon);
					mySlideShow.addSlide(mySlide);
				}
			}
		}	
	}
}
