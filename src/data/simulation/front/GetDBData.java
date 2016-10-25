package data.simulation.front;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import data.simulation.back.PmuSequence;

/**
 * GetInfo will gather all of the pmu info from the db depending on what the
 * user has selected in the toolbar
 * 
 * @author Brett Pelkey
 *
 */
public class GetDBData {

	// private String areaName;
	// private String areaId;

	public static List<PmuSequence> pmuList;// the list to store all info
	public static double[] dataArr = new double[3600];// array to hold the
														// sequences
	private int numEvents;// number of events that are in the db

	public static double vMax, vMin, fMax, fMin;

	// keeps scale of axis a little bigger than the largest and smallest values
	// obtained by getMax and getMin methods
	private double yAxisThreshold = 0.005;

	// public static PmuSequence pmu;

	/**
	 * Constructor will set numEvents to 0
	 */
	public GetDBData() {
		super();
		numEvents = 0;
	}

	/**
	 * getInfo will get all of the data from the database
	 * 
	 * @param eventType
	 *            the event type to get from the db
	 * @param areaId
	 *            the area id to get from the db
	 * @param areaName
	 *            the area name to get from the db
	 * @throws SQLException
	 */
	public void getInfo(String eventType, String areaId, String areaName) throws SQLException {

		// System.out.println(eventType);
		// System.out.println("Starting time: " + new Date());
		long tStart = System.currentTimeMillis();

		// get the number of events for the selected data in toolbar
		numEvents = getNumEvents(areaId, eventType);
		// if no events than display error to user
		if (numEvents == 0) {
			ToolbarListener.tools.getComponent(Toolbar.START_BUTTON).setEnabled(true);
			ToolbarListener.tools.getComponent(Toolbar.EVENT_DROP_DOWN).setEnabled(true);
			ToolbarListener.tools.getComponent(Toolbar.AREA_DROP_DOWN).setEnabled(true);
			ToolbarListener.tools.getComponent(Toolbar.PMU_DROP_DOWN).setEnabled(true);
			ToolbarListener.tools.getComponent(Toolbar.VARIANCE_1).setEnabled(true);
			ToolbarListener.tools.getComponent(Toolbar.VARIANCE_2).setEnabled(true);
			ToolbarListener.tools.getComponent(Toolbar.EVENT_BUTTON).setEnabled(false);
			// error dialog
			JOptionPane.showMessageDialog(Main.getInnerFrame(), "No Data for " + eventType + " in area " + areaName,
					"Data Error", JOptionPane.ERROR_MESSAGE);
			// if (selection == JOptionPane.CLOSED_OPTION)
			// System.out.println("OK was clicked on JOptionPane");
			return;
		}

		// create random numbers based on the number of events in the database
		Random rand = new Random();
		// store the numbers in a string array with the rest of the unique
		// sequence id
		List<String> seqIdArray = new ArrayList<String>();
		String seqId = "";
		// for loop to create the number of random numbers to match the number
		// of PMU's selected
		for (int i = 0; i < ToolbarListener.getPMUnumber(); i++) {
			int randNum = rand.nextInt(numEvents) + 1;
			// System.out.println(randNum);
			seqId = "A" + areaId + "_E" + eventType + "_S" + randNum;
			// check if seqId has already been created (can't have duplicates)
			if (seqIdArray.contains(seqId)) {
				System.out.println("ARRAY ALREADY CONTAINED THE SEQ ID");
				i--;
			} else {
				// else add seqId to array
				seqIdArray.add(seqId);
				// System.out.println(seqId);
			}
		}

		// original sql query to select only one pmu from db
		// String selectSQL =
		// "SELECT PMU.zone_id, PMU.pmu_name, PMU.bus_id, PMU.state, SEQUENCES.*
		// FROM PMU, SEQUENCES "
		// +
		// "WHERE PMU.bus_number = SEQUENCES.bus_number AND SEQUENCES.seq_id =
		// '"
		// + seqId + "'";

		// sql statement to execute
		String selectSQL = "SELECT PMU.zone_id, PMU.pmu_name, PMU.bus_id, PMU.state, SEQUENCES.* FROM PMU, SEQUENCES "
				+ "WHERE PMU.bus_number = SEQUENCES.bus_number AND (SEQUENCES.seq_id = ";

		// for loop to finalize the sql statement depending upon the number of
		// pmu's that is needed to fetch
		for (int i = 0; i < ToolbarListener.getPMUnumber(); i++) {
			selectSQL += "'" + seqIdArray.get(i) + "'";
			if ((ToolbarListener.getPMUnumber() - i) == 1) {
				selectSQL += ") order by type desc";
			} else {
				selectSQL += " OR SEQUENCES.seq_id = ";
			}
			// print query for testing
			// System.out.println(selectSQL);
		}

		// execute query
		try {
			Main.preparedStatement = Main.dbConnection.prepareStatement(selectSQL);

			// execute select SQL statement
			ResultSet rs = Main.preparedStatement.executeQuery();
			rs.setFetchSize(2);

			pmuList = new ArrayList<PmuSequence>();

			// int i = 0;
			// while loop to store all the data to an arraylist of PmuSequences
			while (rs.next()) {
				PmuSequence pmu = new PmuSequence();
				List<Double> dataList = new ArrayList<Double>();
				int busNumber = Integer.parseInt(rs.getString("bus_number"));
				int i_areaId = Integer.parseInt(areaId);
				int zoneId = Integer.parseInt(rs.getString("zone_id"));
				String pmuName = rs.getString("pmu_name");
				int busId = Integer.parseInt(rs.getString("bus_id"));
				String state = rs.getString("state");
				String type = rs.getString("type");
				String fileName = rs.getString("file_name");
				String data = rs.getString("data");
				String[] dataArr = data.split(",");
				// check for data sequences with a value of 0.0
				// if so make a recursive call to method with same params
				// will slow down the program especially if a data set contains
				// 0.0 in multiple recursive calls
				// need to figure out a better way of doing this but for now it
				// works
				if (dataArr[0].equals("0.0")) {
					System.out.println("\n\n***************");
					System.out.println("data array contains 0's: another call is made to getInfo()");
					System.out.println("***************\n\n");
					// clear list to remove any items that have already been
					// added
					pmuList.clear();
					// recursive call
					getInfo(eventType, areaId, areaName);
					// upon returning to this point after making recursive call
					// just return the method to prevent further execution of
					// method
					return;
				}
				for (int j = 0; j < dataArr.length; j++)
					dataList.add(Double.parseDouble(dataArr[j]));
				pmu.setBusNumber(busNumber);
				pmu.setAreaId(i_areaId);
				pmu.setAreaName(areaName);
				pmu.setZoneId(zoneId);
				pmu.setPmuName(pmuName);
				pmu.setBusId(busId);
				pmu.setState(state);
				pmu.setType(type);
				pmu.setEventType(eventType);
				pmu.setFileName(fileName);
				pmu.setData(dataList);
				pmuList.add(pmu);
				// i++;
			}

			/* MOVED TO TOOLBAR LISTENER BECAUSE OF DELAY */
			// // schedule execution for the voltage frame
			// VoltageFrame.scheduledExecutorService = Executors
			// .newSingleThreadScheduledExecutor();
			// VoltageFrame.future = VoltageFrame.scheduledExecutorService
			// .scheduleAtFixedRate(VoltageFrame.runnable, 0, 5,
			// TimeUnit.MILLISECONDS);
			//
			// // schedule execution for the frequency frame
			// FrequencyFrame.scheduledExecutorService = Executors
			// .newSingleThreadScheduledExecutor();
			// FrequencyFrame.future = FrequencyFrame.scheduledExecutorService
			// .scheduleAtFixedRate(FrequencyFrame.runnable, 0, 5,
			// TimeUnit.MILLISECONDS);

			// VoltageFrame.scheduledExecutorService.scheduleWithFixedDelay(
			// VoltageFrame.runnable, 0, 5, TimeUnit.MILLISECONDS);
			// get the max and min for V and F from the pmuList
			vMax = getVMax(pmuList) + yAxisThreshold;
			vMin = getVMin(pmuList) - yAxisThreshold;
			fMax = getFMax(pmuList) + yAxisThreshold;
			fMin = getFMin(pmuList) - yAxisThreshold;
			VoltageFrame.getCp().getChart().getXYPlot().getRangeAxis().setRange(vMin, vMax);
			FrequencyFrame.getCp().getChart().getXYPlot().getRangeAxis().setRange(fMin, fMax);

			// start the timers
			// VoltageFrame.t.start();
			// FrequencyFrame.t.start();
			// print the pmuList
			// for (int j = 0; j < pmuList.size(); j++)
			// System.out.println(pmuList.get(j));
			// System.out.println(i);
			// print out the elapsed time to get info
			long tEnd = System.currentTimeMillis();
			long tDelta = tEnd - tStart;
			double elapsedSeconds = tDelta / 1000.0;
			System.out.println("QUERY TOOK " + elapsedSeconds + " SECONDS");
			// System.out.println("Ending time: " + new Date());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			// close prepared statement when done
			if (Main.preparedStatement != null) {
				Main.preparedStatement.close();
			}
			// if (dbConnection != null) {
			// dbConnection.close();
			// }
		}

	}

	/**
	 * getNumEvents will search the db table SUMMARY and get the number of
	 * events related to the info that the user selects
	 * 
	 * @param areaId
	 *            the id of the area to search for
	 * @param eventType
	 *            the event type to search for
	 * @return the number of events that are in the database
	 * @throws SQLException
	 */
	public int getNumEvents(String areaId, String eventType) throws SQLException {

		// System.out.println(areaId + " -- " + eventType);
		// String selectSQL = "SELECT * FROM SUMMARY";
		String selectSQL = "SELECT num_events FROM SUMMARY WHERE area_id = " + areaId + " AND event_type = '"
				+ eventType + "'";
		int numEvents = 0;
		// System.out.println(selectSQL);
		try {

			// preparedStatement = dbConnection.prepareStatement(selectSQL);
			Main.preparedStatement = Main.dbConnection.prepareStatement(selectSQL);

			// execute select SQL stetement
			ResultSet rs = Main.preparedStatement.executeQuery();
			// System.out.println("result set is: " + rs);
			rs.next();
			numEvents = Integer.parseInt(rs.getString("num_events"));

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			// close prepared statement when done
			if (Main.preparedStatement != null) {
				try {
					Main.preparedStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// the number of events to return
		return numEvents;
	}

	// methods to get the min and max of a given sequence list
	/**
	 * Gets the max value out of all the Voltage data sets. Used with vMin to
	 * determine the y axis scale for the voltage chart
	 * 
	 * @param list
	 *            the list of PmuSequences to get the max from
	 * @return the max value in the list
	 */
	private double getVMax(List<PmuSequence> list) {
		// set max initially to a value that will always be smaller than any
		// values in the data set
		double max = 0;
		// for loop goes through first half of list because second half contains
		// F data
		for (int i = 0; i < list.size() / 2; i++) {
			for (int j = 0; j < list.get(i).getData().size(); j++) {
				if (list.get(i).getData().get(j) > max)
					max = list.get(i).getData().get(j);
			}
		}
		return max;
	}

	/**
	 * Gets the max value out of all the Frequency data sets. Used with fMin to
	 * determine the y axis scale for the frequency chart
	 * 
	 * @param list
	 *            the list of PmuSequences to get the max from
	 * @return the max value in the list
	 */
	private double getFMax(List<PmuSequence> list) {
		// set max initially to a value that will always be smaller than any
		// values in the data set
		double max = 0;
		// for loop goes through second half of list because first half contains
		// V data
		for (int i = list.size() / 2; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).getData().size(); j++) {
				if (list.get(i).getData().get(j) > max)
					max = list.get(i).getData().get(j);
			}
		}
		return max;
	}

	/**
	 * Gets the min value out of all the Voltage data sets. Used with vMax to
	 * determine the y axis scale for the voltage chart
	 * 
	 * @param list
	 *            the list of PmuSequences to get the min from
	 * @return the min value in the list
	 */
	private double getVMin(List<PmuSequence> list) {
		// set min initially to a value that will always be larger than any
		// values in the data set
		double min = 1000;
		// for loop goes through first half of list because second half contains
		// F data
		for (int i = 0; i < list.size() / 2; i++) {
			for (int j = 0; j < list.get(i).getData().size(); j++) {
				if (list.get(i).getData().get(j) < min)
					min = list.get(i).getData().get(j);
			}
		}
		return min;
	}

	/**
	 * Gets the min value out of all the Frequency data sets. Used with fMax to
	 * determine the y axis scale for the frequency chart
	 * 
	 * @param list
	 *            the list of PmuSequences to get the min from
	 * @return the min value in the list
	 */
	private double getFMin(List<PmuSequence> list) {
		// set min initially to a value that will always be larger than any
		// values in the data set
		double min = 1000;
		// for loop goes through second half of list because first half contains
		// V data
		for (int i = list.size() / 2; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).getData().size(); j++) {
				if (list.get(i).getData().get(j) < min)
					min = list.get(i).getData().get(j);
			}
		}
		return min;
	}

	/**
	 * Gets the number of events that have been assigned to class variable
	 * 
	 * @return the number of events
	 */
	public int getNumEvents() {
		return numEvents;
	}

	/**
	 * Sets the number of events
	 * 
	 * @param numEvents
	 *            the number of events to set
	 */
	public void setNumEvents(int numEvents) {
		this.numEvents = numEvents;
	}

	// @Override
	// public String toString() {
	// return "GetInfoTest [areaId=" + areaId + "]";
	// }

}
