import g4p_controls.*;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;


public class Controller
{
    private final PApplet p;
    private final DbController db = new DbController();
    private final OwnerSystem ownerSystem;
    private final TextUI textUI;
    private final ArrayList<GImageButton> buttons = new ArrayList<>();
    private PImage soldOut;

    public Controller(PApplet p)
    {
        this.p = p;
        this.textUI = new TextUI(p, this);
        this.ownerSystem = new OwnerSystem(p, this);
    }

    public void setup()
    {
        soldOut = p.loadImage("udsolgt.png");
        ownerSystem.drawOwnerBtn();
        updateVendingMachine();
        textUI.allText();
    }

    public void drawLayout()
    {
        Layout layout = new Layout(p);
        layout.display();
        layout.drawSoldOutLabels(soldOut);
    }

    public void updateVendingMachine()
    {
        int rectX = 150;
        int rectY = 170;
        int rectW = 30 + 50;
        int rectH = 60 + 50;
        int spaceX = 20;
        int spaceY = 0;
        int i = 0;

        for (GImageButton button : buttons)
        {
            button.dispose();
        }

        ArrayList<Product> products = db.getAllProducts();
        for (Product pro : products)
        {
            String[] images = {pro.getImagePath()};
            GImageButton button = new GImageButton(p, rectX + spaceX, rectY + spaceY, rectW, rectH, images);
            button.tag = String.valueOf(pro.getLocationId());
            buttons.add(button);
            pro.setImageButton(button);
            enableDisableProductIfInStock(button, pro.isInStock());

            i++;
            if (i % 4 == 0)
            {
                spaceX = 20;
                spaceY += 150;
            } else
            {
                spaceX += 90;
            }
        }

    }

    public void updateStock(int locationId)
    {
        db.updateStock(locationId);
    }

    public Product getProductById(int locationId)
    {
        return db.getProductById(locationId);
    }

    public void enableDisableProductIfInStock(GImageButton button, boolean isInStock)
    {
        if (isInStock)
        {
            button.setVisible(true);
        } else
        {
            button.setAlpha(90);
        }
    }

    public TextUI getTextUI()
    {
        return textUI;
    }

    public OwnerSystem getOwnerSystem()
    {
        return ownerSystem;
    }

    public ArrayList<GImageButton> getButtons()
    {
        return this.buttons;
    }


}
