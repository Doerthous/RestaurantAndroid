package restaurant.communication.android;

import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

import restaurant.communication.core.impl.IIPTools;
import restaurant.communication.core.impl.ISocketWrapper;


/**
 * Created by Doerthous on 2017/12/18.
 */

public class IPTools implements IIPTools {
    private WifiManager wm;
    public IPTools(WifiManager wm) {
        this.wm = wm;
    }

    private String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 24);
    }

    @Override
    public List<String> getAllIpv4() {
        List l = new ArrayList();
        l.add(intToIp(wm.getDhcpInfo().ipAddress));
        return l;
    }

    @Override
    public String getIpv4SubnetMask(String s) {
        return intToIp(wm.getDhcpInfo().netmask);
    }
}
