@artifact.package@import griffon.plugins.processing.artifact.AbstractGriffonProcessingView;

public class @artifact.name@ extends AbstractGriffonProcessingView {
    public @artifact.name.plain@Model model;
    public @artifact.name.plain@Controller controller;

    public void setModel(@artifact.name.plain@Model model) {
        this.model = model;
    }

    public void setController(@artifact.name.plain@Controller controller) {
        this.controller = controller;
    }

    private float y = 100;

    // The statements in the setup() function 
    // execute once when the program begins
    public void setup() {
        size(200, 200);  // Size should be the first statement
        stroke(255);     // Set line drawing color to white
        frameRate(30);
    }

    // The statements in draw() are executed until the 
    // program is stopped. Each statement is executed in 
    // sequence and after the last line is read, the first 
    // line is executed again.
    public void draw() {
        background(0);   // Set the background to black
        y = y - 1; 
        if (y < 0) { y = height; } 
        line(0, y, width, y); 
    }
}
