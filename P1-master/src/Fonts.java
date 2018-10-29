import java.awt.*;



// This Class would provide some easier access to relevant
// methods for editing texts. for example changing color, font or size and positions
public class Fonts {

    public static void drawString(Graphics g, Font f, Color c, String text, int x, int y) {
        g.setColor(c);
        g.setFont(f);
        g.drawString(text, x, y);
    }

    public static void drawString(Graphics g, Font f, Color c, String text) {
        FontMetrics fm = g.getFontMetrics(f);
        int x = (Game.getWindowWidth() - fm.stringWidth(text)) / 2;
        int y = (Game.getWindowHeight() - fm.getHeight() / 2) + fm.getAscent();
        drawString(g, f, c, text, x, y);

    }

    public static void drawString(Graphics g, Font f, Color c, String text, int y) {
        FontMetrics fm = g.getFontMetrics(f);
        int x = (Game.getWindowWidth() - fm.stringWidth(text)) / 2;
        drawString(g, f, c, text, x, y);

    }
}
