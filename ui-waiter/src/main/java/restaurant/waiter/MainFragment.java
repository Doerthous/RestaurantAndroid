package restaurant.waiter;


import android.app.FragmentTransaction;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;

import restaurant.waiter.component.FragmentBase;
import restaurant.waiter.tool.UITools;
import restaurant.service.core.IWaiterService;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends FragmentBase {


    public MainFragment() {
        // Required empty public constructor
        this.layout = R.layout.fragment_main;
    }

    /*

     */
    @Override
    protected void initServiceComponent() {

    }


    /*

     */
    FragmentBase notification;
    FragmentBase personal;
    @Override
    protected void initUIComponent() {
        findViewById(R.id.mainBtnNtfct).setOnClickListener(this);
        findViewById(R.id.mainBtnPsnl).setOnClickListener(this);
        notification = new NotificationFragment();
        notification.setParent(this);
        personal = new PersonalFragment();
        personal.setParent(this);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.mainFlChildren, notification, "Ntfct");
        transaction.add(R.id.mainFlChildren, personal, "Psnl");
        transaction.commit();
        toNotification();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case R.id.mainBtnNtfct:{
                toNotification();
            } break;
            case R.id.mainBtnPsnl: {
                toPersonal();
            } break;
            case R.id.psnlBtnLogout: {
                super.handleMessage(msg);
            }break;
        }
    }

    private void toNotification(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.hide(notification);
        transaction.hide(personal);
        transaction.show(notification);
        transaction.commit();
    }
    private void toPersonal(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.hide(notification);
        transaction.hide(personal);
        transaction.show(personal);
        transaction.commit();
    }
}
