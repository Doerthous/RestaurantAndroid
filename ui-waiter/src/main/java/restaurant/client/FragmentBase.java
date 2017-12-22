package restaurant.client;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Doerthous on 2017/12/20.
 */

public abstract class FragmentBase extends Fragment implements View.OnClickListener {
    /*
        R.layout.xxx
     */
    protected int layout; //
    /*
        这个fragment本身的view
     */
    protected View view;
    /*
        父组件
     */
    private ViewBase parent;
    public void setParent(ViewBase parent){
        this.parent = parent;
    }

    abstract void initUIComponent();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(layout, container, false);
        initUIComponent();
        return view;
    }

    @Override
    public void onClick(View view) {
        if(parent != null){
            parent.onClick(view);
        }
    }

    public View findViewById(int id){
        return view.findViewById(id);
    }

}
