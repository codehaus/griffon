package org.hybird.animation.trident.triggers;

/**
 * Focus In/Out events
 * 
 * @author Chet
 * @author Rémy Rakic - Modifications for Trident + renaming the IN/OUT events to the more familiar GAINED/LOST
 */
public class FocusTriggerEvent extends TriggerEvent
{
    /**
     * Event fired when Component receives focus
     */
    public static final FocusTriggerEvent GAINED = new FocusTriggerEvent ("FocusGained");
    /**
     * Event fired when Component loses focus
     */
    public static final FocusTriggerEvent LOST = new FocusTriggerEvent ("FocusLost");

    /**
     * Private constructor; this helps ensure type-safe use of pre-defined
     * TriggerEvent objects.
     */
    private FocusTriggerEvent (String name)
    {
        super (name);
    }

    /**
     * This method finds the opposite of the current event.: GAINED -> LOST and LOST
     * -> GAINED.
     */
    public TriggerEvent getOppositeEvent ()
    {
        if (this == FocusTriggerEvent.GAINED)
        {
            return FocusTriggerEvent.LOST;
        }
        else
        {
            return FocusTriggerEvent.GAINED;
        }
    }
};