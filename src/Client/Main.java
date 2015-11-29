package Client;

import Main.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Client cl = new Client();
        //cl._listener.setDaemon(true);
       // cl._listener.start();
        cl.startProcess();

    }
}
