import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Lamports_Mutual_Exclusion {

    static EventThread site = new EventThread();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter process id:");
        int id = sc.nextInt();
        System.out.print("Enter priority:");
        int priority = sc.nextInt();
        String[][] peers = new String[2][2];
        System.out.print("Enter peer1 IP:");
        peers[0][0] = sc.next();
        System.out.print("Enter peer1 ID:");
        peers[0][1] = sc.next();
        System.out.print("Enter peer2 IP:");
        peers[1][0] = sc.next();
        System.out.print("Enter peer2 ID:");
        peers[1][1] = sc.next();
        site.start(id, priority, peers, 12345);

        // for any incoming message we call interrupt
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();

                // Start a new thread to handle client communication
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String message;
            while ((message = reader.readLine()) != null) {
                site.interrupt(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class EventThread extends Thread {

    int processID, priority, replyCount;
    static int PORT;
    String[][] peers = new String[2][2];
    ArrayList<Integer[]> queue = new ArrayList<>();

    public void start(int processID, int priority, String[][] peers, int port) {
        this.processID = processID;
        this.priority = priority;
        PORT = port;
        for (int i = 0; i < 2; i++) {
            System.arraycopy(peers[i], 0, this.peers[i], 0, 2);
        }
        start();
    }

    public void run() {
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            int choice = (int) (Math.random() * 5) + 1; // want to or not want to do an event to create randomness
            if (choice > 2) {
                int time = (int) ((Math.random() * 3) + 3);
                try {
                    sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            choice = (int) (Math.random() * 2) + 1; // select type of process: local or send event
            switch (choice) {
                case 1: // local event
                    System.out.println("Executing a local event, no need of mutual exclusion\n\n");
                    break;
                case 2: // send event
                    System.out.println("Executing a send event, need of mutual exclusion\n");

                    // update Queue
                    Integer[] processReq = new Integer[2];
                    processReq[0] = processID;
                    processReq[1] = priority;
                    if (queue.size() == 0)
                        queue.add(processReq);
                    else {
                        int index = find(queue, processReq);
                        queue.add(index, processReq);
                    }

                    // request critical section and wait till process on top of queue
                    replyCount = 0;
                    for (String[] peer : peers) {
                        sendMessage(peer[0], peer[1], processReq[0] + ":" + processReq[1]);
                    }
                    while (replyCount > 2 && queue.indexOf(processReq) != 0) ;

                    // critical section
                    System.out.println("Implementing Critical Section\n\n");
                    try {
                        sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Completed Critical Section\n\n");

                    // broadcast completion of critical section
                    for (String[] peer : peers) {
                        sendMessage(peer[0], peer[1], "CS completed");
                    }
            }
        }
    }

    private int find(ArrayList<Integer[]> queue, Integer[] processReq) {
        int start = 0, end = queue.size(), mid = (start + end) / 2;
        while (start < end) {
            if (queue.get(mid)[0] <= processReq[0]) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
            mid = (start + end) / 2;
        }
        if (mid == -1)
            mid = 0;
        return mid;
    }

    public void interrupt(String message) { // for mutual exclusion request from other sites
        if (Objects.equals(message, "Reply"))
            replyCount++;
        else if (Objects.equals(message, "CS completed"))
            queue.remove(0);
        else {
            Integer[] processReq = new Integer[2];
            int index = message.indexOf(':');
            processReq[0] = Integer.valueOf(message.substring(0, index));
            processReq[1] = Integer.valueOf(message.substring(index+1));
            index = find(queue, processReq);
            queue.add(index, processReq);
            for (String[] peer : peers) {
                sendMessage(peer[0], peer[1], "Reply");
            }
        }
    }

    private static void sendMessage(String peerIp, String peerId, String message) {
        try (Socket socket = new Socket(peerIp, PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            writer.println(message);
        } catch (IOException e) {
            System.err.println("Error sending message to " + peerId + ": " + e.getMessage());
        }
    }
}