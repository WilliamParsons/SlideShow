
import java.io.File;
import java.util.Vector;

public class makeListOfImages {
    
	public Vector<String> listFilesAndFilesSubDirectories(String directoryName){
        File directory = new File(directoryName);
        Vector<String> imageFileVector = new Vector<String>();
        //get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList){
            if (file.isFile()){
            	if (file.getAbsolutePath().contains(".jpg")){
            		imageFileVector.add(file.getAbsolutePath());
            		System.out.println(file.getAbsolutePath());
            	}
            } else if (file.isDirectory()){
                listFilesAndFilesSubDirectories(file.getAbsolutePath());
            }
        }
        return imageFileVector;
    }
}
