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
 * This class represents the AppointmentsMenu which provides all available
 * operations to be performed on appointments
 *
 * @author
 */
public class AppointmentsMenu extends Stage {

    Optional<String> result;
    int id;

    /**
     * Constructor to create a JavaFx stage with required UI elements, to be
     * used to perform different operations.
     */
    AppointmentsMenu() {
        // Declaring required buttons for the form
        Button addNewAppointmentRecord = null, updateExistingAppointment = null, deleteAppointmentRecord = null, viewAppointmentRecords = null, backToStarterMenu = null;

        Scene scene;

        // Setting up above declared buttons
        viewAppointmentRecords = new Button("View all Appointments");
        viewAppointmentRecords.setMinWidth(200);
        
        viewAppointmentRecords.setFont(new Font("Open Sans",15));
        viewAppointmentRecords.setStyle("-fx-background-color: #EB984E ");
        viewAppointmentRecords.setTextFill(Color.WHITE);
        
        
        addNewAppointmentRecord = new Button("Add new Appointments");
        addNewAppointmentRecord.setMinWidth(200);
        
        addNewAppointmentRecord.setFont(new Font("Open Sans",15));
        addNewAppointmentRecord.setStyle("-fx-background-color: #EB984E ");
        addNewAppointmentRecord.setTextFill(Color.WHITE);
        

        updateExistingAppointment = new Button("Update existing Appointments");
        updateExistingAppointment.setMinWidth(200);
        
        updateExistingAppointment.setFont(new Font("Open Sans",15));
        updateExistingAppointment.setStyle("-fx-background-color: #EB984E ");
        updateExistingAppointment.setTextFill(Color.WHITE);

        deleteAppointmentRecord = new Button("Delete existing Appointments");
        deleteAppointmentRecord.setMinWidth(200);
        
        deleteAppointmentRecord.setFont(new Font("Open Sans",15));
        deleteAppointmentRecord.setStyle("-fx-background-color: #EB984E ");
        deleteAppointmentRecord.setTextFill(Color.WHITE);

        backToStarterMenu = new Button("Back to starting menu ");
        backToStarterMenu.setMinWidth(200);
        
        backToStarterMenu.setFont(new Font("Open Sans",15));
        backToStarterMenu.setStyle("-fx-background-color: #EB984E ");
        backToStarterMenu.setTextFill(Color.WHITE);
        
        
        // Setting up action on back button
        backToStarterMenu.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                // Return to the main menu
                toStartingMenu();

            }
        });

        // Setting up action on view appointment button
        viewAppointmentRecords.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                // Creating ShowAppointmentRecords frame to be displayed and hiding the current frame
                ShowAppointmentRecords va = new ShowAppointmentRecords();
                va.show();
                tohide();
            }
        });

        //Setting up action on Add appointment button
        addNewAppointmentRecord.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                // Creating Appointment frame to be displayed and hiding the current frame
                Appointment ap = new Appointment();
                AddNewAppointmentRecord a = new AddNewAppointmentRecord(ap);
                a.show();
                tohide();

            }
        });

        //Setting up action on update button
        updateExistingAppointment.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                // Preparing dialog for taking appointment id as input which needs to be updated
                TextInputDialog td = new TextInputDialog();
                td.setHeaderText("Enter id to update appointment: ");

                result = td.showAndWait();

                // Take input and save in id variable
                result.ifPresent(input -> id = Integer.parseInt(input));
                // Check if appointment with given id exists, then get it's data
                Appointment c = lookForAppointment(id);

                // if appointment doesn't exists, show some custom dialog
                if (c == null) {

                    Dialog<String> dialog = new Dialog<String>();
                    //Setting the title
                    dialog.setTitle("Dialog");
                    ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                    //Setting the content of the dialog
                    dialog.setContentText("Requested appointment doesn't exist with id:" + id + ", please try again.");
                    //Adding buttons to the dialog pane
                    dialog.getDialogPane().getButtonTypes().add(type);
                    dialog.showAndWait();
                } // If appointment is availabe get it's data and display add appointment frame with it's data
                else {
                    // display AddNewAppointmentRecord frame and hide current frame
                    AddNewAppointmentRecord ad = new AddNewAppointmentRecord(c);
                    ad.show();
                    tohide();
                }

            }
        });

        // Setting up action on delete button
        deleteAppointmentRecord.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                // Preparing dialog for taking appointment id as input which needs to be deleted
                TextInputDialog td = new TextInputDialog();
                td.setHeaderText("Enter id to delete appointment: ");

                result = td.showAndWait();
                // Get id as input and save in id variable
                result.ifPresent(input -> id = Integer.parseInt(input));
                // Check if current appointment exists 
                Appointment c = lookForAppointment(id);

                // If requested appointment doesn't exist, show custom dialog
                if (c == null) {
                    Dialog<String> dialog = new Dialog<String>();
                    //Setting the title
                    dialog.setTitle("Dialog");
                    ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                    //Setting the content of the dialog
                    dialog.setContentText("Requested appointment doesn't exist with id:" + id + ", please try again.");
                    //Adding buttons to the dialog pane
                    dialog.getDialogPane().getButtonTypes().add(type);
                    dialog.showAndWait();
                } // If it exists, delete the appointment from the database and show custom message
                if (deleteAppointmentRecord(id)) {

                    Dialog<String> dialog = new Dialog<String>();
                    //Setting the title
                    dialog.setTitle("Dialog");
                    ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                    //Setting the content of the dialog
                    dialog.setContentText("Requested appointment with id:" + id + " deleted successfully.");
                    //Adding buttons to the dialog pane
                    dialog.getDialogPane().getButtonTypes().add(type);
                    dialog.showAndWait();

                } else {
                    Dialog<String> dialog = new Dialog<>();
                    //Setting the title
                    dialog.setTitle("Dialog");
                    ButtonType btype = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                    //Setting the content of the dialog
                    dialog.setContentText("Some problem occured while deleting appointment,try again.");
                    //Adding buttons to the dialog pane
                    dialog.getDialogPane().getButtonTypes().add(btype);
                    dialog.showAndWait();
                }

            }

        });

        // VBox for putting in form elements
        VBox vBox = new VBox();

        vBox.setSpacing(5);
        vBox.setAlignment(Pos.CENTER);
        vBox.setMargin(viewAppointmentRecords, new Insets(20, 20, 20, 20));
        vBox.setMargin(addNewAppointmentRecord, new Insets(20, 20, 20, 20));
        vBox.setMargin(updateExistingAppointment, new Insets(20, 20, 20, 20));
        vBox.setMargin(deleteAppointmentRecord, new Insets(20, 20, 20, 20));
        vBox.setMargin(backToStarterMenu, new Insets(20, 20, 20, 20));

        //retrieving the observable list of the VBox 
        ObservableList list = vBox.getChildren();

        //Adding all the nodes to the observable list 
        list.addAll(viewAppointmentRecords, addNewAppointmentRecord, updateExistingAppointment, deleteAppointmentRecord, backToStarterMenu);

        scene = new Scene(vBox, 500, 500);
        this.setScene(scene);
        this.setTitle("Appointments Menu");
        
        this.setResizable(false);

    }

    /**
     * This method is used to navigate back to StartingMenu and hiding current
     * form
     */
    public void toStartingMenu() {
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

    /**
     * This method is used to check if appointment with given id exists in
     * database
     *
     * @param id referring the appointment id
     * @return Appointment details if exists, otherwise null
     */
    public Appointment lookForAppointment(int id) {
        Connection con = DbProvider.makeConnection();
        Statement stmt;
        try {
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(" select * from appointments where Appointment_ID = " + id);
            // If appointment exists, populate an Appointment object and return it
            if (rs.next()) {
                Appointment a = new Appointment();
                a.setId(rs.getInt("Appointment_ID"));
                a.setDescription(rs.getString("description"));
                a.setTitle(rs.getString("title"));
                a.setType(rs.getString("type"));
                a.setLocation(rs.getString("location"));
                a.setStartDateTime(rs.getString("start"));
                a.setEndDateTime(rs.getString("end"));
                a.setCustomerId(rs.getInt("Customer_ID"));
                a.setUserId(rs.getInt("User_ID"));
                ResultSet rs2 = stmt.executeQuery("select * from contacts where Contact_ID = "+rs.getInt("Contact_ID"));
                rs2.next();
                a.setContact(rs2.getString("Contact_Name"));

                return a;

            }

        } catch (SQLException ex) {
             ;
            return null;
        }
        return null;
    }

    /**
     * This method is used to delete appointment from the database with given id
     *
     * @param id referring appointment id which needs to be deleted
     * @return true if successfully deleted the appointment otherwise false
     */
    public boolean deleteAppointmentRecord(int id) {
        Connection con = DbProvider.makeConnection();

        Statement stmt;
        try {
            stmt = con.createStatement();

            int a = stmt.executeUpdate(" delete from appointments where Appointment_ID = " + id);
            if (a == 0) {
                return false;
            }

        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

}
