/*
 * griffon-jcalendar: JCalendar Griffon plugin
 * Copyright 2010 and beyond, Andres Almiray
 *
 * SmartGWT is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  SmartGWT is also
 * available under typical commercial license terms - see
 * smartclient.com/license.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */


import com.toedter.calendar.JCalendar
import com.toedter.calendar.JDateChooser
import com.toedter.calendar.JDayChooser
import com.toedter.calendar.JMonthChooser
import com.toedter.calendar.JYearChooser
import com.toedter.components.JLocaleChooser
import com.toedter.components.JSpinField

/**
 * @author Andres Almiray
 */
class JcalendarGriffonAddon {
    def factories = [
        calendar: JCalendar,
        dateChooser: JDateChooser,
        dayChooser: JDayChooser,
        monthChooser: JMonthChooser,
        yearChooser: JYearChooser,
        localeChooser: JLocaleChooser,
        spinField: JSpinField
    ]
}
