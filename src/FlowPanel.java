/* Keagan Chasenski
 * CHSKEA001
 * Prac2 - FlowPanel Class
 * 17/09/20
 */
//view

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Responsible for creating the GUI (View)
 */
public class FlowPanel extends JPanel implements Runnable
{
    Terrain land;
    Water water ;

    int x ;
    int y ;
    int first ;
    int arrSize ;

    /**
     * Terrain Constructor
     * @param terrain
     * @param water
     * @param x
     * @param y
     * @param first
     * @param arrSize
     */
    FlowPanel(Terrain terrain, Water water, int x, int y, int first, int arrSize)
    {
        land=terrain;
        this.water = water ;
        this.x = x ;
        this.y = y ;
        this.first =  first ;
        this. arrSize = arrSize ;
    }

    /**
     * Creates the GUI with both terrain and water layer
     * @param g
     */
    protected synchronized void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        // draw the landscape in greyscale as an image
        if (land.getImage() != null)
        {
            g.drawImage(land.getImage(),0,0,null) ;
        }
        if (water.getImage() != null)
        {
            g.drawImage(water.getImage(),0 , 0, null) ;
        }
    }

    /**
     * Synchronized method
     * Runs by repainting the canvas and waiting 100 milliseconds
     */
    public synchronized void run()
    {
        try
        {
            repaint() ;
            Thread.sleep(100);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }

}