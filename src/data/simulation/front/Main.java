package data.simulation.front;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import data.simulation.back.DbConf;

/**
 * The Main class sets up the dimensions of the JFrame and acts as a canvas to
 * apply all the JPanels
 * 
 * @author Stefan Ceballos
 * @author Brett Pelkey
 * @date 7-14-15
 */
// added extends for applet launching in browser
public class Main {

	// insets set the space between all the components
	private static final Insets insets = new Insets(12, 12, 12, 12);
	private static JPanel innerFrame; // panel that all sub panels are snapped
										// to
	private static ColorFrame cFrame; // panel that displays line colors
	private static VoltageFrame vFrame; // panel that displays the voltage graph
	private static FrequencyFrame fFrame; // panel that displays the frequency
											// graph
	private static DisturbanceFrame dFrame; // panel that displays the
											// disturbance frame
	private static Toolbar toolbar; // contains all the JButtons
	final static JFrame applicationFrame = new JFrame(); // application frame
															// that contains
															// innerFrame and
															// toolbar

	// global variables to open connection of db and keep open for duration of
	// applet
	// connection gets closed only when the frame is closed by the exit button
	// will not close conn when end program is clicked from within eclipse
	public static Connection dbConnection = null; //
	public static PreparedStatement preparedStatement = null; //

	public static DbConf dbConf;

	private static boolean connected = false;

	// added for applet
	/*
	 * public void init() { start(); }
	 */

	/**
	 * Main is the starting point of the application and will open a connection
	 * to the database first and then set up the application frame which will
	 * hold all of the inner components
	 * 
	 * @param args
	 *            param args not used
	 */
	// changed from static void main(String[] args) to start() for applet
	public static void main(String[] args) {
		dbConf = new DbConf();
		dbConf = dbConf.getPropValues("/path/to/.properties/file");
		// start connection of database
		try {
			/*
			 * try { Class.forName(JDBC_DRIVER); } catch (ClassNotFoundException
			 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
			 */
			System.out.println("Connecting to database...");
			System.out.println("Please wait...");
			if (dbConf != null) {
				dbConnection = DriverManager.getConnection(dbConf.url, dbConf.userName, dbConf.password);
				System.out.println("DB CONNECTION OPENED");
				connected = true;
			} else
				System.out.println("Could not get DB config file... Check internet conncetion");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Launching applet...");
		int toolsHeight = 25;
		int appWidth = 1244;
		int appHeight = toolsHeight + 819;

		applicationFrame.setPreferredSize(new Dimension(appWidth, appHeight));
		applicationFrame.setTitle("Phasor Measurement Unit Display");
		applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// comment out this next line if your screen res is larger than
		// 1920x1080
		// applicationFrame.setResizable(false);
		applicationFrame.getContentPane().setLayout(new BorderLayout());

		toolbar = new Toolbar();
		Main.setInnerFrame(new JPanel());
		Main.getInnerFrame().setLayout(new GridBagLayout());
		Main.getInnerFrame().setBackground(new Color(190, 190, 190));

		applicationFrame.add(toolbar, BorderLayout.NORTH);

		cFrame = new ColorFrame(GridBagConstraints.NORTHEAST);
		vFrame = new VoltageFrame(GridBagConstraints.NORTHWEST);
		fFrame = new FrequencyFrame(GridBagConstraints.SOUTHWEST);
		dFrame = new DisturbanceFrame(GridBagConstraints.SOUTHEAST);

		applicationFrame.add(innerFrame, BorderLayout.CENTER);

		applicationFrame.pack();
		applicationFrame.setLocationRelativeTo(null);
		applicationFrame.setVisible(true);
		innerFrame.revalidate();
		applicationFrame.validate();
		System.out.println("Applet launched");
		if (!connected) {
			JOptionPane.showMessageDialog(Main.getInnerFrame(),
					"Could not connect to DB...\nCheck internet connection.", "Connection Error",
					JOptionPane.ERROR_MESSAGE);
		}
		applicationFrame.addWindowListener(new WindowAdapter() {

			/*
			 * Window listener to listen to when the window is closed via the x
			 * button. When the window is closed the connection the database is
			 * also closed
			 */

			public void windowClosing(WindowEvent evt) {
				if (dbConnection != null) {
					try {
						dbConnection.close();
						System.out.println("DB CONNECTION CLOSED");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		/*
		 * Main a = new Main(); a.setUpGui();
		 */

	}

	/*
	 * private void setUpGui() {
	 * 
	 * }
	 */

	// the weight is the key to making components not snap to center
	/**
	 * This method will add a component to a container that uses GridBagLayout
	 * 
	 * 
	 * @param container
	 *            the container to add the component to
	 * @param component
	 *            the component to add to the container
	 * @param gridx
	 *            an int determining where in the grid on the x axis you want
	 *            the component
	 * @param gridy
	 *            an int determining where in the grid on the y axis you want
	 *            the component
	 * @param gridwidth
	 *            the width the component should take up in the grid
	 * @param gridheight
	 *            the height the component should take up in the grid
	 * @param anchor
	 *            the position in the grid (e.g. north, northwest, etc)
	 * @param fill
	 *            the fill of the component (e.g. both, horizontal, etc)
	 * @param weightX
	 *            the weight of the component on the x axis
	 * @param weightYthe
	 *            weight of the component on the y axis
	 */
	public static void addComponent(Container container, Component component, int gridx, int gridy, int gridwidth,
			int gridheight, int anchor, int fill, double weightX, double weightY) {
		// create the grid bag constraints
		GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightX, weightY, anchor,
				fill, insets, 0, 0);
		// add the component
		container.add(component, gbc);
	}

	/**
	 * Returns the inner frame
	 * 
	 * @return JPanel innerFrame
	 */
	public static JPanel getInnerFrame() {
		return innerFrame;
	}

	/**
	 * Sets the innerFrame
	 * 
	 * @param innerFrame
	 *            JPanel that all other inner JPanels are snapped to
	 */
	public static void setInnerFrame(JPanel innerFrame) {
		Main.innerFrame = innerFrame;
	}

	/**
	 * Returns the cFrame
	 * 
	 * @return cFrame
	 */
	public static ColorFrame getColorFrame() {
		return cFrame;
	}

	/**
	 * Sets the cFrame
	 * 
	 * @param cFrame
	 */
	public static void setColorFrame(ColorFrame cFrame) {
		Main.cFrame = cFrame;
	}

	/**
	 * Returns the vFrame
	 * 
	 * @return vFrame
	 */
	public static VoltageFrame getvFrame() {
		return vFrame;
	}

	/**
	 * Sets the vFrame
	 * 
	 * @param vFrame
	 */
	public static void setvFrame(VoltageFrame vFrame) {
		Main.vFrame = vFrame;
	}

	/**
	 * Returns the fFrame
	 * 
	 * @return fFrame
	 */
	public static FrequencyFrame getfFrame() {
		return fFrame;
	}

	/**
	 * Sets the fFrame
	 * 
	 * @param fFrame
	 *            the frame to set
	 */
	public static void setfFrame(FrequencyFrame fFrame) {
		Main.fFrame = fFrame;
	}

	/**
	 * Returns the dFrame
	 * 
	 * @return dFrame
	 */
	public static DisturbanceFrame getdFrame() {
		return dFrame;
	}

	/**
	 * Sets the dFrame
	 * 
	 * @param dFrame
	 *            the frame to set
	 */
	public static void setdFrame(DisturbanceFrame dFrame) {
		Main.dFrame = dFrame;
	}

	/**
	 * Returns toolbar
	 * 
	 * @return toolbar
	 */
	public static Toolbar getToolbar() {
		return toolbar;
	}

	/**
	 * Sets the toolbar
	 * 
	 * @param toolbar
	 *            the toolbar to set
	 */
	public static void setToolbar(Toolbar toolbar) {
		Main.toolbar = toolbar;
	}
}
