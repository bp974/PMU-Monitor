package data.simulation.back;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * class will get the voltage sequence data from the database and will create a
 * text file out of them that will be used to generate a train file for the knn
 * algorithm.
 * 
 * Note: Not used during running of the applet
 * 
 * @author Brett Pelkey
 *
 */
public class GetAllDBData {
	// JDBC driver name and database URL
	// static final String JDBC_DRIVER = DBConstants.JDBC_DRIVER;
	// static final String DB_URL = DBConstants.DB_URL;

	public static DbConf dbConf;

	public static void main(String[] args) {
		dbConf = new DbConf();
		dbConf = dbConf.getPropValues("/path/to/.properties/file");
		try {
			for (int i = 3; i < 10; i++) {
				List<Seq> sequences = null;
				switch (i) {
				case 3:
					sequences = getRecords("LSF");
					break;
				case 4:
					sequences = getRecords("LSN");
					break;
				case 5:
					sequences = getRecords("SHF");
					break;
				case 6:
					sequences = getRecords("SHN");
					break;
				case 7:
					sequences = getRecords("SMS");
					break;
				case 8:
					sequences = getRecords("SRF");
					break;
				case 9:
					sequences = getRecords("SRN");
					break;
				}
				writeRecords(sequences, i);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// will get the records from the db
	private static List<Seq> getRecords(String event) throws SQLException {
		System.out.println("Connecting to Database...");
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		dbConnection = DriverManager.getConnection(dbConf.url, dbConf.userName, dbConf.password);
		System.out.println("Connected to Database");
		// "SELECT * FROM PMU WHERE USER_ID = ?"
		// $query =
		// "Select * FROM words ORDER BY word ASC LIMIT $startRowNum,
		// $numWordsToLoad";
		String selectSQL = "SELECT * FROM SEQUENCES WHERE event_type = '" + event + "' and type = 'V' LIMIT 3000";
		List<Seq> sequenceList = null;

		try {
			System.out.println("Running SQL statement...");
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			// preparedStatement.setInt(1, 30);

			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();

			sequenceList = new ArrayList<Seq>();
			// Sequence a = new Sequence();
			int i = 0;
			System.out.println("Gathering Data...");
			while (rs.next()) {
				Seq a = new Seq();

				// int busNumber =
				// Integer.parseInt(rs.getString("bus_nubmer"));
				// String type = rs.getString("type");
				// char c_type = type.charAt(0);
				String eventType = rs.getString("event_type");
				// String seqId = rs.getString("seq_id");
				// String fileName = rs.getString("file_name");
				String data = rs.getString("data");
				// String[] dataArr = data.split(",");
				// List<Double> dataList = new ArrayList<Double>();
				// for (int j = 0; j < dataArr.length; j++) {
				// dataList.add(Double.parseDouble(dataArr[j]));
				// }
				// a.setBusNumber(busNumber);
				// a.setType(c_type);
				a.setEventType(eventType);
				// a.setSeqId(seqId);
				// a.setFileName(fileName);
				a.setData(data);

				i++;
				sequenceList.add(a);
			}
			// System.out.println(a);
			// System.out.println(a.getData().size());
			// print the pmuList
			// for (int j = 0; j < pmuList.size(); j++)
			// System.out.println(pmuList.get(j));
			System.out.println(i + " rows of data gathered");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		return sequenceList;
	}

	private static void writeRecords(List<Seq> sequences, int num) {
		try {
			File target = new File("resources/vTrainFull_" + num + ".txt");
			if (!target.exists())
				target.createNewFile();
			FileWriter fw = new FileWriter(target.getAbsolutePath());
			BufferedWriter bw = new BufferedWriter(fw);
			System.out.println("Writing data to file...");
			for (int i = 0; i < sequences.size(); i++) {
				String event = getEvent(sequences.get(i).getEventType());
				// System.out.println(event + ","
				// + sequences.get(i).getData().toString());
				bw.write(event + ", " + sequences.get(i).getData().toString());
				if (i != sequences.size() - 1)
					bw.write("\n");
			}
			System.out.println("Writing done");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getEvent(String event) {
		switch (event) {
		case "FLT":
			event = "1";
			break;
		case "GNL":
			event = "2";
			break;
		case "LSF":
			event = "3";
			break;
		case "LSN":
			event = "4";
			break;
		case "SHF":
			event = "5";
			break;
		case "SHN":
			event = "6";
			break;
		case "SMS":
			event = "7";
			break;
		case "SRF":
			event = "8";
			break;
		case "SRN":
			event = "9";
			break;
		}
		return event;
	}

	static class Seq {
		private String eventType;
		private String data;

		public String getEventType() {
			return eventType;
		}

		public void setEventType(String eventType) {
			this.eventType = eventType;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}
	}
}
