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
import java.util.Locale
import org.apache.pivot.wtk.CalendarButton
import org.apache.pivot.util.Filter
import org.apache.pivot.wtk.CalendarButtonListener

/**
 * @author Andres Almiray
 */
class CalendarButtonListenerAdapter extends BuilderDelegate implements CalendarButtonListener {
    private Closure onLocaleChanged
    private Closure onDisabledDateFilterChanged
    private Closure onSelectedDateKeyChanged
 
    CalendarButtonListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onLocaleChanged(Closure callback) {
        onLocaleChanged = callback
        onLocaleChanged.delegate = this
    }

    void onDisabledDateFilterChanged(Closure callback) {
        onDisabledDateFilterChanged = callback
        onDisabledDateFilterChanged.delegate = this
    }

    void onSelectedDateKeyChanged(Closure callback) {
        onSelectedDateKeyChanged = callback
        onSelectedDateKeyChanged.delegate = this
    }

    void localeChanged(CalendarButton arg0, Locale arg1) {
        if(onLocaleChanged) onLocaleChanged(arg0, arg1)
    }

    void disabledDateFilterChanged(CalendarButton arg0, Filter arg1) {
        if(onDisabledDateFilterChanged) onDisabledDateFilterChanged(arg0, arg1)
    }

    void selectedDateKeyChanged(CalendarButton arg0, String arg1) {
        if(onSelectedDateKeyChanged) onSelectedDateKeyChanged(arg0, arg1)
    }
}