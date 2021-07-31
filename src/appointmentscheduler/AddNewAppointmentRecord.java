package appointmentscheduler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The AddNewAppointmentRecord is used to handle the scenarios for adding new
 * appointments or updating the existing one.
 *
 * @author
 */
public class AddNewAppointmentRecord extends Stage {

    TextField id;

    /**
     * This constructor is used to load the existing appointment details in the
     * fields in update scenario where as displays empty fields while adding new
     * appointment.
     *
     * @param c This is used to hold existing appointment details in update
     * scenario and empty Appointment instance in add appointment scenario
     */
    AddNewAppointmentRecord(Appointment c) {
        // VBox for settinp up elements in form UI
        VBox vBox = new VBox();

        vBox.setSpacing(8);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        // Declaring all required labels to be used in UI
        Label title, description, location, contact, type, startDateTime, endDateTime, customerId, userId;
        // Declaring all required text fields to be used in UI
        TextField titlefield, descriptionfield, locationfield, typefield, startDateTimefield, endDateTimefield, customerIdfield, userIdfield;

        title = new Label("Appointment  Title");
        titlefield = new TextField(c.getTitle());
        description = new Label("Description");
        descriptionfield = new TextField(c.getDescription());
        location = new Label("Location");
        locationfield = new TextField(c.getLocation());
        contact = new Label("Contact");
        type = new Label("Type");
        typefield = new TextField(c.getType());
        startDateTime = new Label("Start date & time i.e 2021-12-31 10:00:00 ");
        // if appointment is new set date fields empty
        if (c.getStartDateTime() == null) {
            startDateTimefield = new TextField(c.getStartDateTime());
        } // if appointment is existing, convert the UTC time extracted from database to user's local time.
        else {
            startDateTimefield = new TextField(TimeConversionMethods.toLocal(c.getStartDateTime()).replace('T', ' ').substring(0, 16) + ":00");
        }
        endDateTime = new Label("End date & time i.e 2021-12-31 11:00:00");
        // if appointment is new set date fields empty
        if (c.getEndDateTime() == null) {
            endDateTimefield = new TextField(c.getEndDateTime());
        } // if appointment is existing, convert the UTC time extracted from database to user's local time.
        else {
            endDateTimefield = new TextField(TimeConversionMethods.toLocal(c.getEndDateTime()).replace('T', ' ').substring(0, 16) + ":00");
        }
        customerId = new Label("Customer id");
        customerIdfield = new TextField(c.getCustomerId() + "");
        userId = new Label("User id");
        userIdfield = new TextField(c.getUserId() + "");

        // Combobox for showing contacts availabe in the database
        ComboBox contacts = new ComboBox();
        // Getting contact list from the database using getContacts() method
        ArrayList contactNames = getContacts();
        // Populating contacts combobox with values retrieved from getContacts() method
        for (int i = 0; i < contactNames.size(); i++) {
            contacts.getItems().add(contactNames.get(i));
        }
        // setting previous contact value for the appointment
        contacts.setValue(c.getContact());
        // Button used for adding new appointment operation
        Button add = new Button("Add Appointment");
        // Button used for updating existing appointment operation
        Button update = new Button("Update Appointment");
        // Button used for navigating back to appointments menu 
        Button back = new Button("Back to appointments main");
        // If passed appointment is empty, create all UI elements excepty ID
        if (c.id == 0) {

            // Adding UI elements to the VBox structure
            vBox.getChildren().addAll(
                    title, titlefield, description, descriptionfield, location, locationfield,
                    contact, contacts, type, typefield, startDateTime, startDateTimefield, endDateTime,
                    endDateTimefield, customerId, customerIdfield, userId, userIdfield, add, back);

            this.setTitle("Add Appointment");

        } // If passed appointment is existing, create all UI elements with ID too
        else {
            id = new TextField(c.getId() + "");
            // Disabling id field
            id.setEditable(false);
            // Adding UI elements to the VBox structure
            vBox.getChildren().addAll(
                    new Label("Id"),
                    id, title, titlefield, description, descriptionfield, location, locationfield,
                    contact, contacts, type, typefield, startDateTime, startDateTimefield, endDateTime,
                    endDateTimefield, customerId, customerIdfield, userId, userIdfield,
                    update,
                    back);

            this.setTitle("Update Appointments");
        }
        // Setting action events on the add button using lambda expression
        add.setOnAction((e) -> {
            // After add button is clicked, if any of the required field is empty then prompt to user about putting valid input
            if (titlefield.getText() == null || descriptionfield.getText().isEmpty() || locationfield.getText() == null || contacts.getValue().toString().equals("") || typefield.getText().isEmpty() || startDateTimefield.getText().isEmpty() || endDateTimefield.getText().isEmpty() || customerIdfield.getText().isEmpty() || userIdfield.getText().isEmpty()) {
                Dialog<String> dialog = new Dialog<>();
                //Setting the title
                dialog.setTitle("Dialog");
                ButtonType btype = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                //Setting the content of the dialog
                dialog.setContentText("Can't leave any field empty. Please fill all required data.");
                //Adding buttons to the dialog pane
                dialog.getDialogPane().getButtonTypes().add(btype);
                dialog.showAndWait();
            } // Now checking if given appointment times doesn't overlap the times set up for existing appointments
            else if (isAppointmentTimeOverlapping(startDateTimefield.getText(), endDateTimefield.getText())) {
                Dialog<String> dialog = new Dialog<>();

                dialog.setTitle("Dialog");
                ButtonType btype = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                // Setting custom messgage
                dialog.setContentText("Current appointment times are overlapping with another appointment, please try another one.");

                dialog.getDialogPane().getButtonTypes().add(btype);
                dialog.showAndWait();
            } // If all fields are non empty and appointment times are not overlapping then finally add the appointment to the database
            else {
                Appointment cus = new Appointment();
                cus.setTitle(titlefield.getText());
                cus.setDescription(descriptionfield.getText());
                cus.setLocation(locationfield.getText());
                cus.setContact(contacts.getValue().toString());
                cus.setType(typefield.getText());
                // Converting input start date time to UTC time zone then passing on to store to the database 
                cus.setStartDateTime(TimeConversionMethods.toUTC(startDateTimefield.getText()).substring(0, TimeConversionMethods.toUTC(startDateTimefield.getText()).length() - 6));

                // Converting input end date time to UTC time zone then passing on to store to the database
                cus.setEndDateTime(TimeConversionMethods.toUTC(endDateTimefield.getText()).substring(0, TimeConversionMethods.toUTC(endDateTimefield.getText()).length() - 6));
                cus.setCustomerId(Integer.parseInt(customerIdfield.getText()));
                cus.setUserId(Integer.parseInt(userIdfield.getText()));

                // Check if appointment is succesfully added to the database
                boolean b = addAppointment(cus);
                if (b) {
                    Dialog<String> dialog = new Dialog<>();

                    dialog.setTitle("Dialog");
                    ButtonType btype = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);

                    dialog.setContentText("Successfully added requested appointment details.");

                    dialog.getDialogPane().getButtonTypes().add(btype);
                    dialog.showAndWait();
                    titlefield.setText("");
                    locationfield.setText("");
                    descriptionfield.setText("");
                    contacts.setValue("");
                    typefield.setText("");
                    startDateTimefield.setText("");
                    endDateTimefield.setText("");
                    customerIdfield.setText("");
                    userIdfield.setText("");

                } else {
                    Dialog<String> dialog = new Dialog<>();
                    //Setting the title
                    dialog.setTitle("Dialog");
                    ButtonType btype = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                    //Setting the content of the dialog
                    dialog.setContentText("Some problem occured while adding appointment, check input details and try again.");
                    //Adding buttons to the dialog pane
                    dialog.getDialogPane().getButtonTypes().add(btype);
                    dialog.showAndWait();
                }
            }

        });
        // Adding back functionality to the back button to navigate back to Appointments Menu
        back.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
               
                    toAppointmentMenu();

              
            }
        });
        // Adding action on update button to update the appointment with current given ID
        update.setOnAction((event) -> {

            // After update button is clicked, if any of the required field is empty then prompt to user about putting valid input
            if (titlefield.getText() == null || descriptionfield.getText().isEmpty() || locationfield.getText() == null || contacts.getValue().toString().equals("") || typefield.getText().isEmpty() || startDateTimefield.getText().isEmpty() || endDateTimefield.getText().isEmpty() || customerIdfield.getText().isEmpty() || userIdfield.getText().isEmpty()) {
                Dialog<String> dialog = new Dialog<>();
                //Setting the title
                dialog.setTitle("Dialog");
                ButtonType btype = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                //Setting the content of the dialog
                dialog.setContentText("Can't leave any field empty. Please fill all required data.");
                //Adding buttons to the dialog pane
                dialog.getDialogPane().getButtonTypes().add(btype);
                dialog.showAndWait();
            } // Now checking if given appointment times doesn't overlap the times set up for existing appointments
            else if (isAppointmentTimeOverlapping(startDateTimefield.getText(), endDateTimefield.getText())) {
                Dialog<String> dialog = new Dialog<>();
                //Setting the title
                dialog.setTitle("Dialog");
                ButtonType btype = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                //Setting the content of the dialog
                dialog.setContentText("Current appointment times are overlapping with another appointment, please try another one.");
                //Adding buttons to the dialog pane
                dialog.getDialogPane().getButtonTypes().add(btype);
                dialog.showAndWait();
            } // If all fields are non empty and appointment times are not overlapping then finally add the appointment to the database
            else {
                try {
                    Appointment cus = new Appointment();
                    cus.setTitle(titlefield.getText());
                    cus.setDescription(descriptionfield.getText());
                    cus.setLocation(locationfield.getText());
                    cus.setContact(contacts.getValue().toString());
                    cus.setType(typefield.getText());

                    // COnverting given start and end date times into UST before storing into database
                    cus.setStartDateTime(TimeConversionMethods.toUTC(startDateTimefield.getText()).substring(0, TimeConversionMethods.toUTC(startDateTimefield.getText()).length() - 6));
                    cus.setEndDateTime(TimeConversionMethods.toUTC(endDateTimefield.getText()).substring(0, TimeConversionMethods.toUTC(endDateTimefield.getText()).length() - 6));
                    cus.setCustomerId(Integer.parseInt(customerIdfield.getText()));
                    cus.setUserId(Integer.parseInt(userIdfield.getText()));
                    cus.setId(Integer.parseInt(id.getText()));
                    // Check if appointment is succesfully added to the database with updated values
                    boolean b = updateAppointment(cus);
                    if (b) {
                        Dialog<String> dialog = new Dialog<>();
                        //Setting the title
                        dialog.setTitle("Dialog");
                        ButtonType btype = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                        //Setting the content of the dialog
                        dialog.setContentText("Successfully updated requested appointment details.");
                        //Adding buttons to the dialog pane
                        dialog.getDialogPane().getButtonTypes().add(btype);
                        dialog.showAndWait();
                        titlefield.setText("");
                        locationfield.setText("");
                        descriptionfield.setText("");
                        contacts.setValue("");
                        typefield.setText("");
                        startDateTimefield.setText("");
                        endDateTimefield.setText("");
                        customerIdfield.setText("");
                        userIdfield.setText("");
                        id.setText("");

                    } else {
                        Dialog<String> dialog = new Dialog<>();
                        //Setting the title
                        dialog.setTitle("Dialog");
                        ButtonType btype = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                        //Setting the content of the dialog
                        dialog.setContentText("Some problem occured while updating appointment, check input details and try again.");
                        //Adding buttons to the dialog pane
                        dialog.getDialogPane().getButtonTypes().add(btype);
                        dialog.showAndWait();

                    }
                } catch (Exception ex) {

                }
            }

        });
        Scene scene = new Scene(vBox, 500, 650);
        this.setScene(scene);
        this.setTitle("Appointment operations");
        this.setResizable(false);

    }

    /**
     * This method is used to move back to appointment menu form, when back
     * button is clicked
     */
    public void toAppointmentMenu() {

        // Creating AppointmentsMenu stage to show up
        AppointmentsMenu m = new AppointmentsMenu();
        m.show();
        // Making current form invisible
        this.hide();

    }

    /**
     * This method is for adding new appointment to the database
     *
     * @param c this parameter refers to the newly created appointment details
     * which needs to be stored in database.
     * @return true if new appointment is successfully added, otherwise false
     */
    public boolean addAppointment(Appointment c) {
        Connection con = DbProvider.makeConnection();
        Statement stmt;

        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select contact_id from contacts where contact_name='"+c.getContact()+"'");
            rs.next();
            String query = "insert into appointments(title,description,location,Contact_ID,type,start,end,Customer_Id,User_ID) values('" + c.getTitle() + "','" + c.getDescription() + "','" + c.getLocation() + "',"+rs.getInt("Contact_Id")+",'"+ c.getType() + "','" + c.getStartDateTime() + "','" + c.getEndDateTime() + "','" + c.getCustomerId() + "','" + c.getUserId() + "');";
            System.out.println(query);
            int a = stmt.executeUpdate(query);
            if (a == 0) {
                return false;
            }

        } catch (SQLException ex) {
             ;
            return false;
        }
        return true;
    }

    /**
     * This method is for adding updated appointment to the database
     *
     * @param c this parameter refers to the updated appointment details which
     * needs to be stored in database.
     * @return true if new appointment is successfully updated, otherwise false
     */
    public boolean updateAppointment(Appointment c) {
        Connection con = DbProvider.makeConnection();
        Statement stmt;

        try {
            stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("select contact_id from contacts where contact_name='"+c.getContact()+"'");
            rs.next();
            String query = "update appointments set title='" + c.getTitle() + "',description='" + c.getDescription() + "',location='" + c.getLocation() + "',Contact_ID='" + rs.getInt("Contact_ID") + "',type='" + c.getType() + "',start='" + c.getStartDateTime() + "',end='" + c.getEndDateTime() + "',Customer_ID=" + c.getCustomerId() + ",User_ID=" + c.getUserId() + " where Appointment_ID=" + c.getId() + ";";

            System.out.println(query);
            int a = stmt.executeUpdate(query);
            if (a == 0) {
                return false;
            }

        } catch (SQLException ex) {
             ;
            return false;
        }
        return true;
    }

    /**
     * This method is used to confirm that given dates for new appointment don't
     * overlap the existing appointments' schedule
     *
     * @param start This parameter refers to the start date of new appointment
     * to be added
     * @param end This parameter refers to the end date of new appointment to be
     * added
     * @return it returns true if overlapping is happening otherwise, false
     */
    public static boolean isAppointmentTimeOverlapping(String start, String end) {
        try {
            Connection con = DbProvider.makeConnection();
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select start,end from appointments");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(sdf.parse(TimeConversionMethods.toUTC(start).substring(0, TimeConversionMethods.toUTC(start).length() - 6).replace('T', ' ') + ":00"));
            Calendar c2 = Calendar.getInstance();
            System.out.println("timestamp:" + end);
            c2.setTime(sdf.parse(TimeConversionMethods.toUTC(end).substring(0, TimeConversionMethods.toUTC(end).length() - 6).replace('T', ' ') + ":00"));
            while (rs.next()) {
                Calendar cal1 = Calendar.getInstance();

                cal1.setTime(sdf.parse(rs.getString("start")));
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(sdf.parse(rs.getString("end")));

                if (c1.after(cal1) && c1.before(cal2) || c2.after(cal1) && c2.before(cal2)) {
                    return true;
                }

            }
        } catch (SQLException ex) {
            return false;
        } catch (ParseException ex) {
            return false;
        }
        return false;
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
            ResultSet rs = st.executeQuery("Select Contact_Name from contacts");
            while (rs.next()) {
                list.add(rs.getString("Contact_Name"));

            }

        } catch (SQLException ex) {
            return list;
        }
        return list;
    }
}
