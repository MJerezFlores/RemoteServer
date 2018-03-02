package address;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.Enumeration;

public class AddressManager {

    public InetAddress getExternalAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while(true) {
                NetworkInterface netInterface = networkInterfaces.nextElement();
                if (!netInterface.isVirtual() && !netInterface.isLoopback() && netInterface.isUp()){
                    InetAddress validAddress = getValidAddress(netInterface);
                    if(validAddress!=null)
                        return validAddress;
                }
                if (!networkInterfaces.hasMoreElements()) throw new Error("Not network found it");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private InetAddress getValidAddress(NetworkInterface netInterface) throws IOException {
        for(InetAddress address : Collections.list(netInterface.getInetAddresses())){
            if (address instanceof Inet6Address)
                continue;
            if (!address.isReachable(3000))
                continue;

            if (!hasExternalAccess(address)) continue;
            return address;
        }
        return null;
    }

    private boolean hasExternalAccess(InetAddress address) {
        try (SocketChannel socket = SocketChannel.open()) {
            // again, use a big enough timeout
            socket.socket().setSoTimeout(3000);

            // bind the socket to your local interface
            socket.bind(new InetSocketAddress(address, 80));

            // try to connect to somewhere
            socket.connect(new InetSocketAddress("google.com", 80));
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

}