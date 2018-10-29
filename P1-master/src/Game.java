import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JPanel implements ActionListener {

private static int windowWidth = 1024;  //window's width
private static int windowHeight = 720;  //window's height
private final int DELAY = 16;   //delay between each frame
private int levelTime = 60000;  //default time for level, will be multiplied by levelNum
private int levelNum = 1;   //number of chosen level
private AudioClip damageSound;   //audio of damage sound
private AudioClip dangerSound;  //audio of danger sound
private AudioClip gameOverSound;//audio of game over screen
private Player player;  //player object
private Shooter shooter;    //shooter object
private static int gameState = 0;  //0-game menu, 1-game, 2-game over screen
private ArrayList<BgSprite> bgSprites;  //list with all background sprites visible on screen
private ArrayList<Obstacle> obstacles;  //list with all obstacles visible on screen
private ArrayList<Heart> lifes;   //list with all hearts visible on screen
private ArrayList<Bullet> bullets;  //list with all bullets visible on screen
private ArrayList<Danger> danger;   //list with all danger sings visible on screen
private Timer mainTimer;    //main timer for the game
private Timer bgSpriteTimer;     //timer for background sprites
private Timer obstacleTimer;    //timer for obstacles
private Timer shooterTimer;     //timer for shooter
private Timer levelTimer;    //timer for time left to beat the level

private Menu menu; // menu object

public Game(){
    initGame();
}

private void initGame(){

    addKeyListener(new TAdapter());

    setFocusable(true);
    setBackground(Color.cyan);  //when we have our background ready it will be deleted
    setDoubleBuffered(true);
    initSounds();

    //creates timer with delay of our DELAY variable,
    // It's the main Timer which should be created separately at the top of the program
    // (Other timers regarding sprites are created at initTime() method
    mainTimer = new Timer(DELAY, this);
    mainTimer.start();  //starts mainTimer



    if(gameState < 2){   //Game state 0 (menu) or 1 (actual game)

        menu = new Menu(); // menu object from Menu class is created

        levelTime *= levelNum;  //time needed to complete level, levelNum will be decided in the menu
        player = new Player();  //creates new Player object
        shooter = new Shooter(0, player.getY());    //creates new Shooter object on the edge of the
        // screen and the same floor level as player
        initLifes();
    }

}


@Override
public void paintComponent(Graphics g){     //draws everything on screen
    super.paintComponent(g);

    if (gameState == 0) {
        drawMenu(g);
    }
    if(gameState==1){

        drawGame(g);
    }
    else if(gameState==2){

        drawGameOver(g);
    }

    Toolkit.getDefaultToolkit().sync();
}


private void drawMenu(Graphics g) { // draws menu scene

    menu.render(g);


}

private void drawGame(Graphics g){
    Graphics2D g2d = (Graphics2D) g;

    //draw Floor, will probably be deleted in the future
    g.setColor(Color.green);
    g.fillRect(-1, windowHeight-100, windowWidth, 100);

    //draw BgSprites
    for(BgSprite bgSprite : bgSprites){
        g2d.drawImage(bgSprite.getImage(), bgSprite.getX(), bgSprite.getY(), this);
    }

    //draw player
    g2d.drawImage(player.getImage(),player.getX(), player.getY(), this);

    //draw shooter
    g2d.drawImage(shooter.getImage(), shooter.getX(), shooter.getY(), this);

    //draw obstacles
    for(Obstacle obstacle : obstacles){
        g2d.drawImage(obstacle.getImage(), obstacle.getX(), obstacle.getY(), this);
    }

    //draw lifes
    for(Heart life : lifes){
        g2d.drawImage(life.getImage(), life.getX(), life.getY(), this);
    }

    //draw bullets
    for(Bullet bullet : bullets){
        g2d.drawImage(bullet.getImage(), bullet.getX(), bullet.getY(), this);
    }

    //draw danger
    for(Danger danger : danger){
        g2d.drawImage(danger.getImage(), danger.getX(), danger.getY(), this);
    }

    //draw timer
    int tempTime = levelTime/1000;
    String msg = "Time: " +String.valueOf((tempTime/60))+":"+String.valueOf((tempTime%60));   //time in format M:SS
    Font small = new Font("Helvetica", Font.BOLD, 30);
    FontMetrics fm = getFontMetrics(small);

    g.setColor(Color.black);
    g.setFont(small);
    g.drawString(msg,  0, fm.getHeight());
}

private void drawGameOver(Graphics g){  //draws game over screen

    String msg = "Game Over";
    Font small = new Font("Helvetica", Font.BOLD, 30);  //creating new font object
    FontMetrics fm = getFontMetrics(small);     //creating new font metrics object with dimensions of our font
    setBackground(Color.black);

    g.setColor(Color.white);       //set's color of drawing tool to white
    g.setFont(small);       //sets font of drawing tool to our font
    g.drawString(msg, (windowWidth - fm.stringWidth(msg)) / 2, windowHeight / 2);   //draws string msg
    int tempTime = levelTime/1000;
    msg = "Time: " +String.valueOf((tempTime/60))+":"+String.valueOf((tempTime%60));
    g.drawString(msg, (windowWidth - fm.stringWidth(msg)) / 2, windowHeight / 2 + fm.getHeight());
    g.drawString("-Press SPACE to retry-", (windowWidth/2) - (fm.stringWidth(msg)), windowHeight / 2 + (fm.getHeight()*3));
}

@Override
public void actionPerformed(ActionEvent e){     //actions performed by mainTimer


    if(gameState == 0){
        menu.tick(); // Update the menu

    }

    if(gameState==2){
        stopTimers();
        gameOverSound.play();
    }

    if (gameState == 1) {
        updateSprites();

        checkCollisions();
    }

    repaint();
}

private void updateSprites(){

    //updates player position and animation frame
    player.animationFrame();
    player.move();

    //updates position of BgSprites and deletes ones that are out of screen
    for(int i = 0; i < bgSprites.size(); i++){

        if(bgSprites.get(i).isVisible()){
            bgSprites.get(i).move();
        }
        else{
            bgSprites.remove(i);
        }
    }

    //updates position of obstacles and deletes ones that are out of screen
    for(int i = 0; i < obstacles.size(); i++){

        if(obstacles.get(i).isVisible()){
            obstacles.get(i).move();
        }
        else{
            obstacles.remove(i);
        }
    }

    //updates position of bullets and deletes ones that are out of screen
    for(int i =0; i < bullets.size(); i++){

        if(bullets.get(i).isVisible()){

            bullets.get(i).move();
        }
        else{
            bullets.remove(i);
        }
    }

}

private void checkCollisions(){

    Rectangle rP = player.getBounds(); //creates hit box for player

    //checks collision with shooter
    Rectangle rS = shooter.getBounds(); //creates hit box for shooter

    if(rP.intersects(rS)){  //if player touches the shooter

        damageSound.stop();
        gameState=2;    //game over screen
    }

    //checks collision with obstacles
    for (int i = 0; i < obstacles.size(); i++){

        Rectangle rO = obstacles.get(i).getBounds();    //creates hit box for obstacle

        if(rP.intersects(rO)){   //if player touches obstacle
            player.obstacleHit();
            obstacles.remove(i);
            damageSound.play();
        }

        if(rS.intersects(rO)){  //if shooter touches obstacle
            //later there will be some visual effect added here
            obstacles.remove(i);
        }
    }

    //checks collision with bullet
    for (int i =0; i < bullets.size(); i++){

        Rectangle rB = bullets.get(i).getBounds();  //creates hit box for bullet

        if(rP.intersects(rB)){  //if player touches bullet

            lifes.remove(lifes.size()-1);  //removes one life
            bullets.remove(i);
            damageSound.play();

            if(lifes.size() == 0){  //checks if player is still alive

                gameState=2;    //game over screen
                damageSound.stop();
            }
        }
    }


}

private void initLifes(){   //creates 3 heart objects and adds them to lifes list

    lifes = new ArrayList<>();
    for(int i = 0; i<3;i++){
        lifes.add(new Heart(windowWidth-(55+(i*55)), 0));
    }
}

private void initSounds(){  //method to load all game sounds

    URL url = this.getClass().getResource("/damage.wav");   //url to damage sound
    damageSound = Applet.newAudioClip(url);     //damage sound object

    url=this.getClass().getResource("/danger.wav");
    dangerSound = Applet.newAudioClip(url);

    url = this.getClass().getResource("/gameover.wav");
    gameOverSound = Applet.newAudioClip(url);
}

private void initTimers(){   //starts all timers except main timer which is started at the first steps

    bgSprites = new ArrayList<>();
    bgSpriteTimer = new Timer(3000, e -> {      //creates timer that will add new background sprite every 3 seconds
        bgSprites.add(new BgSprite(windowWidth, new Random().nextInt(windowHeight/3)+1));   //adds new BgSprite object to bgSprites list at random y position
    });
    bgSpriteTimer.start();

    obstacles = new ArrayList<>();
    obstacleTimer = new Timer(1500, e -> {      //creates timer that adds new obstacles every 1.5 second, will be
        // changed in the future based on difficulty level
        obstacles.add(new Obstacle(windowWidth, windowHeight, new Random().nextInt(3)+1));
    });
    obstacleTimer.start();

    bullets = new ArrayList<>();
    danger = new ArrayList<>();
    shooterTimer = new Timer(new Random().nextInt(7000)+3000, e ->{     //timer that makes shooter shoot at random intervals between 3-10 seconds
       danger.add(new Danger(0, shooter.getY()-15));    //ads danger icon above the shooter
       dangerSound.play();      //plays danger sounds
        Timer tempTimer = new Timer(1000, e1 -> {   //temporary timer that will only run once that creates new bullet and removes danger icon
           bullets.add(new Bullet(shooter.getWidth(), windowHeight-140));
           danger.remove(danger.size()-1);
        });
        tempTimer.setRepeats(false);    //runs only once
        tempTimer.start();

        int newDelay = new Random().nextInt(7000)+3000;     //new random delay for shooterTimer
        shooterTimer.setDelay(newDelay);
        shooterTimer.setInitialDelay(newDelay);
        shooterTimer.restart();     //those 3 line implement new delay for this timer
    });
    shooterTimer.start();

    levelTimer = new Timer(1000, e -> {
       levelTime-=1000;
    });
    levelTimer.start();

}

private void stopTimers(){  //method that stops all timers

    mainTimer.stop();
    bgSpriteTimer.stop();
    obstacleTimer.stop();
    shooterTimer.stop();
    levelTimer.stop();
}

public static int getWindowWidth(){
    return windowWidth;
}


public static int getWindowHeight(){
    return windowHeight;
}




    private class TAdapter extends KeyAdapter{  //checks for key inputs

        @Override
        public void keyReleased(KeyEvent e){

            if (gameState == 0) {
                menu.keyRelesed(e);
            }

            if (gameState == 1) {
                player.keyRelesed(e);
            }


        }

        @Override
        public void keyPressed(KeyEvent e){

            if (gameState == 1) {
                player.keyPressed(e);
            }

            if(gameState == 0) {
                menu.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    if (menu.currentSelection == 0) { // CLICK ON PLAY
                        gameState = 1;
                        initTimers(); //////////////// ATTENTION /////////////////
                        ////////////////////////////// After clicking on play all the other timers need to be start
                    }

                    if (menu.currentSelection == 3) { // CLICK ON EXIT
                        System.exit(1);
                    }
                }


            }

            if(gameState == 2){
                if(e.getKeyCode() == KeyEvent.VK_SPACE){
                    gameOverSound.stop();
                    gameState=1;
                    levelTime = 60000;
                    initGame(); // Besides starting the game again we need to start timers for sprites so we will call initTimers();
                    initTimers();
                }
            }
        }
    }
}


