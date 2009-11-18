/*
 * Copyright 2009 the original author or authors.
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
package griffon.scalacheck;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Andres.Almiray
 */
class SwitchingPrintStream extends PrintStream {
    private OutputStream currentOutputStream;

    public SwitchingPrintStream(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
        currentOutputStream = out;
    }

    public SwitchingPrintStream(OutputStream out) {
        super(out);
        currentOutputStream = out;
    }

    public void switchTo(OutputStream out) {
        if(out == null || out == currentOutputStream) {
            return;
        }
        try {
            currentOutputStream.close();
        } catch(IOException ex) {
            Logger.getLogger(SwitchingPrintStream.class.getName()).log(Level.WARNING, null, ex);
        }
        currentOutputStream = out;
        this.out = out;
        try {
            Method init = PrintStream.class.getDeclaredMethod("init", new Class[]{OutputStreamWriter.class});
            init.setAccessible(true);
            init.invoke(this, new Object[]{new OutputStreamWriter(out)});
        } catch(Exception ex) {
            Logger.getLogger(SwitchingPrintStream.class.getName()).log(Level.WARNING, null, ex);
        }
    }
}
