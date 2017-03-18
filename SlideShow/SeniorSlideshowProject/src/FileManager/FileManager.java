package FileManager;

import Slides.*;
import java.io.*;

public class FileManager {
	
	public void writeFile(SlideShowStateMachine saveState, String filename)
	{
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		
		try {

			fout = new FileOutputStream(filename);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(saveState);

			System.out.println("Done");

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}		
	}
	
	public SlideShowStateMachine readFile(String filename) {

		SlideShowStateMachine saveState = null;

		FileInputStream fin = null;
		ObjectInputStream ois = null;

		try {

			fin = new FileInputStream(filename);
			ois = new ObjectInputStream(fin);
			saveState = (SlideShowStateMachine) ois.readObject();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return saveState;
	}
}
