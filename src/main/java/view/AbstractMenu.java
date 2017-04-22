package view;


import javafx.scene.paint.Color;
import controller.GameController;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import runner.CivilizationGame;
/**
 *
 * @author Timothy Baba, RuYiMarone
 */
public class AbstractMenu {
    public static final int PREFWIDTH = 140;
    private Button exploreButton = new Button("Explore");
    private Button endTurnButton = new Button("End Turn");
    private Text terrain = new Text();
    private Text unitStatus = new Text();

    private VBox menu = new VBox(10, terrain, unitStatus,
            exploreButton, endTurnButton);

    public AbstractMenu() {
        terrain.setStyle("-fx-font-weight: bold");
        terrain.setFill(Color.WHITE);
        unitStatus.setStyle("-fx-font-weight: bold");
        unitStatus.setFill(Color.WHITE);


        menu.setPrefWidth(PREFWIDTH);
        unitStatus.setWrappingWidth(120);

        exploreButton.setOnMousePressed(e -> {
                GameController.getCivilization().explore();
                if (endTurn()) {
                    Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    newAlert.initOwner(CivilizationGame.getPrimaryStage());
                    newAlert.setHeaderText("Congratulations");
                    newAlert.setTitle("You Won!");
                    AudioClip plonkSound2 = new AudioClip(CivilizationGame
                        .getFile() + "win.mp3");
                    plonkSound2.play();
                    newAlert.showAndWait();
                    System.exit(0);
                }
            });

        endTurnButton.setOnAction(event -> {
                if (endTurn()) {
                    Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    newAlert.initOwner(CivilizationGame.getPrimaryStage());
                    newAlert.setHeaderText("Congratulations");
                    newAlert.setTitle("You Won!");
                    AudioClip plonkSound = new AudioClip(CivilizationGame
                        .getFile() + "win.mp3");
                    plonkSound.play();
                    newAlert.showAndWait();
                    System.exit(0);
                }
            });
        menu.setPrefWidth(PREFWIDTH);
        updateItems();
    }
    /**
     * This method updates the items and return the vbox as
     * the menu
     */
    public VBox getRootNode() {
        updateItems();
        return menu;
    }
    /**
     * This method takes in a node and added the node as
     * a child of the vbox menu
     */
    public void addMenuItem(Node node) {
        menu.getChildren().add(node);
    }
    /**
     * ends the player's turn and check for winning condition
     */
    public boolean endTurn() {
        GameController.setLastClicked(null);
        GameController.tick();
        GameController.ai();
        GridFX.update();
        GameController.updateResourcesBar();
        if (GameController.getCivilization().getNumSettlements() <= 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(CivilizationGame.getPrimaryStage());
            alert.setHeaderText("Your last settlement has been destroyed!");
            alert.setTitle("Game Over");
            AudioClip plonkSound = new AudioClip(CivilizationGame.getFile()
                + "end.mp3");
            plonkSound.play();
            alert.showAndWait();
            System.exit(0);
        }
        return GameController.getCivilization()
                .getStrategy().conqueredTheWorld()
                || GameController.getCivilization()
                .getTechnology().hasTechnologyWin();
    }

    private void updateItems() {
        unitStatus.setText("");
        if (GameController.getLastClicked() != null) {
            terrain.setText(GameController.getLastClicked()
                    .getTile().getType().toString());
            if (!GameController.getLastClicked().getTile().isEmpty()) {
                unitStatus.setText(GameController.getLastClicked()
                        .getTile().getOccupant().getStatusString());
            }
        }
    }
}