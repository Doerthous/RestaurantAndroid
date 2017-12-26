package restaurant.waiter;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import restaurant.waiter.component.FragmentBase;
import restaurant.waiter.tool.UITools;

public class NotificationFragment extends FragmentBase {


    public NotificationFragment() {
        // Required empty public constructor
        layout = R.layout.fragment_notification;
    }

    /*

     */
    @Override
    protected void initServiceComponent() {
    }


    /*

     */
    private FragmentBase suspending;
    private FragmentBase solved;
    private Button btnSuspending;
    private Button btnSolved;
    @Override
    protected void initUIComponent() {
        suspending = new SuspendingFragment();
        suspending.setParent(this);
        solved = new SolvedFragment();
        solved.setParent(this);
        btnSuspending = (Button) findViewById(R.id.ntfctBtnSuspending);
        btnSuspending.setOnClickListener(this);
        btnSolved = (Button) findViewById(R.id.ntfctBtnSolved);
        btnSolved.setOnClickListener(this);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.ntfctFlChildren, suspending, "Ssp");
        transaction.add(R.id.ntfctFlChildren, solved, "Slv");
        transaction.commit();
        toSuspending();
    }
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case R.id.ntfctBtnSuspending: {
                toSuspending();
            } break;
            case R.id.ntfctBtnSolved:{
                toSolved();
            }break;
            case R.id.cbSspSolve: {
                updateSuspending(msg.getData().getInt("suspending count"));
                solved.handleMessage(msg);
            } break;
            case R.id.sspLvList: {
                updateSuspending(msg.getData().getInt("suspending count"));
            } break;
            case R.id.svlLvList: {
                updateSolved(msg.getData().getInt("solved count"));
            } break;
        }
    }


    private void toSuspending(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.hide(solved);
        transaction.hide(suspending);
        transaction.show(suspending);
        transaction.commit();
    }
    private void toSolved(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.hide(solved);
        transaction.hide(suspending);
        transaction.show(solved);
        transaction.commit();
    }

    private void updateSuspending(int count){
        String text = String.valueOf(count);
        if(count > 99){
            text = "99+";
        }
        btnSuspending.setText("未处理("+text+")");
    }
    private void updateSolved(int count){
        String text = String.valueOf(count);
        if(count > 99){
            text = "99+";
        }
        btnSolved.setText("已处理("+text+")");
    }
}
