import java.util.Observable;

public class ComponentObservable extends Observable {
	ComponentObservable(){
		super();
	}
	void changeData() {
        setChanged(); // the two methods of Observable class
        notifyObservers();
        System.out.print("ComponentObservable: Observable changed\n");
    }
}
