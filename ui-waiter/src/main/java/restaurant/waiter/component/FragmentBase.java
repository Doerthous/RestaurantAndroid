package restaurant.waiter.component;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Doerthous on 2017/12/20.
 */
public abstract class FragmentBase extends Fragment implements ViewBase, View.OnClickListener {
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
    protected boolean isInForeground;
    public void setParent(ViewBase parent){
        this.parent = parent;
    }

    protected abstract void initServiceComponent();
    protected abstract void initUIComponent();

    @Override
    public void onResume() {
        super.onResume();
        isInForeground = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isInForeground = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(layout, container, false);
        initServiceComponent();
        initUIComponent();
        return view;
    }

    /*
        将控件事件转交由handler处理
     */
    @Override
    public void onClick(View view) {
        handleMessage(MessageBuilder.getInstance().what(view.getId()).build());
    }

    public View findViewById(int id){
        return view.findViewById(id);
    }

    private Handler handler = new Handler(msg->{handleMessage(msg); return false;});
    @Override
    public void handleMessage(Message msg) {
        if(parent != null){
            parent.handleMessage(msg);
        }
    }
    @Override
    public void sendMessage(Message msg) {
        handler.sendMessage(msg);
    }
}
