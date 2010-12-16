@artifact.package@import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;
import griffon.plugins.slick.artifact.AbstractGriffonGameState;
import java.util.Map;

public class @artifact.name@ extends AbstractGriffonGameState {
    public static final int ID = 0; // CHANGE ME!!
    private @artifact.name.plain@Model model;
    private @artifact.name.plain@Controller controller;
    
    public int getID() { return ID; } 

    public void setModel(@artifact.name.plain@Model model) {
        this.model = model;
    }

    public void setController(@artifact.name.plain@Controller controller) {
        this.controller = controller;
    }

    public void mvcGroupInit(Map<String, ?> args) {
        getStateBasedApp().getStateBasedGame().addState(this);
    }

    protected void doInit(GameContainer gc, StateBasedGame game) throws SlickException {
        // handle initialization
    }
    
    protected void doRender(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        // handle painting
    }
}
