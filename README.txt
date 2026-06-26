=========================================================
Project: Object-Oriented Programming - Game Task 3
Authors: Itay Aharony and Ilan Hacohen
=========================================================

1. BONUS TASKS IMPLEMENTED
---------------------------------------------------------
** ALL BONUS TASKS**
-Hunter player class
- Boss enemy class

2. SIGNIFICANT DESIGN CHOICES
---------------------------------------------------------
* Input/Output Callbacks:
  Instead of using regular System.out.println and Scanner directly in the game classes, we used interfaces (MessageCallback & InputCallback). This separates the logic from the UI.

* Singleton Pattern:
  The DefaultCombat class is a Singleton. This ensures we only have one instance of the combat system running in the game.

* Separation of Classes:
  We divided the responsibilities clearly. 'Level' handles the game board and parsing, 'GameController' runs the main loop and turns, and 'DefaultCombat' handles all the combat calculations.

* Safe List Iteration:
  We used an Iterator in the 'removeDeadEnemies' method in the Level class. This is to safely remove enemies from the list while the game is running without causing a ConcurrentModificationException.

* Visitor pattren :
 In the combat of two units we have used the visitor pattren and in the attack method.