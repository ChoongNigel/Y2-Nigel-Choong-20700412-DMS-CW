package com.comp2042.SceneController.MainMenu;

import com.comp2042.GameLogic.SimpleBoard;
import com.comp2042.GameInfo.UsersFunction;
import com.comp2042.GameInfo.ViewData;
import com.comp2042.SceneController.BGControl.Background;
import com.comp2042.SceneController.BGControl.SceneChanger;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;

import java.io.IOException;

public class User_Register {

    @FXML
    private Button Back_Button;

    @FXML
    private Button Back_to_Login;

    @FXML
    private Button LogIn_Button;

    @FXML
    private PasswordField New_Confirm_Password;

    @FXML
    private PasswordField New_Password;

    @FXML
    private TextField New_Username;

    @FXML
    private PasswordField Password;

    @FXML
    private Button Sign_Up_Submit;

    @FXML
    private Hyperlink To_SignUp;

    @FXML
    private TextField Username;

    @FXML
    private MediaView background;

    @FXML
    private Pane Sign_Up_Pane;

    @FXML
    private Pane Log_In_Pane;

    public static SimpleBoard board;
    public static ViewData brick;


    @FXML
    public void initialize(){
        Background.ApplyGif(background,"GameBackground.mp4");

        Sign_Up_Submit.setOnAction(event -> {
            if (New_Password.getText().equals(New_Confirm_Password.getText())) {
                UsersFunction.RegisterUser(New_Username.getText(), New_Password.getText());
                ShowAlert("User Successful Created");
                Log_In_Pane.setVisible(true);
                Sign_Up_Pane.setVisible(false);

            }
            else{
                ShowAlert("Passwords do not match");
            }
        });

        LogIn_Button.setOnAction(event -> {
            String name=Username.getText();
            String pass=Password.getText();

            if(name.isEmpty()||pass.isEmpty()){
                ShowAlert("Please fill all the fields");
            } else if(UsersFunction.loginVerify(name, pass)) {
                try {
                    ShowAlert("Logged in Successfully");
                    UsersFunction.currentUserUpdate(name);
                    UsersFunction.UserLoggedIn=true;
                    SceneChanger.scenechange("MainMenu", "MainMenuBGM");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                ShowAlert("Incorrect username or password");
            }
        });

        To_SignUp.setOnAction(event -> {
            Log_In_Pane.setVisible(false);
            Sign_Up_Pane.setVisible(true);
        });

        Back_Button.setOnAction(event -> {
            try {
                SceneChanger.scenechange("MainMenu", "MainMenuBGM");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Back_to_Login.setOnAction(event -> {
            Sign_Up_Pane.setVisible(false);
            Log_In_Pane.setVisible(true);
        });


    }

    /**
     * When specific illegal action is took, warn the user
     * @param message
     */
    private void ShowAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
