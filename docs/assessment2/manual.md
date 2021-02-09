---
layout: default
title: Game Manual
parent: Assessment 2
---

PUT A PDF LINK HERE 

{: .no_toc }

<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}
</details>

---

# Game Manual

Welcome to Dragon Boat Racing 2021! A single-player Boat Racing game based on the annual Dragon Boat Race held in York along the River Ouse.
In Dragon Boat Racing 2021, the player competes against 6 AI opponents, racing their dragon boats across 3 legs to achieve the fastest time to cross the finish line.

## Starting the Game

![Main Menu](/docs/assets/assessment2/static/mainmenu.png "Main Menu")

Upon starting up the game you will be presented with the main menu screen. In this screen you will have three options: 
* Play
    * Takes you to the boat selection screen.
* Load
    * Takes you to the save slot selection screen where you can load a save from a previous race.
* Exit
    * Closes the game window.

Using your mouse, click on which option you would like to proceed with. 

## Selecting a Boat

![Boat Selection](/docs/assets/assessment2/static/boatselection.png "Boat Selection")

In this screen you will be able to select one of seven boats as the boat you would like to race with. Every boat has 4 statistics with each boat having a different distribution of these statistics. To view each boat's statistics, hover your mouse cursor over the desired boat. Clicking on your desired boat will take you to the difficulty selection screen.

## Boat Statistics

* Robustness
    * Determines how much damage a boat can take.
    * A boat with higher robustness will lose a smaller percentage damage to the durability upon collision with an obstacle.
* Manoeuvrability
    * Determines how fast the boat can avoid obstacles.
    * A boat with higher manoeuvrability will be able to move side to side better without losing speed.
* Max Speed
    * Determines how fast a boat can go.
    * A boat with higher max speed will be able to go faster than other boats.
* Acceleration
    * Determines how quickly a boat can achieve its max speed.
    * A boat with higher acceleration will achieve its max speed before other boats.

## Game Difficulty

![Game Difficulty](/docs/assets/assessment2/static/difficulty.png "Game Difficulty")

You will be presented with three difficulty options on screen:
* Easy
* Normal
* Hard

Game difficulty affects the statistics of AI boats and the number of obstacles that will be found in a lane.

## Starting the Race

![In Game](docs/assets/assessment2/static/ingame.png "In Game")

After selecting a difficulty, you will begin the race immediately. Every time you play the game you will be in the left most lane. You will have to try and get to the finish line in the fastest time possible.

## Game Controls 

| Button       | Effect                             |
|:-------------|:-----------------------------------|
| W            | Accelerates the boat               |
| A            | Roates the boat towards the left   |
| S            | Decelerates the boat               |
| D            | Rotates the boat towards the right |
| Esc          | Opens the pause menu               |

## UI

The in game UI displays all important information during the course of the race. In the top left of the screen you will find bars displaying your current robustness, stamina, and speed. In the bottom left of the screen you will see the current leg that you are racing in. In the top right you will see your current race position. At the top of the screen you will see a timer that updates in real time to tell you how long you have been racing for in your current leg.

## Obstacles 

During the races, there are a series of obstacles that will be floating down the Ouse. You must avoid these obstacles in order to prevent taking damage to your boat. If the boat's durability is reduced to 0 at any point within the game, the game ends and the player loses. So, watch out for those geese!! 
Whilst navigating the obstacles, you will have to make sure to stay in your lane to avoid incurring a time penalty. Your time penalty will be added to your leg time and will affect your position on the leaderboard.

## Power Ups 

Amongst the obstacles, there is a collection of power ups that you can collide with. They provide the following benefits:

* Speed
![Speed](/core/assets/PowerUps/SpeedBoost.png "Speed")
    * The player receives a speed boost for 2 seconds.
* Stamina
![Stamina](/core/assets/PowerUps/StaminaBoost.png "Stamina")
    * The player receives a boost to their current stamina.
* Invulnerability
![Invulnerability](/core/assets/PowerUps/Invulnerability.png "Invulnerability")
    * The player becomes invulnerable to damage for 7 seconds.
* Health
![Health](/core/assets/PowerUps/HealthBoost.png "Health")
    * The player receives a boost to their current health.
* Bomb
![Bomb](/core/assets/PowerUps/ObstacleClearer.png "Bomb")
    * The bomb clears the current path of obstacles in the players lane.

## Pause Menu

![Pause Menu](/docs/assets/assessment2/static/pausemenu.png "Pause Menu")

The pause menu can be accessed by pressing “Esc” at any point during the race. You can remain paused for as long as you wish.
In the pause menu, you can select between three options:
* Resume
    * Leave the pause menu and resumes the current game
* Save
    * Takes you to the save slot selection screen allowing you to save your current game progress.
* Exit
    * Quits the current race and takes you back to the main menu. Make sure to save your game if you wish to return to it otherwise all progress will be lost.

## Saving a Game

![Saving Game](/docs/assets/assessment2/static/savemenu.png "Saving Game")

In the save selection screen you will be able to save your current game progress into any of three save slots. These save slots will store your current game progress allowing you to return to that game later upon loading a save slot. Game save states will remain even after closing down the game allowing you to return to your game at any point. Saving a game into a save slot that already stores a game state will override that save causing you to replace your current save with your previous save.

## Loading a Game

In the main menu you can select “Load” to load a save slot that you have previously saved into. Loading a save will resume your game from where saved and exited.

## Winning the Game

If the player manages to achieve one of the 3 fastest times across the 3 legs, they will qualify for the final race where they will compete against the other 3 fastest boats. 

Upon completing the final race, the player will place first, second or third respective to their finishing position.
