package bfst19;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.IOException;

public class ControllerInfoPanel implements BackBtnEffect {

    private Controller controller;

    @FXML
    private VBox vBox;

    @FXML
    private ImageView clearBtn;

    @FXML
    private ImageView addBtn;

    @FXML
    private javafx.scene.control.Label address;

    @FXML
    private javafx.scene.control.Label latlon;

    public void init(Controller controller, String adress, double x, double y) {
        this.controller = controller;
        setAddressCoordsLabels(AutoTextField.autoTextFieldAdress,AutoTextField.x,AutoTextField.y);
    }

    @FXML
    private void setBackBtnEffect(){
        DropShadow dropShadow = new DropShadow(BlurType.ONE_PASS_BOX, Color.rgb(0,0,0,0.3), 10, 0, 0, 0);
        clearBtn.setEffect(dropShadow);
    }

    @FXML
    private void setBackBtnEffectNone(){ clearBtn.setEffect(null);}

    @FXML
    private void setAddBtnEffect(){
        DropShadow dropShadow = new DropShadow(BlurType.ONE_PASS_BOX, Color.rgb(0,0,0,0.3), 10, 0, 0, 0);
        addBtn.setEffect(dropShadow);
    }

    @FXML
    private void setAddBtnEffectNone(){ addBtn.setEffect(null);}

    @FXML
    private void clearBtnAction(){
        controller.getBorderPane().setRight(null);
    }

    private void setAddressCoordsLabels(String location, double x, double y){
        address.setText(location);
        latlon.setText("Coords: " + x + ", " + y);
    }

    public void addPointOfInterest(ActionEvent actionEvent) {
        PointOfInterestItem pointOfInterestItem = new PointOfInterestItem(AutoTextField.autoTextFieldAdress, AutoTextField.x, AutoTextField.y);
        pointOfInterestItem.init(controller);
        controller.pointOfInterestList().add(pointOfInterestItem);
    }

}
