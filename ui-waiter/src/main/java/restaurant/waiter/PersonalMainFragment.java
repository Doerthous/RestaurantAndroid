package restaurant.waiter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import restaurant.waiter.component.FragmentBase;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalMainFragment extends FragmentBase {

    public PersonalMainFragment() {
        // Required empty public constructor
        layout = R.layout.fragment_personal_main;
    }


    /*

     */
    @Override
    protected void initServiceComponent() {

    }


    /*

     */
    @Override
    protected void initUIComponent() {
        findViewById(R.id.psnlBtnPswChange).setOnClickListener(this);
        findViewById(R.id.psnlBtnLogout).setOnClickListener(this);
        findViewById(R.id.psnlBtnIssueReport).setOnClickListener(this);
    }
}
