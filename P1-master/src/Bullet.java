public class Bullet extends Sprite {

    private final int BULLET_SPEED = 10; //speed of the bullet in pixels per frame

    public Bullet(int x, int y){

        super(x, y);    //invokes main method of super class(Sprite)

        initBullet();
    }

    private void initBullet(){

        loadImage("/bullet.png");   //gives path to the file with bullet to method from Sprite class that will load the image
        getImageDimensions();   //method from Sprite class
    }

    public void move(){     //changes x position of bullet

        x += BULLET_SPEED;

        if(x > Game.getWindowWidth()){  //checks if the image is still on the screen
            visible = false;
        }
    }
}
