package portmapper;

import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.support.igd.PortMappingListener;
import org.fourthline.cling.support.model.PortMapping;

import java.net.InetAddress;

public class UpnpPortMapper {

    public void openUpnpPorts(InetAddress localAddress, int port) {
        System.out.println("Opening ports for address: " + localAddress.getHostAddress());
        PortMapping[] arr = new PortMapping[2];

        arr[0] = new PortMapping(port, localAddress.getHostAddress(), PortMapping.Protocol.TCP,"Film Media Server");
        arr[1] = new PortMapping(port, localAddress.getHostAddress(), PortMapping.Protocol.UDP,"Film Media Server");

        UpnpServiceImpl upnpService = new UpnpServiceImpl(new PortMappingListener(arr));

        upnpService.getControlPoint().search();
    }
}