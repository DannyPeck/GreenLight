import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Sprite {
    //Variables are protected so that they are inherited into future subclasses of Sprite
    protected SpriteSheet sheet; // The SpriteSheet the Sprite's images are derived from
    protected BufferedImage image; // Sprite's current Image
    protected BufferedImage secondImage; // Sprites Second Image for the purpose of image switch
    protected BufferedImage masterImage; // Master copy of the first image
    protected BufferedImage masterSecImage; // Master copy of the second image
    protected int arrayCol = 0; // Variable used for updating sprite's Column location
    protected int arrayRow = 0; // Variable used for updating sprite's Row location
    protected int width; // Sprite's width
    protected int height; // Sprite's height
    protected int x; // Sprite's Horizontal position
    protected int y; // Sprite's Vertical position
    protected int left; // Sprite's left boundary
    protected int right; // Sprite's right boundary
    protected int top; // Sprite's top boundary
    protected int bottom; // Sprites bottom boundary
    protected int dx; // Sprite's current x velocity
    protected int dy; // Sprite's current y velocity
    protected Rectangle rect; // Rectangle based off of our sprite's image dimensions
		
    public Sprite() {
			
    }
    
    // Sprite Overloaded Constructor
    public Sprite(SpriteSheet spriteSheet, int sheetX, int sheetY, int gridX, int gridY) {
	this.sheet = spriteSheet;
	this.arrayCol = sheetX;
	this.arrayRow = sheetY;
	this.image = sheet.spriteArray[arrayCol][arrayRow]; //sets the sprite's initial image
	this.masterImage = sheet.spriteArray[arrayCol][arrayRow];
	this.secondImage = sheet.spriteArray[arrayCol][arrayRow];
	this.masterSecImage = sheet.spriteArray[arrayCol][arrayRow];
	this.width = image.getWidth();
	this.height = image.getHeight();
	this.x = gridX * Control.GRIDSIZE;
	this.y = gridY * Control.GRIDSIZE;
	this.left = x;
	this.right = x + width;
	this.top = y;
	this.bottom = y + height;
	this.rect = new Rectangle(x, y, width, height);
    }
		
    public void update() {
	//Update position based off of new velocity
	x += dx;
	y += dy;
			
	// Update boundaries
	this.left = x;
	this.right = x + width;
	this.top = y;
	this.bottom = y + height;
			
	//update the sprites collision rectangle to follow the sprite
	rect.x = x;
	rect.y = y;
    }
		
    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }
		
    public int getDX() {
	return dx;
    }
		
    public int getDY(){
	return dy;
    }
		
    public void setDX(int dx){
	this.dx = dx;
    }
		
    public void setDY(int dy){
	this.dy = dy;
    }

    public int getHeight() {
	return height;
    }
		
    public int getWidth() {
	return width;
    }
		
    public Rectangle getRect() {
	return rect;
    }
		
    public BufferedImage getImage() {
	return image;
    }
		
    public void setImage(BufferedImage bufferedImage) {
	this.image = bufferedImage;
	this.masterImage = bufferedImage;
    }

    public BufferedImage getSecondImage() {
	return secondImage;
    }

    public void setSecondImage(BufferedImage bufferedImage) {
	this.secondImage = bufferedImage;
	this.masterSecImage = bufferedImage;
    }

    public BufferedImage getMasterImage() {
	return this.masterImage;
    }
    
    /* 
    Checks to see which image is currently set and switches
    to the other image.  As explained in other parts of my code
    due to Java passing objects by reference, the normal swap function
    broke after first pass, so had to implement a variation of your
    technique in python for maintaining master copies and using those
    */
    public void switchImage() {
	if(this.secondImage == this.masterSecImage) {
	    this.secondImage = this.masterImage;
	    this.image = this.masterSecImage;
	} else {
	    this.secondImage = this.masterSecImage;
	    this.image = this.masterImage;
	}
    }
}
