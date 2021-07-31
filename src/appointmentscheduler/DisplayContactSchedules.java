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

public class DisplayContactSchedules extends Stage{
    
    /**
     * Constructor to create the instance of Stage whenever initialized and
     * display the requested data
     */
    DisplayContactSchedules(){
        
       
        // TableView representing the table in form
        TableView tableView = new TableView();

        // TableColumns representing each column in the TableVeiw 
        TableColumn<Customer, String> appointmentId  = new TableColumn<>("Appointment_ID");
        appointmentId .setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Customer, String> appointmentTitle = new TableColumn<>("Title");
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Customer, String> appointmentDescription = new TableColumn<>("Description");
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Customer, String> appointmentContact = new TableColumn<>("Contact");
        appointmentContact.setCellValueFactory(new PropertyValueFactory<>("contact"));

        TableColumn<Customer, String> appointmentStartDateTime = new TableColumn<>("Start date & time");
        appointmentStartDateTime.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));

        TableColumn<Customer, String> appointmentEndDateTime = new TableColumn<>("End date & time");
        appointmentEndDateTime.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));

        TableColumn<Customer, String> appointmentCustomerId = new TableColumn<>("Customer_Id");
        appointmentCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        // Adding above declared columns to the tableview
        tableView.getColumns().add(appointmentId );
        tableView.getColumns().add(appointmentTitle);
        tableView.getColumns().add(appointmentDescription);
        tableView.getColumns().add(appointmentContact);
        tableView.getColumns().add(appointmentStartDateTime);
        tableView.getColumns().add(appointmentEndDateTime);
        tableView.getColumns().add(appointmentCustomerId);

        // Getting all the appointments data from the database using getAppointments method
        ArrayList<Appointment> appointments = getAppointments();

        // Back button initialization
        Button back = new Button("Back to reports menu");

        // Setting up action on back button
        back.setOnAction((event) -> {
            // Hiding current form
            this.hide();
            // Initializing AppointmentsMenu to display 
            ReportsForm cm = new ReportsForm();
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
        this.setTitle("Report 1");
        this.setResizable(false);
        this.show();

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
            ResultSet rs = null;
            ArrayList list = getContacts();
            for (int i = 0; i < list.size(); i++) {
                rs = st.executeQuery("select * from appointments where contact_id="+list.get(i)+"");
                if(rs.next()){
                     Appointment a = new Appointment();
                    a.setId(rs.getInt("Appointment_Id"));
                    a.setDescription(rs.getString("description"));
                    a.setTitle(rs.getString("title"));
                    a.setStartDateTime(TimeConversionMethods.toLocal(rs.getString("start")));
                    a.setEndDateTime(TimeConversionMethods.toLocal(rs.getString("end")));
                    a.setCustomerId(rs.getInt("Customer_ID"));
                    ResultSet rs2 = st.executeQuery("Select Contact_name from contacts where contact_id="+list.get(i));
                    rs2.next();
                    a.setContact(rs2.getString("contact_name"));
                    
                    appointments.add(a);
                }
            }
           
        } catch (SQLException ex) {
             ;
            return appointments;
        }
        return appointments;
    }
    
    
    /**
     * This method is used to get the contact names stored from the database
     *
     * @return ArrayList containing contact names from the database
     */
    public static ArrayList getContacts() {
        ArrayList list = new ArrayList();
        try {
            Connection con = DbProvider.makeConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("Select Contact_ID from contacts");
            while (rs.next()) {
                list.add(rs.getString("Contact_ID"));

            }

        } catch (SQLException ex) {
            return list;
        }
        return list;
    }

}
