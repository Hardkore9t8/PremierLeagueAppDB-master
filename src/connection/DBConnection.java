

package connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafxApp.Controller;
import javafxApp.TableTeam;
import org.apache.derby.client.am.SqlException;

import java.sql.*;

public class DBConnection {

    //database connection
    public static java.sql.Connection connection;
    private static Statement stmt = null;
    public static int recordID = 0;


    /**
     * This method connection starts the connection to the database
     */
    public static void getConnection() {
        String db_URL = "jdbc:derby:PLeagueDB;create=true";

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            try {
                connection = DriverManager.getConnection(db_URL);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        getRecordID();
        //setupTeamTable(); only used this to create table the first time
    }

    private static void getRecordID() {
        String sqlStr = "SELECT ID From team order by id desc FETCH FIRST row only";
        ResultSet rs = sqlQuery(sqlStr);
        try {
            rs.next();
            recordID = rs.getInt("id");
            System.out.println("Last record id " + recordID);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /**
     * This method makes a connection to the database.
     * Then it uses a result set method to check the database for the table named TEAM
     * If the table is not there it will create the table
     */
    static void setupTeamTable() {
        String tableName = "TEAM";

        try {
            stmt = connection.createStatement();
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName.toUpperCase(), null);
            if (tables.next()) {
                //System.out.println(("Table exist"));
            } else {
                System.out.println(("Table does not exist"));
                stmt.execute("Create Table Team ( "
                        + "id int primary key, team varchar(20), name varchar(50), age int, points int, position varchar(20), "
                        + "country varchar(50))");
            }

        } catch (SQLException e) {
            System.out.println("Error creating table..");
            e.printStackTrace();
        }
    }

    /**
     * @param sqlStr
     * @return rs
     */
    public static ResultSet sqlQuery(String sqlStr) {
        System.out.println(sqlStr);
        ResultSet rs;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sqlStr);

        } catch (SQLException ex) {
            return null;
        }
        return rs;
    }

    /**
     * @param sqlStr
     * @return boolean
     */
    public static boolean sqlInsert(String sqlStr) {
        try {
            stmt = connection.createStatement();
            stmt.execute(sqlStr);
            return true;
        } catch (SQLException ex) {
            System.out.println(sqlStr);
            System.out.println(ex.getMessage());
            return false;
        }

    }

    /**
     * @param sqlStr
     * @return getTeamObjects(rs)
     * @throws ClassNotFoundException
     * @throws SqlException
     */
    public static ObservableList<TableTeam> getSelectedRecords(String sqlStr) throws ClassNotFoundException, SqlException {
        ResultSet rs = sqlQuery(sqlStr);
        return getTeamObjects(rs);
    }

    /**
     * @return
     * @throws ClassNotFoundException
     * @throws SqlException
     */
    public static ObservableList<TableTeam> getAllRecords() throws ClassNotFoundException, SqlException {
        String sql = "Select * From Team";
        ResultSet rs = sqlQuery(sql);
        return getTeamObjects(rs);
    }

    /**
     * @param rs
     * @return teamList
     * @throws ClassNotFoundException
     * @throws SqlException
     */
    private static ObservableList<TableTeam> getTeamObjects(ResultSet rs) throws ClassNotFoundException, SqlException {
        ObservableList<TableTeam> teamList = FXCollections.observableArrayList();

        try {
            while (rs.next()) {
                TableTeam team = new TableTeam();
                team.setId(rs.getInt("id"));
                team.setTeam(rs.getString("Team"));
                team.setName(rs.getString("Name"));
                team.setAge(rs.getInt("Age"));
                team.setPoints(rs.getInt("points"));
                team.setPosition(rs.getString("position"));
                team.setCountry(rs.getString("Country"));
                teamList.add(team);
            }
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.getMessage());
        }
        return teamList;
    }


}