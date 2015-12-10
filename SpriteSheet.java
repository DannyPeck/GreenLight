import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class SpriteSheet {
	
    public BufferedImage sheet = null;
    public BufferedImage[][] spriteArray = null;
    public int sheetCol = 0;
    public int sheetRow = 0;
	
    SpriteSheet() {
		
    }
	
    SpriteSheet(String fileName, int sheetCol, int sheetRow){
	//Creates file based off of imageFile
	File file = new File(fileName);
		
	//Makes a bufferedImage of the image inside the file using ImageIO
	try{
	    sheet = ImageIO.read(file);
	}catch(Exception e){
	    System.out.println("ERROR: couldn't load file (" + fileName + ")");
	    System.exit(42);
	}
		
	this.sheetCol = sheetCol;
	this.sheetRow = sheetRow;
		
	spriteArray = getSprites(this.sheetCol, this.sheetRow);
    }
	
    //Returns the entire sheet
    public BufferedImage getImage(){
	return sheet;
    }
	
    //Creates a 2D array of all the sprites in our SpriteSheet an returns it
    public BufferedImage[][] getSprites(int sheetCol, int sheetRow){
	//Our 2 dimensional array 
	BufferedImage[][] spriteArray = new BufferedImage[sheetCol][sheetRow];
	//spriteCol represents our number of Sprite's per row
	//spriteRow represents how many rows we have of images
		
	//represents each sprite's dimensions based off given spriteCol and spriteRow
	int imgW = sheet.getWidth() / sheetCol;
	int imgH = sheet.getHeight() / sheetRow;

	//For each sprite in the sheet get its image and store it into the corresponding position in the array
	for(int i = 0; i < sheetCol; i++){
	    for(int j = 0; j < sheetRow; j++){
		//getSubimage takes (x,y) position of image and (width, height) of image on the sheet and returns the bufferedImage
		spriteArray[i][j] = sheet.getSubimage(i * imgW, j * imgH, imgW, imgH);
	    }
	}
		
	return spriteArray;
    }
	
}
