/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentscheduler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * This class contains all the buttons to generate reports for particular
 * statistics
 *
 * @author
 */
public class ReportsForm extends Stage {

    String type, month, year;
    Optional<String> result, result2;

    /**
     * Constructor used to display ReportsForm when this class is instantiated.
     */
    ReportsForm() {
        Button report1 = null, report2 = null, report3 = null, backToMain = null;

        // button for generating reports of appointments regarding type and month
        report1 = new Button("Display number of customer appointments filtered with type and month");
        report1.setMinWidth(500);
        report1.setFont(new Font("Open Sans", 15));
        report1.setStyle("-fx-background-color: #EB984E ");
        report1.setTextFill(Color.WHITE);

        // button for generating reports of appointments regarding contacts of organization
        report2 = new Button("View contacts of organization's schedule");
        report2.setMinWidth(500);
        report2.setFont(new Font("Open Sans", 15));
        report2.setStyle("-fx-background-color: #EB984E ");
        report2.setTextFill(Color.WHITE);

        
        // button for generating reports of appointments handled in any particular year
        report3 = new Button("Display no: of appointments per particular year");
        report3.setMinWidth(500);
        report3.setFont(new Font("Open Sans", 15));
        report3.setStyle("-fx-background-color: #EB984E ");
        report3.setTextFill(Color.WHITE);

        // Button for naviagting back to menu
        backToMain = new Button("Back to starting Menu");
        backToMain.setMinWidth(500);
        backToMain.setFont(new Font("Open Sans", 15));
        backToMain.setStyle("-fx-background-color: #EB984E ");
        backToMain.setTextFill(Color.WHITE);

        report1.setOnAction((event) -> {
            // Setting up dialog for taking appointment month as input
            TextInputDialog td = new TextInputDialog();
            td.setHeaderText("Enter appointments month no i.e (01-12) for report details: ");

            result = td.showAndWait();

            // take the input and store in the month variable
            result.ifPresent(input -> month = input);

            // Setting up dialog for taking month input 
            TextInputDialog td2 = new TextInputDialog();
            td2.setHeaderText("Enter appointments type for report details: ");

            result2 = td2.showAndWait();

            // take the input and store in the type variable
            result2.ifPresent(input -> type = input);

            int numberOfAppointments = getAppointmentsNumber(month, type);

            Dialog<String> dialog = new Dialog<>();
            //Setting the title
            dialog.setTitle("Dialog");
            ButtonType type2 = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            //Setting the content of the dialog
            dialog.setContentText("There are in total " + numberOfAppointments + " appointments in month " + month + ", with type='" + type + "'.");
            //Adding buttons to the dialog pane
            dialog.getDialogPane().getButtonTypes().add(type2);
            dialog.showAndWait();
        });

        report2.setOnAction((event) -> {
            DisplayContactSchedules sc = new DisplayContactSchedules();
            sc.show();
            tohide();
        });

        report3.setOnAction((event) -> {
            // Setting up dialog for taking year as input 
            TextInputDialog td = new TextInputDialog();
            td.setHeaderText("Enter year you want to check appointments for e.g. 2021:  ");

            result = td.showAndWait();

            // take the input and store in the year variable
            result.ifPresent(input -> year = input);

            int numberOfAppointments = getAppointmentsNumberByYear(year);

            Dialog<String> dialog = new Dialog<>();
            //Setting the title
            dialog.setTitle("Dialog");
            ButtonType type2 = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            //Setting the content of the dialog
            dialog.setContentText("There are in total " + numberOfAppointments + " appointments dealt with in year " + year + ".");
            //Adding buttons to the dialog pane
            dialog.getDialogPane().getButtonTypes().add(type2);
            dialog.showAndWait();
        });

        // setting up action on back button
        backToMain.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                // Move back to main menu and hide this frame
                toMainMenu();

            }
        });
        VBox vBox = new VBox();

        vBox.setSpacing(5);
        vBox.setAlignment(Pos.CENTER);
        vBox.setMargin(report1, new Insets(20, 20, 20, 20));
        vBox.setMargin(report2, new Insets(20, 20, 20, 20));
        vBox.setMargin(report3, new Insets(20, 20, 20, 20));
        vBox.setMargin(backToMain, new Insets(20, 20, 20, 20));
        //retrieving the observable list of the VBox 
        ObservableList list = vBox.getChildren();

        //Adding all the nodes to the observable list 
        list.addAll(report1, report2, report3, backToMain);

        this.setTitle("Reports Form");
        Scene scene = new Scene(vBox, 700, 400);
        this.setScene(scene);
        
        this.setResizable(false);

    }

    /**
     * This method is used to provide data of appointments filtered with particular month and type
     * @param month parameter denotes the month for which we are searching appointments
     * @param type paramteter denotes the type for which we are searching appointments
     * @return 
     */
    public static int getAppointmentsNumber(String month, String type) {

        int count = 0;
        if (month.length() == 1) {
            month = "0" + month;
        }
        try {
            Connection con = DbProvider.makeConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from appointments where start like '_____" + month + "____________' and type ='" + type + "'");

            while (rs.next()) {
                count++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReportsForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    
    /**
     * This method is used to get number of total appointments dealt with in any particular year
     * @param year this parameter denotes the year for which we are getting statistics.
     * @return 
     */
    public static int getAppointmentsNumberByYear(String year) {

        int count = 0;
        try {
            Connection con = DbProvider.makeConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from appointments where start like '" + year + "_______________'");

            while (rs.next()) {
                count++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReportsForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    /**
     * This method is used to navigate back to MainMenuForm and hiding current
     * form
     */
    public void toMainMenu() {

        StartingMenu m = new StartingMenu();
        m.show();
        tohide();

    }

    /**
     * This method is used to hide current form
     */
    void tohide() {
        this.hide();
    }

}
