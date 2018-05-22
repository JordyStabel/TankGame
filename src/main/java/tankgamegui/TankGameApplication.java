package tankgamegui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TankGameApplication extends Application {

    // Labels for level, nr edges, calculation time, and drawing time
    private Label labelLevel;
    private Label labelNrEdges;
    private Label labelNrEdgesText;
    private Label labelCalc;
    private Label labelCalcText;
    private Label labelDraw;
    private Label labelDrawText;

    // Koch panel and its size
    private Canvas gamePanel;
    private final int kpWidth = 1000;
    private final int kpHeight = 500;

    @Override
    public void start(Stage primaryStage) {

        // Define grid pane
        GridPane grid;
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // For debug purposes
        // Make de grid lines visible
        // grid.setGridLinesVisible(true);

        // Drawing panel for Koch fractal
        gamePanel = new Canvas(kpWidth,kpHeight);
        grid.add(gamePanel, 0, 3, 25, 1);

        // Labels to present number of edges for Koch fractal
        labelNrEdges = new Label("Nr edges:");
        labelNrEdgesText = new Label();
        grid.add(labelNrEdges, 0, 0, 4, 1);
        grid.add(labelNrEdgesText, 3, 0, 22, 1);

        // Labels to present time of calculation for Koch fractal
        labelCalc = new Label("Calculating:");
        labelCalcText = new Label();
        grid.add(labelCalc, 0, 1, 4, 1);
        grid.add(labelCalcText, 3, 1, 22, 1);

        // Labels to present time of drawing for Koch fractal
        labelDraw = new Label("Drawing:");
        labelDrawText = new Label();
        grid.add(labelDraw, 0, 2, 4, 1);
        grid.add(labelDrawText, 3, 2, 22, 1);

        // Label to present current level of Koch fractal
        labelLevel = new Label("Level: " + 1);
        grid.add(labelLevel, 0, 6);

        // Button to increase level of Koch fractal
        Button buttonIncreaseLevel = new Button();
        buttonIncreaseLevel.setText("Increase Level");
        buttonIncreaseLevel.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                //increaseLevelButtonActionPerformed(event);
            }
        });
        grid.add(buttonIncreaseLevel, 3, 6);

        // Button to decrease level of Koch fractal
        Button buttonDecreaseLevel = new Button();
        buttonDecreaseLevel.setText("Decrease Level");
        buttonDecreaseLevel.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                //decreaseLevelButtonActionPerformed(event);
            }
        });
        grid.add(buttonDecreaseLevel, 5, 6);

        // Button to fit Koch fractal in Koch panel
        Button buttonFitFractal = new Button();
        buttonFitFractal.setText("Fit Fractal");
        buttonFitFractal.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                //fitFractalButtonActionPerformed(event);
            }
        });
        grid.add(buttonFitFractal, 14, 6);

        // Progressbars
        Text textLeft = new Text("Left");
        ProgressBar barLeft = new ProgressBar();
        Text textRight = new Text("Right");
        ProgressBar barRight = new ProgressBar();
        Text textBottom = new Text("Bottom");
        ProgressBar barBottom = new ProgressBar();

        // Add mouse clicked event to Koch panel
        gamePanel.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        //kochPanelMouseClicked(event);
                    }
                });

        // Add mouse pressed event to Koch panel
        gamePanel.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        //kochPanelMousePressed(event);
                    }
                });

        // Add mouse dragged event to Koch panel
        gamePanel.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                //kochPanelMouseDragged(event);
            }
        });

        // Adding the progressbars with their labels
        grid.add(textLeft, 0, 7);

        grid.add(barLeft, 3, 7);

        grid.add(textRight, 0, 8);

        grid.add(barRight, 3, 8);

        grid.add(textBottom, 0, 9);
        grid.add(barBottom, 3, 9);


        // Create the scene and add the grid pane
        Group root = new Group();
        Scene scene = new Scene(root, kpWidth+50, kpHeight+170);
        root.getChildren().add(grid);

        // Define title and assign the scene for main window
        primaryStage.setHeight(500);
        primaryStage.setTitle("Koch Fractal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void clearKochPanel() {
        GraphicsContext gc = gamePanel.getGraphicsContext2D();
        gc.clearRect(0.0,0.0,kpWidth,kpHeight);
        gc.setFill(Color.BLACK);
        gc.fillRect(0.0,0.0,kpWidth,kpHeight);
    }

    public void setTextNrEdges(String text) {
        labelNrEdgesText.setText(text);
    }

    public void setTextCalc(String text) {
        labelCalcText.setText(text);
    }

    public void setTextDraw(String text) {
        labelDrawText.setText(text);
    }

    public static void main(String[] args){
        launch(args);
    }
}
