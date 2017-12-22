package restaurant.client;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import restaurant.client.component.FragmentBase;
import restaurant.client.component.ViewBase;
import restaurant.client.tool.UITools;
import restaurant.communication.core.ICommandObserver;
import restaurant.communication.core.IData;
import restaurant.service.core.IWaiterService;
import restaurant.service.android.ServiceFactory;

public class MainActivity extends AppCompatActivity implements ViewBase, ICommandObserver {
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
        }).start();
        dialog.show();
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
            } break;
        }
    }

    @Override
    public void update(IData iData) {

    }

    IWaiterService getService(){
        return service;
    }
}
