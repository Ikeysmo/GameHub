# GameHub
Hub for playing tic tac toe, pong and more!
<img src="/GameHub/icons/gamehub_logo.png" width="250">

#Isaiahs List
#Graphics Upgrading:
- [ ] graphics upgrade
- [ ] better the graphics of gamehub
- [ ] invite system improvement
- [ ] cleaner chat client
- [ ] cleaner online list

#Refactoring:
- [ ] set of APIs for server
- [ ] redesign of how game works
- [ ] make baseGameClass
- [ ] clean up code 

#Features:
- [ ] highscores/trophies
- [ ] log system server (done)

#Games:
- [ ] TicTacToe
- [ ] Conn4...
- [ ] etc..

______________________________________________________________________________________________________
# Tasks
- [ ] Implement a Log for Server to keep track of all happenings
- [ ] Console commands (commented in the code what to add)
- [ ] Graphics upgrade (Need to research the other Java GUI a little bit more)
- [ ] Set of APIs for the Server, in order to make networking easier
- [ ] Resign of how the games work online
- [ ] Finish development on rest of existing games
- [ ] 1) TicTacToe
- [ ] 2) ConnectFour
- [ ] 3) Hangman
- [ ] 4) Snake
- [ ] 5) Word Whomp
- [ ] 6) Brickbreaker
- [ ] Take the games and make a class that connects them all, that way networking is easier. And Adding new games will not be difficult
- [ ] Implement HighScores Class
- [ ] Implement Awards Class
- [ ] Implement Trophies Class
- [ ] Implement Data Class
- [ ] Implement Player Class\
- [ ] Better the graphics of Gamehub
- [ ] Make the invite system functional
- [ ] split screen for 2/4 player matches
- [ ] Add in spectating
- [ ] Make separate, cleaner chat client
- [ ] Make Online list cleaner and more functional for invite system
- [ ] Make website with HTML, Javascript and CSS (don't have to host or anything, just make it
- [ ] Make android app for the game (more like a manual for the game), but we will add more functionality to it
- [ ] Anymore you can think of, go for it :) We can talk about which of this you would like in the post-beta(? name) But I believe all of this can be done

______________________________________________________________________________________________________
# Other Project Ideas
-  Full fledged RPS Game
-  Google chat sort of
-  Python communciation with Apps (Updaters)
-  GPS communication (App/Server/Website)
-  Game Programming
-  Android Applications
-  Android ("Suite Inventory")
-  Monopoly
-  Multigame App w/ multiplayer Server
-  Make minigames over a long time
-  proximity App (close to somebody else)
-  Discuession webpage/app for our group
- Private chat for us
-  Website/App to keep track of our ideas

# OFFICIAL RELEASE ADDITIONS
Have games loaded/instantiated at RUN-TIME vs COMPILE-TIME. This feature will allow users to play new games via GameHubLogin without having to recompile GameHubLogin. 


(Explained from messages below:)
btw, by having the games inherit from basegame class, we can have gamehublogin work on the fly. Meaning it doesn't have to be recompiled. Any new games are loaded on runtime. It is something I did in my java class, where we load objects at runtime vs compile time 
It is very tricky to do though, but what it would do is. Let's say Alex has gamehub isntalled on his pc. And he has 4 games. He can just get the new basketball game downlaoded to the jar, and next time he opens gamehub, it sees the game and adds the button/icon for it 
and he can open it up and run it like normal

yeah. I think that's what AI uses in a way. By loading stuff at runtime vs compile time. It would be the ultimate feature, that we will add in later. Because it will be tricky to implement fully. Everything will be abstract. Like in gamehub log in, we'd have a for loop to look in the directory for the games that have that basegame class, get the file name, look for an icon in the same directory (for the button logo), and the nisntantiate the objects and call constructor on them. Actually... that doesn't seem that difficult yet. 

The server programs will have to be game agnostic (don't care), and just pass the invites and etc. Ah... but The clients would have to be the ones to see if they have the game locally on their machines, and if they don't, say something 
This would be a feature for what would be an official release. Like a google play store where one can download new games
