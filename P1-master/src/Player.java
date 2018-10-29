import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;

public class Player extends Sprite {

    private final int jumpSpeed = -8;
    private final int fallingSpeed = 8;
    private final int floorLevel = 60;
    private int standingOrDucking = 0;  //variable for Array list to determine which sprite to use
    private int animationFrame = 0;     //^
    private ArrayList<ArrayList<Image>> imageList = new ArrayList<>();  //Array list with all frames of animation for player
    private ArrayList<Image> standingSprites = new ArrayList<>();   //Array list with all standing sprites
    private ArrayList<Image> duckingSprites = new ArrayList<>();    //Array list with all ducking sprites
    private boolean jumpKey;    //check if jump key is pressed
    private boolean jumpAvailable;  //checks if player landed after the last jump
    private URL jumpUrl = this.getClass().getResource("/jump.wav"); //URL address to jump sound
    private AudioClip jumpSound = Applet.newAudioClip(jumpUrl);     //jump audio clip
    private int standingHeight;
    private int duckingHeight;
    private int gameHeight = Game.getWindowHeight();


    public Player(){

        super(0, 0); //starting x and y position of the player

        initPlayer();

        x = (int)(gameHeight/2);
        y = gameHeight-floorLevel-standingHeight;

    }

    private void initPlayer(){

        ImageIcon ii;
        for(int i = 0; i<1; i++){   //loop for standing loading images to list, right now there is only one picture but the loop is ready for the future

            ii = new ImageIcon(this.getClass().getResource("/player.png"));
            standingSprites.add(ii.getImage());
        }
        standingHeight = standingSprites.get(0).getHeight(null);


        for(int i = 0; i<1; i++){   //loop for loading ducking images to list

            ii = new ImageIcon(this.getClass().getResource("/player_short.png"));
            duckingSprites.add(ii.getImage());
        }

        duckingHeight = duckingSprites.get(0).getHeight(null);

        imageList.add(standingSprites);     //adding 2 lists to imageList list
        imageList.add(duckingSprites);
        image = imageList.get(standingOrDucking).get(animationFrame); //setting the first frame of standing player to image variable

    }

    public void setImage(){     //sets new image from Array list for each frame

        image = imageList.get(standingOrDucking).get(animationFrame);
        getImageDimensions();
    }

    public void animationFrame(){   //changes animationFrame variable each frame

        switch(standingOrDucking){
            case 0:
                if(animationFrame<standingSprites.size()-1){
                    animationFrame++;
                }
                else{
                    animationFrame=0;
                }
                break;
            case 1:
                if(animationFrame<duckingSprites.size()-1){
                    animationFrame++;
                }
                else{
                    animationFrame=0;
                }

                break;
        }
        setImage();
    }

    public void move(){     //changes y position of player

        if(jumpKey&& jumpAvailable){    //jump key is pressed and highest jump position is not reached
            y += jumpSpeed;
        }
        else if(y < gameHeight-floorLevel-standingHeight){   //else player will start falling down
            y += fallingSpeed;
        }
        if(y == gameHeight-floorLevel-standingHeight){       //if player touches the ground he can jump again
            jumpAvailable =true;
        }
        if(y <= gameHeight-floorLevel-(standingHeight*2.5)) {  //if player reaches highest point of jump
            jumpAvailable = false;
        }
    }

    public void obstacleHit(){  //player is moved 40 pixels back if he hits the obstacle

        x-=40;
    }


    public void keyPressed(KeyEvent e){

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP && jumpAvailable && standingOrDucking == 0 && isVisible()){ //up key pressed
            jumpKey = true;
            jumpSound.play();
        }

        if (key == KeyEvent.VK_DOWN){ //down key pressed
            if(jumpAvailable && y == gameHeight-floorLevel-standingHeight){
                standingOrDucking = 1;
                setImage();
                y += (standingHeight-duckingHeight);
            }
        }

    }


    public void keyRelesed(KeyEvent e){

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP){ //up key released
            jumpKey = false;
        }

        if (key == KeyEvent.VK_DOWN){ //down key released
            if(jumpAvailable && y == gameHeight-floorLevel-duckingHeight){
                standingOrDucking = 0;
                y -= (standingHeight-duckingHeight);
            }

        }
    }

}
