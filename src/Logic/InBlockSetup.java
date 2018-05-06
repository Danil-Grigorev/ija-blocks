package Logic;

import Elements.Blocks.Block;
import Elements.DataTypes.DataType;
import Elements.DataTypes.DoubleType;
import Elements.DataTypes.FloatType;
import Elements.DataTypes.IntType;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author xgrigo02
 */
public class InBlockSetup implements Initializable {


    public ChoiceBox typeChoise1;
    public TextField valTextIn1;
    public Button okBtn1;
    public Button cancelBtn1;
    public AnchorPane inBlockSetup;
    private Block caller;

    /**
     * Keyboard key listeners setup. Allows to use escape and enter keys.
     *
     * @param caller    reference to clicked in block
     */
    public void init(Block caller) {
        this.caller = caller;
        inBlockSetup.getScene().addEventFilter(KeyEvent.KEY_PRESSED,
                event -> {
                    switch (event.getCode()) {
                        case ESCAPE:
                            cancelClick(null);
                            event.consume();
                            break;
                        case ENTER:
                            okClick(null);
                            event.consume();
                            break;
                    }
                });
    }

    /**
     * OK button click handler, saves new value to IN block.
     *
     * @param actionEvent   mouse click on ok button
     */
    public void okClick(ActionEvent actionEvent) {
        Stage tmpWind = (Stage) inBlockSetup.getScene().getWindow();
        DataType newData;
        double val;
        try {
            val = Double.parseDouble(valTextIn1.getText());
        }
        catch (java.lang.NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unexpected value");
            alert.setContentText("The value on input has unexpected format.\nPlease try again.");
            alert.showAndWait();
            tmpWind.close();
            return;
        }
        switch (typeChoise1.getSelectionModel().getSelectedItem().toString()) {
            case "Int":
                newData = new IntType(val);
                break;
            case "Float":
                newData = new FloatType(val);
                break;
            case "Double":
                newData = new DoubleType(val);
                break;
            default:
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Unknown type for InBlockSetup"
                        + typeChoise1.getSelectionModel().getSelectedItem().toString());
                alert.showAndWait();
                tmpWind.close();
                return;
        }
        caller.setData(newData);
        tmpWind.close();
        actionEvent.consume();
    }

    /**
     * Cancle button click handler, returns to scheme without changes.
     *
     * @param actionEvent   mouse click
     */
    public void cancelClick(ActionEvent actionEvent) {
        Stage tmpWind = (Stage) inBlockSetup.getScene().getWindow();
        tmpWind.close();
        actionEvent.consume();
    }

    /**
     * Initializable implementation.
     *
     * @param location  URL for file position
     * @param resources additional resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
