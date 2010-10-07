package net.sourceforge.gvalidation.artifact

import org.codehaus.griffon.runtime.core.DefaultGriffonClass

/**
 * @author Nick Zhu
 */
class DefaultGriffonConstraintClass extends DefaultGriffonClass implements GriffonConstraintClass {

    def DefaultGriffonConstraintClass(app, clazz) {
        super(app, clazz, GriffonConstraintClass.TYPE, GriffonConstraintClass.TRAILING);
    }

}
