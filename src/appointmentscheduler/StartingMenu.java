package appointmentscheduler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * This class represents the Main menu form which navigate user to the all
 * possible operations after logging in.
 *
 * @author
 */
public class StartingMenu extends Stage {

    /**
     * Constructor to initialize the required UI elements to display in the form
     */
    public StartingMenu() {
        // Check if there are any appointments in upcoming 15 mins
        Appointment ap = checkRecentAppointments();

        // If have, show a custom message with appointment id and date
        if (ap != null) {
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("Dialog");
            ButtonType btype = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            //Setting the content of the dialog
            dialog.setContentText("You have a appointment in next 15 minutes, with Id " + ap.getId() + ", Date:" + ap.getStartDateTime().replace('T', ' ').substring(0, 16) + ".");
            //Adding buttons to the dialog pane
            dialog.getDialogPane().getButtonTypes().add(btype);
            dialog.showAndWait();

        } // If not, show a custom message of not having any upcoming appointments
        else {
            Dialog<String> dialog = new Dialog<>();
            //Setting the title
            dialog.setTitle("Dialog");
            ButtonType btype = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            //Setting the content of the dialog
            dialog.setContentText("There are no upcoming appointments");
            //Adding buttons to the dialog pane
            dialog.getDialogPane().getButtonTypes().add(btype);
            dialog.showAndWait();
        }

        // Initializing UI buttons required in the form
        Button manageCustomers = null, manageAppointments = null, exitprogram = null,checkReports=null;

        Scene scene = null;

        manageCustomers = new Button("Operate Customers");
        manageCustomers.setMinWidth(200);
        manageCustomers.setFont(new Font("Open Sans",15));
        manageCustomers.setStyle("-fx-background-color: #EB984E ");
        manageCustomers.setTextFill(Color.WHITE);

        
        
        manageAppointments = new Button("Operate Appointments");
        manageAppointments.setMinWidth(200);
        manageAppointments.setFont(new Font("Open Sans",15));
        manageAppointments.setTextFill(Color.WHITE);
        manageAppointments.setStyle("-fx-background-color: #EB984E ");
        
        checkReports = new Button("Check reports");
        checkReports.setMinWidth(200);
        checkReports.setTextFill(Color.WHITE);
        checkReports.setFont(new Font("Open Sans",15));
        checkReports.setStyle("-fx-background-color: #EB984E ");
        
        exitprogram = new Button("Exit ");
        exitprogram.setMinWidth(200);
        exitprogram.setTextFill(Color.WHITE);
        exitprogram.setFont(new Font("Open Sans",15));
        exitprogram.setStyle("-fx-background-color: #EB984E ");
        

        // Setting action on manage customers button
        manageCustomers.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                // Move to the CustomersMenu form
                toCustomerMenu();

            }
        });

        // Setting action on manage appointments button
        manageAppointments.setOnAction((event) -> {
            // Move to the AppointmentsMenu form
            toAppointmentMenu();

        });
        
        checkReports.setOnAction((event) -> {
            toReportsMenu();
        });

        // Setting action on exit program button 
        exitprogram.setOnAction((event) -> {
            // Sign out the user from the system
            toLogin();
        });
        VBox vBox = new VBox();

        vBox.setSpacing(5);
        vBox.setAlignment(Pos.CENTER);
        vBox.setMargin(manageCustomers, new Insets(20, 20, 20, 20));
        vBox.setMargin(manageAppointments, new Insets(20, 20, 20, 20));
        vBox.setMargin(checkReports, new Insets(20, 20, 20, 20));
        vBox.setMargin(exitprogram, new Insets(20, 20, 20, 20));
        //retrieving the observable list of the VBox 
        ObservableList list = vBox.getChildren();
//Color.
        //Adding all the nodes to the observable list 
        list.addAll(manageCustomers, manageAppointments,checkReports, exitprogram);

        scene = new Scene(vBox, 500, 350);
        this.setTitle("Main menu");
        this.setScene(scene);
        
        this.setResizable(false);

    }

    /**
     * This method is used to move user to CustomersMenu and hide current form
     */
    public void toCustomerMenu() {
        CustomerMenu c = new CustomerMenu();
        c.show();
        this.hide();

    }

    /**
     * This method is used to move user to AppointmentsMenu and hide current
     * form
     */
    public void toAppointmentMenu() {

        AppointmentsMenu am = new AppointmentsMenu();
        am.show();
        this.hide();

    }
    
    public void toReportsMenu(){
        ReportsForm r = new ReportsForm();
        r.show();
        this.hide();
    }

    /**
     * This method is used to sign user out and move back to the login form and
     * hiding this one
     */
    public void toLogin() {
        SignIn l = new SignIn();
        l.show();
        this.hide();
    }

    /**
     * This method is used to check the appointments within next 15 minutes, and
     * alert the user
     *
     * @return If user have upcoming appointments return appointment details,
     * otherwise null
     */
    private Appointment checkRecentAppointments() {
        try {
            Connection con = DbProvider.makeConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from appointments where Appointment_ID="+SignIn.id);
            while (rs.next()) {
                Calendar start_time = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

                Calendar end_time = Calendar.getInstance();
                end_time.setTime(sdf.parse(TimeConversionMethods.toLocal(rs.getString("start")).replace('T', ' ').substring(0, 16) + ":00"));

                Calendar new_start_time = Calendar.getInstance();

                new_start_time.add(Calendar.MINUTE, 15); // adding 15 minutes to the start_time
                if (new_start_time.after(end_time) && start_time.before(end_time)) {
                    Appointment a = new Appointment();
                    a.setId(rs.getInt("Appointment_ID"));
                    a.setContact(rs.getString("contact"));
                    a.setDescription(rs.getString("description"));
                    a.setTitle(rs.getString("title"));
                    a.setType(rs.getString("type"));
                    a.setLocation(rs.getString("location"));
                    a.setStartDateTime(TimeConversionMethods.toLocal(rs.getString("start")));
                    a.setEndDateTime(TimeConversionMethods.toLocal(rs.getString("end")));
                    a.setCustomerId(rs.getInt("Customer_ID"));
                    a.setUserId(rs.getInt("User_ID"));
                    return a;
                }
            }
        } catch (SQLException | ParseException ex ) {
            ex.printStackTrace();
            return null;
        }
        return null;
    }

}
