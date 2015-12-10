import java.awt.Point;

// My Player Class designed for Platform usage
public class Player extends Sprite {
	
    //starting at top left point going clockwise
    protected Point topLeft = new Point(0, 0);
    protected Point topRight = new Point(0, 0);
    protected Point bottomLeft = new Point(0, 0);
    protected Point bottomRight = new Point(0, 0);
	
    protected Point[] pointArray = {topLeft, topRight, bottomLeft, bottomRight};
	
    //variable that represents the current Sprite the player is colliding with
    public Sprite collidedObject;
	
    //Player's grounded state
    protected boolean grounded = false;
	
    //Players default directional speeds
    protected int xSpeed = 5;
    protected int ySpeed = -15;
	
    //Players maximum directional speeds
    public int maxSpeedX = 20;
    public int maxSpeedY = 20;
	
    //Animation states
    public int animateRight = 0;
    public int animateLeft = 1;
	
    Player() {
		
    }
	
    //Player Constructor
    public Player(SpriteSheet spriteSheet, int sheetX, int sheetY, int gridX, int gridY) {
	super(spriteSheet, sheetX, sheetY, gridX, gridY);
	updatePointLocation();
    }
	
    public void update() {
		
	if(left + dx < Control.frameLeft) {
	    this.dx = 0;
	}
	else if(right + dx > Control.frameRight) {
	    this.dx = 0;
	}
		
	//Update position based off of new velocity
	x += dx;
	//Caps dy at a terminal Velocity
	dy = Math.min(dy, maxSpeedY);
	y += dy;
		
	// Update boundaries
	this.left = x;
	this.right = x + width;
	this.top = y;
	this.bottom = y + height;
		
	//update the players collision rectangle to follow the sprite
	rect.x = x;
	rect.y = y;
    }
	
    //Updates sprites current collision points, added more accurate points based off image
    public void updatePointLocation() {
	pointArray[0] = new Point(rect.x + 2, rect.y + 2); // Top Left
	pointArray[1] = new Point(rect.x + width - 2, rect.y + 2); // Top Right
	pointArray[2] = new Point(rect.x + width - 2, rect.y + height); // Bottom Right
	pointArray[3] = new Point(rect.x + 2, rect.y + height); // Bottom Left
    } // end updatePointLocation
	
    public void userInput(boolean[] inputKeyArray) {
	boolean left = inputKeyArray[0];
	boolean right = inputKeyArray[1];
	boolean up = inputKeyArray[2];
		
	//if only moving in the left direction
	if(left && !right){
	    left();
	}
		
	//if only moving in the right direction
	if(!left && right){
	    right();
	}
		
	//if not moving at all or trying to move in both directions, stand still
	if((left && right) || (!left && !right)){
	    dx = 0;
	    //Resets image to the stand still image
	    image = sheet.spriteArray[0][arrayRow];
	}
		
	if(up && grounded){
	    up();
	}
		
	if(!grounded){
	    gravity();
	}
    }
	
    public void left() {
	dx = -xSpeed;
	animate(animateLeft);
    }
	
    public void right() {
	dx = xSpeed;
	animate(animateRight);
    }
	
    public void up() {
	dy = ySpeed;
    }
	
    public void gravity(){
	dy += 1;
    }
	
    //Takes the spriteSheet row number corresponding to the row of images
    //that are moving in the correct direction
    public void animate(int direction){
	arrayRow = direction; //Updates the current Row of images we are using to animate
	arrayCol = arrayCol % (sheet.sheetCol - 1); //Allows the animation to loop through the frames
	arrayCol += 1; //Advances through the loop of images
	image = sheet.spriteArray[arrayCol][arrayRow]; //Sets the image to the current frame
    }
	
    public void resolveDX(){
	int distance = 0;
    	//if player is moving to the right then calculate distance from players right boundary
	if(dx > 0) {
            distance = getDistanceXFrom(collidedObject, 0, width);
            dx = distance;
        }
	//Else if player is moving to the left then calculate distance from players left boundary
        else if(dx < 0){
            distance = getDistanceXFrom(collidedObject, collidedObject.width, 0);
            dx = distance;
        }
		
	//System.out.println("Distance: " + distance + " DX: " + dx);
    }
    
    public void resolveDY(){
    	//if player is moving down then calculate distance from players bottom boundary
	if(dy > 0) {
            int distance = getDistanceYFrom(collidedObject, 0, height);
            dy = distance;
        }
	//Else if player is moving up then calculate distance from players top boundary
        else if(dy < 0){
            int distance = getDistanceYFrom(collidedObject, collidedObject.height, 0);
            dy = distance;
        }
    }
	
    //calculates the distance in the X direction from the player to the collidedObject
    public int getDistanceXFrom(Sprite sprite, int spriteOffSet, int playerOffSet){
        int distanceX = (sprite.getX() + spriteOffSet) - (this.x + playerOffSet);
        //System.out.println("Sprite's Border: " + (sprite.getX() + spriteOffSet) + " Player's Border: " + (this.x + playerOffSet) + " Distance: " + distanceX + " DX: " + dx);
        return distanceX;
    }
	
    //calculates the distance in the Y direction from the player to the collidedObject
    public int getDistanceYFrom(Sprite sprite, int spriteOffSet, int playerOffSet){
        int distanceY = (sprite.y + spriteOffSet) - (this.y + playerOffSet);
        return distanceY;
    }
	
    public boolean getGrounded() {
	return grounded;
    }
	
    public void setGrounded(boolean state) {
	grounded = state;
    }
	
}
