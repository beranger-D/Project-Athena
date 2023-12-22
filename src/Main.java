import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("MainWindow.fxml"));
        AnchorPane rootLayout = loader.load();

        MainWindowController mainController = loader.getController();

        // Create the scene
        Scene scene = new Scene(rootLayout);
        primaryStage.setMinWidth(500.0);
        primaryStage.setMinHeight(760.0);
        primaryStage.setMaxWidth(500.0);
        primaryStage.setMaxHeight(760.0);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Project Athena");
        primaryStage.show();
    }
}