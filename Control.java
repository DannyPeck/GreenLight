import java.awt.*;
import javax.swing.*;

public class Control {
    // Declaring all static variables
    public static int frameWidth = 774;
    public static int frameHeight = 614;
    public static int tilesWide = 24;
    public static int tilesHigh = 17;
	
    public static int frameLeft = 0;
    public static int frameRight = frameWidth;
    public static int frameTop = 0;
    public static int frameBottom = 614;
	
    public static boolean keepGoing = true;
    public static final int GRIDSIZE = 32;
    public static final int FPS = 20;
	
    // Declare GUI Elements
    private JFrame frame;
    private Container container;
    private Level currentLevel;
    
    // Declare InputHandler variables
    private InputHandler inputHandler;
    private boolean[] inputKeys;

    // Game Threads
    private Updater updater;
    private Repainter painter;
	
    //Where program starts executing, create a new instance of Main
    public static void main(String[] args) {
	new Control();
    }
	
    //Main Constructor
    public Control() {
	// Setup GUI Elements
	frame = new JFrame();
	container = new Container();
	currentLevel = new GreenLight();
	
	// Create input handler and register it as a listener to the level
	inputHandler = new InputHandler();
	currentLevel.addKeyListener(inputHandler);
		
	// Create our update and painter threads
	updater = new Updater();
	painter = new Repainter();
		
	// Finish setup for GUI Elements
	container = frame.getContentPane();
	container.add(currentLevel);
		
	// Start program focused on the level element
	currentLevel.setFocusable(true);
	
	// Setting the frame's properties, basic house keeping
	frame.setTitle("GreenLight");
	frame.setSize(frameWidth, frameHeight);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
		
	//Start our update and painter threads
	updater.start();
	painter.start();
    }

    public void input(){
	//returns arrays of data about current user input and passes it to the current level
	inputKeys = inputHandler.processKeys();
	currentLevel.userInput(inputKeys);
    }
	
    //Thread used to update the level based off of the current user input
    class Updater extends Thread {
		
	public Updater () {
			
	}
		
	public synchronized void run() {
	    while(keepGoing){
				
		// Starting user input at beginning so that it doesn't change while updating
		input();
		
		// Calls the current level's update method
		currentLevel.levelUpdate();
				
		try{
		    Thread.sleep(FPS);
					
		}catch(Exception e){
		    System.out.println("Updater Thread Died");
		}
	    }
	}
    }
	
    //Thread which updates our frame components graphics
    class Repainter extends Thread {
		
	public Repainter() {
			
	}
		
	public synchronized void run(){
	    while(keepGoing){
				
		//if its a lightweight component it will call the components paint method
		//which can be override with paintComponent
		frame.repaint();

		try{
		    Thread.sleep(FPS);
		}catch(Exception e){
		    System.out.println("Repainter Thread Died");
		}
	    }
	}
    }
}
