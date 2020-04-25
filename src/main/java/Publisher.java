import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import static java.time.temporal.ChronoUnit.MILLIS;

public class Publisher {
    public static void main(String[] args) throws Exception {
        LocalTime start = java.time.LocalTime.now();
        System.out.println("Test started: " + start);

        MQTT mqtt = new MQTT();
        mqtt.setHost("localhost", 1883);
        BlockingConnection connection = mqtt.blockingConnection();
        connection.connect();

        String send = "";
        for(int i = 0; i < 2000; i++) {
            send = java.time.LocalTime.now().toString();
            connection.publish("a/b/c", send.getBytes(), QoS.EXACTLY_ONCE, false);
        }

        LocalTime end = java.time.LocalTime.now();
        System.out.println("Test finished: " + end);
        System.out.println("Test took: " + MILLIS.between(start,end) + " ms.");
    }
}