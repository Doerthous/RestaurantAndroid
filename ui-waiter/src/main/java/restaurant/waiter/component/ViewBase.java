package restaurant.waiter.component;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import restaurant.waiter.tool.UITools;

/**
 * Created by Doerthous on 2017/12/20.
 */
/*
    结合Andoird的Handler使用
 */
public interface ViewBase {
    /*
        直接处理
     */
    void handleMessage(Message msg);
    /*
        间接处理（切换到ui线程）
     */
    void sendMessage(Message msg);
}
