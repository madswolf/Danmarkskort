package bfst19.osmdrawing;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import java.io.IOException;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;

public class Controller {
    private Model model;
    double x, y;
    private double factor, oldDeterminant, zoomLevel;


    //This only means that .fxml can use this field despite visibility

    @FXML
    private MapCanvas mapCanvas;

    @FXML
    private Text scaleText;


    @FXML
    private BorderPane borderPane;


    public void init(Model model) {
        //TODO: figure out init methods
        this.model = model;
        mapCanvas.init(model);
        oldDeterminant = mapCanvas.getDeterminant();
        setScalebar();
        setUpBar();

        // update scalebar when the mxxproperty has changed. The lambda expression
        // sets the method changed from the interface (ChangeListener). mxxProperty() Defines the X coordinate scaling element of the 3x4 matrix.
        mapCanvas.transform.mxxProperty().addListener((observable, oldVal, newVal) -> {
            // sets the after mx is changed scale
            setScalebar();
        });

    }

    public void parseSearchText(String searchText){
        model.parseSearch(searchText);
    }

    public void setUpBar(){
        if(borderPane.getLeft() != null){
            borderPane.setLeft(null);
        }

        HBox hBox = null;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ViewBarPanel.fxml"));
        try {
            hBox = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        borderPane.setLeft(hBox);

        ControllerBarPanel controllerBarPanel = fxmlLoader.getController();
        controllerBarPanel.init(this);
    }


    public void setupMenuPanel(){
        if(borderPane.getLeft() != null){
            borderPane.setLeft(null);
        }


        VBox VBox = null;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ViewMenuPanel.fxml"));
        try {
            VBox = fxmlLoader.load();
        } catch (IOException event) {
            event.printStackTrace();
        }

        borderPane.setLeft(VBox);

        ControllerMenuPanel controllerMenuPanel = fxmlLoader.getController();
        controllerMenuPanel.init(this);
    }

    public void setupRutePanel() {
        if(borderPane.getLeft() != null){
            borderPane.setLeft(null);
        }

        VBox VBox = null;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ViewRutePanel.fxml"));
        try {
            VBox = fxmlLoader.load();
        } catch (IOException event) {
            event.printStackTrace();
        }

        borderPane.setLeft(VBox);

        ControllerRutePanel controllerRutePanel = fxmlLoader.getController();
        controllerRutePanel.init(this);
    }




    public void setScalebar() {
        // TODO findout and resolve getY so it can be getX, since it the te x-coor we want
        double minX = mapCanvas.getModelCoords(0, 0).getY();
        double maxX = mapCanvas.getModelCoords(0, mapCanvas.getHeight()).getY();
        double y = mapCanvas.getModelCoords(0, 0).getX();
        scaleText.setText(Scalebar.getScaleText(minX, y, maxX, y, mapCanvas.getWidth()));
    }


    @FXML
    private void onKeyPressed(KeyEvent e) {
        switch (e.getCode()) {//e.getcode() gets the specific keycode for the pressed key
            case T: //toggle so that the canvas only draws roads or similar draws everything by default
                mapCanvas.toggleNonRoads();
                mapCanvas.repaint();
                break;
            case C: //Toggle colorblind colorfile
                model.switchColorScheme();
                break;
        }
    }

    @FXML
    private void onScroll(ScrollEvent e) {
        //because scrollwheels/touchpads scroll by moving up and down the zoomfactor as calculated based on the distance moved by the "scroll"
        //The pow part is just about trial and error to find a good amount of zoom per "scroll"

        factor = Math.pow(1.01, e.getDeltaY());
        mapCanvas.zoom(factor, e.getX(), e.getY());


    }

    @FXML
    private void onMouseDragged(MouseEvent e) {
        //pans based on difference between mousePressed event and current mouse coords
        if (e.isPrimaryButtonDown()) mapCanvas.pan(e.getX() - x, e.getY() - y);
        x = e.getX();
        y = e.getY();
    }

    @FXML
    private void onMousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }


}


