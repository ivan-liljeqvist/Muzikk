package muzikk.backend;

/**
 * Created by IvanLiljeqvist on 08/05/15.
 *
 * This interface will be used to notify the class that implements this
 * interface that another thread has completed.
 */

public interface ThreadCompleteListener<T> {
    //The listener function. Will be called when 'thread' has finished running.
    void notifyOfThreadComplete(final NotifyingThread<T> thread);
}
