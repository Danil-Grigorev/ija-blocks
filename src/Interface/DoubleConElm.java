package Interface;

import javafx.scene.shape.Line;

public interface DoubleConElm extends InterfaceElm {
    Line getVisuals();
    void setVisualsA(double X, double Y);
    void setVisualsB(double X, double Y);
}
