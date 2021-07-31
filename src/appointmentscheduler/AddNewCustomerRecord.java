package appointmentscheduler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import javafx.collections.FXCollections;
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
 */
public class AddNewCustomerRecord extends Stage {

    String language = Locale.getDefault().getLanguage();
    ComboBox divisions;
    TextField id;

    /**
     * This constructor is used to load the existing customer details in the
     * fields in update scenario where as displays empty fields while adding new
     * customer.
     *
     * @param c This is used to hold existing customer details in update
     * scenario and empty Customer instance in add customer scenario
     */
    AddNewCustomerRecord(Customer c) {
        // VBox for settinp up elements in form UI
        VBox vBox = new VBox();

        vBox.setSpacing(8);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setPadding(new Insets(10, 10, 10, 10));

        // Declaring all required labels to be used in UI
        Label name, address, postal, phone, country, division;
        // Declaring all required text fields to be used in UI

        TextField namefield, addressfield, phonefield, postalfield;

        name = new Label("Customer  Name");
        namefield = new TextField(c.getName());
        address = new Label("Customer Address i.e 123 ABC Street, White Plains");
        addressfield = new TextField(c.getAddress());
        postal = new Label("Postal Code");
        postalfield = new TextField(c.getPostalCode());
        phone = new Label("Phone no");
        phonefield = new TextField(c.getPhone());
        country = new Label("Country");
        division = new Label("State/Province");

        // Populating countries combo box with countries to be used in the customer details
        ComboBox countries = new ComboBox();
        countries.getItems().add("US");
        countries.getItems().add("Canada");
        countries.getItems().add("UK");
        countries.setValue(c.getCountry());

        // Populating divisions used with US in the customer details
        ComboBox usDivisions = new ComboBox();
        usDivisions.getItems().add("California");
        usDivisions.getItems().add("Texas");
        usDivisions.getItems().add("Florida");
        usDivisions.getItems().add("Alaska");
        usDivisions.getItems().add("Virginia");
        usDivisions.getItems().add("Washington");
        usDivisions.getItems().add("Arizona");
        usDivisions.getItems().add("Ohio");
        usDivisions.getItems().add("Georgia");
        usDivisions.getItems().add("Colorado");

        // Populating divisions used with UK in the customer details
        ComboBox ukDivisions = new ComboBox();

        ukDivisions.getItems().add("England");
        ukDivisions.getItems().add("Scotland");
        ukDivisions.getItems().add("Wales");
        ukDivisions.getItems().add("Northern Ireland");

        // Populating divisions used with Canada in the customer details
        ComboBox canadaDivisions = new ComboBox();
        canadaDivisions.getItems().add("Alberta");
        canadaDivisions.getItems().add("British Columbia");
        canadaDivisions.getItems().add("Manitoba");
        canadaDivisions.getItems().add("New Brunswich");
        canadaDivisions.getItems().add("Newfoundland and Labrador");
        canadaDivisions.getItems().add("Northwest territories");
        canadaDivisions.getItems().add("Nova Scotia");
        canadaDivisions.getItems().add("Nunavut");

        // Combobox to be populated with divisions of currently selected country
        divisions = new ComboBox();

        divisions.setValue(c.getDivision());

        // Implementing event listener when clicked on countries combo box
        EventHandler<ActionEvent> event
                = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                // if selected country is US, populate divisions combobox with usdivisions
                if (countries.getValue().equals("US")) {
                    System.out.println("in US");
                    divisions.setItems(FXCollections.observableArrayList(usDivisions.getItems()));
                    divisions.setValue("");
                } // if selected country is UK, populate divisions combobox with ukdivisions
                else if (countries.getValue().equals("UK")) {
                    divisions.setItems(FXCollections.observableArrayList(ukDivisions.getItems()));
                    divisions.setValue("");

                } // if selected country is Canada, populate divisions combobox with canadadivisions
                else if (countries.getValue().equals("Canada")) {
                    divisions.setItems(FXCollections.observableArrayList(canadaDivisions.getItems()));
                    divisions.setValue("");

                }
            }
        };

        // adding above event on countries box
        countries.setOnAction(event);

        Button add = new Button("Add Customer");
        Button update = new Button("Update Customer");
        Button back = new Button("Back to main");
        // If passed customer is empty, create all UI elements excepty ID

        if (c.id == 0) {
            vBox.getChildren().addAll(
                    name,
                    namefield,
                    address,
                    addressfield,
                    country,
                    countries,
                    division,
                    divisions,
                    postal,
                    postalfield,
                    phone,
                    phonefield,
                    add,
                    back);

            this.setTitle("Add Customers");

        } // If passed cutomer is existing, create all UI elements with ID too
        else {
            id = new TextField(c.getId() + "");
            // Disabling id field
            id.setEditable(false);
            vBox.getChildren().addAll(
                    new Label("Id"),
                    id,
                    name,
                    namefield,
                    address,
                    addressfield,
                    country,
                    countries,
                    division,
                    divisions,
                    postal,
                    postalfield,
                    phone,
                    phonefield,
                    update,
                    back);

            this.setTitle("Update Customers");
        }
        // Setting action events on the add button using lambda expression
        add.setOnAction((e) -> {
            System.out.println(namefield.getText());
            System.out.println(addressfield.getText());
            System.out.println(countries.getValue());
            System.out.println(divisions.getValue());
            System.out.println(postalfield.getText());
            System.out.println(phonefield.getText());
            // After add button is clicked, if any of the required field is empty then prompt to user about putting valid input
            if (namefield.getText() == null || namefield.getText().isEmpty() || addressfield.getText() == null || addressfield.getText().isEmpty() || countries.getValue() == null || countries.getValue().toString().isEmpty() || divisions.getValue() == null || divisions.getValue().toString().isEmpty() || postalfield.getText().isEmpty() || phone.getText().isEmpty()) {
                Dialog<String> dialog = new Dialog<>();
                //Setting the title
                dialog.setTitle("Dialog");
                ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                //Setting the content of the dialog
                dialog.setContentText("Can't leave any field empty. Please fill all required data.");
                //Adding buttons to the dialog pane
                dialog.getDialogPane().getButtonTypes().add(type);
                dialog.showAndWait();
            } // If all fields are non empty, add customer to the database
            else {
                Customer cus = new Customer();
                cus.setAddress(addressfield.getText());
                cus.setCountry(countries.getValue().toString());
                cus.setDivision(divisions.getValue().toString());
                cus.setName(namefield.getText());
                cus.setPostalCode(postalfield.getText());
                cus.setPhone(phonefield.getText());

                // Check if appointment is succesfully added to the database
                boolean b = addCustomer(cus);
                if (b) {
                    Dialog<String> dialog = new Dialog<String>();
                    //Setting the title
                    dialog.setTitle("Dialog");
                    ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                    //Setting the content of the dialog
                    dialog.setContentText("Successfully added requested customer details.");
                    //Adding buttons to the dialog pane
                    dialog.getDialogPane().getButtonTypes().add(type);
                    dialog.showAndWait();
                    namefield.setText("");
                    addressfield.setText("");
                    postalfield.setText("");
                    phonefield.setText("");
                    id.setText("");
                    countries.setValue("Select any country");
                    divisions.setValue("");
                }
                else{
                      Dialog<String> dialog = new Dialog<String>();
                    //Setting the title
                    dialog.setTitle("Dialog");
                    ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                    //Setting the content of the dialog
                    dialog.setContentText("Some problem occured while adding customer, check input data and try again.");
                    //Adding buttons to the dialog pane
                    dialog.getDialogPane().getButtonTypes().add(type);
                    dialog.showAndWait();
                }
            }

        });
        // Adding back functionality to the back button to navigate back to Customers Menu
        back.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (language.equals("en")) {
                    System.out.println("back to main");
                    toCustomerMenu();

                } else if (language.equals("fr")) {

                    System.out.println("manage customers");

                }
            }
        });
        // Adding action on update button to update the customer with current given ID
        update.setOnAction((event2) -> {
            // After update button is clicked, if any of the required field is empty then prompt to user about putting valid input

            if (namefield.getText() == null || namefield.getText().isEmpty() || addressfield.getText() == null || addressfield.getText().isEmpty() || countries.getValue() == null || countries.getValue().toString().isEmpty() || divisions.getValue() == null || divisions.getValue().toString().isEmpty() || postalfield.getText().isEmpty() || phone.getText().isEmpty()) {
                Dialog<String> dialog = new Dialog<>();
                //Setting the title
                dialog.setTitle("Dialog");
                ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                //Setting the content of the dialog
                dialog.setContentText("Can't leave any field empty. Please fill all required data.");
                //Adding buttons to the dialog pane
                dialog.getDialogPane().getButtonTypes().add(type);
                dialog.showAndWait();
            } // If all fields are non empty, add customer to the database
            else {
                Customer cus = new Customer();
                cus.setId(Integer.parseInt(id.getText()));
                cus.setAddress(addressfield.getText());
                cus.setCountry(countries.getValue().toString());
                cus.setDivision(divisions.getValue().toString());
                cus.setName(namefield.getText());
                cus.setPostalCode(postalfield.getText());
                cus.setPhone(phonefield.getText());
                boolean b = updateCustomer(cus);
                if (b) {
                    Dialog<String> dialog = new Dialog<>();
                    //Setting the title
                    dialog.setTitle("Dialog");
                    ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                    //Setting the content of the dialog
                    dialog.setContentText("Successfully updated requested customer details.");
                    //Adding buttons to the dialog pane
                    dialog.getDialogPane().getButtonTypes().add(type);
                    dialog.showAndWait();
                    namefield.setText("");
                    addressfield.setText("");
                    postalfield.setText("");
                    phonefield.setText("");

                    countries.setValue("Select any country");
                    divisions.setValue("");
                    id.setText("");
                }
                else{
                     Dialog<String> dialog = new Dialog<>();
                    //Setting the title
                    dialog.setTitle("Dialog");
                    ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                    //Setting the content of the dialog
                    dialog.setContentText("Some problems occured while updating customer, check type of input data and try again.");
                    //Adding buttons to the dialog pane
                    dialog.getDialogPane().getButtonTypes().add(type);
                    dialog.showAndWait();
                  
                }
            }

        });
        Scene scene = new Scene(vBox, 500, 500);
        this.setScene(scene);
        this.setTitle("Customer operations");
        this.setResizable(false);

    }

    /**
     * This method is used to get back to the CustomersMenu, hide the current
     * frame
     */
    public void toCustomerMenu() {

        CustomerMenu m = new CustomerMenu();
        m.show();
        this.hide();

    }

    /**
     * This method is used to add customer to the database
     *
     * @param c This parameter refers to the customer needed to be stored
     * @return true if customer is added, false otherwise
     */
    public boolean addCustomer(Customer c) {
        Connection con = DbProvider.makeConnection();
        Statement stmt;

        try {
            stmt = con.createStatement();
            ResultSet rs =stmt.executeQuery("Select * from first_level_divisions where Division='"+c.getDivision()+"'");
            rs.next();
            int div_id = rs.getInt("Division_ID");
            String query = "insert into customers(CUSTOMER_Name,Address,Postal_Code,Phone,Division_ID) values('" + c.getName() + "','" + c.getAddress() + "','" + c.getPostalCode() + "','" + c.getPhone() + "','" + div_id+"');";
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
     * This method is for adding updated customer to the database
     *
     * @param c this parameter refers to the updated customer details which
     * needs to be stored in database.
     * @return true if new customer is successfully updated, otherwise false
     */
    public boolean updateCustomer(Customer c) {
        Connection con = DbProvider.makeConnection();
        Statement stmt;

        try {
            stmt = con.createStatement();
            
            ResultSet rs =stmt.executeQuery("Select * from first_level_divisions where Division='"+c.getDivision()+"'");
            rs.next();
            int div_id = rs.getInt("Division_ID");
            String query = "update customers set Customer_Name='" + c.getName() + "',address='" + c.getAddress() + "',postal_code='" + c.getPostalCode() + "',phone='" + c.getPhone() + "',Division_ID="+div_id+" where Customer_ID=" + c.getId();
            ;
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

}
