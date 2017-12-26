package restaurant.waiter;


import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;


import java.util.HashMap;

import restaurant.waiter.component.FragmentBase;
import restaurant.waiter.component.MessageBuilder;
import restaurant.waiter.component.ViewBase;
import restaurant.waiter.tool.UITools;
import restaurant.communication.core.ICommandObserver;
import restaurant.communication.core.IData;
import restaurant.service.core.IWaiterService;
import restaurant.service.android.ServiceFactory;

public class MainActivity extends AppCompatActivity
        implements ViewBase, IWaiterService.INotificationObserver,
                        IWaiterService.IAccountObserver{
    private FragmentBase login;
    private FragmentBase main;
    private IWaiterService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initServiceComponent();
        initFragment();
    }

    private void initServiceComponent(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("初始化中...");
        new Thread(()->{
            service = ServiceFactory.getWaiterService(
                    (WifiManager) getApplicationContext()
                            .getSystemService(Context.WIFI_SERVICE));
            dialog.dismiss();
            service.addNotificationObserver(this);
            service.addAccountObserver(this);
        }).start();
        dialog.show();
    }


    private void initFragment(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        login = new LoginFragment();
        login.setParent(this);
        main = new MainFragment();
        main.setParent(this);
        transaction.add(R.id.activityFlChildren, login, "Lgi");
        transaction.add(R.id.activityFlChildren, main, "Mi");
        transaction.commit();
        toLogin();
    }

    IWaiterService getService(){
        return service;
    }

    private void logout() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.remove(login);
        transaction.remove(main);
        login = new LoginFragment();
        login.setParent(this);
        main = new MainFragment();
        main.setParent(this);
        transaction.add(R.id.activityFlChildren, login, "Lgi");
        transaction.add(R.id.activityFlChildren, main, "Mi");
        transaction.commit();
        toLogin();
    }
    private void toLogin(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.hide(login);
        transaction.hide(main);
        transaction.show(login);
        transaction.commit();
    }
    private void toMain(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.hide(login);
        transaction.hide(main);
        transaction.show(main);
        transaction.commit();
    }


    // 返回后不退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Handler handler = new Handler(msg->{handleMessage(msg); return false;});
    @Override
    public void sendMessage(Message msg) {
        handler.sendMessage(msg);
    }
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.loginBtn: {
                toMain();
            }
            break;
            case R.id.psnlBtnLogout: {
                logout();
                String toast = msg.getData().getString("toast");
                if(toast != null) {
                    UITools.showToast(this, toast, Toast.LENGTH_LONG);
                }
            } break;
        }
    }


    private IWaiterService.INotificationObserver observer;
    public void setNotificationObserver(IWaiterService.INotificationObserver observer){
        this.observer = observer;
    }
    @Override
    public void dishFinish(String s, String s1) {
        observer.dishFinish(s, s1);
    }
    @Override
    public void customerCall(String s) {
        observer.customerCall(s);
    }
    @Override
    public void forceGoDown() {
        sendMessage(MessageBuilder.getInstance()
                .what(R.id.psnlBtnLogout)
                .putString("toast","你账号已在别处登陆")
                .build());
    }
}
