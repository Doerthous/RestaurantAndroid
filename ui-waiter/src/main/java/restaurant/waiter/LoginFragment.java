package restaurant.waiter;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import restaurant.waiter.component.FragmentBase;
import restaurant.waiter.tool.UITools;

import static java.lang.Thread.sleep;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends FragmentBase {

    public LoginFragment() {
        // Required empty public constructor
        this.layout = R.layout.fragment_login;
    }

    /*

     */
    private MainActivity ma;
    @Override
    protected void initServiceComponent() {
        ma = (MainActivity)getActivity();
    }

    /*

     */
    private EditText account;
    private EditText password;
    @Override
    protected void initUIComponent() {
        findViewById(R.id.loginBtn).setOnClickListener(this);
        account = (EditText) findViewById(R.id.loginEtAccount);
        password = (EditText) findViewById(R.id.loginEtPassword);
    }
    private Boolean isLogin;
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case R.id.loginBtn: {
                final ProgressDialog dialog = new ProgressDialog(ma);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("登陆中...");
                new Thread(()->{
                    isLogin = ma.getService().login(account.getText().toString(),
                            password.getText().toString());
                    dialog.dismiss();
                    msg.what = -1;
                    sendMessage(msg);
                }).start();
                dialog.show();
            } break;
            case -1:{
                if(isLogin){
                    msg.what = R.id.loginBtn;
                    super.handleMessage(msg);
                } else {
                    UITools.showToast(getActivity(),
                            ma.getService().getLoginFailedReason(),
                            Toast.LENGTH_LONG);
                }
            }
        }
    }
}
