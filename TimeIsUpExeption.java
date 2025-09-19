public class TimeIsUpExeption extends RuntimeException {

    public TimeIsUpExeption() {
        super("Search time limit reached");
    }

}
