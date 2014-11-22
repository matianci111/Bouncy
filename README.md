Bouncy
======

Google Glass hand interactive game
The game asks the user to put his/her hand infront of the camera upon initiation for data collection. The program
transforms the collected RGB values into HSV values and finds the modes for each channels. Then the game thread
starts running, which starts the game and the hand tracking algorithm. User can control the plate with his/her
hand to catch the falling ball and to cause the ball to bounce back to the bricks, which will then destroy them. 
The user wins the game when all the bricks are destroyed, and loses the game if he/she is not able to catch the ball.

The Application starts from the MainActivity. Once the user confirms to enter the application, PreviewActivity is
activated, where a camera preview will be shown on the screen. If the user taps the pad, data will be recorded and 
the game is started. Screen, Gameupdate and GameloopThread classes will be initialized. The GameloopThread will keep
calling function from Gameupdate and keep updating the game, while the screen class will create instances of Ball, Block
and plate class and output them on the screen according to the data obtained from the Gameupdate Class. At the same time
the PreviewActivity class will keep tracking the hand in background and keep updating the location of the plate as the
location of the hand.

OpenCV libraries are included in this project. OpenCV manager must be installed in Google Glass for this Application to
work. OpenCV_x.x.x_Manager_x.x_armv7a-neon.apk manager apk is needed.


