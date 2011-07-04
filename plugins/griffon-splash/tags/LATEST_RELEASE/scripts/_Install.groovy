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

/**
 * @author Jim Shingler
 */

// check to see if we already have a SplashGriffonAddon
boolean addonIsSet1
builderConfig.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'SplashGriffonAddon' == builder
    }
}

if (!addonIsSet1) {
    println 'Adding SplashGriffonAddon to Builder.groovy'
    builderConfigFile.append('''
root.'SplashGriffonAddon'.addon=true
''')
}

text = 'SplashGriffonAddon.display(app)\n'
initializeScript = new File("${basedir}/griffon-app/lifecycle/Initialize.groovy")
if(!initializeScript.text.contains(text)) {
    initializeScript.text += text
}

text = 'def splashScreen = SplashScreen.getInstance()\n'
if(initializeScript.text.contains(text)) {
    initializeScript.text -= text
}

text = '''
splashScreen.splash()
splashScreen.waitForSplash()
'''.trim()
if(initializeScript.text.contains(text)) {
    initializeScript.text -= text
}

initializeScript.text = initializeScript.text.replaceAll("\nURL url = this.class.getResource","\n//URL url = this.class.getResource")
initializeScript.text = initializeScript.text.replaceAll("\nsplashScreen.setImage","\n//splashScreen.setImage")

text = 'SplashScreen.getInstance().dispose()\n'
readyScript = new File("${basedir}/griffon-app/lifecycle/Ready.groovy")
if(readyScript.text.contains(text)) {
    readyScript.text -= text
}