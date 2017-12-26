package restaurant.communication.android;

import android.net.wifi.WifiManager;

import restaurant.communication.core.IPeer;
import restaurant.communication.core.impl.ISocketWrapper;
import restaurant.communication.core.impl.Peer;


/**
 * Created by Doerthous on 2017/12/18.
 */

public class PeerFactory {
    public static IPeer getPeer(WifiManager wm){
        ISocketWrapper socketWrapper = new SocketWrapper(wm.createMulticastLock("test wifi"));
        return new Peer(socketWrapper, new IPTools(wm));
    }
}
