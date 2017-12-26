package restaurant.waiter;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import restaurant.waiter.component.FragmentBase;
import restaurant.waiter.component.MessageBuilder;
import restaurant.waiter.tool.UITools;


/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordFragment extends FragmentBase {


    public PasswordFragment() {
        // Required empty public constructor
        layout = R.layout.fragment_password;
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
    private EditText etOldPassword;
    private EditText etNewPassword;
    private Boolean isSuccess;
    @Override
    protected void initUIComponent() {
        findViewById(R.id.pswBtnOk).setOnClickListener(this);
        etOldPassword = (EditText) findViewById(R.id.pswEtOldPsw);
        etNewPassword = (EditText) findViewById(R.id.pswEtNewPsw);
    }
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case R.id.pswBtnOk: {
                final ProgressDialog dialog = new ProgressDialog(ma);
                dialog.setCanceledOnTouchOutside(false);
                new Thread(()->{
                    isSuccess = ma.getService().changePassword(ma.getService().getAccount(),
                            etOldPassword.getText().toString(),
                            etNewPassword.getText().toString());
                    dialog.dismiss();
                    sendMessage(MessageBuilder.getInstance().what(-1).build());
                }).start();
                dialog.show();
            } break;
            case -1:{
                if(isSuccess){
                    UITools.showToast(getActivity(),
                            "修改成功，请重新登陆",
                            Toast.LENGTH_LONG);
                    msg.what = R.id.pswBtnOk;
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
