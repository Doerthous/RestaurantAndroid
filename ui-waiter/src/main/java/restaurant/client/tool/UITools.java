package restaurant.client.tool;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import restaurant.client.R;

/**
 * Created by Doerthous on 2017/12/20.
 */

public class UITools {
    public static void showFragment(FragmentManager fragmentManager, Fragment fragment,
                                    int containerId, int enter, int exit){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(enter, exit);
        transaction.replace(containerId, fragment);
        transaction.commit();
    }
    public static void showFragment(FragmentManager fragmentManager, Fragment fragment,
                                    int containerId){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.commit();
    }
    public static void showNotification(Context context, String ticker,
                                        String title, String content){
        new Thread(()->{
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle(title);
            builder.setContentText(content);
            builder.setWhen(System.currentTimeMillis());
            builder.setTicker(ticker);
            builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
            builder.setAutoCancel(true);
            //builder.setContentIntent(pendingIntent);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                Notification notification = builder.build();
                notificationManager.notify(1, notification);
            }
        }).start();
    }
    public static void showToast(Context context, String message, int time){
        Toast.makeText(context, message, time).show();
    }

    public class Handler extends android.os.Handler {

    }
}
