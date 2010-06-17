import javax.swing.JFrame
import groovy.swing.SwingBuilder
import java.util.concurrent.CountDownLatch
import griffon.countries.Country

includeTargets << griffonScript("Init")

target(default: "Displays all Flag icons") {
    def countries = Country.COUNTRIES
    // hack to avoid script from terminating before time
    CountDownLatch latch = new CountDownLatch(1i)
    def swing = new SwingBuilder()
    swing.edt {
        swing.frame(title: 'Flags of the World', pack: true, visible: true, resizable: true,
                    defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE,
                    windowClosed: { latch.countDown() }) {
            scrollPane(preferredSize: [400, 420]) {
                panel {
                    gridLayout(cols: 1, rows: countries.size())
                    countries.each { country ->
                        try {
                            label(icon: imageIcon("/com/famfamfam/flags/icons/${country.code.toLowerCase()}.png"),
                                  "[$country.code] $country.name")
                        } catch(x) {
                            // missing icon
                            println "No icon found for $country"
                        }
                    }
                } 
            }
        }
    }

    latch.await()
}
