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
import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class is used to display all the appointments present in database with
 * complete details in table view form
 *
 * @author
 */
public class ShowAppointmentRecords extends Stage {

    /**
     * Constructor to create the instance of Stage whenever initialized and
     * display the requested data
     */
    ShowAppointmentRecords() {
        // TableView representing the table in form
        TableView tableView = new TableView();

        // TableColumns representing each column in the TableVeiw 
        TableColumn<Customer, String> column1 = new TableColumn<>("Appointment_ID");
        column1.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Customer, String> column2 = new TableColumn<>("Title");
        column2.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Customer, String> column3 = new TableColumn<>("Description");
        column3.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Customer, String> column4 = new TableColumn<>("Location");
        column4.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Customer, String> column5 = new TableColumn<>("Contact");
        column5.setCellValueFactory(new PropertyValueFactory<>("contact"));

        TableColumn<Customer, String> column6 = new TableColumn<>("Type");
        column6.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Customer, String> column7 = new TableColumn<>("Start date & time");
        column7.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));

        TableColumn<Customer, String> column8 = new TableColumn<>("End date & time");
        column8.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));

        TableColumn<Customer, String> column9 = new TableColumn<>("Customer_Id");
        column9.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        // Adding above declared columns to the tableview
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);
        tableView.getColumns().add(column4);
        tableView.getColumns().add(column5);
        tableView.getColumns().add(column6);
        tableView.getColumns().add(column7);
        tableView.getColumns().add(column8);
        tableView.getColumns().add(column9);

        // Getting all the appointments data from the database using getAppointments method
        ArrayList<Appointment> appointments = getAppointments();

        // Back button initialization
        Button back = new Button("Back to appointments menu");

        // Setting up action on back button
        back.setOnAction((event) -> {
            // Hiding current form
            this.hide();
            // Initializing AppointmentsMenu to display 
            AppointmentsMenu cm = new AppointmentsMenu();
            cm.show();
        });
        // Populating tableview with all appointment details retrieved from getAppointments method above
        for (int i = 0; i < appointments.size(); i++) {
            tableView.getItems().add(appointments.get(i));
        }

        VBox box = new VBox();
        box.getChildren().addAll(tableView, back);
        Scene scene = new Scene(box, 700, 500);
        this.setScene(scene);
        this.setTitle("View appointments");
        this.show();
        
        this.setResizable(false);

    }

    /**
     * This method is used to retrieve all the appointment details from the
     * database
     *
     * @return an ArrayList of Appointment containing all the appointments
     * instances retrieved from database
     */
    public ArrayList<Appointment> getAppointments() {
        ArrayList<Appointment> appointments = null;
        try {
            appointments = new ArrayList<>();
            Connection con = DbProvider.makeConnection();
            Statement st = con.createStatement();
            Statement st2 = con.createStatement();
            Statement st3 = con.createStatement();
            ResultSet rs = st.executeQuery("select * from appointments");

            while (rs.next()) {
                Appointment a = new Appointment();
                a.setId(rs.getInt("Appointment_ID"));
                a.setContact(rs.getString("Contact_ID"));
                a.setDescription(rs.getString("description"));
                a.setTitle(rs.getString("title"));
                a.setType(rs.getString("type"));
                a.setLocation(rs.getString("location"));
                a.setStartDateTime(TimeConversionMethods.toLocal(rs.getString("start")));
                a.setEndDateTime(TimeConversionMethods.toLocal(rs.getString("end")));
                a.setCustomerId(rs.getInt("Customer_id"));
                a.setUserId(rs.getInt("User_id"));
                ResultSet rs2 = st2.executeQuery("select Contact_Name from contacts where Contact_ID="+rs.getInt("Contact_ID"));
                rs2.next();
                a.setContact(rs2.getString("Contact_Name"));
                appointments.add(a);
            }
        } catch (SQLException ex) {
            return appointments;
        }
        return appointments;
    }

}
