package data.simulation.front;

import java.awt.Choice;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Creates the panel that contains all of the toolbar items (start, stop, etc)
 * 
 * @author Stefan Ceballos
 * @date 7-14-15
 */
public class Toolbar extends JPanel {
	private static final long serialVersionUID = 1L;

	// static variables to keep track of each component
	// if new components are added to toolbar or rearranged these numbers will
	// need to be changed
	public final static int START_BUTTON = 9;
	public final static int STOP_BUTTON = 10;
	public final static int EVENT_BUTTON = 11;
	public final static int EVENT_DROP_DOWN = 1;
	public final static int AREA_DROP_DOWN = 3;
	public final static int PMU_DROP_DOWN = 5;
	public final static int VARIANCE_1 = 7;
	public final static int VARIANCE_2 = 8;

	// labels
	private JLabel eventTypeLabel;
	private JLabel areaLabel;
	private JLabel numberOfPmuLabel;
	private JLabel noiseLabel;

	// choice drop down menus
	private Choice eventTypeDropDown;
	private Choice areaDropDown;
	private Choice numberOfPmuDropDown;

	// text boxes for variance
	private JTextField lowNoiseTextBox;
	private JTextField highNoiseTextBox;

	// buttons
	private JButton startButton;
	private JButton stopButton;
	private JButton createEventButton;

	// private int toolbarValueX;
	// private int toolbarValueY;
	// private int pmuNumberFlag;

	// colors
	private Color boxColor;
	private Color buttonColor;
	
	// instance to self
	private Toolbar tools = this;

	// private Color titleRectColor;
	// private Color graphBottomColor;
	// private Color graphTopColor;

	// private boolean hasRendered = false;

	/**
	 * Contructor of Toolbar
	 * 
	 * initializes all of the components and adds them to the toolbar using a
	 * flowlayout
	 */
	public Toolbar() {
		super();
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		boxColor = new Color(230, 230, 184);
		buttonColor = new Color(255, 255, 102);
		// graphBottomColor = new Color(108, 123, 139);
		// graphTopColor = new Color(159, 182, 205);
		// titleRectColor = new Color(184, 230, 92);

		eventTypeLabel = new JLabel("Event Type:");
		eventTypeDropDown = new Choice();

		areaLabel = new JLabel("Area:");
		areaDropDown = new Choice();

		numberOfPmuLabel = new JLabel("Number of PMU:");
		numberOfPmuDropDown = new Choice();

		noiseLabel = new JLabel("Varience of Noise:");
		lowNoiseTextBox = new JTextField(5);
		highNoiseTextBox = new JTextField(5);

		// adding data to drop down
		eventTypeDropDown.add("-----");
		eventTypeDropDown.add("Fault");
		eventTypeDropDown.add("Generation Loss");
		eventTypeDropDown.add("Load Switch Off");
		eventTypeDropDown.add("Load Switch On");
		eventTypeDropDown.add("Reactive Power Excluded");
		eventTypeDropDown.add("Reactive Power Introduced");
		eventTypeDropDown.add("Synchronous Motor Switching Off");
		eventTypeDropDown.add("Series Capacitor Switching Off");
		eventTypeDropDown.add("Series Capacitor Switching On");

		// adding data to drop down
		areaDropDown.add("ALBERTA    -- 54");
		areaDropDown.add("ARIZONA    -- 14");
		areaDropDown.add("B.C. HYDRO -- 50");
		areaDropDown.add("EL PASO    -- 11");
		areaDropDown.add("IDAHO      -- 60");
		areaDropDown.add("IMPERIALCA -- 21");
		areaDropDown.add("LADWP      -- 26");
		// areaDropDown.add("MEXICO-CFE -- 20");
		areaDropDown.add("MONTANA    -- 62");
		areaDropDown.add("NEVADA     -- 18");
		areaDropDown.add("NEW MEXICO -- 10");
		areaDropDown.add("NORTHWEST  -- 40");
		areaDropDown.add("PACE       -- 65");
		areaDropDown.add("PG AND E   -- 30");
		areaDropDown.add("PSCOLORADO -- 70");
		areaDropDown.add("SANDIEGO   -- 22");
		areaDropDown.add("SIERRA     -- 64");
		areaDropDown.add("SOCALIF    -- 24");
		areaDropDown.add("FORTISBC   -- 52");
		areaDropDown.add("WAPA R.M.  -- 73");
		// areaDropDown.add("WAPA U.M.  -- 63");

		// adding data to drop down
		numberOfPmuDropDown.add("10");
		numberOfPmuDropDown.add("9");
		numberOfPmuDropDown.add("8");
		numberOfPmuDropDown.add("7");
		numberOfPmuDropDown.add("6");
		numberOfPmuDropDown.add("5");
		numberOfPmuDropDown.add("4");
		numberOfPmuDropDown.add("3");
		numberOfPmuDropDown.add("2");
		numberOfPmuDropDown.add("1");

		startButton = new JButton("Start");
		startButton.setFocusPainted(false);
		stopButton = new JButton("Stop");
		stopButton.setFocusPainted(false);
		createEventButton = new JButton("Disturbance");
		createEventButton.setFocusPainted(false);
		createEventButton.setEnabled(false);
		stopButton.setEnabled(false);

		eventTypeDropDown.setBackground(boxColor);
		areaDropDown.setBackground(boxColor);
		numberOfPmuDropDown.setBackground(boxColor);

		startButton.setBackground(Color.green);
		stopButton.setBackground(Color.red);
		createEventButton.setBackground(buttonColor);

		startButton.setForeground(Color.black);
		stopButton.setForeground(Color.black);

		// adding components to the panel
		this.add(eventTypeLabel);
		this.add(eventTypeDropDown);

		this.add(areaLabel);
		this.add(areaDropDown);

		this.add(numberOfPmuLabel);
		this.add(numberOfPmuDropDown);

		this.add(noiseLabel);
		this.add(lowNoiseTextBox);
		this.add(highNoiseTextBox);

		this.add(startButton);
		this.add(stopButton);
		this.add(createEventButton);

		// listeners get initialized here so that when the toolbar is
		// constructed there in only one instance of the listeners
		startButton.addActionListener(new ToolbarListener(
				ToolbarListener.START_BUTTON, tools));
		stopButton.addActionListener(new ToolbarListener(
				ToolbarListener.STOP_BUTTON, tools));
		createEventButton.addActionListener(new ToolbarListener(
				ToolbarListener.CREATE_EVENT_BUTTON, tools));

	}

	// debugging purposes
	/*
	 * public void paintComponent(Graphics g) { // if(!hasRendered){ //
	 * hasRendered = true; System.out.println("paintComponent() CALLED");
	 * setButtonPositions(g); // addButtonListeners(); // } }
	 */
}