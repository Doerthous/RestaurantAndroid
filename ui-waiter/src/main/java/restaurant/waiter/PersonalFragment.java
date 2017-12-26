package restaurant.waiter;


import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import restaurant.waiter.component.FragmentBase;
import restaurant.waiter.tool.UITools;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends FragmentBase {

    public PersonalFragment() {
        // Required empty public constructor
        this.layout = R.layout.fragment_personal;
    }


    /*

     */
    @Override
    protected void initServiceComponent() {

    }


    /*

     */
    private FragmentBase main;
    private FragmentBase password;
    private Button raturn;
    private TextView title;
    @Override
    protected void initUIComponent() {
        main = new PersonalMainFragment();
        main.setParent(this);
        password = new PasswordFragment();
        password.setParent(this);
        raturn = (Button) findViewById(R.id.psnlBtnReturn);
        raturn.setOnClickListener(this);
        title = (TextView) findViewById(R.id.psnlTvTitle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.psnlFlChildren, main, "PsnlMain");
        transaction.add(R.id.psnlFlChildren, password, "Psw");
        transaction.commit();
        toMain();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case R.id.psnlBtnReturn: {
                toMain();
            } break;
            case R.id.psnlBtnPswChange: {
                toPasswordChange();
            } break;
            case R.id.psnlBtnLogout: {
                new AlertDialog.Builder(getActivity()).setMessage("确认注销吗？")
                        .setPositiveButton("确定", (dialog, which) -> super.handleMessage(msg))
                        .setNegativeButton("返回", (dialog, which) -> {
                            // 点击“返回”后的操作,这里不设置没有任何操作
                        }).show();
            } break;
            case R.id.pswBtnOk: {
                msg.what = R.id.psnlBtnLogout;
                super.handleMessage(msg);
            }
        }
    }

    private void toMain(){
        raturn.setVisibility(View.INVISIBLE);
        title.setText("个人中心");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.hide(password);
        transaction.hide(main);
        transaction.show(main);
        transaction.commit();
    }
    private void toPasswordChange(){
        raturn.setVisibility(View.VISIBLE);
        title.setText("修改密码");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.hide(password);
        transaction.hide(main);
        transaction.show(password);
        transaction.commit();
    }
    private void toIssueReport(){

    }
}
