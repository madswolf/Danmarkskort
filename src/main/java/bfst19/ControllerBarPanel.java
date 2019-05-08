package bfst19;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class ControllerBarPanel {

	Controller controller;
	@FXML
	private Button menuButton;
	@FXML
	private AutoTextField autoTextField;
	@FXML
	private Button searchButton;

	public void init(Controller controller) {
		this.controller = controller;
		setMenuButton();
		setSearchButton();
		autoTextField.init(controller, "current");
	}

	private void setMenuButton() {
		menuButton.setOnAction(e -> controller.setupMenuPanel());
	}

	@FXML
	private void openRoute(ActionEvent actionEvent) {
		controller.setupRoutePanel();
	}

	private void setSearchButton() {
		searchButton.setOnAction(e -> autoTextField.showResults());
	}

	@FXML
	private void openPointOfInterest(ActionEvent actionEvent) {
		controller.setUpPointOfInterestPanel();
	}
}
