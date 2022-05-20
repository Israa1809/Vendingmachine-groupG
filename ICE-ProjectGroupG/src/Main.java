import g4p_controls.*;
import g4p_controls.GImageButton;
import processing.core.PApplet;
import processing.event.KeyEvent;


public class Main extends PApplet
{
    Controller controller = new Controller(this);

    public static void main(String[] args)
    {
        PApplet.main("Main", args);
    }

    public void settings()
    {
        size(1700, 830, P2D);
    }

    public void setup()
    {
        controller.setup();
    }

    public void draw()
    {
        controller.drawLayout();
    }

    public void handleButtonEvents(GImageButton button, GEvent event)
    {
        controller.getTextUI().imgBtnEvents(button);
    }

    public void handleButtonEvents(GButton button, GEvent event)
    {
        controller.getOwnerSystem().ownerBtnEvents(button);
    }

    @Override
    public void handleKeyEvent(KeyEvent event)
    {
        super.handleKeyEvent(event);
        controller.getTextUI().keyTest(event);
    }
}

