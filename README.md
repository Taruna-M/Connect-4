# Connect Four Java Game
This Java program, developed using JavaFX, implements the Connect Four game. The program allows two players to take turns dropping discs into a 7x6 grid. The goal is to connect four discs vertically, horizontally, or diagonally. The application features a graphical interface where players can interact and see the game progress. This is done in the Controller class
The Main class is the entry point of the Connect Four JavaFX application. It sets up the primary stage, loads the FXML file for the UI layout, initializes the game controller, and creates a menu bar with options for new games, resetting the game, and displaying information about the game and the developer.

🎮 Game Board Setup: Creates a 7x6 grid with circles representing the slots.
🖱️ User Interaction: Allows players to click on columns to drop discs.
🏆 Winning Condition: Checks for four connected discs vertically, horizontally, or diagonally.
🎨 Graphics: Utilizes JavaFX shapes, colors, and transitions for a smooth visual experience.
🔄 Game Reset: Offers the option to restart the game after a win or exit the application.
🏷️ Player Names: Players can set their names, which are displayed during the game.
📊 Disc Management: Keeps track of discs' positions in a 2D array.
⛔ Input Control: Prevents multiple discs from being inserted simultaneously.
🚀 Animation: Implements animations for disc drops using TranslateTransition.
🧩 Structure Formation: Dynamically creates the game structure and clickable columns.

