import g4p_controls.*;
import processing.core.PApplet;
import processing.event.KeyEvent;
import java.awt.*;
import java.util.ArrayList;

public class TextUI

{
    private final PApplet p;
    public static GLabel balanceText;
    public static GTextField balance;
    public static GLabel bigText;
    public static GLabel smallText;
    public static GLabel dkrText;
    private Product productChosen = null;
    private boolean isProductChosen = false;
    private boolean isEnteringAmount = false;
    private int change;
    private final Controller con;

    public TextUI(PApplet p, Controller con)
    {
        this.p = p;
        this.con = con;
    }

    public void showBigInfoText()
    {
        bigText = new GLabel(this.p, 790, 150, 500, 400, " ");
        bigText.setLocalColorScheme(8);
        bigText.setFont(new Font("Monospaced", Font.BOLD, 30));
    }

    public void showSmallInfoText()
    {
        smallText = new GLabel(this.p, 760, 300, 400, 400, " ");
        smallText.setLocalColorScheme(8);
        smallText.setFont(new Font("Monospaced", Font.PLAIN, 15));
    }

    public void showBalanceInfoText()
    {
        balanceText = new GLabel(this.p, 780, 525, 500, 100, "Rådighedsbeløb");
        balanceText.setLocalColorScheme(8);
        balanceText.setFocus(true);
        balanceText.setFont(new Font("Monospaced", Font.PLAIN, 30));

        dkrText = new GLabel(this.p, 1128, 530, 230, 100, "kr.");
        dkrText.setFont(new Font("SansSerif", Font.BOLD, 18));

        balance = new GTextField(this.p, 1050, 558, 100, 40);
        balance.setLocalColorScheme(19);

        balance.setNumeric(0, 1000, 0);
        balance.setFocus(true);
        balance.setVisible(true);
    }

    public void welcomeText()
    {
        balance.setVisible(false);
        balanceText.setVisible(false);
        dkrText.setVisible(false);
        bigText.setText("Velkommen! \n \nTid til at slukke tørsten! :-) \n \nVælg et produkt fra automaten");
    }

    // Shows all the text on the screen
    public void allText()
    {
        G4P.setInputFont("Arial", Font.PLAIN, 26); // sets inputfont
        showBigInfoText();
        showSmallInfoText();
        showBalanceInfoText();
        welcomeText();
    }

    // Eventhandler for imagebuttons pressed
    public void imgBtnEvents(GImageButton pressedButton)    // Eventhandler for imagebuttons
    {
        System.out.println("imgBtnEvents(GImageButton pressedButton)");
        if (change > 0) balance.setText("" + change);

        smallText.setText("");
        isEnteringAmount = false;
        isProductChosen = true;
        balanceText.setVisible(false);

        ArrayList<GImageButton> buttons = con.getButtons();
        for (GImageButton button : buttons)
        {
            if (button == pressedButton)
            {
                int locationId = Integer.parseInt(button.tag);
                productChosen = con.getProductById(locationId);

                if (productChosen.isInStock())
                {
                    balance.setVisible(false);
                    dkrText.setVisible(false);
                    bigText.setText(productChosen.toString());
                } else
                {
                    balance.setVisible(false);
                    dkrText.setVisible(false);
                    bigText.setText(productChosen.getName() + " er udsolgt");
                    balanceText.setVisible(false);
                    balance.setFocus(false);
                }
                con.enableDisableProductIfInStock(button, productChosen.isInStock());
            }
        }

        if (change > 0 && change < productChosen.getPrice() && productChosen.isInStock())
        {
            bigText.setText("Indsæt minimum " + (productChosen.getPrice() - change) + "kr. yderligere, for at kunne købe en " + productChosen.getName() + " til " + productChosen.getPrice() + "kr.");
            isEnteringAmount = true;
            balance.setFocus(true);
            balanceText.setVisible(true);
            dkrText.setVisible(true);
            balance.setVisible(true);
            smallText.setText("Maxbeløb er 1000kr.");
        }

    }

    // Eventhandler for  key pressed
    public void keyTest(KeyEvent event)
    {
        int eventKeyReleased = 2;
        int inputPrice;
        int productPrice;

        try
        {
            if (event.getAction() != eventKeyReleased)
            {
                System.out.println("event.getAction() != eventKeyReleased");
                return;
            }

            if (p.key == 'q' || p.key == 'Q')
            {
                System.out.println("q");
                p.exit();
            }

            if (isEnteringAmount && p.key == p.ENTER)
            {
                System.out.println("isEnteringAmount && p.key == p.ENTER");

                inputPrice = balance.getValueI();
                productPrice = productChosen.getPrice();

                if (inputPrice < productPrice)
                {
                    bigText.setText("Du har desværre ikke nok til at købe en " + productChosen.getName() + ".\n \nKom tilbage igen, når du har lidt flere penge ;-)");
                } else
                {
                    if (productChosen.isInStock())
                    {
                        change = inputPrice - productPrice;
                        bigText.setText("Værsgo' tag din " + productChosen.getName() + ". Du får " + change + "kr. i byttepenge.");
                        // smallText.setText("Valg: " + productChosen + " " + productPrice);
                        smallText.setText("Vil du købe mere, så tryk på et produkt");
                        balance.setVisible(true);
                        balanceText.setVisible(true);
                        balance.setText("" + change);
                        dkrText.setVisible(true);
                        con.updateStock(productChosen.getLocationId());
                        con.updateVendingMachine();
                    }
                }
            }

            if (isProductChosen && !isEnteringAmount)
            {
                System.out.println("isProductChosen && !isEnteringAmount");

                String sKey = String.valueOf(event.getKey()).toLowerCase();
                switch (sKey)
                {
                    case ("j"):
                        isEnteringAmount = true;
                        bigText.setText("Tast dit rådighedsbeløb i feltet nedenunder, efterfulgt af ENTER");
                        smallText.setText("Maxbeløb er 1000kr.");
                        balance.setVisible(true);
                        balanceText.setVisible(true);
                        dkrText.setVisible(true);
                        balance.setFocus(true);


                        break;

                    case ("n"):
                        bigText.setText("Det er i orden.\n \nGod dag!");
                        isProductChosen = false;
                        productChosen = null;
                        smallText.setText("Tast q for at afslutte.");
                        balance.setVisible(false);
                        balanceText.setVisible(false);
                        dkrText.setVisible(false);
                        break;
                }
            }

        } catch (Exception e)
        {
            System.out.println("exception, " + e.getMessage());
        }
    }
}






