module com.example.parkeringsautomat2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.parkeringsautomat2 to javafx.fxml;
    exports com.example.parkeringsautomat2;
}