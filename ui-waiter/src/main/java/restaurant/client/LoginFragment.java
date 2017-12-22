package restaurant.client;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends FragmentBase {
    private EditText account;
    private EditText password;

    public LoginFragment() {
        // Required empty public constructor
        this.layout = R.layout.fragment_login;
    }


    @Override
    void initUIComponent() {
        findViewById(R.id.loginBtn).setOnClickListener(this);
        account = (EditText) findViewById(R.id.loginEtAccount);
        password = (EditText) findViewById(R.id.loginEtPassword);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginBtn: {
                if("123".equals(password.getText().toString())) {
                    super.onClick(view);
                } else {
                    UITools.showToast(getActivity().getApplicationContext(),
                            "密码错误", Toast.LENGTH_LONG);
                }
            } break;
        }
    }
}
