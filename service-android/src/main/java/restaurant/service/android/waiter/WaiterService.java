package restaurant.service.android.waiter;

import java.util.ArrayList;
import java.util.List;

import restaurant.communication.core.ICommandObserver;
import restaurant.communication.core.IData;
import restaurant.communication.core.IPeer;
import restaurant.service.core.IWaiterService;
import restaurant.service.core.impl.InterModuleCommunication;

/**
 * Created by Doerthous on 2017/12/22.
 */

public class WaiterService implements IWaiterService, ICommandObserver {
    private IPeer peer;

    public WaiterService(IPeer peer) {
        observers = new ArrayList<>();
        this.peer = peer;
        peer.addCommandObserver(this,
                InterModuleCommunication.CommandToWaiter.MANAGEMENT_LOGIN_ACK);
        peer.addCommandObserver(this,
                InterModuleCommunication.CommandToWaiter.MANAGEMENT_CHANGE_PASSWORD_ACK);
        peer.addCommandObserver(this,
                InterModuleCommunication.CommandToWaiter.MANAGEMENT_DISH_FINISHED);
        peer.addCommandObserver(this,
                InterModuleCommunication.CommandToWaiter.MANAGEMENT_SERVE_CUSTOMER);
        peer.start();
    }


    private String failedReason;
    private Boolean isResponse;
    private Boolean isSuccess;
    @Override
    public Boolean login(String account, String password) {
        peer.sendCommand(InterModuleCommunication.ModuleId.MANAGEMENT,
                InterModuleCommunication.CommandToManagement.WAITER_LOGIN,
                InterModuleCommunication.Data.MW.login(account, password));
        isResponse = false;
        while(!isResponse);
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
    private List<INotificationObserver> observers;
    @Override
    public void addNotificationObserver(INotificationObserver iNotificationObserver) {
        observers.add(iNotificationObserver);
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
        if(InterModuleCommunication.CommandToWaiter.MANAGEMENT_LOGIN_ACK.equals(cmd)){
            isResponse = true;
            isSuccess = d.isSuccess;
            failedReason = d.failedReason;
            if(isSuccess){
                peer.setId(d.account);
            }
        } else if(InterModuleCommunication.CommandToWaiter.MANAGEMENT_CHANGE_PASSWORD_ACK.equals(cmd)) {
            isResponse = true;
            isSuccess = d.isSuccess;
            failedReason = d.failedReason;
        } else if(InterModuleCommunication.CommandToWaiter.MANAGEMENT_DISH_FINISHED.equals(cmd)) {
            for(INotificationObserver observer : observers){
                observer.newNotification(d.tableId, d.type);
            }
        } else if(InterModuleCommunication.CommandToWaiter.MANAGEMENT_SERVE_CUSTOMER.equals(cmd)) {
            for(INotificationObserver observer : observers){
                observer.newNotification(d.tableId, d.type);
            }
        }
    }
}
