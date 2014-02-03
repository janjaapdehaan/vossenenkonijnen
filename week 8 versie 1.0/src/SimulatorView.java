import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location 
 * representing its contents. It uses a default background color.
 * Colors for each type of species can be defined using the
 * setColor method.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class SimulatorView extends JFrame
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Colors used for empty locations.
    private static final Color EMPTY_COLOR = Color.white;

    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private JLabel stepLabel, population;
    private FieldView fieldView;

    //Periode Week 7
    private JFrame frame;
    private static Simulator sim;
    private JButton buttonView1;
    private JButton buttonView2;
    private JPanel leftPanel;
    private JPanel rightPanel;
    //

    // A map for storing colors for participants in the simulation
    private Map<Class, Color> colors;
    // A statistics object computing and storing simulation information
    private FieldStats stats;
    
    Thread thread1;

    public static void main(String[] args)
    {
        //Gecambieerd pa private static variable
        sim = new Simulator();
        sim.simulate(0);
    }
    /**
     * Create a view of the given width and height.
     * @param height The simulation's height.
     * @param width  The simulation's width.
     */
    public SimulatorView(int height, int width)
    {
        stats = new FieldStats();
        colors = new LinkedHashMap<Class, Color>();

        frame = new JFrame("Vossen en Konijnen");
        
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);

        
        JPanel leftMenu = new JPanel();
        leftMenu.setLayout(new GridLayout(0,1));
        
        buttonView1 = new JButton("Step 1");
        buttonView1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sim.simulateOneStep();
            }
        });
        buttonView2 = new JButton("Step 100");
        buttonView2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sim.simulate(100);
            }
        });
        
        
        final JTextField numberOfSteps = new JTextField();
        JButton simulateButton = new JButton("Simuleer");
        simulateButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try{
        		int input = Integer.parseInt(numberOfSteps.getText());
        		if(input > 0) {
        			sim.simulate(input);
        		}
        		else { makePopupMessage("Je kunt alleen stappen vooruit doen");
        		}
        		
        	} 
        		catch(NumberFormatException exception) {
    			makePopupMessage("Voer eerst een getal in.");
    		}
        }
  });
        
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		sim.reset();
        	}
        });
        
        JLabel simulate = new JLabel("Simulatie");
        leftMenu.add(simulate);
        leftMenu.add(buttonView1);
        leftMenu.add(buttonView2);
        leftMenu.add(numberOfSteps);
        leftMenu.add(simulateButton);
        leftMenu.add(resetButton);
        
        leftMenu.add( new JLabel() );
        
      //Flow for spacing
        JPanel flow = new JPanel(new BorderLayout());
        flow.add(leftMenu, BorderLayout.NORTH);
        
        //making GUI frame
        makeGUIMenu(frame);
       	  
       
        JPanel fieldViewLayout = new JPanel(new BorderLayout());
        fieldView = new FieldView(height, width);
        fieldViewLayout.add(stepLabel, BorderLayout.NORTH);
        fieldViewLayout.add(fieldView, BorderLayout.CENTER);
        fieldViewLayout.add(population, BorderLayout.SOUTH);
        //Here the explanation for the foxes and rabbits is made
        JPanel southinfo = new JPanel(new GridLayout(0,1));
        
        
       
      
        //Adding everything to the container
        Container contents = frame.getContentPane();
        contents.add(fieldViewLayout, BorderLayout.CENTER);
        contents.add(flow, BorderLayout.WEST);
        contents.add(southinfo, BorderLayout.SOUTH);
        
        frame.pack();
        
        //Making sure for a centered position on the screen
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(d.width/2 - frame.getWidth()/2, d.height/2 - frame.getHeight()/2);
        
        //Showing the results
        frame.setResizable(false);
        frame.setVisible(true);
    }
        
        
    
    private void makeGUIMenu(JFrame frame)
    {
    	
    	//The main menu bar.
    	JMenuBar menu = new JMenuBar(); 
    	frame.setJMenuBar(menu);
    	
    	//Menu number 1
    	JMenu menu1 = new JMenu("File");
    	
    	JMenuItem menuItem = new JMenuItem("Quit");
    	menuItem.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			System.exit(0);
    		}
    	});
    	
    	
    	menu1.add(menuItem);
    	menu.add(menu1);
    	
    	//Making the help menu
    	JMenu helpMenu = new JMenu("Help");
    	JMenuItem helpItem = new JMenuItem("Help me!");
    	helpItem.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) { //Popup help menu message.
    			makePopupMessage("Deze applicatie werkt simpel. \n Je kan een stap vooruit, 100 stappen vooruit of een andere getal invoeren en dan op simuleer klikken.");
    		}
    	});
    	helpMenu.add(helpItem);
    	menu.add(helpMenu);
    	
    }
    
    private void makePopupMessage(String message)
    {
    	JOptionPane.showMessageDialog(frame, 
                message,"",
                 JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Define a color to be used for a given class of animal.
     * @param animalClass The animal's Class object.
     * @param color The color to be used for the given class.
     */
    public void setColor(Class animalClass, Color color)
    {
        colors.put(animalClass, color);
    }

    /**
     * @return The color to be used for a given class of animal.
     */
    private Color getColor(Class animalClass)
    {
        Color col = colors.get(animalClass);
        if(col == null) {
            // no color defined for this class
            return UNKNOWN_COLOR;
        }
        else {
            return col;
        }
    }

    /**
     * Show the current status of the field.
     * @param step Which iteration step it is.
     * @param field The field whose status is to be displayed.
     */
    public void showStatus(int step, Field field)
    {
        if(!isVisible()) {
            setVisible(true);
        }

        stepLabel.setText(STEP_PREFIX + step);
        stats.reset();

        fieldView.preparePaint();

        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Object animal = field.getObjectAt(row, col);
                if(animal != null) {
                    stats.incrementCount(animal.getClass());
                    fieldView.drawMark(col, row, getColor(animal.getClass()));
                }
                else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Determine whether the simulation should continue to run.
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field)
    {
        return stats.isViable(field);
    }

    /**
     * Provide a graphical view of a rectangular field. This is 
     * a nested class (a class defined inside a class) which
     * defines a custom component for the user interface. This
     * component displays the field.
     * This is rather advanced GUI stuff - you can ignore this 
     * for your project if you like.
     */
    private class FieldView extends JPanel
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width)
        {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize()
        {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                    gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint()
        {
            if(! size.equals(getSize())) {  // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if(yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }

        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color)
        {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        public void paintComponent(Graphics g)
        {
            if(fieldImage != null) {
                Dimension currentSize = getSize();
                if(size.equals(currentSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                }
                else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }
}
