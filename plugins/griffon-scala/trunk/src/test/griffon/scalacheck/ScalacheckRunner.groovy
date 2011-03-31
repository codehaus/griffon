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

package griffon.scalacheck

/**
 * @author Andres Almiray
 */
class ScalacheckRunner {
    private File outputDir
    private List specifications = []

    private stdout
    private stderr
    private switchingOut
    private switchingErr

    static void main(String[] args) {
        new ScalacheckRunner(args).run()
    }

    ScalacheckRunner(String[] args) {
        parseArguments(args)
        checkArguments()
        outputDir.mkdirs()
    }

    public void run() {
        if(!specifications) {
            println "No specifications defined."
            return
        }
        println "Running ${specifications.size()} specification${specifications.size() > 1 ? 's' : ''}."
        stdout = System.out
        stderr = System.err
        long start = System.currentTimeMillis()
        specifications.each { runSpecification(it) }
        long end = System.currentTimeMillis()
        System.out = stdout
        System.err = stderr
        stdout.println "\nDone in ${end - start} ms."
    }

    private void parseArguments(String[] args) {
        // TODO use CLIBuilder instead ?
        args?.each { token ->
            def nameValueSwitch = token =~ "--?(.*)=(.*)"
            if (nameValueSwitch.matches()) { // this token is a name/value pair (ex: --foo=bar or -z=qux)
                if(nameValueSwitch[0][1] == "o") outputDir = new File(nameValueSwitch[0][2])
            } else {
                def nameOnlySwitch = token =~ "--?(.*)"
                if (nameOnlySwitch.matches()) {  // this token is just a switch (ex: -force or --help)
                    argsMap[nameOnlySwitch[0][1]] = true
                } else { // single item tokens, append in order to an array of params
                    token.split(" ").each { specifications << it }
                }
            }
        }
    }

    private void checkArguments() {
        if(!outputDir) outputDir = new File("target/scalacheck")
    }

    private void runSpecification(String specClassName) {
        clear("TEST-${specClassName}.txt")
        clear("ERROR-${specClassName}.txt")
        Class specClass = null
        try {
            specClass = getClass().classLoader.loadClass(specClassName)
        } catch(Exception e) {
            error(specClassName, "'$specClassName' may not be an specification class", e)
            printout("!")
            return
        }

        def out = new FileOutputStream(new File(outputDir, "TEST-${specClassName}.txt"))
        def err = new FileOutputStream(new File(outputDir, "ERROR-${specClassName}.txt"))
        if(!switchingOut) {
            switchingOut = new SwitchingPrintStream(out, true)
            System.out = switchingOut
        } else {
            switchingOut.switchTo(out);
        }
        if(!switchingErr) {
            switchingErr = new SwitchingPrintStream(err, true)
            System.err = switchingErr
        } else {
            switchingErr.switchTo(err);
        }

        try {
            specClass.main([] as String[])
            printout(".")
            clear("ERROR-${specClassName}.txt", false)
        } catch(Throwable t) {
            error(specClassName, "Unnexpected error while running $specClassName", t)
            printout("E")
        } finally {
            clear("TEST-${specClassName}.txt", false)
        }
    }

    private void clear(String filename, boolean force = true) {
        File file = new File(outputDir, filename)
        if(file.exists() && (force || file.size() == 0)) file.delete()
    }

    private void error(String specClassName, String message, Throwable t) {
        File file = new File(outputDir, "ERROR-${specClassName}.txt")
        String text = file.text
        file.withPrintWriter { writer ->
            if(text) {
                writer.write(text)
                writer.write(System.getProperty("line.separator"))
            }
            writer.write(message)
            writer.write(System.getProperty("line.separator"))
            t.printStackTrace(writer)
            writer.flush()
        }
    }

    private void printout(String s) {
        stdout.print s
        stdout.flush()
    }
}
