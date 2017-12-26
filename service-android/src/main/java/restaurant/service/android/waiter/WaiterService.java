package restaurant.service.android.waiter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import restaurant.communication.core.ICommandObserver;
import restaurant.communication.core.IData;
import restaurant.communication.core.IPeer;
import restaurant.service.core.IWaiterService;
import restaurant.service.core.impl.InterModuleCommunication;

import static java.lang.Thread.sleep;

/**
 * Created by Doerthous on 2017/12/22.
 */

public class WaiterService implements IWaiterService, ICommandObserver {
    private IPeer peer;

    public WaiterService(IPeer peer) {
        nObservers = new ArrayList<>();
        aObservers = new ArrayList<>();
        this.peer = peer;
        peer.addCommandObserver(this,IPeer.CMD_ID_IS_ALREADY_IN_USED);
        peer.addCommandObserver(this,
                InterModuleCommunication.CommandToWaiter.MANAGEMENT_LOGIN_ACK);
        peer.addCommandObserver(this,
                InterModuleCommunication.CommandToWaiter.MANAGEMENT_CHANGE_PASSWORD_ACK);
        peer.addCommandObserver(this,
                InterModuleCommunication.CommandToWaiter.MANAGEMENT_DISH_FINISHED);
        peer.addCommandObserver(this,
                InterModuleCommunication.CommandToWaiter.MANAGEMENT_SERVE_CUSTOMER);
        peer.init();
        peer.start(new SimpleDateFormat("HHmmssSS").format(new Date()));
    }

    private String failedReason;
    private Boolean isResponse;
    private Boolean isSuccess;

    @Override
    public String getAccount() {
        return peer.getId();
    }
    @Override
    public Boolean login(String account, String password) {
        peer.sendCommand(InterModuleCommunication.ModuleId.MANAGEMENT,
                InterModuleCommunication.CommandToManagement.WAITER_LOGIN,
                InterModuleCommunication.Data.MW.login(account, password));
        isResponse = false;
        while(!isResponse){
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
    @Override
    public String getLoginFailedReason() {
        return failedReason;
    }
    @Override
    public Boolean changePassword(String account, String oldPassword, String newPassword) {
        peer.sendCommand(InterModuleCommunication.ModuleId.MANAGEMENT,
                InterModuleCommunication.CommandToManagement.WAITER_CHANGE_PASSWORD,
                InterModuleCommunication.Data.MW.changePassword(account, oldPassword, newPassword));
        isResponse = false;
        while(!isResponse);
        return isSuccess;
    }
    @Override
    public String getChangePasswrodFailedReason() {
        return failedReason;
    }
    private List<INotificationObserver> nObservers;
    @Override
    public void addNotificationObserver(INotificationObserver iNotificationObserver) {
        nObservers.add(iNotificationObserver);
    }
    private List<IAccountObserver> aObservers;
    @Override
    public void addAccountObserver(IAccountObserver iAccountObserver) {
        aObservers.add(iAccountObserver);
    }

    @Override
    public void reportIssue(String s, String s1) {
        peer.sendCommand(InterModuleCommunication.ModuleId.MANAGEMENT,
                InterModuleCommunication.CommandToManagement.WAITER_LOGIN,
                InterModuleCommunication.Data.MW.login(s, s1));
    }
    @Override
    public void update(IData iData) {
        String cmd = iData.getCommand();
        InterModuleCommunication.Data.MW d = (InterModuleCommunication.Data.MW)iData.getData();
        if(IPeer.CMD_ID_IS_ALREADY_IN_USED.equals(cmd)){
            for(IAccountObserver accountObserver: aObservers){
                accountObserver.forceGoDown();
            }
            return;
        }
        if(InterModuleCommunication.CommandToWaiter.MANAGEMENT_LOGIN_ACK.equals(cmd)){
            synchronized (this) {
                isResponse = true;
                isSuccess = d.isSuccess;
                failedReason = d.failedReason;
                if (isSuccess) {
                    peer.start(d.account);
                }
            }
            return;
        }
        if(InterModuleCommunication.CommandToWaiter.MANAGEMENT_CHANGE_PASSWORD_ACK.equals(cmd)) {
            synchronized (this) {
                isResponse = true;
                isSuccess = d.isSuccess;
                failedReason = d.failedReason;
            }
            return;
        }
        if(InterModuleCommunication.CommandToWaiter.MANAGEMENT_DISH_FINISHED.equals(cmd)) {
            for(INotificationObserver observer : nObservers){
                observer.dishFinish(d.dishName, d.tableId);
            }
            return;
        }
        if(InterModuleCommunication.CommandToWaiter.MANAGEMENT_SERVE_CUSTOMER.equals(cmd)) {
            for(INotificationObserver observer : nObservers){
                observer.customerCall(d.tableId);
            }
            return;
        }
    }
}
