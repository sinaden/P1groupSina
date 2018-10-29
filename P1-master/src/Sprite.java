import javax.swing.*;
import java.awt.*;

public class Sprite {

    protected int x;    //x position of the sprite
    protected int y;    //y position of the sprite
    protected int width;    //width of the sprite
    protected int height;   //height of the sprite
    protected boolean visible;  //is a sprite visible on the screen, if not it will be deleted
    protected Image image;  //image to return to Game class

    public Sprite(int x, int y){    //main method of class Sprite

        this.x=x;
        this.y=y;
        visible = true;
    }

    protected void loadImage(String imageName){     //method loading image from resources

        ImageIcon ii = new ImageIcon(this.getClass().getResource(imageName));   //assigns image icon based on given url
        image = ii.getImage();  //ImageIcon method to assign image icon from ii object to image
    }

    protected void getImageDimensions(){    //method returning width and height of object's sprite

        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    public Image getImage(){
        return image;
    }   //method to get image from object

    public int getX(){
        return x;
    }   //method to get x position of object

    public int getY(){
        return y;
    }   //method to get x position of object

    public boolean isVisible(){
        return visible;
    }   //method to get value of "visible' variable of object

    public int getWidth(){
        return width;
    }    //method to get width of object

    public int getHeight() { return height;}    //method to get height of object

    public void setVisible(Boolean visible){
        this.visible= visible;
    }   //method to change value of "visible" variable

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }   //method creating hit box of object
}
