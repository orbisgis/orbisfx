# Main Application

Implementation of the Main Application API module.

### MainApplication

Implementation of the IMainApplication interface.
This class starts a FX Application and register its [Scene](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Scene.html) and its [Stage](https://docs.oracle.com/javase/8/javafx/api/javafx/stage/Stage.html).
On starting the Application, the file mainapplication.properties is loaded. This file contains the following properties :
 * title=`<String>`. Sets the title of the frame.
 * onTop=`true/false`. Indicates of the windows should stay on top of the others. Set to false by default.
 * state=`fullscreen/maximized/normal/iconified`. Sets the window state, `normal` by default.
   * `fullscreen` : the window take the whole screen and hide the widows menu bar (minimize/maximize/close buttons
   * `maximized` : the window take the whole screen but keep the widows menu bar (minimize/maximize/close buttons
   * `normal` : the window take the minimum width and height
   * `iconified` : the window is iconified (minimized) and hidden
 * fullscreenexitkey=`key`. Sets the key used to escape the fullscreen mode.
 * resizable=`true/false`. Make the window resizable or not. Set to `true` by default.
 * minwidth=`<int>`. Define the minimum width of the window.
 * maxwidth=`<int>`. Define the maximum width of the window.
 * minheight=`<int>`. Define the minimum height of the window.
 * maxheight=`<int>`. Define the maximum height of the window.

The application icon are stored under the name `orbisgis_*.png` where `*` is the size of the image in `16/24/32/48/64/128/256`.