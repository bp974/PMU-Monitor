package data.simulation.back;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The PMUDriver class gets all the PMU's data from the AREAS_WECC.csv &
 * PMU_298_FINAL.csv files and creates the 298 PMU's
 * 
 * It also sends all the PMU's to MySQL database
 * 
 * Note: Not used during running of the applet
 *
 * @author Brett Pelkey
 * @date 7-14-15
 */
public class PMUDriver {

	private List<PMU> pmuList = null;// list to hold all the PMU's
	public static DbConf dbConf;

	// main function to make calls to read the csv files and insert the records
	// into the db
	public static void main(String[] args) {
		dbConf = new DbConf();
		dbConf = dbConf.getPropValues("/path/to/.properties/file");
		System.out.println(new Date());
		PMUDriver a = new PMUDriver();
		// System.out.println(a);
		/*
		 * uncomment next line to run the class and uncomment the try block; if
		 * table is not created yet then uncomment the createTableStatement()
		 * method as well
		 */
		/*-a.readCsv();
		// a.createTableStatement();
		try {
			a.insertRecordsIntoTable();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		System.out.println(new Date());
	}

	/**
	 * readCsv() Method
	 * 
	 * will read the data out of the PMU_FINAL csv file and get all the data and
	 * set up each PMU object and add to pmuList arrayList
	 */
	private void readCsv() {
		try {
			// read csv file using CSVReader class
			CsvReader pmuData = new CsvReader("resources/PMU_298_FINAL.csv");

			// create array list to hold all PMU data
			pmuList = new ArrayList<PMU>();

			// no header so just read first record
			int i = 1;
			while (pmuData.readRecord()) {

				int areaId = Integer.parseInt(pmuData.get(8));
				// make call to getAreaName to get the name of the area which
				// resides in a different csv file
				String areaName = getAreaName(areaId);
				int zoneId = Integer.parseInt(pmuData.get(9));
				String pmuName = pmuData.get(1);
				int busId = Integer.parseInt(pmuData.get(0));
				String state = pmuData.get(20);
				PMU pmu = new PMU(i, areaId, areaName, zoneId, pmuName, busId, state);
				// System.out.println(pmu);
				pmuList.add(pmu);
				i++;
			}
			System.out.println("end of while loop");
			// for (int i = 0; i < pmuList.size(); i++)
			// System.out.println(pmuList.get(i));
			pmuData.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * insertRecordsIntoTable() Method
	 * 
	 * will take the list of pmu's and add then to the db using a prepared
	 * statement
	 * 
	 * @throws SQLException
	 */
	private void insertRecordsIntoTable() throws SQLException {

		// connection to db
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		dbConnection = DriverManager.getConnection(dbConf.url, dbConf.userName, dbConf.password);

		// sql statement
		String insertTableSQL = "INSERT INTO PMU"
				+ "(bus_number, area_id, area_name, zone_id, pmu_name, bus_id, state) VALUES" + "(?,?,?,?,?,?,?)";

		// try to add each pmu to db using prepared statement
		try {
			preparedStatement = dbConnection.prepareStatement(insertTableSQL);
			int i = 0;
			while (i < pmuList.size()) {
				preparedStatement.setInt(1, pmuList.get(i).getBusNumber());
				preparedStatement.setInt(2, pmuList.get(i).getAreaId());
				preparedStatement.setString(3, pmuList.get(i).getAreaName());
				preparedStatement.setInt(4, pmuList.get(i).getZoneId());
				preparedStatement.setString(5, pmuList.get(i).getPmuName());
				preparedStatement.setInt(6, pmuList.get(i).getBusId());
				preparedStatement.setString(7, pmuList.get(i).getState());

				// execute insert SQL stetement
				preparedStatement.executeUpdate();

				// System.out.println("Record is inserted into PMU table!");
				i++;
			}
			System.out.println("ALL Records have been inserted into PMU table!");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			// clost both the prepared statement and the db connection
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}

	/**
	 * getAreaName() Method
	 * 
	 * will search the AREAS_WECC.csv for the area name by matching the area id
	 * 
	 * @param areaId
	 *            the areaId to find in the csv file to match with the name
	 * 
	 * @return the string value of area name
	 */
	private String getAreaName(int areaId) {
		// convert areaId to string
		String s_areaId = Integer.toString(areaId);
		String areaName = "";
		try {
			CsvReader areaData = new CsvReader("resources/AREAS_WECC.csv");
			areaData.readRecord();
			// read through each record util column 0 of file equals areaId
			while (!areaData.get(0).equals(s_areaId)) {
				areaData.readRecord();
			}
			// once it matches grab column 1 for the name
			areaName = areaData.get(1);
			areaData.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return areaName;
	}

	/**
	 * createTableStatement() Method
	 * 
	 * Used to create the table in the database.
	 */
	@SuppressWarnings("unused")
	private void createTableStatement() {
		System.out.println("Creating table...");
		Connection dbConnection = null;
		Statement stmt = null;
		try {
			dbConnection = DriverManager.getConnection(dbConf.url, dbConf.userName, dbConf.password);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String createTableSQL = "CREATE TABLE PMU " + "(bus_number INTEGER, " + "area_id INTEGER, "
				+ "area_name VARCHAR(100), " + "zone_id INTEGER, " + "pmu_name VARCHAR(100), " + "bus_id INTEGER, "
				+ "state CHAR(2))";

		try {

			stmt = dbConnection.createStatement();
			stmt.executeUpdate(createTableSQL);
			System.out.println("Table created...");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
