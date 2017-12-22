package restaurant.client;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends FragmentBase {

    public MainFragment() {
        // Required empty public constructor
        this.layout = R.layout.fragment_main;
    }

    @Override
    void initUIComponent() {
        findViewById(R.id.mainBtnTest).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        UITools.showNotification(getActivity().getApplicationContext(),
                "mmp","mmp","mmp");
    }
}
