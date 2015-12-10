import java.awt.event.*;

public class InputHandler implements KeyListener, MouseListener, ActionListener {
	
    boolean left;
    boolean right;
    boolean up;
	
	
    //Arrays holding the current input values that will be returned to Main
    boolean[] inputKeyArray = {left, right, up};

    //Sets up the initial input values
    InputHandler() {
	left = false;
	right = false;
	up = false;
    }
	
    //Return array of Key States
    public boolean[] processKeys(){
	return inputKeyArray;
    }
	
    //Set key state to true
    public void keyPressed(KeyEvent e) {
	int code = e.getKeyCode();
		
	//Left
	if(code == KeyEvent.VK_A){
	    inputKeyArray[0] = true;
	}
		
	//Right
	if(code == KeyEvent.VK_D){
	    inputKeyArray[1] = true;
	}
		
	//Up
	if(code == KeyEvent.VK_W){
	    inputKeyArray[2] = true;
	}
    }
	
    //Sets key state to false
    public void keyReleased(KeyEvent e) {
	int code = e.getKeyCode();
		
	//Left
	if(code == KeyEvent.VK_A){
	    inputKeyArray[0] = false;
	}
		
	//Right
	if(code == KeyEvent.VK_D){
	    inputKeyArray[1] = false;
	}
		
	//Up
	if(code == KeyEvent.VK_W){
	    inputKeyArray[2] = false;
	}
    }

    public void keyTyped(KeyEvent e) {
	// TODO Auto-generated method stub
		
    }

    public void mouseExited(MouseEvent e) {
    
    }

    public void mouseEntered(MouseEvent e) {
    
    }

    public void mouseReleased(MouseEvent e) {
    
    }

    public void mousePressed(MouseEvent e) {
    
    }

    public void mouseClicked(MouseEvent e) {
    
    }

    public void actionPerformed(ActionEvent e) {
    
    }
}
