# Wordle Java-Swing-MVC
This program is a simple Wordle Clone written in Java that uses the Java-Swing Framework for its GUI and follows the Model-View-Controller (MVC) Architecture. 

I created the GUI for this Wordle Clone in a different Java GUI Framework called JavaFX for one of my classes in college. However, I wanted to practice using the Java-Swing framework. Therefore, I created a new GUI for the game using a different Framework.

### Note:
I used code from a project I had in one of my classes in college. Some of the code was starter code given by my instructor Tyler Conklin which is why some of the Java files have his name listed on them. 


## How to Run
Make sure to install Java and the Eclipse IDE on your machine.

1. Once you open the Eclipse IDE and download this project from the repo:
   * You must import the project by going to File -> Import -> General -> Existing Projects into Workplace.
   * Then find the path of the project and click finish. The project should then be in your workplace in Eclipse

     
2. To run the program, you need to create a new run configuration for the program.
   * Go to Run -> Run Configurations -> Select Java Application -> Select the "New Configuration" button
   * You can name the configuration whatever you want. However, I prefer to call it "Wordle"
   * Set the main class to be "view.Wordle".
   * Then go to Arguments and add one of the two arguments under Program Arguments (NOTE: by default, this game will display the GUI):
     * -gui: Display the GUI (graphical user interface) version of the application. (NOTE: When the GUI displays, make sure to click on it so that it can register inputs).
     * -text: Displays the CLI (command line interface) version of the application. The command line will be displayed in Eclipse. (NOTE: make sure to click right next to "Enter a guess:" so that you can enter a word).
   * Press the Run button
  
## How to play

If you haven't played the game before, I recommend reading this [article](https://mashable.com/article/wordle-word-game-what-is-it-explained)

Basic Controls:
- To type in a character, press any letter on your keyboard
- To enter a guess, press the return or enter key
- To delete a character from the word you want to enter, press the delete or backspace key

   
