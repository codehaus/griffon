package coverflow

import griffon.core.GriffonApplication
import griffon.test.*
import groovy.swing.SwingBuilder
import griffon.coverflow.ui.StackLayout
import griffon.coverflow.ui.GradientPanel
import griffon.coverflow.ui.ImageFlow
import griffon.coverflow.ui.ImageFlowItem
import griffon.coverflow.ui.ImageFlowItem.Resource
import java.awt.BorderLayout
import java.awt.Font

class CoverFlowTests extends GriffonUnitTestCase {
  GriffonApplication app

  protected void setUp() {
    super.setUp()
  }

  protected void tearDown() {
    super.tearDown()
  }

  void testCoverFlow() {
    new SwingBuilder().edt {
      frame(title: 'Frame', size: [300, 600], show: true) {
        borderLayout()
        panel(constraints: NORTH) {
          borderLayout()
          def flow
          panel(constraints: CENTER, layout: new StackLayout()) {
            widget(new GradientPanel(), constraints: StackLayout.BOTTOM)
            def list = (1..10).collect { i ->
              return new ImageFlowItem(new Resource("/griffon-icon-16x16.png"), "Icon $i")
            }
            flow = widget(new ImageFlow(list), constraints: StackLayout.TOP, itemSpacing: 0.25, itemFont: new Font('sans-serif', Font.PLAIN, 12))
          }
          panel(constraints: WEST) {
            borderLayout()
            button("<", actionPerformed: { flow.previous() })
          }
          panel(constraints: EAST) {
            borderLayout()
            button(">", actionPerformed: { flow.next() })
          }
        }
        label("TEST")
        panel(constraints: SOUTH) {
          borderLayout()
          def flow
          panel(constraints: CENTER, layout: new StackLayout()) {
            widget(new GradientPanel(), constraints: StackLayout.BOTTOM)
            def list = (1..10).collect { i ->
              return new ImageFlowItem(new Resource("/griffon-icon-128x128.png"), "Icon $i")
            }
            flow = widget(new ImageFlow(list), constraints: StackLayout.TOP)
          }
          panel(constraints: WEST) {
            borderLayout()
            button("<", actionPerformed: { flow.previous() })
          }
          panel(constraints: EAST) {
            borderLayout()
            button(">", actionPerformed: { flow.next() })
          }
        }
      }
    }
  }
}
