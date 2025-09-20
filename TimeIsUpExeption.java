/**
 * Exception used to stop search when time limit is passed.
 *
 * @author Fredrik Alexandre, author
 * @author Samuel Hagner, author
 * @version 1.0, 2025-09-24
 */

public class TimeIsUpExeption extends RuntimeException {

    /**
     * Creates an exception saying that search is out of time.
     */
    public TimeIsUpExeption() {
        super("Search time limit reached!");
    }

}
