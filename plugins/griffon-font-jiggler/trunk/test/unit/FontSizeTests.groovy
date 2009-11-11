import javax.swing.JTextArea
import javax.swing.JButton

class FontSizeTests extends GroovyTestCase {

    void setUp() {
        // trigger addon
        new FontJigglerGriffonAddon().addonInit(null)
    }

    void testJButton() {
        def component = new JButton()
        def oldValue = component.font.size
        component.increaseFont()
        def newValue = component.font.size
        assert newValue == oldValue + 2
    }
    void testIncreasingFont() {
        
        def component = new JTextArea()
        def oldValue = component.font.size
        component.increaseFont()
        def newValue = component.font.size
        assert newValue == oldValue + 2
    }

    void testAdjustingFontUp() {
        
        def component = new JTextArea()
        def oldValue = component.font.size
        component.adjustFont(4)
        def newValue = component.font.size
        assert newValue == oldValue + 4
    }

    void testDecreasingFont() {
        
        def component = new JTextArea()
        def oldValue = component.font.size
        component.decreaseFont()
        def newValue = component.font.size
        assert newValue == oldValue -2
    }

    void testAdjustingFontDown() {
        
        def component = new JTextArea()
        def oldValue = component.font.size
        component.adjustFont(-4)
        def newValue = component.font.size
        assert newValue == oldValue -4
    }
}
