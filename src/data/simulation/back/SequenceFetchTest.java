package data.simulation.back;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * class was used only to test fetching sequence data from the database
 * 
 * @author Brett
 *
 */

public class SequenceFetchTest {

	public static DbConf dbConf;

	public static void main(String[] args) {
		dbConf = new DbConf();
		dbConf = dbConf.getPropValues("/path/to/.properties/file");
		try {
			getRecords();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// will get the records from the db
	private static void getRecords() throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		dbConnection = DriverManager.getConnection(dbConf.url, dbConf.userName, dbConf.password);
		// "SELECT * FROM PMU WHERE USER_ID = ?"
		String selectSQL = "SELECT * FROM SEQUENCES WHERE bus_nubmer = 298 LIMIT 1";

		try {
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			// preparedStatement.setInt(1, 30);

			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();

			List<Sequence> sequenceList = new ArrayList<Sequence>();
			Sequence a = new Sequence();
			int i = 0;
			while (rs.next()) {
				// Sequence a = new Sequence();// moved outside of while loop
				// for testing of 1 sequecne
				// move back within for final program
				int busNumber = Integer.parseInt(rs.getString("bus_nubmer"));
				String type = rs.getString("type");
				char c_type = type.charAt(0);
				String eventType = rs.getString("event_type");
				String seqId = rs.getString("seq_id");
				String fileName = rs.getString("file_name");
				String data = rs.getString("data");
				String[] dataArr = data.split(",");
				List<Double> dataList = new ArrayList<Double>();
				for (int j = 0; j < dataArr.length; j++) {
					dataList.add(Double.parseDouble(dataArr[j]));
				}
				a.setBusNumber(busNumber);
				a.setType(c_type);
				a.setEventType(eventType);
				a.setSeqId(seqId);
				a.setFileName(fileName);
				a.setData(dataList);

				i++;
				// sequenceList.add(a);
			}
			System.out.println(a);
			System.out.println(a.getData().size());
			// print the pmuList
			// for (int j = 0; j < pmuList.size(); j++)
			// System.out.println(pmuList.get(j));
			System.out.println(i);
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
	}
}
