public class Obstacle extends Sprite {

    private final int OBSTACLE_SPEED = 10;

    public Obstacle(int x, int y, int index){

        super (x, y);   //invokes main method of super class(Sprite)

        initSpike(index);
    }

    private void initSpike(int i){

        switch (i){
            case 1:
                loadImage("/spikes_1.png");      //gives path to the file with picture to method from Sprite class that will load the image
                y-=286;
                break;
            case 2:
                loadImage("/spikes_2.png");
                y-=126;
                break;
            case 3:
                loadImage("/spikes_2.png");
                y-=160;
                break;
        }
        getImageDimensions();    //method from Sprite class
    }

    public void move(){     //changes x position of obstacle

        x -= OBSTACLE_SPEED;

        if (x < 0 - getWidth()){     //checks if the image is still on the screen
            visible = false;
        }
    }
}