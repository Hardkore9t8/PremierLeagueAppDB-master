package javafxApp;

import connection.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.derby.client.am.SqlException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static connection.DBConnection.sqlInsert;

//this is the controller class for the fxml
public class Controller implements Initializable {

    public TextField teamtextfield;
    public TextField playertextfield;
    public TextField countrytextfield;
    public TextField addTeamTextF;
    public TextField addNameTextF;
    public TextField addAgeTextF;
    public TextField addPositionTextF;
    public TextField addPointsTextF;
    public TextField addCountryTextF;
    public TextField playerIdTF;
    public Label teamLabel;
    public Label playerLabel;
    public Button teambutton;
    public Button playerbutton;
    public Button resetbutton;
    public Button countrybutton;
    public Button clearbutton;
    public Button addbutton;
    public Button deletebutton;
    public TextArea messagebox;
    public CheckBox checkbox;
    public ImageView textfieldImage;


    @FXML
    private TableView TeamTable;
    @FXML
    private TableColumn<TableTeam, String> col_team;
    @FXML
    private TableColumn<TableTeam, String> col_name;
    @FXML
    private TableColumn<TableTeam, Integer> col_age;
    @FXML
    private TableColumn<TableTeam, String> col_position;
    @FXML
    private TableColumn<TableTeam, Integer> col_points;
    @FXML
    private TableColumn<TableTeam, String> col_country;
    @FXML
    private TableColumn<TableTeam, Integer> col_ID;

    /**
     * @param location
     * @param resources
     */
    // create array to hold table objects
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBConnection.getConnection();
        Connection con = DBConnection.connection;
        col_ID.setCellValueFactory(cellData -> cellData.getValue().getTeamId().asObject());
        col_team.setCellValueFactory(cellData -> cellData.getValue().getTeamName());
        col_name.setCellValueFactory(cellData -> cellData.getValue().getPlayerName());
        col_age.setCellValueFactory(cellData -> cellData.getValue().getPlayerAge().asObject());
        col_points.setCellValueFactory(cellData -> cellData.getValue().getPlayerPoints().asObject());
        col_position.setCellValueFactory(cellData -> cellData.getValue().getPlayerPosition());
        col_country.setCellValueFactory(cellData -> cellData.getValue().getPlayerCountry());


    }


    /**
     * This method populates the uses the DBConnection
     *
     * @param sqlStr
     */
    private void populateTableList(String sqlStr) {
        ObservableList<TableTeam> teamList = null;
        try {
            teamList = DBConnection.getSelectedRecords(sqlStr);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SqlException e) {
            e.printStackTrace();
        }
        TeamTable.setItems(teamList);
    }

    /**
     * This method performs an action event that resets the tableview and runs a sql query that displays all records
     * in the database
     *
     * @param actionEvent
     */
    @FXML
    public void resetbutton(ActionEvent actionEvent) {
        String sqlStr = "Select * From Team";
        populateTableList(sqlStr);
        messagebox.setText("Here are all the records for players in the Premier League");
    }

    /**
     * This method performs an action event that runs an sql query using the text entered into the team text field
     *
     * @param actionEvent
     */
    @FXML
    public void teambutton(ActionEvent actionEvent) {
        TeamTable.getItems().clear();
        String team = teamtextfield.getText();
        String sqlStr = "Select * From Team Where Team = '" + team + "' Order By name";
        populateTableList(sqlStr);
        messagebox.setText("Here all the records for " + team + " sorted by player name.");
        teamtextfield.clear();
        imgSelector(team);


    }

    /**
     * This method performs an action event that runs an sql query using the text entered into the player text field
     *
     * @param actionEvent
     */
    @FXML
    public void playerbutton(ActionEvent actionEvent) {
        TeamTable.getItems().clear();
        String player = playertextfield.getText();
        String sqlStr = "Select * From Team Where Name = '" + player + "'";
        populateTableList(sqlStr);
        messagebox.setText("Here is the record for " + player);
        playertextfield.clear();
        imgSelector(player);
    }

    /**
     * This method performs an action event that runs an sql query using the text entered into the country text field
     *
     * @param actionEvent
     */
    @FXML
    public void countrybutton(ActionEvent actionEvent) {
        TeamTable.getItems().clear();
        String country = countrytextfield.getText();
        String sqlStr = "Select * From Team Where Country = '" + country + "'Order By name";
        populateTableList(sqlStr);
        messagebox.setText("Here are all the records for players from " + country);
        countrytextfield.clear();
        imgSelector(country);
    }

    /**
     * The method performs an action event that clears the tableview
     *
     * @param actionEvent
     */
    @FXML
    public void clearbutton(ActionEvent actionEvent) {
        TeamTable.getItems().clear();
        messagebox.setText("Table has been clear");
    }

    /**
     * This method performs an action event that adds a record to the database
     *
     * @param actionEvent
     */
    @FXML
    public void addbutton(ActionEvent actionEvent) {
        TeamTable.getItems().clear();

        String team = addTeamTextF.getText();
        String name = addNameTextF.getText();
        String age = addAgeTextF.getText();
        String points = addPointsTextF.getText();
        String position = addPositionTextF.getText();
        String country = addCountryTextF.getText();
        String recID = String.valueOf(++DBConnection.recordID);
        String sqlStr = "INSERT INTO TEAM(ID,Team,Name,Age,Points,Position,Country)" +
                "VALUES(" + recID + ",'" + team + "','" + name + "'," + age + "," + points + ",'" + position + "','" + country + "')";

        sqlInsert(sqlStr);
        String sqlStr2 = "Select * From Team Where id = " + recID;
        populateTableList(sqlStr2);
        messagebox.setText("Nice a new player was added to " + team);


    }


    /**
     * This method deletes records from the database. Checkbox must be checked or alert warning will notify user.
     * If checkbox is check record is deleted and message appears in message console
     *
     * @param actionEvent
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @FXML
    public void deletebutton(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        if (!checkbox.isSelected()) {
            messagebox.setText("Delection of records cannot be rolled back!");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Deletion Warning");
            alert.setHeaderText(null);
            alert.setContentText("Confirm Deletion by checking check box.");
            alert.showAndWait();
        } else {

            try {
                String id = playerIdTF.getText();
                String sqlStr = "DELETE FROM team WHERE ID = " + id;
                sqlInsert(sqlStr);
                messagebox.setText("Player with id " + id + " was deleted successfully.");
            } catch (Exception e) {
                System.out.println("Error occured while deleting record " + e);
                e.printStackTrace();
                throw e;

            }
        }

    }

    public void imgSelector(String img) {
        String imageURL = null;


        switch (img) {
            case "Black Panther":
                imageURL = "/Users/DaddyNupe/OneDriveFGCU/OneDrive - Florida Gulf Coast University/PremierLeagueAppDB-master/src/images/Black Panther.png";
                System.out.println(imageURL);
                break;
            case "Alexandre Lacazette":
                imageURL = "/Users/DaddyNupe/OneDriveFGCU/OneDrive - Florida Gulf Coast University/PremierLeagueAppDB-master/src/images/Alexandre Lacazette.png";
                System.out.println(imageURL);
                break;
            case "Nathan Ash":
                imageURL = "/Users/DaddyNupe/OneDriveFGCU/OneDrive - Florida Gulf Coast University/PremierLeagueAppDB-master/src/images/Nathan Ash.png";
                System.out.println(imageURL);
                break;
            case "Olson Twins":
                imageURL = "/Users/DaddyNupe/OneDriveFGCU/OneDrive - Florida Gulf Coast University/PremierLeagueAppDB-master/src/images/Olson Twins.png";
                System.out.println(imageURL);
                break;
            case "FGCU":
                imageURL = "/Users/DaddyNupe/OneDriveFGCU/OneDrive - Florida Gulf Coast University/PremierLeagueAppDB-master/src/images/FGCU.png";
                System.out.println(imageURL);
                break;
            case "Arsenal":
                imageURL = "/Users/DaddyNupe/OneDriveFGCU/OneDrive - Florida Gulf Coast University/PremierLeagueAppDB-master/src/images/Arsenal.png";
                System.out.println(imageURL);
                break;
            case "Chelsea":
                imageURL = "/Users/DaddyNupe/OneDriveFGCU/OneDrive - Florida Gulf Coast University/PremierLeagueAppDB-master/src/images/Chelsea.png";
                System.out.println(imageURL);
                break;
            case "Crystal Palace":
                imageURL = "/Users/DaddyNupe/OneDriveFGCU/OneDrive - Florida Gulf Coast University/PremierLeagueAppDB-master/src/images/Crystal Palace.png";
                System.out.println(imageURL);
                break;
            case "Liverpool":
                imageURL = "/Users/DaddyNupe/OneDriveFGCU/OneDrive - Florida Gulf Coast University/PremierLeagueAppDB-master/src/images/Liverpool.png";
                System.out.println(imageURL);
                break;
            case "Newcastle United":
                imageURL = "/Users/DaddyNupe/OneDriveFGCU/OneDrive - Florida Gulf Coast University/PremierLeagueAppDB-master/src/images/Chelsea.png";
                System.out.println(imageURL);
                break;
            case "Germany":
                imageURL = "/Users/DaddyNupe/OneDriveFGCU/OneDrive - Florida Gulf Coast University/PremierLeagueAppDB-master/src/images/Germany.png";
                System.out.println(imageURL);
                break;
            case "France":
                imageURL = "/Users/DaddyNupe/OneDriveFGCU/OneDrive - Florida Gulf Coast University/PremierLeagueAppDB-master/src/images/France.png";
                System.out.println(imageURL);
                break;
            case "England":
                imageURL = "/Users/DaddyNupe/OneDriveFGCU/OneDrive - Florida Gulf Coast University/PremierLeagueAppDB-master/src/images/England.png";
                System.out.println(imageURL);
                break;
            case "United States":
                imageURL = "/Users/DaddyNupe/OneDriveFGCU/OneDrive - Florida Gulf Coast University/PremierLeagueAppDB-master/src/images/United States.png";
                System.out.println(imageURL);
                break;
            case "Wakanda":
                imageURL = "/Users/DaddyNupe/OneDriveFGCU/OneDrive - Florida Gulf Coast University/PremierLeagueAppDB-master/src/images/Wakanda.png";
                System.out.println(imageURL);
                break;
            case "Bournemouth":
                imageURL = "/Users/DaddyNupe/OneDriveFGCU/OneDrive - Florida Gulf Coast University/PremierLeagueAppDB-master/src/images/AFC Bournemouth.png";
                System.out.println(imageURL);
                break;
            case "Burnley":
                imageURL = "/Users/DaddyNupe/OneDriveFGCU/OneDrive - Florida Gulf Coast University/PremierLeagueAppDB-master/src/images/Burnley.png";
                System.out.println(imageURL);
                break;
            case "Everton":
                imageURL = "/Users/DaddyNupe/OneDriveFGCU/OneDrive - Florida Gulf Coast University/PremierLeagueAppDB-master/src/images/Everton.png";
                System.out.println(imageURL);
                break;
            case "Manchester United":
                imageURL = "/Users/DaddyNupe/OneDriveFGCU/OneDrive - Florida Gulf Coast University/PremierLeagueAppDB-master/src/images/Manchester United.png";
                System.out.println(imageURL);
                break;
        }


        {
            try {
                FileInputStream input = new FileInputStream(imageURL);
                Image image = new Image(input);
                textfieldImage.setImage(image);

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }

        //return imageURL;
    }


}