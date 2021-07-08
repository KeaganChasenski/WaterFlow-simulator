/* Keagan Chasenski
 * CHSKEA001
 * Prac2 - Water Class
 * 17/09/20
*/
//model


import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 */
public class Water extends Terrain
{

    private int dimx;
    private int dimy;
    private int[][] water;

    /**
     * Water constructor
     * @param dimx
     * @param dimy
     */
    public Water(int dimx, int dimy)
    {
        water = new int[dimx][dimy];
        this.dimx = dimx;
        this.dimy = dimy;
    }

    //Accessor methods
    int getDimX(){ return dimx; }
    int getDimY(){ return dimy; }


    BufferedImage img;

    /**
     * Creates bufferedImage for water
     * @return
     */
    public BufferedImage getImage()
    {
        return img;
    }

    /**
     * Creates a copy of the waterArray to use for safe handling of data
     * @return
     */
    public int[][] copyWaterArray()
    {
        int[][] WaterArrayCopy = new int[dimx][dimy];
        System.arraycopy(water, 0, WaterArrayCopy, 0, water.length);
        return WaterArrayCopy;
    }

    /**
     * Copied from the terrain file
     * Checks that water has reached the boundary and clears it. (Changes colour)
     */
    public void deriveImage()
    {
        img = new BufferedImage(dimy, dimx, BufferedImage.TYPE_INT_ARGB);
        for(int x = 0; x < dimx; x++)
        {
            for(int y = 0; y < dimy; y++)
            {
                if (water[x][y] == 0)
                {
                    Color col = new Color(0,0,0,0);
                    img.setRGB(x,y,col.getRGB());
                }
                else{
                    Color col = new Color(0,0,200);
                    img.setRGB(x, y, col.getRGB());
                }
            }
        }
    }


    /**
     * Adds water to the array from a mouse click
     * Accounts for the 9x9 size of a pixel block in the for loop
     * Calls the deriveImage method()
     * @param x
     * @param y
     */
    public void addWater(int x, int y)
    {
        // accounts for the size of 9x9 pixel block
        for (int xax = x -3; xax < x + 4; xax++)
        {
            for (int yax = y - 3; yax < y + 4; yax++)
            {
                if (!(xax <0 || yax <0 || xax >= dimx || yax >= dimy ))
                {
                    water[xax][yax] = water[xax][yax] + 3;
                }
            }
        }
        deriveImage();
    }

    /**
     * Clears and water from the water array when reset is needed
     * Sets all values to 0 through for loop
     * repaints the canvas by calling the deriveImage() method
     */
    public void removeWater()
    {
        for (int i = 0; i < dimx; i++)
        {
            for (int j=0; j < dimy; j++)
            {
                water[i][j] = 0;
            }
        }
        deriveImage();
    }

    /**
     * Takes in array of current point and array of point moving to.
     * Moves the water to the correct block.
     * @param from
     * @param to
     */
    public void moveUnit(int[] from, int[] to)
    {
        if (!(to[0] == -1 || to[1] == -1 || water[from[0]][from[1]] == 0))
        {
            water[from[0]][from[1]] = water[from[0]][from[1]] - 1 ;
            water[to[0]][to[1]] = water[to[0]][to[1]] + 1 ;
            //tcounter ++ ;
            //System.out.println(tcounter);
        }
    }
}
