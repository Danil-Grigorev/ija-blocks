package Interface;

import java.awt.*;

public interface SingConElm extends  InterfaceElm {
    javafx.scene.shape.Rectangle getVisuals();
    void setVisuals(double X, double Y);
    double getCenterX();
    double getCenterY();
}
