# Clocks

Demo project for different ways to handle and display time:

- 7 segments LCD style display 
  00:00:00 (hours:minutes:seconds)
  
- Timer & stopwatch: 
  00:00:00 (minutes:seconds:milliseconds)
  
    - Inc- or decrement mode
    - Left or right click on display will set a new value.
    - Doubleclick will reset (if timer is not running).
               
- LED circle clock
    Outer circle showing minutes, inner one hours and seconds in center.
    
- 8x256(1024) led style matrix.

    - Showing the time is just an option here. Any text will do as well.
    - Maximum is 8(64) chars at a time.
    - Start/stop moving the text with doubleclick on display.

Installation

Run 'Clocks.jar' from the 'dist' folder

Additional Files
  
  logo.png: used for the App.
  bell.mp3: will be played if the counter reached 0.
