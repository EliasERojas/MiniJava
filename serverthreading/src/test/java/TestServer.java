import server.threading.Client;
import server.threading.Server;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class TestServer {
    public static void main(String[] args) {
        try {
            Server sv = new Server();
            Thread svt = new Thread(sv);
            svt.start();
            Thread.sleep(100);

            for (int i = 0; i < 16; i++) {
                Client c = new Client("localhost", 8080);
                if(i % 2 == 0) {
                    c.setStringToServer("yes");
                } else {
                    c.setStringToServer("no");
                }
                new Thread(c).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
