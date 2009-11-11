import java.awt.Font

class FontJigglerGriffonAddon {

    private static MAX_FONT = 96
    private static MIN_FONT = 6

    // lifecycle methods

    // called once, after the addon is created
    def addonInit(app) {
        
        java.awt.Component.metaClass.adjustFont = { adjustment ->
            doChange(delegate, font.size + adjustment)
        }
        java.awt.Component.metaClass.scaleFont = { adjustment ->
            doChange(delegate, font.size * adjustment)
        }
        java.awt.Component.metaClass.decreaseFont = { 
            doChange(delegate, font.size - 2)
        }
        java.awt.Component.metaClass.increaseFont = { 
            doChange(delegate, font.size + 2)
        }
        
    }

    /** Changes the font on an object. */
    private static void doChange(self, newSize) {
        if (newSize > MIN_FONT && newSize < MAX_FONT) {
            def newFont = new Font(self.font.name, self.font.style, newSize)
            self.font = newFont
        }
    }
}

