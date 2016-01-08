# Clocks

Demo project for different ways to handle and display time:

<h5>7-segments LCD style display</h5>

![Alt text](/../master/screenshots/lcdwatch.png?raw=true "")
  
  00:00:00 (hours:minutes:seconds)
  
<h5>Timer & stopwatch</h5>

  00:00:00 (minutes:seconds:milliseconds)
  
  - Inc- or decrement mode
  - Left or right click on display will set a new value.
  - Doubleclick will reset (if timer is not running).
               
<h5>LED circle clock</h5>
  
  Outer circle showing minutes, inner one hours and seconds in center.
    
<h5>8x256(1024) LED style matrix</h5>

  - Showing the time is just an option here. Any text will do as well.
  - Maximum is 8(64) chars at a time.
  - Start/stop moving the text with doubleclick on display.

<h5>Installation</h5>

  Run 'Clocks.jar' from the 'dist' folder

<h5>Additional Files</h5>
  
  logo.png: used for the App.
  bell.mp3: will be played if the counter reached 0.
