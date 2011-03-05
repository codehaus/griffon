/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import griffon.core.GriffonApplication
import griffon.plugins.csv.CSVReaderUtils
import au.com.bytecode.opencsv.CSVReader

/**
 * @author Andres Almiray
 */
class CsvGriffonAddon {
    void addonInit(GriffonApplication app) {
        CSVReader.metaClass.eachLine = { closure ->
            CSVReaderUtils.eachLine((CSVReader) delegate, closure)
        }

        File.metaClass.eachCsvLine = { closure ->
            CSVReaderUtils.eachLine((File) delegate, closure)
        }
        File.metaClass.toCsvReader = { settingsMap ->
            return CSVReaderUtils.toCsvReader((File)delegate, settingsMap)
        }

        InputStream.metaClass.eachCsvLine = { closure ->
            CSVReaderUtils.eachLine((InputStream) delegate, closure)
        }
        InputStream.metaClass.toCsvReader = { settingsMap ->
            return CSVReaderUtils.toCsvReader((InputStream)delegate, settingsMap)
        }

        Reader.metaClass.eachCsvLine = { closure ->
            CSVReaderUtils.eachLine((Reader)delegate, closure)
        }
        Reader.metaClass.toCsvReader = { settingsMap ->
            return CSVReaderUtils.toCsvReader((Reader)delegate, settingsMap)
        }

        String.metaClass.eachCsvLine = { closure ->
            CSVReaderUtils.eachLine((String) delegate, closure)
        }
        String.metaClass.toCsvReader = { settingsMap ->
            return CSVReaderUtils.toCsvReader((String)delegate, settingsMap)
        }
    }
}
