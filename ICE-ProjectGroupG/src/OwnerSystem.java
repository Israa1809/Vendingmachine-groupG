import g4p_controls.*;
import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class OwnerSystem
{
    private final Controller connection;
    private final DbController db = new DbController();
    private final PApplet p;
    public GButton ownerBtn;
    private GButton inventoryBtn;
    private GButton incomeBtn;
    public GButton addItemsBtn;
    private GButton addQuantityBtn;
    private GButton logOutBtn;
    private GButton logInBtn;
    private GLabel header;
    private GLabel nameLabel;
    private GLabel quantityLabel;
    private GLabel idLabel;
    private GLabel priceLabel;
    private GLabel incomeLabel;
    private GLabel soldLabel;
    private GLabel totalLabel;
    private GLabel infoToUpdateLabel;
    private GLabel updatedLabel;
    private GLabel ownerInputText;
    private GLabel passwordLabel;
    private GLabel userNameLabel;
    private GTextField ownerInputID;
    private GTextField ownerInputQuantity;
    private GTextField username;
    private GPassword password;
    private final String space = "\n \n \n";


    public OwnerSystem(PApplet p, Controller connection)
    {
        this.p = p;
        this.connection = connection;
    }

    // method to tell what each "owner" - button should do
    public void ownerBtnEvents(GButton pressedButton)
    {


        // button to show what the owner can do. (show inventory, income, add products and logout
        if (this.ownerBtn == pressedButton)
        {
            drawLogInBtn();
            drawLogInTextFields();
            drawPasswordField(true);
            username.setFocus(true);

        }
        String actualUsername = "admin";
        String actualPassword = "1234";
        String usernameEntered = username.getText();
        String passwordEntered = password.getPassword();

        if (this.logInBtn == pressedButton)
        {
            TextUI.bigText.setText("");
            TextUI.dkrText.setText("");
            TextUI.balanceText.setText("");
            TextUI.smallText.setText("");
            TextUI.balance.setVisible(false);

            if (actualUsername.equals(usernameEntered) && actualPassword.equals(passwordEntered))
            {
                showAllLabelsAndBtnsOnScreen();
                drawOwnerInputFields(p);
                hideOwnerInputFields();
                username.setVisible(false);
                password.setVisible(false);
                userNameLabel.setVisible(false);
                passwordLabel.setVisible(false);
                logInBtn.setVisible(false);
            } else
            {
                TextUI.bigText.setText("Forkert brugernavn eller adgangskode");
                username.setText("");
                username.setFocus(true);
                drawPasswordField(false);
            }
        }
        // button to log out - (gets rid of information only important for the owner)
        if (this.logOutBtn == pressedButton)
        {
            hideAllLabels();
            hideOwnerInputFields();
            inventoryBtn.setVisible(false);
            incomeBtn.setVisible(false);
            addItemsBtn.setVisible(false);
            logOutBtn.setVisible(false);
            ownerInputID.setVisible(false);
            ownerInputQuantity.setVisible(false);
            ownerInputText.setVisible(false);
            username.setVisible(false);
            password.setVisible(false);
            userNameLabel.setVisible(false);
            passwordLabel.setVisible(false);
            logInBtn.setVisible(false);
        }
        // button to show inventory
        if (this.inventoryBtn == pressedButton)
        {
            showInventory();
            hideOwnerInputFields();
        }
        // button to show income
        if (this.incomeBtn == pressedButton)
        {
            showIncome();
            hideOwnerInputFields();
        }
        // button to show the text and text-fields, where products can be added
        if (this.addItemsBtn == pressedButton)
        {
            showAddItems();
        }
        // button to add products to stock, by writing locationID
        if (addQuantityBtn == pressedButton)
        {
            db.addStock(ownerInputID.getValueI(), ownerInputQuantity.getValueI());
            showInventory();
            connection.updateVendingMachine();
            updatedLabel.setText("Lagerbeholdningen er blevet opdateret");
            showAddItems();
        }
    }

    //shows information about the product as lokationId, name, price, quantity
    private void showInventory()
    {
        header.setText("Lok.   Vare               Pris          Antal ----------------------------------------------");
        String rsID = "", rsName = "", rsPrice = "", rsQuantity = "";

        ArrayList<Product> products = db.getAllProducts();

        for (Product pro : products)
        {
            rsID += "\n" + pro.getLocationId();
            rsName += "\n" + pro.getName();
            rsPrice += "\n" + pro.getPrice();
            rsQuantity += "\n" + pro.getQuantity();
        }

        idLabel.setText(space + rsID);
        nameLabel.setText(space + rsName);
        priceLabel.setText(space + rsPrice);
        quantityLabel.setText(space + rsQuantity);

        soldLabel.setText("");
        incomeLabel.setText("");
        totalLabel.setText("");
        updatedLabel.setText("");
        infoToUpdateLabel.setText("");
        TextUI.bigText.setText("");
    }

    //shows information about the product as productId, name, sold, income
    private void showIncome()
    {

        String space = "\n \n \n";
        header.setText("Vnr.   Vare               Solgt       Indtægt ----------------------------------------------");
        String rsID = "", rsName = "", rsSold = "", rsIncome = "";
        int sold = 0, income = 0;

        ArrayList<Map<String, Object>> table = db.getAllProductsWithIncomeData();
        for (Map<String, Object> row : table)
        {
            rsID += "\n" + row.get("ID");
            rsName += "\n" + row.get("NAME");
            rsSold += "\n" + row.get("SOLD");
            rsIncome += "\n" + row.get("INCOME");

            sold += Integer.parseInt((row.get("SOLD")).toString());
            income += Integer.parseInt((row.get("INCOME")).toString());
        }

        idLabel.setText(rsID + space);
        nameLabel.setText(rsName + space);
        soldLabel.setText(rsSold + space);
        incomeLabel.setText(rsIncome + space);
        totalLabel.setText("");
        infoToUpdateLabel.setText("");
        priceLabel.setText("");
        quantityLabel.setText("");
        totalLabel.setText(space + "----------------------------------------------\n \n       I alt            " + sold + " stk       " + income + " kr.");
        TextUI.bigText.setText("");
    }

    //shows how to add products to the machine
    private void showAddItems()
    {
        showInventory();
        addQuantityBtn.setVisible(true);
        ownerInputID.setVisible(true);
        ownerInputID.setFocus(true);
        ownerInputQuantity.setVisible(true);
        ownerInputText.setVisible(true);
        ownerInputText.setText("LOK.___ ANTAL:___");
        infoToUpdateLabel.setText(space + "For at tilføje, tast lokation samt antal\nFor at fjerne, tast lokation samt (-)antal");
        updatedLabel.setText("");
        TextUI.bigText.setText("");
    }

    // draws all the Gbuttons
    public void drawOwnerBtn()
    {
        ownerBtn = new GButton(this.p, 1850, 30, 60, 30, "Ejer");
        ownerBtn.setFont(new Font("Monospaced", Font.BOLD, 17));
        ownerBtn.setLocalColorScheme(9);
    }

    private void drawLogInBtn()
    {
        logInBtn = new GButton(this.p, 1730, 250, 100, 40, "Log på");
        logInBtn.setFont(new Font("Arial", Font.BOLD, 18));
        logInBtn.setLocalColorScheme(9);
    }

    private void drawInventoryBtn()
    {
        inventoryBtn = new GButton(this.p, 750, 100, 100, 35, "Lagerbeholdning");
        inventoryBtn.setLocalColorScheme(9);
    }

    private void drawIncomeBtn()
    {
        incomeBtn = new GButton(this.p, 900, 100, 100, 35, "Indtægt");
        incomeBtn.setLocalColorScheme(9);
    }

    private void drawAddItemsBtn()
    {
        addItemsBtn = new GButton(this.p, 1050, 100, 100, 35, "Tilføj/fjern produkter");
        addItemsBtn.setLocalColorScheme(9);
        addQuantityBtn = new GButton(this.p, 1085, 728, 90, 30, "Opdater");
        addQuantityBtn.setFont(new Font("monospaced", Font.BOLD, 18));
        addQuantityBtn.setLocalColorScheme(17);
        addQuantityBtn.setVisible(false);
    }

    private void drawLogOutBtn()
    {
        logOutBtn = new GButton(this.p, 1200, 100, 100, 35, "Log ud");
        logOutBtn.setLocalColorScheme(9);
    }

    // draws the text-fields on screen, where owner can give input
    private void drawLogInTextFields()
    {
        passwordLabel = new GLabel(p, 1700, 170, 150, 40, "");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passwordLabel.setText("Adgangskode");

        userNameLabel = new GLabel(p, 1700, 100, 150, 40, "");
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userNameLabel.setText("Brugernavn");

        username = new GTextField(p, 1700, 130, 150, 40);
        username.setFont(new Font("SanSerif", Font.BOLD, 22));
    }

    private void drawPasswordField(Boolean isCorrect)
    {

        password = new GPassword(p, 1700, 200, 150, 40);
        password.setFont(new Font("SanSerif", Font.BOLD, 28));
        password.setMaxWordLength(45);

        if (isCorrect)
        {
            this.password.setVisibleChar('*');
        }
        else
        {
            this.password.setVisibleChar(' ');
        }

    }


    private void drawOwnerInputFields(PApplet p)
    {
        G4P.setInputFont("monospaced", Font.BOLD, 23);
        ownerInputID = new GTextField(p, 840, 725, 40, 30);
        ownerInputID.setLocalColorScheme(9);
        ownerInputID.setNumeric(0, 16, 0);
        ownerInputID.setFocus(true);
        ownerInputID.setVisible(false);

        ownerInputQuantity = new GTextField(p, 985, 725, 40, 30);
        ownerInputQuantity.setLocalColorScheme(9);
        ownerInputQuantity.setNumeric(-10, 10, 0);
        ownerInputQuantity.setFocus(true);
        ownerInputQuantity.setVisible(false);

        ownerInputText = new GLabel(this.p, 780, 690, 700, 100, "");
        ownerInputText.setFont(new Font("monospaced", Font.BOLD, 24));
        ownerInputText.setVisible(false);
    }

    private void hideOwnerInputFields()
    {
        addQuantityBtn.setVisible(false);
        ownerInputID.setVisible(false);
        ownerInputQuantity.setVisible(false);
        ownerInputText.setVisible(false);
    }

    // draws all the labels (text) on the screen
    private void drawHeaderOnScreen()
    {
        header = new GLabel(this.p, 760, 140, 700, 100, "");
        header.setFont(new Font("monospaced", Font.BOLD, 19));
        header.setText("");
    }

    private void drawNameLabelOnScreen()
    {
        nameLabel = new GLabel(this.p, 840, 25, 200, 700, " ");
        nameLabel.setFont(new Font("monospaced", Font.BOLD, 18));
        nameLabel.setText("");
    }

    private void drawPriceLabelOnScreen()
    {
        priceLabel = new GLabel(this.p, 1070, 25, 400, 700, " ");
        priceLabel.setFont(new Font("monospaced", Font.BOLD, 18));
        priceLabel.setText("");
    }

    private void drawQuantityOnScreen()
    {
        quantityLabel = new GLabel(this.p, 1230, 25, 400, 700, " ");
        quantityLabel.setFont(new Font("monospaced", Font.BOLD, 18));
        quantityLabel.setText("");
    }

    private void drawIdLabelOnScreen()
    {
        idLabel = new GLabel(this.p, 760, 25, 400, 700, "");
        idLabel.setFont(new Font("monospaced", Font.BOLD, 18));
        idLabel.setText("");
    }

    private void drawIncomeLabelOnScreen()
    {
        incomeLabel = new GLabel(this.p, 1220, 25, 400, 700, " ");
        incomeLabel.setFont(new Font("monospaced", Font.BOLD, 18));
        incomeLabel.setText("");
    }

    private void drawSoldLabelOnScreen()
    {
        soldLabel = new GLabel(this.p, 1070, 25, 400, 700, " ");
        soldLabel.setFont(new Font("monospaced", Font.BOLD, 18));
        soldLabel.setText("");
    }

    private void drawTotalLabelOnScreen()
    {
        totalLabel = new GLabel(this.p, 760, 540, 700, 150, "");
        totalLabel.setFont(new Font("monospaced", Font.BOLD, 19));
        totalLabel.setText("");
    }

    private void drawInfoToUpdateLabelOnScreen()
    {
        infoToUpdateLabel = new GLabel(this.p, 760, 540, 700, 150, "");
        infoToUpdateLabel.setFont(new Font("monospaced", Font.BOLD, 18));
        infoToUpdateLabel.setText("");
    }

    private void drawUpdateLabelOnScreen()
    {
        updatedLabel = new GLabel(this.p, 760, 750, 700, 150, "");
        updatedLabel.setFont(new Font("monospaced", Font.BOLD, 16));
        updatedLabel.setText("");
    }

    private void showAllLabelsAndBtnsOnScreen()
    {
        drawInventoryBtn();
        drawIncomeBtn();
        drawAddItemsBtn();
        drawLogOutBtn();
        drawHeaderOnScreen();
        drawIdLabelOnScreen();
        drawNameLabelOnScreen();
        drawPriceLabelOnScreen();
        drawQuantityOnScreen();
        drawSoldLabelOnScreen();
        drawIncomeLabelOnScreen();
        drawTotalLabelOnScreen();
        drawUpdateLabelOnScreen();
        drawInfoToUpdateLabelOnScreen();

    }

    public void hideAllLabels()
    {
        header.setText("");
        idLabel.setText("");
        nameLabel.setText("");
        incomeLabel.setText("");
        priceLabel.setText("");
        quantityLabel.setText("");
        soldLabel.setText("");
        totalLabel.setText("");
        infoToUpdateLabel.setText("");
        updatedLabel.setText("");
        TextUI.bigText.setText("");
        TextUI.dkrText.setText("");
        TextUI.balanceText.setText("");
        TextUI.smallText.setText("");
        TextUI.balance.setVisible(false);

    }
}