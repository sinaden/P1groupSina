import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {
    private static boolean[] keys = new boolean[256];
    private static boolean[] lastKeys = new boolean[256];


    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;

    }

    public static void update() {
        for (int i = 0; i < 256; i++) {
            lastKeys[i] = keys[i];
        }
    }



    public static boolean isDown(int key) {
        return keys[key];
    }

    public static boolean wasPressed(int keyCode) {
        return isDown(keyCode) && !lastKeys[keyCode];
    }

    public static boolean wasReleased(int keyCode) {
        return !isDown(keyCode) && lastKeys[keyCode];
    }

}
