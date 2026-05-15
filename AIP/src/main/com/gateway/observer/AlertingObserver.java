package main.com.gateway.observer;

public class AlertingObserver implements Observer {
    private static final int ERROR_THRESHOLD = 3;
    private int errorCount = 0;

    @Override
    public void update(Event event, Object data) {
        if (event == Event.REQUEST_FAILED) {
            errorCount++;
            if (errorCount >= ERROR_THRESHOLD) {
                System.out.println("[ALERT] High error rate detected!");
                errorCount = 0; // сброс после алерта
            }
        } else {
            errorCount = 0;
        }
    }
}
