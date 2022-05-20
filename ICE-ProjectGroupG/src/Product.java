import g4p_controls.GImageButton;
import processing.core.PImage;

public class Product
{
    private String name;
    private int price;
    private String imagePath;
    private int locationId;
    private int quantity = 0;
    private GImageButton imageButton;
    private DbController db = new DbController();

    public Product(int locationId, String name, int price, String imagePath)
    {
        this.locationId = locationId;
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
    }

    public int getLocationId()
    {
        return locationId;
    }

    public String getName()
    {
        return name;
    }

    public int getPrice()
    {
        return price;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public int getQuantity()
    {
        return this.quantity;
    }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void setImageButton(GImageButton imageButton)
    {
        this.imageButton = imageButton;
    }

    public boolean isInStock() { return this.quantity > 0; }

    @Override
    public String toString()
    {
        return "Du har valgt en " + name + " til " + price + "kr." + "\n \n" + "Vil du købe?" + "\n \n      J for ja  \n      N for nej";
    }
}