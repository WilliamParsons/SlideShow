
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
public class AddImageListener implements ActionListener {

	private static final int FILES_AND_DIRECTORIES = 2;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileTypeFilter(".jpg", "image file"));
		fc.setFileSelectionMode(FILES_AND_DIRECTORIES);
		int result = fc.showDialog(null, "Add Image");
		if (result == JFileChooser.APPROVE_OPTION){
			String selectedFilePath = fc.getSelectedFile().getPath();
			if (selectedFilePath.contains(".jpg")){
				ImageIcon icon = new ImageIcon(selectedFilePath);
			}
			else{
				makeListOfImages imageVector = new makeListOfImages();
				Vector imageFileVector = imageVector.listFilesAndFilesSubDirectories(selectedFilePath);
				for (int i = 0; i < imageFileVector.size(); i++){
					String filePath = (String) imageFileVector.elementAt(i);
					ImageIcon icon = new ImageIcon(selectedFilePath);
				}
			}
			System.out.println("getCurrentDirectory(): " + fc.getCurrentDirectory());
			System.out.println("getSelectedFile(): " + fc.getSelectedFile());
		}	
	}
}
