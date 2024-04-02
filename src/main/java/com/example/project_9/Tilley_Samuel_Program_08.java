package com.example.project_9;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

//Samuel Tilley, Ciss-111-300, Program 08
public class Tilley_Samuel_Program_08 extends Application {


    //Class Variables
    ComboBox comboBox;
    TextField qTextField;
    ArrayList<String> Order = new ArrayList<String>();
    ArrayList<String[]> prices = new ArrayList<String[]>();

    @Override
    public void start(Stage stage) throws IOException {


        //setTitle
        stage.setTitle("Ordering Program");

        //Prompts user to select file
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(stage);


        //Creates a scanner and reads the file. splits the lines and adds to an arraylist
        Scanner scanner = new Scanner(selectedFile);
        while (scanner.hasNextLine()) {
            prices.add(scanner.nextLine().split(" "));
        }

        //Creating objects and Gridpane
        GridPane gridPane = new GridPane();
        String Sizes[] = {"XS","S","M","L","XL","XXL"};
        comboBox = new ComboBox(FXCollections.observableArrayList(Sizes));
        qTextField = new TextField();
        Label qLabel = new Label("Quantity:");
        Button atoButton = new Button("Add to Order");
        Button roButton = new Button("Review Order");
        Button helpButton = new Button("Help");
        Button cButton = new Button("Clear");


        //Setting Parameters
        gridPane.setVgap(4);
        gridPane.setHgap(4);
        qTextField.setPrefWidth(60);
        qTextField.setMaxWidth(60);

        //Placing objects onto gridpane
        gridPane.add(comboBox,0,0);
        gridPane.add(qLabel,1,0);
        gridPane.add(qTextField,2,0);
        gridPane.add(atoButton,3,0);
        gridPane.add(cButton,3,2);
        gridPane.add(roButton,3,1);
        gridPane.add(helpButton,3,3);

        //Button handling
        atoButton.setOnAction(new AtoButtonHandler());
        roButton.setOnAction(new roButtonHandler());
        cButton.setOnAction(new cButtonHandler());
        helpButton.setOnAction(new hButtonHandler());

        //Sets scene, renders, and adds stylesheet
        Scene scene = new Scene(gridPane,327,200);
        scene.getStylesheets().add("MainStyleSheet.css");
        stage.setScene(scene);
        stage.show();
    }

    //Handler for the Add to Order button. Adds to Orders ArrayList
    private class AtoButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

            //try catch statement for some general things. just prints error to the console
            try {

                //Throws exception if size wasn't selected
                if (comboBox.getValue() == null) {
                    throw new Exception();
                }

                //Throws exception if quantity wasn't inputted
                if (qTextField.getText() == null) {
                    throw new Exception();
                }

                //Throws exception if inputted quantity is negative or 0
                if (Integer.valueOf(qTextField.getText()) < 1) {
                    throw new Exception();
                }

                Order.add(comboBox.getValue() + " " + qTextField.getText());

            }

            catch (Exception e) {
                System.out.println("That is not valid input.");
            }
        }
    }

    //Handler for the Review order button
    private class roButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

            //Setting up variables
            int totalShirts = 0;
            float totalPrice = 0;

            //Creating a new stage and scene for the review order window
            Stage reviewOrderWindow = new Stage();
            GridPane reviewOrderGridpane = new GridPane();

            //Setting Parameters
            reviewOrderWindow.setTitle("Order Window");
            reviewOrderGridpane.setHgap(4);
            reviewOrderGridpane.setVgap(8);

            //Uses a for loop to iterate through the Order Arraylist
            for (int i = 0; i < Order.size(); i++) {

                //Pulls elements from Order and splits them
                String tempString = Order.get(i);
                String[] tempStringSplit = tempString.split(" ");

                //Compares strings from orders Arraylist to strings from prices Arraylist to determine what size of
                //shirt has been ordered. then it converts strings from both arraylists into floats and multiplies
                //them together to get a total price of shirts bought in that size. then adds that to a total price.
                for (int x = 0; x < prices.size(); x++) {
                    String[] tempArray = prices.get(x);
                    if (Objects.equals(tempArray[0], tempStringSplit[0])) {
                        totalPrice += (Float.parseFloat(tempArray[1])*Float.parseFloat(tempStringSplit[1]));
                    }
                }


                //Totals the quantity of shirts bought
                totalShirts += Integer.parseInt(tempStringSplit[1]);

                //Creates labels using the Order Arraylist
                Label sizeLabel = new Label(tempStringSplit[0]);
                Label quantityLabel = new Label(tempStringSplit[1]);
                Label spacingLabel = new Label(".........................");

                //Adds the labels to the gridpane
                reviewOrderGridpane.add(sizeLabel,0,0 + i);
                reviewOrderGridpane.add(spacingLabel,1,0 + i);
                reviewOrderGridpane.add(quantityLabel,2, 0 + i);

            }

            //Total Quantity Labels
            Label totalLabel2 = new Label("Total Quantity:");
            Label totalLabel = new Label(String.valueOf(totalShirts));
            reviewOrderGridpane.add(totalLabel,2,1 + Order.size());
            reviewOrderGridpane.add(totalLabel2,1,1 + Order.size());

            //Total Price Labels
            Label totalPriceLabel = new Label("Total Price:");
            Label totalPriceLabel2 = new Label("$" + totalPrice);
            reviewOrderGridpane.add(totalPriceLabel,1,2 + Order.size());
            reviewOrderGridpane.add(totalPriceLabel2,2,2 + Order.size());


            //sets new scene, renders, and adds stylesheet
            Scene reviewOrderScene = new Scene(reviewOrderGridpane,275,400);
            reviewOrderScene.getStylesheets().add("MainStyleSheet.css");
            reviewOrderWindow.setScene(reviewOrderScene);
            reviewOrderWindow.show();
        }
    }

    //Handler for the clear button. Clears the Orders Arraylist
    private class cButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

            Order.clear();
        }
    }

    //Handler for the help button
    private class hButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

            //makes new stage for help window and sets title
            Stage helpWindow = new Stage();
            helpWindow.setTitle("Help");

            //Creates text area and adds help text to it
            TextArea textArea = new TextArea("To add an item to your order select the size from the box on the left " +
                    "and type the quantity in the box labeled Quantity, then click add to order. To review your order " +
                    "click the review order button. To clear order and start again click the Clear button.");

            //Creates Gridpane to put text area in.
            GridPane helpGridpane = new GridPane();
            helpGridpane.add(textArea,0,0);

            //sets scene, renders, and adds stylesheet
            Scene helpScene = new Scene(helpGridpane,310,125);
            helpScene.getStylesheets().add("MainStyleSheet.css");
            helpWindow.setScene(helpScene);
            helpWindow.show();

        }
    }

    //Main method
    public static void main(String[] args) {
        launch(args);
    }
}
