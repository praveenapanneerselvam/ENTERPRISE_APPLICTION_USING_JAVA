import java.net.*;
import java.util.*;

public class UDPChatServer {
    private static final int PORT = 12345;
    private static List<InetAddress> clientAddresses = new ArrayList<>();
    private static List<Integer> clientPorts = new ArrayList<>();
    
    public static void main(String[] args) {
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket(PORT);
            byte[] buffer = new byte[1024];
            DatagramPacket packet;

            System.out.println("Server is running...");

            while (true) {
                // Receive message from client
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                // Add new client
                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();
                
                if (!clientAddresses.contains(clientAddress) || !clientPorts.contains(clientPort)) {
                    clientAddresses.add(clientAddress);
                    clientPorts.add(clientPort);
                }

                String message = new String(packet.getData(), 0, packet.getLength());

                // Broadcast message to all clients
                for (int i = 0; i < clientAddresses.size(); i++) {
                    packet = new DatagramPacket(message.getBytes(), message.length(), clientAddresses.get(i), clientPorts.get(i));
                    socket.send(packet);
                }

                System.out.println("Received and broadcasted message: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
