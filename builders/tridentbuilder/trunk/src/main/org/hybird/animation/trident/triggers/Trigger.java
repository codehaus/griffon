package org.hybird.animation.trident.triggers;

import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.TimelineState;

/**
 * This abstract class should be overridden by any class wanting to implement a
 * new Trigger. The subclass will define the events to trigger off of and any
 * listeners to handle those events. That subclass will call either
 * {@link #fire()} or {@link #fire(TriggerEvent)} to start the timeline based on
 * an event that occurred.
 * <p>
 * Subclasses should call one of the constructors in Trigger, according to
 * whether they want Trigger to discern between different TriggerEvents and
 * whether they want Trigger to auto-reverse the animation based on opposite
 * TriggerEvents.
 * <p>
 * Subclasses should call one of the <code>fire</code> methods based on whether
 * they want Trigger to perform any event logic or simply start the animation.
 * 
 * @author Chet
 * @author Rémy Rakic - Modifications for Trident + javadocs
 */
public abstract class Trigger
{
    private boolean disarmed = false;
    private Timeline timeline;
    private TriggerEvent triggerEvent;
    private boolean autoReverse = false;

    /**
     * Creates a Trigger that will start the timeline when {@link #fire()} is
     * called. Subclasses call this method to set up a simple Trigger that will
     * be started by calling {@link #fire()}, and will have no dependency upon
     * the specific {@link TriggerEvent} that must have occurred to start the
     * animator.
     * 
     * @param timeline
     *            the Timeline that will start when the Trigger is fired
     */
    protected Trigger (Timeline timeline)
    {
        this (timeline, null);
    }

    /**
     * Creates a Trigger that will start the timeline when
     * {@link #fire(TriggerEvent)} is called with an event that equals
     * triggerEvent.
     * 
     * @param timeline
     *            the Timeline that will start when the Trigger is fired
     * @param triggerEvent
     *            the TriggerEvent that must occur for this Trigger to fire
     */
    protected Trigger (Timeline timeline, TriggerEvent triggerEvent)
    {
        this (timeline, triggerEvent, false);
    }

    /**
     * Creates a Trigger that will start the timeline when
     * {@link #fire(TriggerEvent)} is called with an event that equals
     * triggerEvent. Also, automatically stops and reverses the timeline when
     * opposite event occurs, and stops reversing the timeline likewise when
     * triggerEvent occurs.
     * 
     * @param timeline
     *            the Timeline that will start when the Trigger is fired
     * @param triggerEvent
     *            the TriggerEvent that must occur for this Trigger to fire
     * @param autoReverse
     *            flag to determine whether the timeline should stop and reverse
     *            based on opposite triggerEvents.
     * @see TriggerEvent#getOppositeEvent()
     */
    protected Trigger (Timeline timeline, TriggerEvent triggerEvent,
            boolean autoReverse)
    {
        this.timeline = timeline;
        this.triggerEvent = triggerEvent;
        this.autoReverse = autoReverse;
    }

    /**
     * This method disables this Trigger and effectively noop's any actions that
     * would otherwise occur
     */
    public void disarm ()
    {
        disarmed = true;
    }

    /**
     * Called by subclasses to start the timeline if currentEvent equals the
     * event that the Trigger is based upon. Also, if the Trigger is set to
     * autoReverse, stops and reverses the timeline running in the opposite
     * direction as appropriate.
     * 
     * @param currentEvent
     *            the {@link TriggerEvent} that just occurred, which will be
     *            compared with the TriggerEvent used to construct this Trigger
     *            and determine whether the timeline should be started or
     *            reversed
     */
    protected void fire (TriggerEvent currentEvent)
    {
        if (disarmed)
        {
            return;
        }
        if (currentEvent == triggerEvent)
        {
            // event occurred; fire the animation
            direction = Direction.FORWARD;
            fire ();
        }
        else if (triggerEvent != null && currentEvent == triggerEvent.getOppositeEvent ())
        {
            // Opposite event occurred - run reverse anim if autoReverse
            if (autoReverse)
            {
                direction = Direction.BACKWARD;
                fire ();
            }
        }
    }

    /** The direction in which the timeline will be started. */
    private Direction direction = Direction.FORWARD;

    /**
     * Utility method called by subclasses to start the timeline. This variant
     * assumes that there need be no check of the TriggerEvent that fired, which
     * is useful for subclasses with simple events.
     */
    protected void fire ()
    {
        if (disarmed)
        {
            return;
        }
        direction.play (timeline);
    }

    /**
     * Utility method checking if the timeline is running in forward or reverse
     * direction.
     */
    protected boolean isTimelineRunning ()
    {
        return timeline.getState () == TimelineState.PLAYING_FORWARD
                || timeline.getState () == TimelineState.PLAYING_REVERSE;
    }

    /**
     * Utility enum wrapping the the way to start a timeline in both directions.
     */
    private static enum Direction
    {
        FORWARD
        {
            @Override
            public void play (Timeline t)
            {
                t.play ();
            }
        },
        BACKWARD
        {
            @Override
            public void play (Timeline t)
            {
                t.playReverse ();
            }
        };

        protected abstract void play (Timeline t);
    }
}