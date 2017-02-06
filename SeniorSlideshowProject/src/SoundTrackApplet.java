import javax.swing.JApplet;

public class SoundTrackApplet extends JApplet {
	static SoundTrackApplet applet;
    private SoundTrack demo;

    public void init() {
        applet = this;
        String media = "./SoundFiles";
        String param = null;
        if ((param = getParameter("dir")) != null) {
            media = param;
        } 
        getContentPane().add("Center", demo = new SoundTrack(media));
    }

    public void start() {
        demo.open();
    }

    public void stop() {
        demo.close();
    }
}
