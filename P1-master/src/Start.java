import javax.swing.*;
import java.awt.*;

public class Start extends JFrame {

    public Start(){
        initWindow();
    }

    private void initWindow(){
        add(new Game());    //creates new object Game where all of our game coding is
        setResizable(false); //windows size can't be changed
        setSize(Game.getWindowWidth(), Game.getWindowHeight()); //sets size of the window based on static variables in Game class
        setTitle("The Quiet Kid");  //sets the name of the application
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //the program will end when you close it with X
        setLocationRelativeTo(null);    //window with game will always start at the center of the screen

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Start ex = new Start();
            ex.setVisible(true);
        });
    }
}
