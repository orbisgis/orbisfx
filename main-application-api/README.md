# Main Application API

API dedicated to the definition of all the interfaces which are used to build the User Interface (UI) of the OrbisStudio application.

### IMainApplication
Definition of the methods used to manipulate the FX classes [Scene](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Scene.html) and [Stage](https://docs.oracle.com/javase/8/javafx/api/javafx/stage/Stage.html).
The implementations should used the [BorderPane](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/BorderPane.html) class as root [Node](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html) or similar in order to be able to implements the methods :
`void addDock(IDock dock);`

To add CSS style the method `void registerStyle(URL cssUrl);` is used.

### IDock
UI element which can be displayed in the IMainApplication according to its DockingLocation returned by the methods `DockLocation getLocation();` and its content Node returned by the method `Node getBaseNode();`
