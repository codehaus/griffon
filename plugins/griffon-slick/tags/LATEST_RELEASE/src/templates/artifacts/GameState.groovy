@artifact.package@import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.SlickException
import org.newdawn.slick.Graphics
import org.codehaus.griffon.runtime.slick.AbstractGriffonGameState

class @artifact.name@ extends AbstractGriffonGameState {
    static final int ID = 0 // CHANGE ME!!
    def model
    def controller
    
    int getID() { ID } 
    
    void mvcGroupInit(Map<String, Object> args) {
        app.game.addState(this)
    }
    
    protected void doInit(GameContainer gc, StateBasedGame game) throws SlickException {
        // handle initialization
    }
    
    protected void doRender(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        // handle painting
    }
}
