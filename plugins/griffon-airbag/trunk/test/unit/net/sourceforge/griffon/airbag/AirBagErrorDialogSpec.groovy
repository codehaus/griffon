package net.sourceforge.griffon.airbag

import griffon.spock.UnitSpec
import java.awt.Dimension

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class AirBagErrorDialogSpec extends UnitSpec {

    def 'Airbag dialog should be able to display exception'() {
        when:
        AirBagErrorDialog dialog = new AirBagErrorDialog(null, 'title', new RuntimeException('Sample error message'))

        then:
        dialog.title == 'title'
        dialog.modal == true
    }

}
