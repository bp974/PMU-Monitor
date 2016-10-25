package data.simulation.back;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class was used just to test getting some records from the db
 * 
 * Note: Not used during running of the applet
 * 
 * @author Brett
 *
 */
public class PMUFetchTest {

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
		String selectSQL = "SELECT * FROM PMU";

		try {
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			// preparedStatement.setInt(1, 30);

			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();

			List<PMU> pmuList = new ArrayList<PMU>();
			int i = 0;
			while (rs.next()) {
				PMU a = new PMU();
				int areaId = Integer.parseInt(rs.getString("area_id"));
				String areaName = rs.getString("area_name");
				int zoneId = Integer.parseInt(rs.getString("zone_id"));
				String pmuName = rs.getString("pmu_name");
				int busId = Integer.parseInt(rs.getString("bus_id"));
				String state = rs.getString("state");
				a.setAreaId(areaId);
				a.setAreaName(areaName);
				a.setZoneId(zoneId);
				a.setPmuName(pmuName);
				a.setBusId(busId);
				a.setState(state);
				i++;
				pmuList.add(a);
			}
			// print the pmuList
			for (int j = 0; j < pmuList.size(); j++)
				System.out.println(pmuList.get(j));
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
