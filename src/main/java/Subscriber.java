import org.fusesource.mqtt.client.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import static java.time.temporal.ChronoUnit.MILLIS;

public class Subscriber {
    public static void main(String[] args) throws Exception {
        ArrayList<Long> listOfMessageLatencyTimes = new ArrayList<>();
        AtomicLong sumOfMessageLatency = new AtomicLong();

        // Connect to the broker
        MQTT mqtt = new MQTT();
        mqtt.setHost("localhost", 1883);
        BlockingConnection connection = mqtt.blockingConnection();
        connection.connect();

        // Subscribe to  a/b/c topic
        Topic[] topics = {new Topic("a/b/c", QoS.EXACTLY_ONCE)};
        connection.subscribe(topics);

        // Get Ads Messages
        while (true) {
            Message receivemessage = connection.receive();
            if (receivemessage != null) {
                String message = new String(receivemessage.getPayload());
                String[] split = message.split(" ");
                long messageLatency = calculateMessageLatency(split[1]);
                sumOfMessageLatency.addAndGet(messageLatency);
                listOfMessageLatencyTimes.add(messageLatency);
                printCurrentResult(sumOfMessageLatency.get(),listOfMessageLatencyTimes.size(), split[0]);
                receivemessage.ack();
                Thread.sleep(500);
            }
        }
    }

    private static long calculateMessageLatency(String message) {
        LocalTime sentTime = LocalTime.parse(message);
        LocalTime receivedTime = java.time.LocalTime.now();
        return MILLIS.between(sentTime, receivedTime);
    }

    private static void printCurrentResult(long sumOfMessageLatency, int numberOfMessagesReceived, String messageNumber){
        System.out.println("======================");
        System.out.println("Message number: " + messageNumber);
        System.out.println("Number Of Messages Received : " + numberOfMessagesReceived);
        System.out.println("Total latency : " + sumOfMessageLatency);
        System.out.println("======================\n");
    }
}
