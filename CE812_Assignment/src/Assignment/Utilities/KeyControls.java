// RegNo. 2312089

package Assignment.Utilities;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyControls extends KeyAdapter {

    private static boolean keyPressed;

    public static boolean isKeyPressed() {
        return keyPressed;
    }

    // used to set keyPressed to false in AngryBirdsGame
    public static void setKeyPressed(boolean keyPressed) {
        KeyControls.keyPressed = keyPressed;
    }

    // only checks for the enter key being pressed
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            keyPressed = true;
        }
    }
}
