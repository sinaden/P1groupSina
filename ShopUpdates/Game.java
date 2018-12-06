package Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import Assets.*;
import Menus.*;
import Menus.Menu;

/**
 *This class contains everything that makes the game work, such as drawing graphics, checking objects collision and updating objects position.
 * @author Konrad Krysiak
 * @see JPanel
 * @see ActionListener
 */
public class Game extends JPanel implements ActionListener {

    /**
     * @param windowWidth a static value of game's window width
     * @param windowHeight a static value of game's window height
     * @param DELAY delay of mainTimer in ms, to achieve 30 frames per second it's value is set to 33
     * @param levelTime variable used as time needed to complete the level
     * @param levelNum variable used to determine the number of selected level, also used as multiplier to levelTime,
     *                 as to achieve different times need for completions of each level
     * @param player an object created using Player class
     * @see Player
     * @param shooter an object created using Shooter class
     * @see Shooter
     * @param gameState variable used to determine in which part of the game the user is currently in
     *                  0-Menu, 1-Game, 2-Game-Over, 3-Congratulation screen, 4-Pause game
     * @param sprites ArrayList of all the sprites visible on the screen other then Shooter and Player
     * @see ArrayList
     * @param mainTimer swing timer used as internal clock for the game
     * @see Timer
     * @param livesNum variable holding number of lives the player has left
     * @param levelTimer variable used to count seconds based on mainTimer
     * @param obstacleTimer variable used to create new obstacles, based on mainTimer and spritesRandomTimer
     * @see Obstacle
     * @param shooterTimer variable used to create new danger sign and bullet, based on mainTimer and shooterRandomTimer
     * @see Danger
     * @see Bullet
     * @param timersOn boolean used to pause the timers if needed
     * @param shooterRandomTimer variable used to determine at random when new danger sign and bullet is created
     * @param spritesRandomTimer variable used to determine at random when new obstacle is created
     * @param powerupRandomTimer variable used to determine at random when new powerup is created
     * @see Powerup
     * @param musicOption boolean used to check if music is turned on or off
     * @see Music
     * @param soundOption boolean used to check if sounds are turned on or off
     * @see Sound
     * @param
     */
    private static final int windowWidth = 1024;  //window's width
    private static final int windowHeight = 720;  //window's height
    private final int DELAY = 33;   //delay between each frame
    private int levelTime;  //default time for level, will be multiplied by levelNum
    private int levelNum;   //number of chosen level
    private Player player;  //player object
    private Shooter shooter;    //shooter object
    private int gameState = 0;  //0-game menu, 1-game, 2-game over screen, 3-congrats screen
    private ArrayList<Sprite> sprites;
    private Timer mainTimer;    //main timer for the game
    private int livesNum;
    private int levelTimer;
    private int obstacleTimer;
    private int shooterTimer;
    private boolean timersOn;
    private int shooterRandomTimer;
    private int spritesRandomTimer;
    private int powerupRandomTimer;
    private static boolean musicOption;
    private static boolean soundOption;
    private Menus.Menu menu; // menu object
    private CongratsScreen congratsScreen; // congrats screen object
    private Shop shop; // shop object
    private Options options; // options object
    private Pause pause; // pause object
    private GameOverScreen gameOverScreen; //game over screen object
    private Levels levels; // levels object
    private Sound sounds;
    private Music music;
    private int MenuState = 0;
    private static int levelsUnlocked = 2;
    private static int coins =20;
    private boolean newInShop = false;

    public Game() {
        initGame();
    }

    private void initGame() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(new Color(45, 138, 78));  //when we have our background ready it will be deleted
        setDoubleBuffered(true);
        musicOption = true;
        soundOption = true;
        sounds= new Sound();
        music = new Music();
        loadSave();


        //creates timer with delay of our DELAY variable,
        // It's the main Timer which should be created separately at the top of the program
        // (Other timers regarding sprites are created at initTime() method
        mainTimer = new Timer(DELAY, this);
        mainTimer.start();  //starts mainTimer

        newMenu();
    }

    private void newGame() {
        pause = new Pause();

        congratsScreen = new CongratsScreen(); //congrats screen object created

        gameOverScreen = new GameOverScreen(); // game over screen object created

        levelTime *= levelNum;  //this will make levelTime scale up incredibly fast //time needed to complete level, levelNum will be decided in the menu

        player = new Player();  //creates new Player object

        shooter = new Shooter(0, player.getY());    //creates new Shooter object on the edge of the screen and the same floor level as player

        sprites = new ArrayList<>(11);
        for (int i = 0; i < 11; i++) {
            sprites.add(null);
        }
        initLives();
        sprites.set(4, new Background(0, new Random().nextInt(5) + 1));
        sprites.set(5, new Background(0, new Random().nextInt(5) + 1));

    }

    private void newMenu() {
        menu = new Menu(); // menu object from Menu class is created

        shop = new Shop();

        options = new Options();

        levels = new Levels(); // levels object
    }

    private void closeGame() {
        pause = null;

        congratsScreen = null;

        gameOverScreen = null;

        levelTime = 60;

        player = null;

        shooter = null;

        sprites = null;
    }

    private void closeMenu() {
        menu = null;

        shop = null;

        options = null;

        levels = null;
    }

    @Override
    public void paintComponent(Graphics g) {     //draws everything on screen
        super.paintComponent(g);

        if (gameState == 0) {

            if (MenuState == 3) {
                drawShop(g);
            }
            if (MenuState == 2) {
                drawOptions(g);
            }
            if (MenuState == 1) {
                drawLevels(g);
            }
            if (MenuState == 0) {
                drawMenu(g);
            }

        }

        if (gameState == 1) {

            drawGame(g);
        }

        if (gameState == 2) {

            drawGameOverScreen(g);
        }
        if (gameState == 3) {

            drawCongratsScreen(g);
        }
        if (gameState == 4) {
            drawPause(g);
        }


        Toolkit.getDefaultToolkit().sync();
    }


    private void drawMenu(Graphics g) { // draws menu scene

        menu.render(g);


    }

    private void drawShop(Graphics g) {
        shop.render(g);
    }

    private void drawOptions(Graphics g) {
        options.render(g);
    }

    private void drawPause(Graphics g) {
        pause.render(g);
    }

    private void drawLevels(Graphics g) {
        levels.render(g);
    }

    private void drawGame(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(new Color(127, 127, 127));
        g.fillRect(0, windowHeight-164, windowWidth, 164);

        //draw Background
        g2d.drawImage(sprites.get(livesNum + 1).getImage(), sprites.get(livesNum + 1).getX(), sprites.get(livesNum + 1).getY(), this);
        g2d.drawImage(sprites.get(livesNum + 2).getImage(), sprites.get(livesNum + 2).getX(), sprites.get(livesNum + 2).getY(), this);

        //draw player
        g2d.drawImage(player.getImage(), player.getX(), player.getY(), this);

        //draw shooter
        g2d.drawImage(shooter.getImage(), shooter.getX(), shooter.getY(), this);

        //draw obstacles
        if (sprites.get(livesNum + 4)!=null)
        g2d.drawImage(sprites.get(livesNum + 4).getImage(), sprites.get(livesNum + 4).getX(), sprites.get(livesNum + 4).getY(), this);
        if (sprites.get(livesNum + 5)!=null)
            g2d.drawImage(sprites.get(livesNum + 5).getImage(), sprites.get(livesNum + 5).getX(), sprites.get(livesNum + 5).getY(), this);

        //draw lives
        for (int i = 0; i < livesNum; i++) {
            g2d.drawImage(sprites.get(i).getImage(), sprites.get(i).getX(), sprites.get(i).getY(), this);
        }

        if(sprites.get(livesNum+6)!=null){
            g2d.drawImage(sprites.get(livesNum + 6).getImage(), sprites.get(livesNum + 6).getX(), sprites.get(livesNum + 6).getY(), this);
        }

        //draw bullets/danger
        if (sprites.get(livesNum + 7)!= null)
        g2d.drawImage(sprites.get(livesNum + 7).getImage(), sprites.get(livesNum + 7).getX(), sprites.get(livesNum + 7).getY(), this);


        //draw timer
        g.setColor(Color.black);
        Font small = pause.loadFont();  //creating new font object
        small = small.deriveFont(Font.BOLD, 30);
        g.setFont(small);
        String msg = "Time: " + String.valueOf((levelTime / 60)) + ":" + String.valueOf((levelTime % 60));   //time in format M:SS
        FontMetrics fm = getFontMetrics(small);


        g.drawString(msg, 0, fm.getHeight());
    }


    private void drawGameOverScreen(Graphics g) {  //draws game over screen

        gameOverScreen.render(g);
        int tempTime = levelTime;
        String msg = "Time: " + String.valueOf((tempTime / 60)) + ":" + String.valueOf((tempTime % 60));
        FontMetrics fm = g.getFontMetrics();
        int width = fm.stringWidth(msg) - msg.length();
        // int width = getWindowWidth() - fm.stringWidth(msg) /2;
        g.drawString(msg, width, 200);
    }

    private void drawCongratsScreen(Graphics g) { // draws congrats screen

        congratsScreen.render(g);
    }


    @Override
    public void actionPerformed(ActionEvent e) {     //actions performed by mainTimer


        if (gameState == 0) {
            menu.tick(); // Update the menu
            if (MenuState == 1)
                levels.tick();
            if (MenuState == 2)
                options.tick();
            if (MenuState == 3)
                shop.tick();
        }

        if (gameState == 1) {

            Timers();
            updateSprites();
            checkCollisions();
            checkTimer();

        }

        if (gameState == 2) {

            gameOverScreen.tick();
            music.music(false);

        }
        if (gameState == 3) {
            //   stopTimers();
            if (levelsUnlocked == levelNum) {
                levelsUnlocked++;
                saveGame();
            }
            congratsScreen.tick();
            music.music(false);
            sounds.congrats(true);
        }

        if (gameState == 4) {
            pause.tick();
        }
        repaint();

    }

    private void updateSprites() {


        //updates player position and animation frame
        player.move();
        player.animationFrame();
        shooter.animationFrame();

        //updates position of background


        if (sprites.get(livesNum + 2).getX() == 0) {
            sprites.remove(livesNum + 1);
            sprites.add(livesNum + 2, new Background(windowWidth - ((Background) sprites.get(livesNum + 1)).getSpeed(), new Random().nextInt(5) + 1));
        }
        ((Background) sprites.get(livesNum + 1)).move();
        ((Background) sprites.get(livesNum + 2)).move();


        //updates position of obstacles and deletes ones that are out of screen

        if (sprites.get(livesNum + 4) instanceof Obstacle) {
            if (sprites.get(livesNum + 4).isVisible()) {
                ((Obstacle) sprites.get(livesNum + 4)).move();
            } else {
                sprites.set(livesNum + 4, null);
            }
        }
        if (sprites.get(livesNum + 5) instanceof Obstacle) {
            if (sprites.get(livesNum + 5).isVisible()) {
                ((Obstacle) sprites.get(livesNum + 5)).move();
            } else {
                sprites.set(livesNum + 5, null);
            }
        }

        if(sprites.get(livesNum + 6) instanceof Powerup){
            if(sprites.get(livesNum + 6).isVisible()){
                ((Powerup) sprites.get(livesNum+6)).move();
            }
            else{
                sprites.set(livesNum +6, null);
            }
        }



        //updates position of bullets and deletes ones that are out of screen
        if (sprites.get(livesNum + 7) instanceof Bullet) {

            if (sprites.get(livesNum + 7).isVisible()) {

                ((Bullet) sprites.get(livesNum + 7)).move();
            } else {
                sprites.set(livesNum + 7, null);
            }
        }


    }

    private void checkCollisions() {


        Rectangle rP = player.getBounds(); //creates hit box for player

        //checks collision with shooter
        Rectangle rS = shooter.getBounds(); //creates hit box for shooter

        if (rP.intersects(rS)) {  //if player touches the shooter

            sounds.damage(false);
            gameState = 2;    //game over screen
            sounds.gameover(true);
        }

        //checks collision with obstacles

        if (sprites.get(livesNum + 4) instanceof Obstacle) {
            Rectangle rO = sprites.get(livesNum + 4).getBounds();    //creates hit box for obstacle

            if (rP.intersects(rO)) {   //if player touches obstacle
                player.obstacleHit();
                sprites.set(livesNum + 4, null);
                sounds.damage(true);
            }


            if (rS.intersects(rO)) {  //if shooter touches obstacle
                //later there will be some visual effect added here
                sprites.set(livesNum + 4, null);
            }

        }
        if (sprites.get(livesNum + 5) instanceof Obstacle) {
            Rectangle rO = sprites.get(livesNum + 5).getBounds();    //creates hit box for obstacle

            if (rP.intersects(rO)) {   //if player touches obstacle
                player.obstacleHit();
                sprites.set(livesNum + 5, null);
                sounds.damage(true);
            }

            if (rS.intersects(rO)) {  //if shooter touches obstacle
                //later there will be some visual effect added here
                sprites.set(livesNum + 5, null);
            }

        }
        if(sprites.get(livesNum+6) instanceof Powerup){
            Rectangle rPow = sprites.get(livesNum+6).getBounds();

            if(rP.intersects(rPow)){
                player.powerup();
                sprites.set(livesNum+6, null);
            }
        }


        //checks collision with bullet
        if (sprites.get(livesNum + 7) instanceof Bullet) {

            Rectangle rB = sprites.get(livesNum + 7).getBounds();  //creates hit box for bullet

            if (rP.intersects(rB)) {  //if player touches bullet

                sprites.set(livesNum + 7, null);
                sprites.remove(0);  //removes one life
                sounds.damage(true);
                livesNum--;

                if (livesNum == 0) {  //checks if player is still alive
                    gameState = 2;    //game over screen
                    sounds.damage(false);
                   sounds.gameover(true);
                }

            }
        }


    }

    private void checkTimer() {
        if (levelTime <= 0) {
            gameState = 3;
            stopTimers();
        }
    }

    private void initLives() {   //creates 3 heart objects and adds them to lives list

        for (int i = 0; i < 3; i++) {
            sprites.set(i, new Heart(windowWidth - (55 + (i * 55)), 0));
        }
        livesNum = 3;
    }

    public static boolean getSoundOption() {
        return soundOption;
    }

    public static boolean getMusicOption() {
        return musicOption;
    }

    public static int getCoins(){return coins;}


    private void initTimers() {   //starts all timers except main timer which is started at the first steps

        levelTime = 20;  //default time for level, will be multiplied by levelNum
        levelTime *= levelNum;

        timersOn = true;

        shooterRandomTimer = new Random().nextInt(420) + 180;

        spritesRandomTimer = new Random().nextInt(40) + 50;

        powerupRandomTimer = new Random().nextInt(5)+3;



        levelTimer = 0;
        obstacleTimer = 0;
        shooterTimer = 0;


    }

    private void Timers() {

        if (timersOn) {
            levelTimer += 1;
            obstacleTimer += 1;
            shooterTimer += 1;
            if (obstacleTimer == spritesRandomTimer) {
                if (sprites.get(livesNum + 4) == null)
                    sprites.set(livesNum + 4, new Obstacle(windowWidth, windowHeight, new Random().nextInt(4) + 1));
                else
                    sprites.set(livesNum + 5, new Obstacle(windowWidth, windowHeight, new Random().nextInt(4) + 1));
                obstacleTimer = 0;
                spritesRandomTimer = new Random().nextInt(40) + 50;
                powerupRandomTimer--;
                if(powerupRandomTimer==0){
                    powerupRandomTimer = new Random().nextInt(5)+3;
                    sprites.set(livesNum+6, new Powerup(windowWidth+100, windowHeight, new Random().nextInt(3)+1));
                }
            }
            if (shooterTimer == shooterRandomTimer) {
                sprites.set(livesNum + 7, new Danger(0, shooter.getY() - 15));    //ads danger icon above the shooter
                sounds.danger(true);      //plays danger sounds
            }
            if (shooterTimer == shooterRandomTimer + 60) {
                sprites.set(livesNum + 7, new Bullet(shooter.getWidth(), shooter.getY() + 28));
                shooterRandomTimer = new Random().nextInt(420) + 180;
                shooterTimer = 0;
            }
            if (levelTimer == 30) {
                levelTime -= 1;
                levelTimer = 0;
            }
        }

    }

    private void stopTimers() {  //method that stops all timers

        if (gameState != 3)
            mainTimer.stop();

        timersOn = false;
    }

    private void loadSave() {
        File file = new File("./saveFile.txt");
        try {
            Scanner in = new Scanner(file);
            levelsUnlocked = in.nextInt();
        } catch (Exception e) {
            try {
                PrintWriter writer = new PrintWriter("saveFile.txt", "UTF-8");
                writer.println("1");
                writer.close();
                levelsUnlocked = 1;

            } catch (Exception f) {
                System.out.println("FILE NOT FOUND");
            }
        }

    }

    private void saveGame() {
        try {
            PrintWriter writer = new PrintWriter("saveFile.txt", "UTF-8");
            writer.println(levelsUnlocked);
            writer.close();

        } catch (Exception e) {
            System.out.println("FILE NOT FOUND");
        }

    }

    public static int getWindowWidth() {
        return windowWidth;
    }

    public static int getWindowHeight() {
        return windowHeight;
    }

    public static int getLevelsUnlocked() {
        return levelsUnlocked;
    }


    private class TAdapter extends KeyAdapter {  //checks for key inputs

        @Override
        public void keyReleased(KeyEvent e) {

            if (gameState == 0) {
                if (MenuState == 0) {
                    menu.keyReleased(e);
                }
                if (MenuState == 1) {
                    levels.keyReleased(e);
                }
                if (MenuState == 2) {
                    options.keyReleased(e);
                }
                if (MenuState == 3) {
                    shop.keyRelesed(e);
                }

            }

            if (gameState == 1) {
                player.keyReleased(e);
            }

            if (gameState == 2) {
                gameOverScreen.keyReleased(e);
            }

            if (gameState == 3) {
                congratsScreen.keyReleased(e);
            }

            if (gameState == 4) {
                pause.keyReleased(e);
            }


        }

        @Override
        public void keyPressed(KeyEvent e) {

            if (gameState == 1) {
                player.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    gameState = 4;
                    timersOn = false;
                    music.music(false);
                }
            }

            if (gameState == 0) {
                if (MenuState == 0) {
                    menu.keyPressed(e);
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                        if (menu.currentSelection == 0) {
                            MenuState = 1;
                            levels.currentSelection = 0;
                            e.setKeyCode(KeyEvent.VK_Z);
                        }


                        if (menu.currentSelection == 1) { // OPTIONS
                            MenuState = 2;
                            options.currentSelection = 0;
                            musicOption = !musicOption;
                        }

                        if (menu.currentSelection == 2) {
                            MenuState = 3;
                            newInShop = false;
                            shop.currentSelection = 0;
                        }

                        if (menu.currentSelection == 3) { // CLICK ON EXIT
                            System.exit(1);
                        }
                    }
                }
                if (MenuState == 1) {
                    levels.keyPressed(e);
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (levels.currentSelection == 0) {
                            levelNum = levels.currentSelection + 1;
                        }
                        if (levels.currentSelection == 1) {
                            levelNum = levels.currentSelection + 1;
                        }
                        if (levels.currentSelection == 2) {
                            levelNum = levels.currentSelection + 1;
                        }
                        if (levels.currentSelection == 3) {

                            levelNum = levels.currentSelection + 1;
                        }
                        if (levels.currentSelection == 4) {

                            levelNum = levels.currentSelection + 1;
                        }
                        if (levels.currentSelection == 5) {

                            levelNum = levels.currentSelection + 1;
                        }
                        if (levels.currentSelection == 6) {

                            levelNum = levels.currentSelection + 1;
                        }
                        if (levels.currentSelection == 7) {
                            gameState = 1;
                        }
                        if (levels.currentSelection == 8) {
                            MenuState = 0;
                        }

                        if (levels.currentSelection != 8) {
                            gameState=1;
                            newGame();
                            closeMenu();
                            music.music(true);
                            initTimers();
                        }


                    }
                }
                if (MenuState == 2) {
                    options.keyPressed(e);
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                        if (options.currentSelection == 0) { // turn off background music

                            musicOption = !musicOption;
                            music.initMusic();
                        }

                        if (options.currentSelection == 1) { // turn off sound effects
                            soundOption = !soundOption;
                            sounds.initSounds();
                        }

                        if (options.currentSelection == 2) { // goes back
                            MenuState = 0;
                        }
                    }
                }
                if (MenuState == 3) {
                    shop.keyPressed(e);
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                        if (newInShop == false) {
                            newInShop = true;
                        }
                        else {
                            if (shop.currentSelection == 0) {
                                System.out.println("chosen");
                                coins -= 5;// The number of coins gets reduced by the price of one shield.
                            }
                            if (shop.currentSelection == 1) {
                                System.out.println("Back to menu");
                                MenuState = 0;
                            }
                        }
                    }
                }
            }

            if (gameState == 2) {
                gameOverScreen.keyPressed(e);

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    if (gameOverScreen.currentSelection == 0) {
                        sounds.gameover(false);
                        gameState = 1;
                        music.music(true);
                        newGame();
                        initTimers();
                    }

                    if (gameOverScreen.currentSelection == 1) {
                        gameState = 0;
                        newMenu();
                        closeGame();
                    }

                    if (gameOverScreen.currentSelection == 2) {
                        System.exit(1);
                    }
                }
            }
            if (gameState == 3) {
                congratsScreen.keyPressed(e);

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (congratsScreen.currentSelection == 0) { // CLICK ON NextLevel
                        gameState = 1;
                        levelNum++;
                        newGame();
                        initTimers();
                        music.music(true);

                    }

                    if (congratsScreen.currentSelection == 1) { // CLICK ON Menu
                        gameState = 0;
                        MenuState = 0;
                        newMenu();
                        closeGame();
                        menu.currentSelection = 0;
                        e.setKeyCode(KeyEvent.VK_Z);
                    }
                }

            }
            if (gameState == 4) {
                pause.keyPressed(e);

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    if (pause.currentSelection == 0) {
                        gameState = 1;
                        music.music(true);
                        timersOn = true;
                    }
                    else if (pause.currentSelection == 1) {
                        gameState = 0;
                        MenuState = 0;
                        newMenu();
                        menu.currentSelection = 0;
                        closeGame();
                        e.setKeyCode(KeyEvent.VK_Z);
                    }
                    else if (pause.currentSelection == 2) {
                        System.exit(1);
                    }
                }
            }
        }
    }
}


