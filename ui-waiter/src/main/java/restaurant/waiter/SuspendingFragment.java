package restaurant.waiter;


import android.os.Bundle;
import android.os.Message;
import android.text.style.TtsSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import restaurant.service.core.IWaiterService;
import restaurant.waiter.component.FragmentBase;
import restaurant.waiter.component.MessageBuilder;
import restaurant.waiter.tool.UITools;

import static java.lang.Thread.sleep;

public class SuspendingFragment extends FragmentBase implements IWaiterService.INotificationObserver {

    public SuspendingFragment() {
        // Required empty public constructor
        layout = R.layout.fragment_suspending;
        itemAdapter = new ItemAdapter();
    }

    private MainActivity ma;
    @Override
    protected void initServiceComponent() {
        ma = (MainActivity)getActivity();
        ma.setNotificationObserver(this);
    }

    /*

     */
    private ListView lvList;
    private ItemAdapter itemAdapter;
    @Override
    protected void initUIComponent() {
        lvList = (ListView) findViewById(R.id.sspLvList);
        lvList.setAdapter(itemAdapter);
    }
    @Override
    public void handleMessage(Message message) {
        switch (message.what){
            case R.id.cbSspSolve: {
                itemAdapter.remove(message.getData().getInt("index"));
                if(isInForeground) {
                    itemAdapter.notifyDataSetChanged();
                }
                message.getData().putInt("suspending count", itemAdapter.getCount());
                super.handleMessage(message);
            } break;
            case R.id.sspLvList:{
                if(isInForeground) {
                    itemAdapter.notifyDataSetChanged();
                }
                String tableId = message.getData().getString("table id");
                String dishName = message.getData().getString("dish name");
                if(dishName == null){
                    UITools.showNotification(getActivity().getApplicationContext(),
                            "服务通知","服务通知", tableId);
                } else {
                    UITools.showNotification(getActivity().getApplicationContext(),
                            "传菜通知","传菜通知",tableId+": "+dishName);
                }
                message.getData().putInt("suspending count", itemAdapter.getCount());
                super.handleMessage(message);
            } break;
        }
    }
    @Override
    public void dishFinish(String dishName, String tableId) {
        itemAdapter.add(tableId+"（"+dishName+"）", "传菜通知",
                new SimpleDateFormat("HH:mm:ss").format(new Date()));
        Message message = new Message();
        message.what = R.id.sspLvList;
        Bundle bundle = new Bundle();
        bundle.putString("dish name", dishName);
        bundle.putString("table id", tableId);
        message.setData(bundle);
        sendMessage(message);
    }
    @Override
    public void customerCall(String tableId) {
        itemAdapter.add(tableId, "服务通知",
                new SimpleDateFormat("HH:mm:ss").format(new Date()));
        Message message = new Message();
        message.what = R.id.sspLvList;
        Bundle bundle = new Bundle();
        bundle.putString("table id", tableId);
        message.setData(bundle);
        sendMessage(message);
    }
    private void updateParent(){

    }
    public class Item {
        public int index;
        public String content;
        public String type;
        public String time;

        public Item(String content, String type, String time) {
            this.content = content;
            this.type = type;
            this.time = time;
        }

        public Item(int index, String content, String type, String time) {
            this.index = index;
            this.content = content;
            this.type = type;
            this.time = time;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "content='" + content + '\'' +
                    ", type='" + type + '\'' +
                    ", time='" + time + '\'' +
                    '}';
        }
    }
    class ItemAdapter extends BaseAdapter{


        private List<Item> items;

        // 1
        public ItemAdapter() {
            this.items = new ArrayList<>();
        }

        // 2
        public void add(String content, String type, String time){
            items.add(new Item(content, type, time));
        }

        public void remove(int index){
            items.remove(index);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            // view, viewGroup 干嘛的？
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View v = inflater.inflate(R.layout.li_notification_suspending,
                    viewGroup, false);
            TextView tvC = (TextView) v.findViewById(R.id.tvSspLiContent);
            TextView tvN = (TextView) v.findViewById(R.id.tvSspLiType);
            TextView tvCa = (TextView) v.findViewById(R.id.tvSspLiCameTime);
            CheckBox cb = (CheckBox) v.findViewById(R.id.cbSspSolve);
            cb.setOnCheckedChangeListener((compoundButton, b) -> {
                if(b){
                    handleMessage(MessageBuilder.getInstance().what(compoundButton.getId())
                            .putInt("index", i)
                            .putString("content", tvC.getText().toString())
                            .putString("type", tvN.getText().toString())
                            .putString("came time", tvCa.getText().toString())
                            .build());
                }
            });
            Item item = items.get(i);
            tvC.setText(item.content);
            tvN.setText(item.type);
            tvCa.setText(item.time);
            return v;
        }
    }
}

