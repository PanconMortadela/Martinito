import java.awt.*;
import java.awt.event.InputEvent;

public class clic extends Robot{

    public clic() throws AWTException {
    }
    public void click()throws AWTException{
        Robot robot1=new Robot();
        robot1.mousePress(InputEvent.BUTTON1_MASK);
        // release the left mouse button
        robot1.mouseRelease(InputEvent.BUTTON1_MASK);
    }
}
