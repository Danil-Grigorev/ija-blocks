package Logic;

import Elements.Blocks.Block;
import Elements.DataTypes.DataType;
import Elements.DataTypes.DoubleType;
import Elements.DataTypes.FloatType;
import Elements.DataTypes.IntType;
import com.sun.org.apache.bcel.internal.generic.LoadClass;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class InBlockSetup implements Initializable {


    public ChoiceBox typeChoise1;
    public TextField valTextIn1;
    public Button okBtn1;
    public Button cancelBtn1;
    public AnchorPane inBlockSetup;
    private Block caller;

    public void init(Block caller) {
        this.caller = caller;
    }

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
    }

    public void cancelClick(ActionEvent actionEvent) {
        Stage tmpWind = (Stage) inBlockSetup.getScene().getWindow();
        tmpWind.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
