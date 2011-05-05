package org.hybird.animation.trident.triggers;

/**
 * Superclass for all TriggerEvents used in the Trigger classes. The methods
 * here are mostly protected; it is expected that callers will not use this
 * class directly, but will instead use subclasses with pre-defined event types.
 * The purpose of this superclass is to provide the ability for {@link Trigger}
 * to treat event types generically, rather than to have all even logic in the
 * subclasses of Trigger.
 * 
 * @author Chet
 * @author Rémy Rakic - toString method using the unused name
 */
public class TriggerEvent
{
    /**
     * The ID of events are simple strings. It is expected that subclasses will
     * define static objects that callers will use instead of users having to
     * manually create TriggerEvent objects from strings directly
     */
    private String name;

    /**
     * Protected constructor; this helps ensure type-safe use of pre-defined
     * TriggerEvent objects.
     */
    protected TriggerEvent (String name)
    {
        this.name = name;
    }

    /**
     * This method returns the 'opposite' event from itself. This is used by
     * {@link Trigger} in running an auto-reversing animation, to determine
     * whether an opposite event has occurred (and whether to stop/reverse the
     * animation). Note that some events may have no opposite. Default behavior
     * returns same event; subclasses with multiple/opposite events must
     * override to do the right thing here.
     */
    public TriggerEvent getOppositeEvent ()
    {
        return this;
    }

    @Override
    public String toString ()
    {
        StringBuilder builder = new StringBuilder ();
        builder.append (getClass ().getSimpleName ()).append (".").append (name);
        TriggerEvent oppositeEvent = getOppositeEvent ();
        if (oppositeEvent != this)
            builder.append (" [opposite event: ").append (oppositeEvent.getClass ().getSimpleName ())
                .append (".") .append (oppositeEvent.name).append ("]");
        return builder.toString ();
    }
}