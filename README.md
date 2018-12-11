# PremierLeagueAppDB
This javafx application displays player statistics for players for a selected team.  
The player data is stored in a derby database named PLeagueDB
The gui prompts the user to select a team which presents all the players on that team along with each player's
Profile: Team, Name, Age, Position, Points.
The database connection is made by the DBConnection class
The gui is designed in the dbproject fxml.  The gui consists of Labels, textfields,  a textarea, a checkbox and a 
tableview.
The gui is controlled by the Controller class.  This class performs all the actions for each feature on the gui
The Table class creates the objects to populate the TableView cells
The is the second version new functions were added.
An Add player function was added. This function is performed by the addplayerbutton.
A delete player function was added. This function is performed by the deleteplayerbutton. The button does not 
execute code unless checkbox is checked.  If it is not checked but the deleted button is pressed a alert warning 
is shown. The delete statement is committed on the player id.
A show all records button was added to display the entire database.
A clear button was added to clear the table view.
Things I would like to improve in a future version.
Show player images and video clips when player query is performed
Improve validation for user input.
Rebuild database to use auto-numbering of for record id which is the primary key for each record. 

