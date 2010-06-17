import javax.swing.JFrame
import groovy.swing.SwingBuilder
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.util.concurrent.CountDownLatch

includeTargets << griffonScript("Init")

target(default: "Displays all Tango! icons") {
    def categories = [:]
    def pathResolver = new PathMatchingResourcePatternResolver(this.class.classLoader)
    pathResolver.getResources('classpath*:/org/freedesktop/tango/16x16/**/*.png').each { r ->
        def (m, category, icon) = (r.getURL().path =~ /.*\/(\w+)\/([a-zA-Z_\-]+)\.png/)[0]
        categories.get(category, []) << icon
    }
    
    // hack to avoid script from terminating before time
    CountDownLatch latch = new CountDownLatch(1i)
    def swing = new SwingBuilder()
    swing.edt {
        swing.frame(title: 'Tango! Icons', pack: true, visible: true, resizable: false,
                    defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE,
                    windowClosed: { latch.countDown() }) {
            borderLayout()
            tabbedPane(constraints: context.NORTH, preferredSize: [320, 420]) {
                categories.each { category, icons ->
                    scrollPane(title: category) {
                        panel {
                            gridLayout(cols: 3, rows: icons.size())
                            icons.each { icon ->
                                label(icon: imageIcon("/org/freedesktop/tango/16x16/${category}/${icon}.png"), toolTipText: icon)
                                label(icon: imageIcon("/org/freedesktop/tango/22x22/${category}/${icon}.png"), toolTipText: icon)
                                label(icon: imageIcon("/org/freedesktop/tango/32x32/${category}/${icon}.png"), toolTipText: icon)
                            }
                        } 
                    }
                }
            }
        }
    }

    latch.await()
}
