package restaurant.service.android;

import android.net.wifi.WifiManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import restaurant.communication.android.PeerFactory;
import restaurant.communication.core.IPeer;
import restaurant.service.android.waiter.WaiterService;
import restaurant.service.core.IWaiterService;

/**
 * Created by Doerthous on 2017/12/22.
 */

public class ServiceFactory {
    public static IWaiterService getWaiterService(WifiManager wm){
        // 临时id
        String id = new SimpleDateFormat("HHmmssSS").format(new Date());
        return new WaiterService(PeerFactory.getPeer(id, wm));
    }
}
