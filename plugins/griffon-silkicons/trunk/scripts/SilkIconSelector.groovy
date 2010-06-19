import javax.swing.JFrame
import groovy.swing.SwingBuilder
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.util.concurrent.CountDownLatch

includeTargets << griffonScript("Init")

target(default: "Displays all Silk icons") {
    def icons = []
    def pathResolver = new PathMatchingResourcePatternResolver(this.class.classLoader)
    pathResolver.getResources('classpath*:/com/famfamfam/silk/icons/*.png').each { r ->
        def (m, icon) = (r.getURL().path =~ /.*\/([a-zA-Z0-9_\-]+)\.png/)[0]
        icons << icon
    }
    
    // hack to avoid script from terminating before time
    CountDownLatch latch = new CountDownLatch(1i)
    def swing = new SwingBuilder()
    swing.edt {
        swing.frame(title: 'Silk Icons', pack: true, visible: true, resizable: true,
                    defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE,
                    windowClosed: { latch.countDown() }) {
            scrollPane(preferredSize: [620, 420]) {
                panel {
                    gridLayout(cols: 4, rows: icons.size()/4)
                    icons.each { icon ->
                        label(icon, icon: imageIcon("/com/famfamfam/silk/icons/${icon}.png"), toolTipText: icon)
                    }
                } 
            }
        }
    }

    latch.await()
}
