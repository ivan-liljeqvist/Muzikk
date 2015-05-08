package muzikk;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by IvanLiljeqvist on 08/05/15.
 *
 * A special class for Threads. The goal with this class is to be able
 * to ping other threads when this NotifyingThread has finished running.
 *
 * You add classes that implement ThreadCompleteListener interface as listeners to this thread.
 * When this NotifyingThread will finish running the listeners will be notified.
 *
 */

public abstract class NotifyingThread extends Thread {

    /*
        A set with listeners to notify.
        CopyOnWriteArraySet is used because it is thread safe and is used in such situations.
     */
    private final Set<ThreadCompleteListener> listeners = new CopyOnWriteArraySet<ThreadCompleteListener>();

    /*
        Adds a new listener that will be notified when this thread has finished running.
        @param listener - the listener you want to add
     */
    public final void addListener(final ThreadCompleteListener listener) {
        listeners.add(listener);
    }

    /*
        Removed a listener.
        @param listener - the listener you want to remove
     */
    public final void removeListener(final ThreadCompleteListener listener) {
        listeners.remove(listener);
    }

    /*
        Notifies all listener that this thread has finished running.
     */
    private final void notifyListeners() {
        for (ThreadCompleteListener listener : listeners) {
            listener.notifyOfThreadComplete(this);
        }
    }

    /*
        Function inherited from Thread. The only difference is that we want to notify all listeners when
        this thread has finished running.
     */

    @Override
    public final void run() {
        try {
            doRun();
        } finally {
            notifyListeners();
        }
    }

    /*
        This method needs to be implemented in each particular NotifyingThread instance.
        Used to give some kind of data to the listeners once this thread has finished running.
     */
    public abstract String[] extractParams();
    /*
        This method needs to be implemented in each particular NotifyingThread instance.
        What is the main task of this thread? Implement doRun and insert the task of this thread there.
     */
    public abstract void doRun();
}