/*
 * Copyright 2007 the original author or authors.
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
package groovy.swing

import groovy.swing.factory.*
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.Logger
import org.jdesktop.swingx.*
import org.jdesktop.swingx.MultiSplitLayout.Divider
import org.jdesktop.swingx.MultiSplitLayout.Leaf
import org.jdesktop.swingx.border.DropShadowBorder
import org.jdesktop.swingx.border.IconBorder
import org.jdesktop.swingx.JXMonthView
import org.jdesktop.swingx.color.EyeDropperColorChooserPanel
import org.jdesktop.swingx.color.GradientPreviewPanel
import org.jdesktop.swingx.combobox.MapComboBoxModel
import org.jdesktop.swingx.decorator.ColorHighlighter
import org.jdesktop.swingx.decorator.PainterHighlighter
//import org.jdesktop.swingx.editors.ImagePicker
//import org.jdesktop.swingx.editors.PaintPicker
//import org.jdesktop.swingx.editors.ShapeChooser
import org.jdesktop.swingx.icon.ColumnControlIcon
import org.jdesktop.swingx.icon.EmptyIcon
import org.jdesktop.swingx.image.ColorTintFilter
import org.jdesktop.swingx.image.FastBlurFilter
import org.jdesktop.swingx.image.GaussianBlurFilter
import org.jdesktop.swingx.image.StackBlurFilter
import org.jdesktop.swingx.painter.*
import org.jdesktop.swingx.painter.effects.*
import org.jdesktop.swingx.tips.DefaultTip
import org.jdesktop.swingx.tips.DefaultTipOfTheDayModel


/**
 * @author James Williams
 * @author Danno Ferrin
 */
public class SwingXBuilder extends SwingBuilder {
    private static final Logger log = Logger.getLogger(SwingXBuilder.getName())

    Map shortcuts = [:]
    Map componentCollections = [:]

    public SwingXBuilder( boolean init = true ) {
        super( false )
        if(init) {
           sxbAutoRegiser()
        }
    }

    protected Factory resolveFactory(Object name, Map attributes, Object value) {
        def classicSwing = (boolean) attributes.remove("classicSwing")
        if (classicSwing != null && classicSwing) {
            // don't use GString, bad equality testing
            return super.resolveFactory("classicSwing:" + name, attributes, value)
        } else {
            return super.resolveFactory(name, attributes, value)
        }
    }

    public sxbAutoRegiser() {
        // if java did atomic blocks, this would be one
        synchronized (this) {
            if (autoRegistrationRunning || autoRegistrationComplete) {
                // registration already done or in process, abort
                return;
            }
        }
        autoRegistrationRunning = true;
        try {
            sxbCallAutoRegisterMethods(getClass());
        } finally {
            autoRegistrationComplete = true;
            autoRegistrationRunning = false;
        }
    }


    private void sxbCallAutoRegisterMethods(Class declaredClass) {
        if (declaredClass == null) {
            return;
        }
        sxbCallAutoRegisterMethods(declaredClass.getSuperclass());
        if (declaredClass == SwingXBuilder) {
            new HashMap(getFactories()).each {k, v ->
                // don't use GString, bad equality testing
                registerFactory('classicSwing:' + k, v)
            }
        }

        for (Method method in declaredClass.getDeclaredMethods()) {
            if (method.getName().startsWith("register") && method.getParameterTypes().length == 0) {
                registringGroupName = method.getName().substring("register".length());
                registrationGroup.put(registringGroupName, new TreeSet<String>());
                try {
                    if (Modifier.isPublic(method.getModifiers())) {
                        method.invoke(this);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Cound not init " + getClass().getName() + " because of an access error in " + declaredClass.getName() + "." + method.getName(), e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("Cound not init " + getClass().getName() + " because of an exception in " + declaredClass.getName() + "." + method.getName(), e);
                } finally {
                    registringGroupName = "";
                }
            }
        }
    }

    public void addShortcut(className, propName, shortcut) {
        if (shortcuts[className] != null) {
            shortcuts[className].put(shortcut, propName)
        }
        else {
            def a = [(shortcut.toString()): propName]
            shortcuts.put(className, a)
        }
    }

    void registerShortcutHandler() {
        addAttributeDelegate {builder, node, attributes ->
            def shortcutList = builder.shortcuts[node.getClass()]
            if (shortcutList) {
                shortcutList.each {entry ->
                    if (attributes[entry.key] != null) {
                        attributes.put(
                                entry.getValue(),
                                attributes.remove(entry.key))
                    }
                }
            }
        }
    }

    def registerSwingxLayouts() {
        registerFactory("multiSplitLayout", new LayoutFactory(MultiSplitLayout))
        registerFactory("stackLayout", new LayoutFactory(StackLayout))
        //use these instead of box layout
        registerFactory("verticalLayout", new LayoutFactory(VerticalLayout))
        registerFactory("horizontalLayout", new LayoutFactory(HorizontalLayout))
    }


    def registerSwingxComponents() {
        registerFactory("frame", new JXFrameFactory())
        registerBeanFactory("panel", JXPanel)
        registerBeanFactory("titledPanel", JXTitledPanel)
        registerBeanFactory("titledSeparator", JXTitledSeparator)
        registerFactory("label", new TextArgWidgetFactory(JXLabel))
        registerFactory("button", new RichActionWidgetFactory(JXButton))
        registerBeanFactory("busyLabel", JXBusyLabel)
        registerBeanFactory("glassBox", JXGlassBox)
        registerBeanFactory("findPanel", JXFindPanel)
        registerBeanFactory("findBar", JXFindBar)
        registerBeanFactory("searchPanel", JXSearchPanel)
        registerBeanFactory("errorPane", JXErrorPane)
        registerBeanFactory("imageView", JXImageView)
        registerBeanFactory("imagePanel", JXImagePanel)
        registerBeanFactory("datePicker", JXDatePicker)
        registerFactory("dialog", new JXDialogFactory())
        registerBeanFactory("list", JXList)
        registerBeanFactory("loginPanel", JXLoginPane)
        // registerBeanFactory("loginDialog", JXLoginPane.JXLoginDialog)
        registerBeanFactory("monthView", JXMonthView)
        registerBeanFactory("statusBar", JXStatusBar)
        registerBeanFactory("collapsiblePane", JXCollapsiblePane)
        registerFactory("editorPane", new TextArgWidgetFactory(JXEditorPane))
        registerBeanFactory("colorSelectionButton", JXColorSelectionButton)
        registerFactory("graph", new JXGraphFactory())
        registerBeanFactory("header", JXHeader)
        registerFactory("hyperlink", new TextArgWidgetFactory(JXHyperlink))
        registerBeanFactory("tipOfTheDay", JXTipOfTheDay)
        registerBeanFactory("table", JXTable)
        registerBeanFactory("tree", JXTree)
        registerBeanFactory("treeTable", JXTreeTable)
        registerBeanFactory("defaultTipModel", DefaultTipOfTheDayModel)
        registerBeanFactory("defaultTip", DefaultTip)
        registerBeanFactory("taskPaneContainer", JXTaskPaneContainer)
        registerBeanFactory("taskPane", JXTaskPane)
        registerBeanFactory("radioGroup", JXRadioGroup)
        registerBeanFactory("eyeDropperColorChooser", EyeDropperColorChooserPanel)
        registerBeanFactory("gradientPreviewPanel", GradientPreviewPanel)
        registerBeanFactory("gradientChooser", JXGradientChooser)
        registerBeanFactory("rootPane", JXRootPane)
        registerBeanFactory("tableHeader", JXTableHeader)
        registerBeanFactory("emptyIcon", EmptyIcon)
        // registerFactory("sortArrowIcon", new SortArrowIconFactory())
        registerBeanFactory("columnControlIcon", ColumnControlIcon)
        // registerBeanFactory("paintPicker", PaintPicker)
        // registerBeanFactory("shapeChooser", ShapeChooser)
        // registerBeanFactory("imagePicker", ImagePicker)
        registerBeanFactory("mapComboBoxModel", MapComboBoxModel)
        registerFactory("listComboBoxModel", new ListCBModelFactory())
        registerBeanFactory("dropShadowBorder", DropShadowBorder)
        registerBeanFactory("iconBorder", IconBorder)
        registerBeanFactory("painterGlasspane", PainterGlasspane)
        registerBeanFactory("leaf", MultiSplitLayout.Leaf)
        registerBeanFactory("divider", MultiSplitLayout.Divider)
        registerFactory("split", new SplitFactory())
        registerBeanFactory("multiSplitPane", JXMultiSplitPane)
    }

    def registerThreading() {
        registerFactory("withWorker", new SwingWorkerFactory())
    }

    def registerSwingxFilters() {
        registerBeanFactory("colorTintFilter", ColorTintFilter)
        registerBeanFactory("fastBlurFilter", FastBlurFilter)
        registerBeanFactory("gaussianBlurFilter", GaussianBlurFilter)
        registerBeanFactory("stackBlurFilter", StackBlurFilter)
    }

    def registerSwingxEffects() {
        registerFactory("glowPathEffect", new AbstractAreaEffectFactory(GlowPathEffect))
        registerFactory("innerGlowPathEffect", new AbstractAreaEffectFactory(InnerGlowPathEffect))
        registerFactory("innerShadowPathEffect", new AbstractAreaEffectFactory(InnerShadowPathEffect))
        registerFactory("neonBorderEffect", new AbstractAreaEffectFactory(NeonBorderEffect))
        registerFactory("shadowPathEffect", new AbstractAreaEffectFactory(ShadowPathEffect))
    }

    def registerSwingxPainters() {
        registerFactory("capsulePainter", new AbstractAreaPainterFactory(CapsulePainter))
        registerFactory("imagePainter", new AbstractAreaPainterFactory(ImagePainter))
        registerFactory("mattePainter", new AbstractAreaPainterFactory(MattePainter))
        registerFactory("shapePainter", new AbstractAreaPainterFactory(ShapePainter))
        registerFactory("textPainter", new AbstractAreaPainterFactory(TextPainter))

        registerFactory("alphaPainter", new CompoundPainterFactory(AlphaPainter))
        registerFactory("compoundPainter", new CompoundPainterFactory(CompoundPainter))

        registerFactory("rectanglePainter", new RectanglePainterFactory(RectanglePainter))

        registerBeanFactory("busyPainter", BusyPainter)
        registerBeanFactory("pinstripePainter", PinstripePainter)
        registerBeanFactory("glossPainter", GlossPainter)
        registerBeanFactory("checkerboardPainter", CheckerboardPainter)
        // registerBeanFactory("urlPainter", URLPainter)
    }

    def registerSwingxHighlighters() {
        registerBeanFactory("colorHighlighter", ColorHighlighter)
        registerBeanFactory("painterHighlighter", PainterHighlighter)
        registerFactory("altStripingHighlighter", new HighlighterFactoryHelper())
        registerFactory("simpleStripingHighlighter", new HighlighterFactoryHelper())
    }

    def registerSwingxAnimators() {
        // high quality stuff for Java 5.0+
        registerFactory("animate", new TimingFrameworkFactory());
    }
}
