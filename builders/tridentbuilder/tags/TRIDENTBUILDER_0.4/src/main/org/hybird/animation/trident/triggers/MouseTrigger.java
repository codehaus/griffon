package org.hybird.animation.trident.triggers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import org.pushingpixels.trident.Timeline;

/**
 * MouseTrigger handles mouse events and triggers an animation based on those
 * events. For example, to have anim start when component receives an ENTER
 * event, one might write the following:
 * 
 * <pre>
 * MouseTrigger trigger = MouseTrigger.addTrigger (component, anim,
 *         MouseTriggerEvent.ENTER);
 * </pre>
 * 
 * 
 * 
 * @author Chet
 */
public class MouseTrigger extends Trigger implements MouseListener
{

    /**
     * Creates a non-auto-reversing MouseTrigger and adds it as a listener to
     * component.
     * 
     * @param component
     *            component that will generate MouseEvents for this trigger
     * @param timeline
     *            the Timeline that will start when the event occurs
     * @param event
     *            the MouseTriggerEvent that will cause the action to fire
     * @return MouseTrigger the resulting trigger
     */
    public static MouseTrigger addTrigger (JComponent component, Timeline timeline, MouseTriggerEvent event)
    {
        return addTrigger (component, timeline, event, false);
    }

    /**
     * Creates a MouseTrigger and adds it as a listener to component.
     * 
     * @param component
     *            component that will generate MouseEvents for this trigger
     * @param timeline
     *            the Tmeline that will start when the event occurs
     * @param event
     *            the FocusTriggerEvent that will cause the action to fire
     * @param autoReverse
     *            flag to determine whether the animator should stop and reverse
     *            based on opposite triggerEvents.
     * @return FocusTrigger the resulting trigger
     */
    public static MouseTrigger addTrigger (JComponent component,
            Timeline timeline, MouseTriggerEvent event, boolean autoReverse)
    {
        MouseTrigger trigger = new MouseTrigger (timeline, event, autoReverse);
        component.addMouseListener (trigger);
        return trigger;
    }

    /**
     * Creates a non-auto-reversing MouseTrigger, which should be added to a
     * Component that will generate the mouse events of interest
     */
    public MouseTrigger (Timeline timeline, MouseTriggerEvent event)
    {
        this (timeline, event, false);
    }

    /**
     * Creates a MouseTrigger, which should be added to a Component that will
     * generate the mouse events of interest
     */
    public MouseTrigger (Timeline timeline, MouseTriggerEvent event, boolean autoReverse)
    {
        super (timeline, event, autoReverse);
    }

    /**
     * Called by the object which added this trigger as a MouseListener. This
     * method starts the timeline if the trigger is waiting for an ENTER event.
     */
    public void mouseEntered (MouseEvent e)
    {
        fire (MouseTriggerEvent.ENTER);
    }

    /**
     * Called by the object which added this trigger as a MouseListener. This
     * method starts the timeline if the trigger is waiting for an EXIT event.
     */
    public void mouseExited (MouseEvent e)
    {
        fire (MouseTriggerEvent.EXIT);
    }

    /**
     * Called by the object which added this trigger as a MouseListener. This
     * method starts the timeline if the trigger is waiting for a PRESS event.
     */
    public void mousePressed (MouseEvent e)
    {
        fire (MouseTriggerEvent.PRESS);
    }

    /**
     * Called by the object which added this trigger as a MouseListener. This
     * method starts the timeline if the trigger is waiting for a RELEASE event.
     */
    public void mouseReleased (MouseEvent e)
    {
        fire (MouseTriggerEvent.RELEASE);
    }

    /**
     * Called by the object which added this trigger as a MouseListener. This
     * method starts the timeline if the trigger is waiting for a CLICK event.
     */
    public void mouseClicked (MouseEvent e)
    {
        fire (MouseTriggerEvent.CLICK);
    }
}