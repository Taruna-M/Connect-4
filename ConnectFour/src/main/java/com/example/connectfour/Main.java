package com.example.connectfour;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    private Controller controller;
    @Override
    public void start(Stage stage) throws IOException {
        //fxml loader object
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("game.fxml"));
        //load the fxml file and assign to the parent/root node
        GridPane rootGridPane=loader.load();
        //load the controller object through fxml file into main file
        controller=loader.getController();
        //use the createPlayground fn written in controller class by me
        controller.createPlayground();
        //set scene in stage add title and make it non-resizable
        Scene scene = new Scene(rootGridPane);
        stage.setScene(scene);
        stage.setTitle("CONNECT 4 GAME");
        stage.setResizable(false);
        stage.show();
        //first pane in the gridpane Pane(0,0)
        Pane menuPane= (Pane) rootGridPane.getChildren().get(0);
        //call the createMenu() function and assign the menuBar object to variable
        MenuBar menuBar=createMenu();
        //fit the menu to the entire stage width
        menuBar.prefWidthProperty().bind(stage.widthProperty());
        //add the menu bar to the pane
        menuPane.getChildren().add(menuBar);

    }
    private MenuBar createMenu(){
        //file menu
        Menu fileMenu=new Menu("File");
        //new game menu item
        MenuItem newGame=new MenuItem("New Game");
        //on click handler of new game
        newGame.setOnAction(event -> controller.resetGame());
        //reset game menu item
        MenuItem resetGame=new MenuItem("Reset Game");
        //on click handler of reset game
        resetGame.setOnAction(event -> controller.resetGame());
        //separator
        SeparatorMenuItem separatorFileMenu=new SeparatorMenuItem();
        //exitgame menu item
        MenuItem exitGame=new MenuItem("Exit Game");
        //on click handler for exit game
        exitGame.setOnAction(event -> exitGame());
        //add menu items to file menu
        fileMenu.getItems().addAll(newGame,resetGame,separatorFileMenu,exitGame);
        //help menu
        Menu helpMenu=new Menu("Help");
        //about game menu item
        MenuItem aboutGame=new MenuItem("About Game");
        //on click handler for about game
        aboutGame.setOnAction(event -> aboutGame());
        //separator
        SeparatorMenuItem separatorHelpMenu=new SeparatorMenuItem();
        //about author menu item
        MenuItem aboutAuthor=new MenuItem("About Author");
        //on click handler for about author
        aboutAuthor.setOnAction(event -> aboutAuthor());
        //add menu items to help menu
        helpMenu.getItems().addAll(aboutGame,separatorHelpMenu,aboutAuthor);
        //menu bar
        MenuBar menuBar=new MenuBar();
        //add menus to bar
        menuBar.getMenus().addAll(fileMenu,helpMenu);
        return menuBar;
    }

    private void aboutAuthor() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ABOUT DEVELOPER");
        alert.setHeaderText("TARU TUNA");
        alert.setContentText("YO IM TARUNA AKA TUNA OK BYE");
        alert.show();
    }

    private void aboutGame() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ABOUT CONNECT 4");
        alert.setHeaderText("HOW TO PLAY");
        alert.setContentText("Connect Four is a classic two-player connection game " +
                "in which the players first choose a color and then take turns dropping " +
                "one colored disc from the top into a vertically suspended grid. " +
                "The grid typically consists of six rows and seven columns. " +
                "The objective of the game is to connect four of one's own discs " +
                "of the same color vertically, horizontally, or diagonally before the opponent does.");
        alert.show();
    }


    private void exitGame() {
        Platform.exit();
        System.exit(0);
    }

    private void resetGame() {
        //TODO
    }

}