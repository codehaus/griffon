package groovyx.ui.view

import org.jdesktop.swingx.JXStatusBar.Constraint
import org.jdesktop.swingx.JXStatusBar.Constraint.ResizeBehavior

statusBar() {
    label(id: 'status',
        text: 'Welcome to Groovy.',
        constraints: new Constraint(ResizeBehavior.FILL)
    )

    label(id: 'rowNumAndColNum',
        text: '1:1',
        preferredSize : [50, 10],
        constraints: new Constraint(ResizeBehavior.FIXED)
    )
}
