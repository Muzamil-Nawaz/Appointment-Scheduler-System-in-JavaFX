
package appointmentscheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class SignIn extends Stage {

    static int id;
    Button btn = null;
    TextField userTextField = null;
    PasswordField pwBox = null;
    String language = Locale.getDefault().getLanguage();
    FileWriter fw;
    File f;
    ZoneId zone = ZoneId.systemDefault();

    public SignIn() {
        f = new File("login_activity.txt");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(grid, 500, 250);
        System.out.println(Locale.getDefault().getLanguage());

        if (language.equals("en")) {
            Text scenetitle = new Text("Welcome user from zone id:"+zone.toString());
            
            scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            
            grid.add(scenetitle, 0, 0, 2, 1);

            Label userName = new Label("User Name:");
            grid.add(userName, 0, 1);

            userTextField = new TextField();
            grid.add(userTextField, 1, 1);

            Label pw = new Label("Password:");
            grid.add(pw, 0, 2);

            pwBox = new PasswordField();
            grid.add(pwBox, 1, 2);
            btn = new Button("Sign in");
            btn.setMinWidth(100);
            btn.setFont(new Font("Helvatica",15));
            btn.setTextFill(Color.WHITE);
            
            btn.setStyle("-fx-background-color: #000000");
            
        } else if (language.equals("fr")) {
            Text scenetitle = new Text("Bienvenue");
            scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            grid.add(scenetitle, 0, 0, 2, 1);

            Label userName = new Label("Nom d'utilisateur:");
            grid.add(userName, 0, 1);

            userTextField = new TextField();
            grid.add(userTextField, 1, 1);

            Label pw = new Label("le mot de passe:");
            grid.add(pw, 0, 2);

            pwBox = new PasswordField();
            grid.add(pwBox, 1, 2);
            btn = new Button("signe");
        }

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                if (language.equals("en")) {
                    if (userTextField.getText().isEmpty() && pwBox.getText().isEmpty()) {

                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("Username or password cannot be empty");
                        try {
                            fw = new FileWriter(f, true);
                            fw.write("Login attempt failed with empty username or password on " + java.time.LocalDateTime.now() + "\n");
                            fw.close();
                        } catch (IOException ex) {
                            Logger.getLogger(SignIn.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        User user = new User();
                        user.setName(userTextField.getText());
                        user.setPass(pwBox.getText());
                        boolean isValid = checkUser(user);
                        if (isValid) {

                            actiontarget.setFill(Color.GREEN);
                            actiontarget.setText("Signing in...");

                            try {
                                fw = new FileWriter(f, true);

                                fw.write("Login attempt successful on " + java.time.LocalDateTime.now() + "\n");
                                fw.close();
                            } catch (IOException ex) {
                                Logger.getLogger(SignIn.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            toMain();

                        } else {

                            actiontarget.setFill(Color.FIREBRICK);
                            actiontarget.setText("Invalid username or password, try again");
                            try {
                                fw = new FileWriter(f, true);
                                fw.write("Login attempt failed with invalid username password on " + java.time.LocalDateTime.now() + "\n");
                                fw.close();
                            } catch (IOException ex) {
                                Logger.getLogger(SignIn.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                } else if (language.equals("fr")) {
                    if (userTextField.getText().equals("") && pwBox.getText().equals(e)) {

                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("Le nom d'utilisateur ou le mot de passe ne peuvent pas être vides");
                    } else {
                        User user = new User();
                        user.setName(userTextField.getText());
                        user.setPass(pwBox.getText());
                        boolean isValid = checkUser(user);
                        if (isValid) {

                            actiontarget.setFill(Color.GREEN);
                            actiontarget.setText("Connectez-vous...");
                        } else {

                            actiontarget.setFill(Color.FIREBRICK);
                            actiontarget.setText("Nom d'utilisateur ou mot de passe invalide, réessayez");
                        }
                    }
                }
            }
        });
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);
        this.setTitle("Login Form");
        
        this.setResizable(false);
        this.setScene(scene);

    }

    public boolean checkUser(User user) {
        Connection con = DbProvider.makeConnection();
        Statement stmt;
        try {
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(" select * from users where User_Name='" + user.getName() + "' and Password='" + user.getPass() + "'");
            if (rs.next()) {
                id = rs.getInt("User_id");
                return true;

            }

        } catch (SQLException ex) {
            return false;
        }
        return false;
    }

    public void toMain() {
        this.hide();
        StartingMenu m = new StartingMenu();
        m.show();

    }

    public static int getId() {
        return id;
    }
}
