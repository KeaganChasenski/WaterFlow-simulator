/* Keagan Chasenski
 * CHSKEA001
 * Prac2 - Flow Class
 * 17/09/20
 */

//controller
import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.BorderLayout;

public class Flow
{
    static long startTime = 0;
    static int frameX;
    static int frameY;
    static FlowPanel fp;

    public static int x;
    public static int y;
    public static boolean play = false ;
    public static int tcounter = 0 ;

    // Threads
    private static Thread t1 ;
    private static Thread t2 ;
    private static Thread t3 ;
    private static Thread t4 ;


    // had to creata a Label variable in order to create a method to constantly updated
    private static JLabel counterLabel = new JLabel(" ");

    /*static ArrayList<Integer> xArray = new ArrayList<Integer>() ;
    static ArrayList<Integer> yArray = new ArrayList<Integer>() ;

    //static Water[] waterArray ;
    static ArrayList<Water> waterArray = new ArrayList<Water>() ;*/


    // start timer
    private static void tick()
    {
        startTime = System.currentTimeMillis();
    }

    // stop timer, return time elapsed in seconds
    private static float tock()
    {
        return (System.currentTimeMillis() - startTime) / 1000.0f;
    }

    public static  synchronized void setupGUI(int frameX,int frameY,Terrain landdata, Water waterData)
    {

        Dimension fsize = new Dimension(800, 800);
        JFrame frame = new JFrame("Waterflow");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS));


        //create Terrain data
        fp = new FlowPanel(landdata, waterData, x,y,frameX,frameY);
        fp.setPreferredSize(new Dimension(frameX,frameY));
        g.add(fp);

        JPanel b = new JPanel();
        b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));

        // Added a new panel for the counter
        JPanel c = new JPanel();
        c.setLayout(new BoxLayout(c, BoxLayout.LINE_AXIS));

        // Mouse listener to get co ords
        fp.addMouseListener(new MouseAdapter()
        {
            //mouse listener activation
            public void mousePressed(MouseEvent e)
            {
                x = e.getX();
                y = e.getY();

                waterData.addWater(x,y);
                fp.repaint() ;
            }
        });

        //Add ActionListeners on those buttons
        JButton resetB = new JButton("Reset");;
        // add the listener to the jbutton to handle the "pressed" event
        resetB.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Flow.tcounter = 0 ;
                setCounterText(tcounter);
                waterData.removeWater() ;
                fp.repaint() ;
            }
        });

        JButton pauseB = new JButton("Pause");;
        // add the listener to the jbutton to handle the "pressed" event
        pauseB.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setPause() ;
            }
        });

        JButton playB = new JButton("Play");;
        // add the listener to the jbutton to handle the "pressed" event
        playB.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setPlay() ;
            }
        });

        JButton endB = new JButton("End");;
        // add the listener to the jbutton to handle the "pressed" event
        endB.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Flow.tcounter = 0 ;
                frame.dispose();
                System.exit(0) ;
            }
        });

        JLabel count = new JLabel("TimeStep Counter ");


        b.add(resetB) ;
        b.add(pauseB) ;
        b.add(playB) ;
        b.add(endB);

        c.add(count) ;
        c.add(Flow.counterLabel) ;

        g.add(c) ;
        g.add(b);

        frame.setSize(frameX, frameY+50);	// a little extra space at the bottom for buttons
        frame.setLocationRelativeTo(null);  // center window on screen
        frame.add(g); //add contents to window
        frame.setContentPane(g);
        frame.setVisible(true);
        Thread fpt = new Thread(fp);
        fpt.start();
    }

    /**
     * Creates terrain image from terrain class.
     * While loop to continuously run
     * If statement to control water flow on play and pause
     * Creates threads, and joins them from correct synchronisation
     * Updates the tcounter to ++ and calls method to Print to JLabel
     * @param args
     */
    public static void main(String[] args)
    {
        Terrain landdata = new Terrain();

        // check that number of command line arguments is correct
		if(args.length != 1)
		{
			System.out.println("Incorrect number of command line arguments. Should have form: java -jar flow.java intputfilename");
			System.exit(0);
		}

        //landdata.readData("/Users/keags/Documents/Varsity/4thYear/Term2/CSC/MyRepo/Docs/medsample_in.txt");
        landdata.readData(args[0]);

        frameX = landdata.getDimX();
        frameY = landdata.getDimY();
        Water waterData = new Water(frameX, frameY) ;
        SwingUtilities.invokeLater(()->setupGUI(frameX, frameY, landdata, waterData));
        // to do: initialise and start simulation

        while (true)
        {
            if (play)
            {
                // Creates 4 threads and passes 1/4 of the array to each.
                t1 = new FlowThread(0, landdata.dim()/4, waterData, landdata, fp) ;
                t2 = new FlowThread(landdata.dim()/4, landdata.dim()/2, waterData, landdata, fp) ;
                t3 = new FlowThread(landdata.dim()/2, 3*landdata.dim()/4, waterData, landdata, fp) ;
                t4 = new FlowThread(3*landdata.dim()/4, landdata.dim(), waterData, landdata, fp) ;

                t1.start();
                t2.start();
                t3.start();
                t4.start();

                try
                {
                    t1.join();
                    t2.join();
                    t3.join();
                    t4.join();
                    tcounter ++ ;

                    // Print tcounter to Jlabel method
                    Flow.setCounterText(tcounter) ;
                    fp.repaint();
                }
                catch (Exception e)
                {
                    System.out.println("error " + e);
                }
            }
            else
            {
                try
                {
                    Thread.sleep(100);
                }
                catch (Exception e)
                {

                }
            }
        }
    }

    /**
     * Sets play boolean to true
     */
    public static void setPlay()
    {
        play = true ;
    }
    /**
     * Sets play boolean to false
     */
    public static void setPause()
    {
        play = false ;
    }

    /** Param int tcounter
     * Forces the JLabel to be printed
     */
    public static void setCounterText(int tcounter)
    {
        if (SwingUtilities.isEventDispatchThread())
        {
            counterLabel.setText(tcounter + " ");
        }
        else
            {
            SwingUtilities.invokeLater(() ->
            {
                counterLabel.setText(tcounter + "");
            });
        }
    }

}