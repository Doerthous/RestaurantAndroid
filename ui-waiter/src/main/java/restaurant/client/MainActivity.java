package restaurant.client;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import restaurant.communication.android.IPTools;
import restaurant.communication.android.PeerFactory;
import restaurant.communication.android.SocketWrapper;
import restaurant.communication.core.ICommandObserver;
import restaurant.communication.core.IData;
import restaurant.communication.core.IPeer;

public class MainActivity extends AppCompatActivity implements ViewBase, ICommandObserver {
    private FragmentBase login;
    private FragmentBase main;
    private IPeer peer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initFragment();
    }

    private void initFragment(){
        login = new LoginFragment();
        login.setParent(this);
        main = new MainFragment();
        main.setParent(this);
        UITools.showFragment(getFragmentManager(), login, R.id.activityFlFrame);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginBtn:{
                UITools.showFragment(getFragmentManager(), main, R.id.activityFlFrame);
                WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                new Thread(()->{
                    peer = PeerFactory.getPeer("waiter1", wm);
                    peer.addCommandObserver(this, "MTWDF");
                    peer.start();
                }).start();
            } break;
        }
    }

    @Override
    public void update(IData iData) {
        switch (iData.getCommand()){
            case "MTWDF":{
                UITools.showNotification(getApplicationContext(),
                        "mmp","mmp","mmp");
            }
        }
    }
}
