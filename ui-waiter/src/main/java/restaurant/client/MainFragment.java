package restaurant.client;


import android.support.v4.app.Fragment;
import android.view.View;

import restaurant.client.component.FragmentBase;
import restaurant.client.tool.UITools;
import restaurant.service.core.IWaiterService;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends FragmentBase implements IWaiterService.INotificationObserver {

    public MainFragment() {
        // Required empty public constructor
        this.layout = R.layout.fragment_main;
    }

    /*

     */
    private MainActivity ma;
    @Override
    protected void initServiceComponent() {
        ma = (MainActivity)getActivity();
        ma.getService().addNotificationObserver(this);
    }


    /*

     */
    @Override
    protected void initUIComponent() {
        findViewById(R.id.mainBtnTest).setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
    }
    @Override
    public void dishFinish(String s, String s1) {
        UITools.showNotification(getActivity().getApplicationContext(),
                "传菜通知","传菜通知",s1+": "+s);
    }
    @Override
    public void customerCall(String s) {
        UITools.showNotification(getActivity().getApplicationContext(),
                "服务通知","服务通知",s);
    }
}
