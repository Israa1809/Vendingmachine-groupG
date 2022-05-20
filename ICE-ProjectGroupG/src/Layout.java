
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class Layout extends PApplet
{
    private final PApplet p;
    private int spaceY;
    private final int white = 255;
    private final int darkGrey = 50;
    private final int black = 0;
    private final int limeGreen = color(162, 216, 46);
    private final int green = color(73, 180, 44);

    public Layout(PApplet p)
    {
        this.p = p;
    }

    public void display()
    {
        vendingMachineLayout();
    }


    private void vendingMachineLayout()
    {
        p.background(limeGreen);
        drawOuterLayout();
        drawWhiteSquareForInfoText();
        drawLinesToStandOn();
    }

    private void drawOuterLayout()
    {
        drawStandRect();
        drawOuterRect();
        drawInnerRect();
        drawPayRect();
        drawGetSodaRect();
    }

    private void drawStandRect()
    {
        int standX = 100;
        int standY = 690;
        int standW = 450;
        int standH = 100;
        p.fill(darkGrey);
        p.rect(standX, standY, standW, standH); // stand on square
    }

    private void drawOuterRect()
    {
        int outerX = 100;
        int outerY = 100;
        int outerW = 600;
        int outerH = 850;
        int outerCor = 15;

        p.fill(black);
        p.rect(outerX, outerY, outerW, outerH, outerCor); // outer big square
    }

    private void drawInnerRect()
    {
        int innerX = 150;
        int innerY = 150;
        int innerW = 390;
        int innerH = 600;
        int innerCor = 5;

        p.fill(white);
        p.rect(innerX, innerY, innerW, innerH, innerCor); // inner square
    }

    private void drawPayRect()
    {
        int payX = 570;
        int payY = 250;
        int payW = 100;
        int payH = 150;

        p.fill(darkGrey);
        p.strokeWeight(8);
        p.stroke(darkGrey);
        p.rect(payX, payY, payW, payH); // paying square
        p.strokeWeight(1);
        p.stroke(black);
        p.rect(payX, payY, payW, payH); // paying square
        p.strokeWeight(1);
        p.stroke(black);
        p.fill(green);
    }

    private void drawGetSodaRect()
    {
        int getX = 230;
        int getY = 800;
        int getW = 230;
        int getH = 100;
        int getCor = 5;
        int getDiff = 5;
        int grey = 175;
        int shadowGrey = 120;

        p.fill(grey);
        p.rect(getX, getY, getW, getH, getCor); // get your things square1
        p.fill(shadowGrey);
        p.rect(getX + getDiff, getY + getDiff, getW - getDiff * 2, getH - getDiff * 2, getDiff); // get your things square2
        p.fill(white);
    }

    private void drawLinesToStandOn()
    {
        int x = 165;
        int y = 290;
        int x2 = 525;
        int yDiff = 20;
        int xDiff = 90;

        for (int j = 0; j < 4; j++)
        {
            for (int i = 0; i < 5; i++)
            {
                p.line(x, y + spaceY, x2, y + spaceY);
                p.line(x + i * xDiff, y - yDiff + spaceY, x + i * xDiff, y + spaceY);
            }
            this.spaceY = spaceY + 150;
        }
    }

    private void drawWhiteSquareForInfoText()
    {
        int x = 750;
        int y = 160;
        int extent = 550;

        p.fill(white);
        p.rect(x, y, extent, extent + 240);
    }

    public void drawSoldOutLabels(PImage soldOut)
    {
        int rectX = 150;
        int rectY = 170;
        int rectW = 30 + 50;
        int rectH = 60 + 50;
        int spaceX = 20;
        int spaceY = 0;
        int i = 0;


        for (int j = 0; j < 16; j++)
        {
            p.image(soldOut, rectX + spaceX, rectY + 30 + spaceY, rectW, rectH - 70);
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

}