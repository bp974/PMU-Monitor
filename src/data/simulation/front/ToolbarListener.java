package data.simulation.front;

import java.awt.Choice;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * ToolbarListener is responsible for all of the actions that occur when the
 * user selects info in the toolbar
 * 
 * @author Brett Pelkey
 * @author Stefan Ceballos
 */
public class ToolbarListener implements ActionListener {

	public static Toolbar tools;// toolbar
	private int component;// component that has action
	public String eventType;
	public String areaId;
	public String numOfPmu;
	private double v1;
	private double v2;
	// array of all abbreviated event types
	private String[] eventTypeArray = { "FLT", "GNL", "LSF", "LSN", "SHF",
			"SHN", "SMS", "SRF", "SRN" };

	// flags
	public static boolean flag;
	public static boolean disturbanceFlag;
	private static boolean intervalFlag;
	private static String eventTypeFlag;
	private static String areaFlag;
	private static int numFlag;
	private static double vNoise1Flag;
	private static double vNoise2Flag;

	// constants to determine which button was pressed
	public static final int START_BUTTON = 0;
	public static final int STOP_BUTTON = 1;
	public static final int CREATE_EVENT_BUTTON = 2;

	/**
	 * Constructor of listener
	 * 
	 * @param component
	 *            the component that has an action
	 * @param tools
	 *            the toolbar in which the action occured in
	 */
	public ToolbarListener(int component, Toolbar tools) {
		this.component = component;
		ToolbarListener.tools = tools;
		ToolbarListener.flag = false;
		intervalFlag = false;
		disturbanceFlag = false;
	}

	// Accessors for PMU toolbar content
	/**
	 * Gets the number of pmu's selected in the pmu drop down menu
	 * 
	 * @return the number that is currently selected
	 */
	public static int getPMUnumber() {
		Choice numPMU = (Choice) tools.getComponent(5);
		return Integer.parseInt(numPMU.getSelectedItem());
	}

	/**
	 * Gets the area of the pmu that is selected in the area drop down
	 * 
	 * @return the area that is currently selected
	 */
	public static String getPMUArea() {
		Choice area = (Choice) tools.getComponent(3);
		return area.getSelectedItem().toString();
	}

	/**
	 * Gets the event type that is selected in the event drop down
	 * 
	 * @return the event type that is currently selected
	 */
	public static String getPMUEvent() {
		Choice event = (Choice) tools.getComponent(1);
		return event.getSelectedItem().toString();
	}

	/**
	 * Gets the value of the variance of the first text box
	 * 
	 * @return the value that is entered into the text box...0 if nothing is
	 *         entered
	 */
	public double getV1() {
		JTextField variance1 = (JTextField) tools.getComponent(7);
		if (variance1.getText().length() > 0)
			return Double.parseDouble(variance1.getText());
		return 0;
	}

	/**
	 * Gets the value of the variance of the second text box
	 * 
	 * @return the value that is entered into the text box...0 if nothing is
	 *         entered
	 */
	public double getV2() {
		JTextField variance2 = (JTextField) tools.getComponent(8);
		if (variance2.getText().length() > 0)
			return Double.parseDouble(variance2.getText());
		return 0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		// this method will detect which button has been selected and make
		// actions depending on the button
		v1 = v2 = 0;
		if (component == START_BUTTON) {
			tools.getComponent(Toolbar.START_BUTTON).setEnabled(false);
			tools.getComponent(Toolbar.EVENT_DROP_DOWN).setEnabled(false);
			tools.getComponent(Toolbar.AREA_DROP_DOWN).setEnabled(false);
			tools.getComponent(Toolbar.PMU_DROP_DOWN).setEnabled(false);
			tools.getComponent(Toolbar.VARIANCE_1).setEnabled(false);
			tools.getComponent(Toolbar.VARIANCE_2).setEnabled(false);
			tools.getComponent(Toolbar.EVENT_BUTTON).setEnabled(false);
			tools.getComponent(Toolbar.STOP_BUTTON).setEnabled(true);
			Main.getToolbar().repaint();

			System.out.println("\nStart button was pressed");
			System.out.println("Disturbance Flag = " + disturbanceFlag);

			// get the information that the user selected in the toolbar
			Choice event = (Choice) tools.getComponent(Toolbar.EVENT_DROP_DOWN);
			Choice area = (Choice) tools.getComponent(Toolbar.AREA_DROP_DOWN);
			Choice numPmu = (Choice) tools.getComponent(Toolbar.PMU_DROP_DOWN);
			JTextField variance1 = (JTextField) tools
					.getComponent(Toolbar.VARIANCE_1);
			JTextField variance2 = (JTextField) tools
					.getComponent(Toolbar.VARIANCE_2);
			if (variance1.getText().length() > 0)
				v1 = Double.parseDouble(variance1.getText());
			if (variance2.getText().length() > 0)
				v2 = Double.parseDouble(variance2.getText());
			eventType = event.getSelectedItem().toString();
			String areaName = area.getSelectedItem().toString();
			areaId = areaName.replaceAll("[^0-9]", "");
			areaName = areaName.substring(0, areaName.indexOf("-"));
			areaName = areaName.replaceFirst("\\s+$", "");
			numOfPmu = numPmu.getSelectedItem().toString();

			// for debugging purposes
			System.out.println("inside start: v1 = " + v1 + " , v2 = " + v2);
			if (disturbanceFlag == false)
				tools.getComponent(11).setEnabled(false);

			System.out.println("Interval Flag = " + intervalFlag);
			// abbreviate the event type so it can be passed to db via a sql
			// query
			String abbvEventType = "";
			// if ----- is selected create a random type based on the array of
			// event types
			if (eventType.equals("-----")) {
				Random rand = new Random();
				int randNum = rand.nextInt(eventTypeArray.length - 1);
				// System.out.println(randNum);
				abbvEventType = eventTypeArray[randNum];
			} else if (eventType.equals("Fault"))
				abbvEventType = "FLT";
			else if (eventType.equals("Generation Loss"))
				abbvEventType = "GNL";
			else if (eventType.equals("Load Switch Off"))
				abbvEventType = "LSF";
			else if (eventType.equals("Load Switch On"))
				abbvEventType = "LSN";
			else if (eventType.equals("Reactive Power Excluded"))
				abbvEventType = "SHF";
			else if (eventType.equals("Reactive Power Introduced"))
				abbvEventType = "SHN";
			else if (eventType.equals("Synchronous Motor Switching Off"))
				abbvEventType = "SMS";
			else if (eventType.equals("Series Capacitor Switching Off"))
				abbvEventType = "SRF";
			else if (eventType.equals("Series Capacitor Switching On"))
				abbvEventType = "SRN";

			// if first time that start button has been clicked
			// scheduled executions get started in GetDBDate.java
			if (!intervalFlag) {
				// get the info from the database
				GetDBData dbData = new GetDBData();
				try {
					dbData.getInfo(abbvEventType, areaId, areaName);
					if (dbData.getNumEvents() == 0)
						return;

					// schedule execution for the voltage frame
					VoltageFrame.scheduledExecutorService = Executors
							.newSingleThreadScheduledExecutor();
					VoltageFrame.future = VoltageFrame.scheduledExecutorService
							.scheduleAtFixedRate(VoltageFrame.runnable, 0, 5,
									TimeUnit.MILLISECONDS);

					// schedule execution for the frequency frame
					FrequencyFrame.scheduledExecutorService = Executors
							.newSingleThreadScheduledExecutor();
					FrequencyFrame.future = FrequencyFrame.scheduledExecutorService
							.scheduleAtFixedRate(FrequencyFrame.runnable, 0, 5,
									TimeUnit.MILLISECONDS);

				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			JButton stopButton = (JButton) tools.getComponent(10);
			stopButton.setText("Pause");
			// if second time the start button has been clicked
			if (intervalFlag) {
				// check if toolbar info is the same
				// if it is the same continue with the plot
				// otherwise start a new plot based on new info entered in the
				// toolbar
				if (!(eventTypeFlag.equals(getPMUEvent())
						&& areaFlag.equals(getPMUArea())
						&& numFlag == getPMUnumber() && vNoise1Flag == getV1() && vNoise2Flag == getV2())) {
					// user input has changes so shutdown scheduled executions
					VoltageFrame.scheduledExecutorService.shutdown();
					FrequencyFrame.scheduledExecutorService.shutdown();
					// set bools from voltage frame back to default
					VoltageFrame.eventDetected = false;
					VoltageFrame.classified = false;
					VoltageFrame.eventListInitialized = false;
					// reset the pmu order for the color frame
					for (int i = 0; i < 10; i++) {
						VoltageFrame.pmuOrder[i] = i;
					}
					VoltageFrame.knnResults = null;
					Main.getInnerFrame().remove(Main.getdFrame());
					// remove frames and add new frames for the new data
					Main.getInnerFrame().remove(VoltageFrame.getCp());
					Main.getInnerFrame().remove(FrequencyFrame.getCp());
					new VoltageFrame(GridBagConstraints.NORTHWEST);
					new FrequencyFrame(GridBagConstraints.SOUTHWEST);
					tools.getComponent(Toolbar.STOP_BUTTON).setEnabled(true);
					disturbanceFlag = true;
					// get info from database
					GetDBData test = new GetDBData();
					try {
						test.getInfo(abbvEventType, areaId, areaName);
						if (test.getNumEvents() == 0)
							return;

						// schedule execution for the voltage frame
						VoltageFrame.scheduledExecutorService = Executors
								.newSingleThreadScheduledExecutor();
						VoltageFrame.future = VoltageFrame.scheduledExecutorService
								.scheduleAtFixedRate(VoltageFrame.runnable, 0,
										5, TimeUnit.MILLISECONDS);

						// schedule execution for the frequency frame
						FrequencyFrame.scheduledExecutorService = Executors
								.newSingleThreadScheduledExecutor();
						FrequencyFrame.future = FrequencyFrame.scheduledExecutorService
								.scheduleAtFixedRate(FrequencyFrame.runnable,
										0, 5, TimeUnit.MILLISECONDS);

					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					if (VoltageFrame.scheduledExecutorService.isShutdown()) {
						stopButton.setText("Stop");
						// error dialog
						JOptionPane.showMessageDialog(Main.getInnerFrame(),
								"Change some data in order to run again!",
								"New Data", JOptionPane.INFORMATION_MESSAGE);
						tools.getComponent(Toolbar.START_BUTTON).setEnabled(
								true);
						tools.getComponent(Toolbar.EVENT_DROP_DOWN).setEnabled(
								true);
						tools.getComponent(Toolbar.AREA_DROP_DOWN).setEnabled(
								true);
						tools.getComponent(Toolbar.PMU_DROP_DOWN).setEnabled(
								true);
						tools.getComponent(Toolbar.VARIANCE_1).setEnabled(true);
						tools.getComponent(Toolbar.VARIANCE_2).setEnabled(true);
						tools.getComponent(Toolbar.EVENT_BUTTON).setEnabled(
								false);
						tools.getComponent(Toolbar.STOP_BUTTON).setEnabled(
								false);
					} else {

						VoltageFrame.scheduledExecutorService = Executors
								.newSingleThreadScheduledExecutor();
						VoltageFrame.future = VoltageFrame.scheduledExecutorService
								.scheduleAtFixedRate(VoltageFrame.runnable, 0,
										5, TimeUnit.MILLISECONDS);

						FrequencyFrame.scheduledExecutorService = Executors
								.newSingleThreadScheduledExecutor();
						FrequencyFrame.future = FrequencyFrame.scheduledExecutorService
								.scheduleAtFixedRate(FrequencyFrame.runnable,
										0, 5, TimeUnit.MILLISECONDS);
						// VoltageFrame.scheduledExecutorService.scheduleAtFixedRate(
						// VoltageFrame.runnable, 0, 5, TimeUnit.MILLISECONDS);
						// VoltageFrame.t.start();
						// FrequencyFrame.t.start();
					}
				}
			}

			// intervalFlag changed to indicate that the PMU Flags had a cycle
			// to initialize
			intervalFlag = true;

			// debugging
			System.out.println();
			System.out.println("*****");
			System.out.println();
			// flag changed to let methods know to run secondary draw
			flag = true;
			// VoltageFrame.t.start();
			// FrequencyFrame.t.start();

			// remove and add the frames back to inner frame
			Main.getInnerFrame().remove(Main.getfFrame());
			Main.addComponent(Main.getInnerFrame(), Main.getfFrame(), 1, 1, 1,
					1, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
					0, 0);

			Main.getInnerFrame().remove(Main.getColorFrame());
			Main.addComponent(Main.getInnerFrame(), Main.getColorFrame(), 1, 1,
					1, 1, GridBagConstraints.NORTHEAST,
					GridBagConstraints.NONE, 0, 0);
			// System.out.println(Main.getInnerFrame().countComponents());
			// Main.getInnerFrame().remove(Main.getdFrame());
			// System.out.println(Main.getInnerFrame().countComponents());
			disturbanceFlag = true;
			// Main.getInnerFrame().remove(Main.getdFrame());
			Main.addComponent(Main.getInnerFrame(), Main.getdFrame(), 1, 1, 1,
					1, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
					0, 0);

			// revalidate and repaint the innerframe
			Main.getInnerFrame().revalidate();
			Main.getInnerFrame().repaint();

			// debugging
			// System.out.println("eventType: " + eventType + "\narea: "
			// + areaName + "\nareaId: " + areaId + "\nnumPmu: "
			// + numOfPmu + "\nv1: " + v1 + "\nv2: " + v2 + "\nflag: "
			// + flag);
			// System.out.println();
			// System.out.println(Main.getInnerFrame().countComponents());

			// System.out.println(Main.getInnerFrame().countComponents());
		} else if (component == STOP_BUTTON) {
			// if stop button is pressd stop the timers
			System.out.println("Stop button was pressed");

			// test
			// Stroke a = new BasicStroke(5);
			// VoltageFrame.getCp().getChart().getXYPlot().getRenderer().setSeriesStroke(0,
			// a);

			// cancel future tasks
			VoltageFrame.future.cancel(true);
			FrequencyFrame.future.cancel(true);
			// VoltageFrame.scheduledExecutorService.shutdown();
			// VoltageFrame.t.stop();
			// FrequencyFrame.t.stop();
			// disturbanceFlag = false;
			// save info of the previous run to compare when start is selected
			// again
			eventTypeFlag = getPMUEvent();
			areaFlag = getPMUArea();
			numFlag = getPMUnumber();
			vNoise1Flag = getV1();
			vNoise2Flag = getV2();

			// for debugging purposes
			System.out.println("inside stop: v1 = " + vNoise1Flag + " , v2 = "
					+ vNoise2Flag);

			tools.getComponent(Toolbar.START_BUTTON).setEnabled(true);
			tools.getComponent(Toolbar.EVENT_DROP_DOWN).setEnabled(true);
			tools.getComponent(Toolbar.AREA_DROP_DOWN).setEnabled(true);
			tools.getComponent(Toolbar.PMU_DROP_DOWN).setEnabled(true);
			tools.getComponent(Toolbar.VARIANCE_1).setEnabled(true);
			tools.getComponent(Toolbar.VARIANCE_2).setEnabled(true);
			tools.getComponent(Toolbar.EVENT_BUTTON).setEnabled(false);

		} else if (component == CREATE_EVENT_BUTTON) {
			System.out.println("Event button was pressed");
			tools.getComponent(Toolbar.EVENT_BUTTON).setEnabled(false);

			// indicates to DisturbanceFrame.java to run either draw(g) or
			// reDraw(g)
			disturbanceFlag = true;

			Main.getInnerFrame().remove(Main.getdFrame());
			Main.addComponent(Main.getInnerFrame(), Main.getdFrame(), 1, 1, 1,
					1, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
					0, 0);
			// System.out.println(Main.getInnerFrame().countComponents());

			Main.getdFrame().revalidate();
			Main.getdFrame().repaint();
		}
	}
}
