import javax.swing.JFrame
import groovy.swing.SwingBuilder
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.util.concurrent.CountDownLatch

includeTargets << griffonScript("Init")

target(default: "Displays all Feed icons") {
    def icons = []
    def pathResolver = new PathMatchingResourcePatternResolver(this.class.classLoader)
    pathResolver.getResources('classpath*:/com/zeusboxstudio/icons/16x16/*.png').each { r ->
        def (m, icon) = (r.getURL().path =~ /.*\/([a-zA-Z_\-]+)\.png/)[0]
        icons << icon
    }
    
    // hack to avoid script from terminating before time
    CountDownLatch latch = new CountDownLatch(1i)
    def swing = new SwingBuilder()
    swing.edt {
        swing.frame(title: 'Feed Icons', pack: true, visible: true, resizable: false,
                    defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE,
                    windowClosed: { latch.countDown() }) {
            borderLayout()
            tabbedPane(constraints: context.NORTH, preferredSize: [320, 420]) {
                ['16x16', '32x32'].each { size ->
                    scrollPane(title: size) {
                        panel {
                            gridLayout(cols: 4, rows: icons.size()/4)
                            icons.each { icon ->
                                label(icon: imageIcon("/com/zeusboxstudio/icons/${size}/${icon}.png"), toolTipText: icon)
                            }
                        } 
                    }
                }
            }
        }
    }

    latch.await()
}
