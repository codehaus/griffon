/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.pivot.adapters
 
import griffon.pivot.impl.BuilderDelegate
import org.apache.pivot.util.CalendarDate
import org.apache.pivot.wtk.Calendar
import org.apache.pivot.wtk.CalendarSelectionListener

/**
 * @author Andres Almiray
 */
class CalendarSelectionListenerAdapter extends BuilderDelegate implements CalendarSelectionListener {
    private Closure onSelectedDateChanged
 
    CalendarSelectionListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onSelectedDateChanged(Closure callback) {
        onSelectedDateChanged = callback
        onSelectedDateChanged.delegate = this
    }

    void selectedDateChanged(Calendar arg0, CalendarDate arg1) {
        if(onSelectedDateChanged) onSelectedDateChanged(arg0, arg1)
    }
}