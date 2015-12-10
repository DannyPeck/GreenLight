public class Camera {
	
    private float x, y;
    private Player player;
	
    public Camera() {
		
    }
	
    public Camera(float x, float y, Player player) {
	this.x = x;
	this.y = y;
	this.player = player;
    }
	
    public void update() {
	//puts camera with the player in the center
	this.x = (-player.getX() - (player.getWidth() / 2)) + (Control.frameWidth / 2);
	this.y = (-player.getY() - (player.getHeight() / 2)) + (Control.frameHeight / 2);
    }
	
    public float getX() {
	return x;
    }
	
    public float getY() {
	return y;
    }
	
}
