/* Keagan Chasenski
 * CHSKEA001
 * Prac2 - FlowThread Class
 * 21/09/20
*/
//model

/**
 * Handles the threads and their respective calculations.
 * Calls the water object and returns the surface height of current water block and the next block it is moving to
 * Block is then repainted in FlowPanel (outputs the coordinates)
 */
public class FlowThread extends java.lang.Thread
{
    //variables
    private int start;
    private int end;
    private int[][] waterDigits;

    Terrain land;
    Water water;
    FlowPanel fp;

    /**
     * FlowThread constructor
     * @param start_
     * @param end_
     * @param water_
     * @param terrain_
     * @param fp_
     */
    FlowThread(int start_, int end_, Water water_, Terrain terrain_, FlowPanel fp_)
    {
        fp = fp_;
        land = terrain_;
        water = water_;
        start = start_;
        end = end_;
    }

    /**
     * Finds the lowest point around the current water block
     * @param point
     * @param pointWater
     * @return
     */
    public synchronized int[] findLowest(int[] point, int pointWater)
    {
        float mainPointHeight = land.height[point[0]][point[1]] + (((float)0.01)*(float) pointWater);

        // Sets values to -1 so can never be miscalculated
        int[] ans = new int[]{-1, -1};


        float max = 0 ;

        for (int i = point[0] -1; i < point[0] +2; i++)
        {
            for (int j = point[1] -1; j < point[1] +2; j++)
            {
                if (i < land.getDimX() && j < land.getDimY() && j >= 0 && i >= 0  && !(point[0] == i && point[1] == j))
                {
                    int templandData = waterDigits[i][j];
                    float diff = mainPointHeight - land.height[i][j] + (float) 0.01 * (float) templandData ;
                    if (diff >= 0.01)
                    {
                        if (diff > max)
                        {
                            max = diff;
                            ans[0] = i;
                            ans[1] = j;
                        }
                    }
                }
            }
        }
        return ans;
    }

    /**
     * Synchronizes the water call
     * Calls getPermute method to randomise the comparing of neighbouring points
     * Calls the moveWater() method.
     * Repaints the canvas
     */
    public void run()
    {

        synchronized(water)
        {
            waterDigits = water.copyWaterArray();
        }

        for (int i = start; i < end; i++)
        {
            int[] point = new int[2];
            land.getPermute(i, point);
            int pointWater = waterDigits[point[0]][point[1]];

            if(pointWater > 0)
            {
                int[] newPoint = findLowest(point, pointWater);
                synchronized(water)
                {
                    water.moveUnit(point, newPoint);
                }
            }
        }

        water.deriveImage();

        //repaints canvas
        fp.repaint();
    }
}

