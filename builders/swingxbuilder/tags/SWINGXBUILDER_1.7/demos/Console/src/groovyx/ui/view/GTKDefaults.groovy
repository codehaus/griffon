package groovyx.ui.view

import javax.swing.JComponent
import javax.swing.text.StyleConstants
import javax.swing.text.StyleContext

build(Defaults)

// change font to DejaVu Sans Mono, much clearer
styles.regular[StyleConstants.FontFamily] = 'DejaVu Sans Mono'
styles[StyleContext.DEFAULT_STYLE][StyleConstants.FontFamily] = 'DejaVu Sans Mono'

// possibly change look and feel
if (System.properties['java.version'] =~ /^1\.5/) {
    // GTK wasn't where it needed to be in 1.5, especially with toolbars
    // use metal instead
    lookAndFeel('metal', boldFonts:false)
    
    // we also need to turn on anti-alising ourselves
    key = com.sun.java.swing.SwingUtilities2.AA_TEXT_PROPERTY_KEY
    addAttributeDelegate {builder, node, attributes ->
        if (node instanceof JComponent) {
            node.putClientProperty(key, new Boolean(true));
        }
    }
}


