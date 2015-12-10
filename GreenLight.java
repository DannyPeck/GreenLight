import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.lang.Math.*;

public class GreenLight extends Level {
    
    // Make SpriteSheets properties of the class
    private SpriteSheet platformSheet;
    private SpriteSheet greenLightSheet;
    private SpriteSheet iconSheet;

    // Basic level entities
    private Sprite background;
    private Player player;
    private Sprite goal;
    private Camera camera;

    // Sprite Groups
    private ArrayList<Sprite> platforms;
    
    // Graphics drawing "brush"
    private Graphics2D g2D;
    
    // Timer variables used to create the random image switch
    private javax.swing.Timer timer;
    private Random random;
    private int randInt;
    private boolean timing;
    
    // Victory or Loss conditions
    private boolean victory;
    private boolean lost;

    // GreenLight Contstructor
    public GreenLight() {
	// Initialize Spritesheets
	platformSheet = new SpriteSheet("platformTiles.png", 8, 8);
	greenLightSheet = new SpriteSheet("greenLightTiles.png", 2, 1); 
	
	// Make a bare bones sprite and add the few properties needed
	background = new Sprite();
	background.x = -575;
	background.y = -280;
	try {
	    background.setImage(ImageIO.read(new File("nes.jpg")));
	} catch (IOException e) {
	    System.out.println("Background didn't load");
	}

	// Initialize the Sprite Groups
	platforms = new ArrayList<Sprite>();

	// Initialize the random object for all random functionality
	random = new Random();	
	
	// Function which creates platforms within a given range of horizontal tiles and a height
	// I return the position of one of the starting green blocks to avoid losing instantly
	int playerLoc = addPlatformsByRange(0, Control.tilesWide, 17);

	// Create the player object with playerLoc
	player = new Player(platformSheet, 6, 0, playerLoc, 16);
	
	/*  
	Creation statements for the platforms
        Had worked on making it random, and still could, but was having
	trouble ensuring win conditions
	*/
	addPlatformsByRange(0, 5, 14);
	addPlatformsByRange(8, 15, 12);
	addPlatformsByRange(18, 24, 9);
	addPlatformsByRange(12, 15, 6);
	addPlatformsByRange(7, 10, 8);
	
	// Using the same logic as the playerLoc ensures the goal is on a valid block
	int victoryLoc = addPlatformsByRange(0, 4, 5);
	
	// Create the goal object with victoryLoc
	goal = new Sprite(platformSheet, 7, 5, victoryLoc, 4);
	
	// Instantiate the camera object, which follows the player
	camera = new Camera(0, 0, player);
	
	// Create a Swing Timer to implement the color image switch
	timer = new Timer(1000, new MyTimerActionListener());

	// I use a timing boolean as a lock to avoid calling it while its still running
	timing = true;

	// Start the timer
	timer.start();

	// Initialize the win and loss conditions to false
	victory = false;
	lost = false;
    }
    
    // Was an attempt at randomly creating levels, ran out of time, hard to ensure winability
    // P.S. it is gross and was in mid exploration.
    /*public void genLevel(int platNum) {
	int randLeft = 0;
	int randRight = 4;
	int randHeight = Control.tilesHigh;
	for(int i = 0; i < platNum; i++) {
	    int tRandLeft = random.nextInt(Control.tilesWide) - 4;
	    int tRandRight = random.nextInt(5) + 2;
	    if(tRandLeft < 0) { tRandLeft = 0; }
	    else if(tRandLeft > Control.tilesWide) { tRandLeft = Control.tilesWide - 2; }
	    
	    if(tRandRight < randLeft - 4) {
		randLeft = randLeft - 10;
	    } else if(tRandLeft > randRight + 4) {
		randLeft = randRight + 4;
	    }

	    randRight = tRandRight + randLeft;
	    if(random.nextInt(2) == 1) {
		randHeight = randHeight - 3;
	    } else {
		randHeight = randHeight - 2;
	    }
	    System.out.println(randHeight);
	    addPlatformsByRange(randLeft, randRight, randHeight);
	}
    }*/
    
    // Generates a row of platform sprites based on a given length and position
    public int addPlatformsByRange(int left, int right, int height) {
	int playerLocation = 0;
	
	// Randomly selects a number within the length of the total platform
	// Then adds the left index to maintain position
	// This number will represent which platforms will become green
	int randInt = random.nextInt(right - left) + left;
	
	// For each platform, with position in mind
	for(int i = left; i < right; i++) {
	    // Creates the sprite
	    Sprite platform = new Sprite(platformSheet, 0, 0, i, height);
	    
	    // If the platform is one of the platforms selected to be green
	    if(i == randInt) {
		// set the player location so the player starts on green
		playerLocation = i;
		
		/*
		This is where I ran into some issues with Java's pass by reference
		When dealing with sharing the same image resources, I first implemented
		a second image property and wanted to simply do a swap on each timer call
		however because of it being references it would chance once and then be overwritten
		So I implemented a variation of your masterImage concept to have master variables
		of both images and set the temp variables from the masters each switch
		*/
		platform.setSecondImage(greenLightSheet.spriteArray[1][0]);
		
		// I also add a second green platform for collision reasons and too difficult without
		Sprite platform2 = new Sprite(platformSheet, 0, 0, i+1, height);
		platform2.setSecondImage(greenLightSheet.spriteArray[1][0]);
		
		// However, because I add 2 sprites in one pass, I need to maintain the loop count
		platforms.add(platform2);
		i++;
	    } else {
		// If the platform is simply red 
		platform.setSecondImage(greenLightSheet.spriteArray[0][0]);
	    }
	    
	    // add the platform to its respective group
	    platforms.add(platform);
	}

	return playerLocation;
    }

    // Updates all of the elements within the level
    public void levelUpdate() {
	
	// Geared for gameplay, determines the length between the image switch
	randInt = random.nextInt(3) + 2;
	
	// This acts as a lock when it is processing
	if(!timing) {
	    // reset the timer on a new random time
	    timer.stop();
	    timing = true;
	    timer.setInitialDelay(randInt * 1000);
	    timer.start();
	}

	//booleans that represent our movement options
	boolean canBoth;
	boolean canX;
	boolean canY;
		
	//checks to see what directions we can move
	canBoth = !collideWith(player.getDX(), player.getDY());
	canY = !collideWith(0, player.getDY());
	canX = !collideWith(player.getDX(), 0);
		
	//Resets grounded to false for re-evaluation each tick
	player.setGrounded(false);
		
	//if the player's velocity is moving downward and we cannot move in the y direction then we are grounded
	if(!canY && player.getDY() >= 0) {
	    player.setGrounded(true);
	}
			
	if (canBoth){
	    //keep values
	}
		
	//if player can only move in the y direction
	else if(canY && !canX){
	    //Based on current direction vector, recalculate the DX to be the distance from the colliding object
	    player.resolveDX();
	}
		
	//if player can only move in the x direction
	else if(canX && !canY){
	    //Based on current direction vector, recalculate the DY to be the distance from the colliding object
	    player.resolveDY();
	}
		
	//if player cannot move in either direction
	else if(!canX && !canY){
	    //Based on current direction vector, recalculate the DX and DY to be the distance from the colliding object
	    player.resolveDX();
	    player.resolveDY();
	    
	    // fixes weird corner bug
	    if(player.grounded) {
		player.dy = 0;
	    }
	}
	
	// Checks for exit conditions
	if(victory) {
	    System.out.println("You Win");
	    System.exit(0);
	}
	else if(lost) {
	    System.out.println("Game Over");
	    System.exit(0);
	}
		
	//After movement calculations have completed update player data
	player.update();
	player.updatePointLocation();
    
	// Updates the goal images
	goal.update();

	// Updates the Camera's position based on players new position
	camera.update();


    }

    // Determines if the player will collide with another sprite based on its current velocity
    public boolean collideWith(int dx, int dy) {
	boolean pointCollision = false;
		
	//if any of the players collision points is within a platform there is a collision
	if(collidePoint(player.pointArray[0], dx, dy)){
	    pointCollision = true;
	}
	else if(collidePoint(player.pointArray[1], dx, dy)){
	    pointCollision = true;
	}
	else if(collidePoint(player.pointArray[2], dx, dy)){
	    pointCollision = true;
	}
	else if(collidePoint(player.pointArray[3], dx, dy)){
	    pointCollision = true;
	}
		
	return pointCollision;
    } // end speculative player point collision
	
    //Checks to see if any platforms contain one of the players collision points
    public boolean collidePoint(Point point, int dx, int dy) {
	// Assumes no collision
	boolean result = false;
		
	//updates the points based off our players velocity
	int x = point.x + dx;
	int y = point.y + dy;
		
	for(Sprite platform : platforms) {
	    //if your points position is within the tiles bounds you collided
	    if(pointCollision(platform, x, y)) {
		
		// If the collided platform's color is currently the
		// red image then you have lost the game
		if(platform.getImage() == greenLightSheet.spriteArray[0][0]) {
		    lost = true;
		}

		result = true;
	    }
	} // end platform collision check
	
	// If the player collided with the goal then you have won the game
	if(pointCollision(goal, x, y)) {
	    victory = true;
	}
	
	return result; //if this false you can go there, that means collision is true
    } // end point collision check
	
    public boolean pointCollision(Sprite sprite, int x, int y) {
	boolean result = false;
		
	//if your points position is within the tiles bounds you collided
	if(x > sprite.left) {
	    if(x < sprite.right) {
		if(y > sprite.top) {
		    if(y < sprite.bottom) {
			player.collidedObject = sprite; //tracks the object the player is currently colliding with
			result = true;
		    } // within bottom bound
		} // within top bound
	    } // within right bound
	} // within left bound
		
	return result;
    }

    // Updates the player based off of user input
    public void userInput(boolean[] inputKeyArray) {
	player.userInput(inputKeyArray);
    }
    
    // Draws the images on the screen using the Graphics2D library
    public void paintComponent(Graphics g) {
	g2D = (Graphics2D) g.create();
	
	// Translate by the camera's position
	g2D.translate(0, camera.getY());
	
	// Draw all assets in the correct Z order
        g2D.drawImage(background.getImage(), null, background.getX(), background.getY());
	    
        for(int i = 0; i < platforms.size(); i++) {
	   g2D.drawImage(platforms.get(i).getImage(), null, platforms.get(i).getX(), platforms.get(i).getY());
	}

	// Drawing the goal and player images on the MasterImage just to be safe :)
	g2D.drawImage(goal.getMasterImage(), null, goal.getX(), goal.getY());
	    
	g2D.drawImage(player.getMasterImage(), null, player.getX(), player.getY());
    }
    
    // The timer's ActionListener
    class MyTimerActionListener implements ActionListener {

	// The action that is performed each pass of the timer
	public void actionPerformed(ActionEvent e) {
	    // switches the image for all platforms
	    for(int i = 0; i < platforms.size(); i++) {
		platforms.get(i).switchImage();
	    }
	    
	    // Releases the lock
	    timing = false;
	}
    }

}
