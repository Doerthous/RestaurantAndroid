package restaurant.waiter.component;

import android.os.Bundle;
import android.os.Message;

/**
 * Created by Doerthous on 2017/12/24.
 */

public class MessageBuilder {
    private Message message;
    private Bundle bundle;

    private MessageBuilder(){
        message = new Message();
        bundle = new Bundle();
    };
    public MessageBuilder what(int what){
        message.what = what;
        return this;
    }
    public MessageBuilder putInt(String key, int value){
        bundle.putInt(key, value);
        return this;
    }
    public MessageBuilder putString(String key, String value){
        bundle.putString(key, value);
        return this;
    }
    public Message build(){
        message.setData(bundle);
        return message;
    }

    public static MessageBuilder getInstance(){
        return new MessageBuilder();
    }
}
