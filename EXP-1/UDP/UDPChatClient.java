import java.net.*;
import java.util.Scanner;

public class UDPChatClient {
    private static final String SERVER_ADDRESS = "localhost"; // Change to server IP address if needed
    private static final int SERVER_PORT = 12345;
    
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(); // No need for final if not reassigned
            InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);
            Scanner scanner = new Scanner(System.in);

            // Thread to receive messages from the server
            new Thread(() -> {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                while (true) {
                    try {
                        socket.receive(packet); // No error here now since socket is not reassigned
                        String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                        System.out.println("Received: " + receivedMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // Sending messages to the server
            while (true) {
                System.out.print("Enter message: ");
                String message = scanner.nextLine();
                byte[] messageBytes = message.getBytes();
                DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, serverAddress, SERVER_PORT);
                socket.send(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
