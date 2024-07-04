package com.example.connectfour;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import java.net.URL;
import java.util.*;
import java.util.stream.IntStream;

public class Controller implements Initializable {
    //constants for columns rows and circle diameter
    private static final int COL=7;
    private static final int ROW=6;
    private static final int CIRCLE_DIAMETER=80;
    //constants for the player discs colors
    private static final String DISC_COLOR_ONE="#FFCE30";
    private static final String DISC_COLOR_TWO="#ff99ff";
    //initialise the player names as 1 and 2 which will later change on input by user
    private static String PLAYER_ONE="Player 1";
    private static String PLAYER_TWO="Player 2";
    //which player is playing right now; true for 1 and false for 2
    private boolean is_player1=true;
    //array to keep track of discs to be inserted in the playground
    private final Disc[][] insertedDiscArr=new Disc[ROW][COL];
    //for multiple discs inserted by same player on double click so to avoid that this process
    private boolean isAllowedToInsert=true;
    //fxml tags
    @FXML
    public GridPane rootGridPane;
    @FXML
    public Pane discPane;
    @FXML
    public Label playerNameLabel;
    @FXML
    public TextField playerOneText;
    @FXML
    public TextField playerTwoText;
    @FXML
    public Button nameButton;

    //creation of the game layout (playground) includes formation of the structure of discs and adding it to the grid pane
    public void createPlayground(){
        //assign the returned value from function to variable
        Shape rectWithHoles=gridStructure();
        //add this cut out shape to the grid pane(0,1)
        rootGridPane.add(rectWithHoles,0,1);
        //assign the returned list from function to a list here
        List<Rectangle> rectangleList=createClickableCol();
        //each rectangle from the list must be added to the grid pane
        for (Rectangle rectangle: rectangleList){
            rootGridPane.add(rectangle,0,1);
        }
        //set names do user input ones
        nameButton.setOnAction(event->setName());
    }
    //function to set names of players
    private void setName() {
        String input1=playerOneText.getText();
        String input2=playerTwoText.getText();
        PLAYER_ONE=input1;
        PLAYER_TWO=input2;
    }
    //function to define the structure of the playground
    private Shape gridStructure(){
        //rectangle shape with width and height specified
        Shape rectWithHoles=new Rectangle((COL+1)*CIRCLE_DIAMETER, (ROW+1)*CIRCLE_DIAMETER);
        //
        for (int row=0;row<ROW;row++){
            for (int col=0;col<COL;col++){
                //circle object
                Circle circle=new Circle();
                //radius and center x,y will create the circle
                circle.setRadius((double) CIRCLE_DIAMETER /2);
                circle.setCenterX((double) CIRCLE_DIAMETER /2);
                circle.setCenterY((double) CIRCLE_DIAMETER /2);
                //give smooth edges to the circle
                circle.setSmooth(true);
                //will position the circle in respective col,row in every iteration of loop
                circle.setTranslateX(col*(CIRCLE_DIAMETER+5)+((double) CIRCLE_DIAMETER /4));
                circle.setTranslateY(row*(CIRCLE_DIAMETER+5)+((double) CIRCLE_DIAMETER /4));
                //for circles to be displayed on the rectangle we built use shape.subtract to cut the circles from the
                //rectangle
                rectWithHoles=Shape.subtract(rectWithHoles,circle);
            }
        }
        //set rectangle color to white so since background grid pane color is aqua from the white rect aqua circles will be formed
        //by cutting the white circles in the for loop
        rectWithHoles.setFill(Color.WHITE);
        //return the cut rectangle object
        return rectWithHoles;

    }
    //these rectangles will indicate the user which column they are hovering over
    private List<Rectangle> createClickableCol(){
        //create a list to which we will add all the rectangles
        List<Rectangle> rectangleList=new ArrayList<>();
        //loop to create a rectangle for each col of the game structure
        for (int col=0;col<COL;col++){
            Rectangle rect=new Rectangle(CIRCLE_DIAMETER,(ROW+1)*CIRCLE_DIAMETER);
            rect.setFill(Color.TRANSPARENT);
            //here rectangle created in each iteration will be moved each col
            rect.setTranslateX(col*(CIRCLE_DIAMETER+5)+((double) CIRCLE_DIAMETER /4));
            //when hovering over the rect it will change color to semi transparent #eeeeee26
            rect.setOnMouseEntered(event -> rect.setFill(Color.valueOf("#eeeeee26")));
            //when not hovering it will go back to blue
            rect.setOnMouseExited(event -> rect.setFill(Color.TRANSPARENT));
            //column used in lambda expression of below event must be final acc to syntax
            final int column=col;
            //click handler for each rectangle on clicking disc will be inserted
            rect.setOnMouseClicked(event-> {
                if (isAllowedToInsert) {
                    //when disc is dropped further in one player's turn multiple won't be inserted
                    isAllowedToInsert=false;
                    insertDisc(new Disc(is_player1), column);
                }
            });
            //add the created rectangle in list
            rectangleList.add(rect);
        }
        //return list
        return rectangleList;

    }

    private void insertDisc(Disc disc, int column) {
        //insert disc on top of another
        //first set the control variable for while loop
        int row=ROW-1;
        //in each iteration we check if the space is empty if not then go to above row
        //if empty then break the loop and go the next bit of code
        while (row>=0){
            if (getDiscIfPresent(row,column)==null) break;
            else row--;
        }
        //no more discs to add if column is full
        if (row<0) return;
        //array initialised at the top of this class for me to keep track of the discs inserted
        insertedDiscArr[row][column]=disc;
        //the disc actually inserted in the pane
        discPane.getChildren().add(disc);
        //insert at the clicked column so move the disc there
        disc.setTranslateX(column*(CIRCLE_DIAMETER+5)+((double) CIRCLE_DIAMETER /4));
        int currentRow=row;
        //make the disc fall from top to bottom along y-axis using translate transition class
        TranslateTransition transition=new TranslateTransition(Duration.seconds(0.5),disc);
        transition.setToY(row*(CIRCLE_DIAMETER+5)+((double) CIRCLE_DIAMETER /4));
        transition.setOnFinished(event -> {
            //after the current player's turn is over now next player needs to drop
            isAllowedToInsert=true;
            //check if game ended
            if (gameEnded(currentRow,column)){
                //if game ended then declare winner
                gameOver();
                return;
            }
            //after the disc reaches the position change the disc color cuz now it is next player's turn
            is_player1=!is_player1;
            //change the player label as well
            playerNameLabel.setText(is_player1?PLAYER_ONE:PLAYER_TWO);
        });
        transition.play();

    }

    private void gameOver() {
        //who's the last person to play when game ended they are the winner
        String winner=is_player1?PLAYER_ONE:PLAYER_TWO;
        System.out.println("Winner is"+winner);
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("CONGRATULATIONS");
        alert.setHeaderText("THE WINNER IS: "+winner);
        alert.setContentText("WANNA PLAY AGAIN?");
        ButtonType yes=new ButtonType("Yes");
        ButtonType no=new ButtonType("No and Exit");
        alert.getButtonTypes().setAll(yes,no);
        //show and wait must happen only after the animation is done so run later
        Platform.runLater(()->{
            Optional<ButtonType> buttonClicked=alert.showAndWait();
            if (buttonClicked.isPresent() && buttonClicked.get()==yes){
                resetGame();
            }
            else{
                Platform.exit();
                System.exit(0);
            }
        });


    }
    public  void resetGame(){
        discPane.getChildren().clear();
        for (Disc[] discs : insertedDiscArr) {
            Arrays.fill(discs, null);
        }
        is_player1=true;
        playerNameLabel.setText(PLAYER_ONE);
        createPlayground();
    }

    //fn to see if game ended
    private boolean gameEnded(int currentRow, int column) {
        //vertical combos
        List<Point2D> vertPts=
                IntStream.rangeClosed(currentRow-3,currentRow+3)//range of rows
                        .mapToObj(r-> new Point2D(r,column))//r is from the range of rows which will be mapped
                        //to 2D coordinates (r,column)
                        .toList();//converts range to List
        //horizontal combos
        List<Point2D> horzPts=
                IntStream.rangeClosed(column-3,column+3)//range of columns
                        .mapToObj(c-> new Point2D(currentRow,c))//r is from the range of columns which will be mapped
                        //to 2D coordinates (row,c)
                        .toList();//converts range to List
        //right diag combos
        Point2D startPoint1=new Point2D(currentRow-3,column+3);
        List<Point2D> diag1Pts=IntStream.rangeClosed(0,6).
                mapToObj(i -> startPoint1.add(i,-i)).
                toList();
        //left diag combos
        Point2D startPoint2=new Point2D(currentRow-3,column-3);
        List<Point2D> diag2Pts=IntStream.rangeClosed(0,6).
                mapToObj(i -> startPoint2.add(i,i)).
                toList();
        //4 disc combo formed or not (true or false) and return that bool
        return checkCombos(vertPts)||checkCombos(horzPts)||checkCombos(diag1Pts)||checkCombos(diag2Pts);
    }

    //check the 4 disc combos
    private boolean checkCombos(List<Point2D> points) {
        int chain=0;
        for (Point2D point : points){
            int rowIndex=(int) point.getX();
            int colIndex=(int) point.getY();
            Disc disc=getDiscIfPresent(rowIndex,colIndex);
            //if there is a disc and correct player is playing the increment chain
            if (disc!=null && disc.is_player1_move==is_player1){
                chain++;
                //as soon as chain is 4 then game ended (true)
                if (chain==4) return true;
            }
            //if not 4 then go back to 0
            else chain=0;
        }
        //all else fails then no combo formed (false)
        return false;
    }

    //check if disc in array is null,valid row and col given if not then null
    private Disc getDiscIfPresent(int row, int col){
        if (row>=ROW || row<0 || col>=COL || col<0) return null;
        else return insertedDiscArr[row][col];
    }

    //we are creating a disc class which extends from Circle class
    private static class Disc extends Circle{
        //constant boolean which is same as is_player1 at top of this class
        private final boolean is_player1_move;
        //constructor for this class takes in the is_player1 stores it in this local variable
        public Disc(boolean is_player1){
            this.is_player1_move=is_player1;
            //on initialization of disc it should create a circle with radius
            setRadius((double) CIRCLE_DIAMETER /2);
            //color of disc changes acc to which player's turn
            setFill(is_player1_move ? Color.valueOf(DISC_COLOR_ONE):Color.valueOf(DISC_COLOR_TWO));
            setCenterY((double) CIRCLE_DIAMETER /2);setCenterX((double) CIRCLE_DIAMETER /2);

        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}