import javax.swing.*;
import java.awt.*;



/*
        Small Guide :
        This class is made to be used during the Menu processing. (GameState = 0)
        Each Button is going to be either one of the following texts : "PLAY", "OPTIONS" , "SHOPS" , "EXIT" ,
        or any other similar button in other menus

        Each button has a boolean "selected" if it's True then
        the button needs to be shown in a bigger font and with two guns drawn at it's sides (for the main menu)

 */

public class Button extends Rectangle { // The class inherits from Rectangle Class because our button is actually a box, filled with text.

    private Font font, selectedFont; // Two fonts. first one is the initiative font (before being selected)
    // and the second one: "selected font" would be used if we want to use a different font for our selected button.

    private Color color, selectedColor; // // Two colors. first one is the initiative color (before being selected)
    // and the second one: "selected color" would be used if we want to use a different color for our selected button.

    // we may need to implements some changes in graphics, so even though we don't change the font after being selected
    // we still have these variables for the probable changes in future.

    private boolean selected; //each button has a boolean "selected" if it's True then
    // the program would compile the button in a different way (for example bigger font or some extra graphics)

    private String text; // The text for the button . it can be "PLAY" or anything that is specified
    // during initializing the button at the main class

    private int textY; // The vertical position of the button


    // Constructor

    public Button( String text, int textY, Font font, Font selectedFont, Color color,
                  Color selectedColor) {
        this.font = font;
        this.selectedFont = selectedFont;
        this.color = color;
        this.selectedColor = selectedColor;
        this.text = text;
        this.textY = textY;

    }



    public void setSelected(boolean selected) { // The method that would change the "selected" variable
        this.selected = selected;
    }

    public void render(Graphics g) {   // The method for drawing button , which would be called at the menu class


        if (selected) { // If the button is selected then draw the second font and second color (second font has a bigger size)
            Fonts.drawString(g, selectedFont, selectedColor,   text , textY);
        }
        else
            Fonts.drawString(g, font, color, text, textY);

        // set the position of the button based on the text's width
        FontMetrics fm = g.getFontMetrics();
        this.x = (Game.getWindowWidth() - fm.stringWidth(text)) /2;
        this.y = textY - fm.getHeight(); // here the vertical position of the button would be set
        this.width = fm.stringWidth(text);
        this.height = fm.getHeight();

        if (selected) { // Adding more graphical features to the selected button
                        // (two guns at the sides of the text// run the game to understand that)

            Graphics2D g2d = (Graphics2D) g;

            ImageIcon igun = new ImageIcon(this.getClass().getResource("/gunSmal.png"));

            Image gunOne = igun.getImage();

            ImageIcon jgun = new ImageIcon(this.getClass().getResource("/gunSmalFlip.png"));

            Image gunTwo = jgun.getImage();

            g2d.drawImage(gunOne, 400 - fm.stringWidth(text) / 2 , textY - 100 , null);
            g2d.drawImage(gunTwo, 520 + fm.stringWidth(text) / 2 , textY - 100 , null);

        }

    }
}
