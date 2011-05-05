package org.hybird.animation.trident.triggers;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JComponent;

import org.pushingpixels.trident.Timeline;

/**
 * FocusTrigger handles focus events and triggers an animation based on those
 * events. For example, to have anim start when component receives an GAINED event,
 * one might write the following:
 * 
 * <pre>
 * FocusTrigger trigger = FocusTrigger.addTrigger (component, anim,
 *         FocusTriggerEvent.GAINED);
 * </pre>
 * 
 * @author Chet
 * @author Rémy Rakic - Modifications for Trident + javadocs
 */
public class FocusTrigger extends Trigger implements FocusListener
{
    /**
     * Creates a non-auto-reversing FocusTrigger and adds it as a FocusListener
     * to the component.
     * 
     * @param component
     *            component that will generate FocusEvents for this trigger
     * @param timeline
     *            the Timeline that will start when the event occurs
     * @param event
     *            the FocusTriggerEvent that will cause the action to fire
     * @return FocusTrigger the resulting trigger
     */
    public static FocusTrigger addTrigger (JComponent component, Timeline timeline, FocusTriggerEvent event)
    {
        return addTrigger (component, timeline, event, false);
    }

    /**
     * Creates a FocusTrigger and adds it as a FocusListener to the component.
     * 
     * @param component
     *            component that will generate FocusEvents for this trigger
     * @param timeline
     *            the Timeline that will start when the event occurs
     * @param event
     *            the FocusTriggerEvent that will cause the action to fire
     * @param autoReverse
     *            flag to determine whether the timeline should stop and reverse
     *            based on opposite triggerEvents.
     * @return FocusTrigger the resulting trigger
     */
    public static FocusTrigger addTrigger (JComponent component, Timeline timeline,
            FocusTriggerEvent event, boolean autoReverse)
    {
        FocusTrigger trigger = new FocusTrigger (timeline, event, autoReverse);
        component.addFocusListener (trigger);
        return trigger;
    }

    /**
     * Creates a non-auto-reversing FocusTrigger, which should be added to a
     * Component that will generate the focus events of interest.
     * 
     * @param timeline
     *            the Timeline that will start when the event occurs
     * @param event
     *            the FocusTriggerEvent that will cause the action to fire
     */
    public FocusTrigger (Timeline timeline, FocusTriggerEvent event)
    {
        this (timeline, event, false);
    }

    /**
     * Creates a FocusTrigger, which should be added to a Component that will
     * generate the focus events of interest.
     * 
     * @param timeline
     *            the Timeline that will start when the event occurs
     * @param event
     *            the FocusTriggerEvent that will cause the action to fire
     * @param autoReverse
     *            flag to determine whether the timeline should stop and reverse
     *            based on opposite triggerEvents.
     */
    public FocusTrigger (Timeline timeline, FocusTriggerEvent event,
            boolean autoReverse)
    {
        super (timeline, event, autoReverse);
    }

    /**
     * Called by the object which added this trigger as a FocusListener. This
     * method starts the timeline if the trigger is waiting for a GAINED event.
     */
    public void focusGained (FocusEvent e)
    {
        fire (FocusTriggerEvent.GAINED);
    }

    /**
     * Called by the object which added this trigger as a FocusListener. This
     * method starts the timeline if the trigger is waiting for a LOST event.
     */
    public void focusLost (FocusEvent e)
    {
        fire (FocusTriggerEvent.LOST);
    }
}