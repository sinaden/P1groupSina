import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/*

The main Class for executing the Menu.
except some small calls in the Game.java,
everything related to Menu would happen here.

 */

public class Menu{

    private final Button[] options; // options is an array of several Buttons for the main menu
    // They would be the "PLAY" "OPTIONS" "SHOP" & "EXIT" buttons.
    // check the "Button.java" class for more details on this object. (button)

    private Image menuOne; // The background Image of the menu
    public int currentSelection;// Which button the user is on now
    // Numbers would be 0 for "PLAY", 1 for "OPTIONS", 2 for "SHOPS" & 3 for "EXIT"

    private int xd; // if it's +1 the current Selection would go up and if it's -1, it goes down
    private boolean stopHere = false; // If it's true, the menu could not be updated
    // (used for not changing the currentselected when user's finger is still on the keyboard.)

    // Constructor
    public Menu() {
        options = new Button[4];

        // Options array would be made here, remember that TTF file for the "DK Cool Crayon"
        // Font should be added to JVM fonts folder

        options[0] = new Button("PLAY", 300 + 0 * 76,
                new Font("DK Cool Crayon", Font.BOLD, 70), // The text format when it's not selected
                new Font("DK Cool Crayon", Font.BOLD, 80),// The text format when it's selected,
                // the only change is the size
                Color.WHITE, Color.WHITE); // The colors don't change but we can change them
                                            // if it's was to be.

        options[1] = new Button("OPTIONS", 300 + 1 * 76,
                new Font("DK Cool Crayon", Font.BOLD, 70),
                new Font("DK Cool Crayon", Font.BOLD, 80), Color.WHITE, Color.WHITE);

        options[2] = new Button("SHOP", 300 + 2 * 76,
                new Font("DK Cool Crayon", Font.BOLD, 70),
                new Font("DK Cool Crayon", Font.BOLD, 80), Color.WHITE, Color.WHITE);

        options[3] = new Button("EXIT", 300 + 3 * 76,
                new Font("DK Cool Crayon", Font.BOLD, 70),
                new Font("DK Cool Crayon", Font.BOLD, 80), Color.WHITE, Color.WHITE);
    }

    public void tick() { // Update the menu (pretty much like the Player's move method)
                        // , the method is called at the ActionPerformed Method at Game.java

        if (!stopHere) {
            if (xd == -1) { // means it goes to an upper button
                currentSelection--;
                stopHere = true;
            }
            if (xd == 1){
                currentSelection++;
                stopHere = true;
            }

        }

        // If the current Selection is less than Zero
        // (i.e it points to a button which is upper than the first button ,"PLAY")
        // then it should be revalued to 3, so it would point to the "Exit" Button.
        if (currentSelection < 0) {
            currentSelection = options.length - 1;
        }

        // The same should be done for the other way
        if (currentSelection >= options.length) {
            currentSelection = 0;
        }

    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP){ //Up key pressed

            xd = -1;

        }

        if (key == KeyEvent.VK_DOWN){ //Down key pressed
            //currentSelection++;
            //wasPressed2 = true;
            xd = 1;

        }



    }


    public void keyRelesed(KeyEvent e){

        int key = e.getKeyCode();
        xd = 0;
        stopHere = false;
    }


    public void render(Graphics g) { // This method draws the whole menu
        // (is called at the drawMenu method of Game.java)

        Graphics2D g2d = (Graphics2D) g;

        ImageIcon ii = new ImageIcon(this.getClass().getResource("/bboard.png"));   //assigns image icon based on given url
        menuOne = ii.getImage();  //ImageIcon method to assign image icon from ii object to image
        g2d.drawImage(menuOne, 0, 0, null);




        // drawing The title "THE QUITE KID"
        Fonts.drawString(g, new Font("DK Cool Crayon", Font.BOLD, 80),
                Color.WHITE, " \"THE QUITE KID\" ", 100);


        // Rendereing every Button, And implementing the "currentSelection"
        // to the button by changing the selected variable of that button
        for (int i = 0; i < options.length; i++) {

            if (i == currentSelection)
                options[i].setSelected(true);
            else
                options[i].setSelected(false);
            options[i].render(g);
        }
    }
}
