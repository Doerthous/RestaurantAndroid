package restaurant.waiter;


import android.os.Bundle;
import android.os.Message;
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

import restaurant.waiter.component.FragmentBase;

public class SolvedFragment extends FragmentBase {

    public SolvedFragment() {
        // Required empty public constructor
        layout = R.layout.fragment_solved;
        itemAdapter = new ItemAdapter();
    }

    /*

     */
    @Override
    protected void initServiceComponent() {

    }


    /*

     */
    private ListView lvList;
    private ItemAdapter itemAdapter;
    @Override
    protected void initUIComponent() {
        lvList = (ListView) findViewById(R.id.svlLvList);
        lvList.setAdapter(itemAdapter);
    }
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case R.id.cbSspSolve: {
                itemAdapter.add(msg.getData().getString("content"),
                        msg.getData().getString("type"),
                        msg.getData().getString("came time"),
                        new SimpleDateFormat("HH:mm:ss").format(new Date()));
                if(isInForeground){
                    itemAdapter.notifyDataSetChanged();
                }
                msg.what = R.id.svlLvList;
                msg.getData().putInt("solved count", itemAdapter.getCount());
                super.handleMessage(msg);
            } break;
        }
    }

    public class Item {
        public int index;
        public String content;
        public String type;
        public String cameTime;
        public String finishTime;

        public Item(String content, String type, String cameTime, String finishTime) {
            this.content = content;
            this.type = type;
            this.cameTime = cameTime;
            this.finishTime = finishTime;
        }

        public Item(int index, String content, String type, String cameTime, String finishTime) {
            this.index = index;
            this.content = content;
            this.type = type;
            this.cameTime = cameTime;
            this.finishTime = finishTime;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "index=" + index +
                    ", content='" + content + '\'' +
                    ", type='" + type + '\'' +
                    ", cameTime='" + cameTime + '\'' +
                    ", finishTime='" + finishTime + '\'' +
                    '}';
        }
    }
    class ItemAdapter extends BaseAdapter {

        List<Item> items;

        // 1
        public ItemAdapter() {
            this.items = new ArrayList<>();
        }

        // 2
        public void add(String content, String type, String cameTime, String finishTime){
            items.add(new Item(content, type, cameTime, finishTime));
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
            View v = inflater.inflate(R.layout.li_notification_solved,
                    viewGroup, false);
            TextView tvC = (TextView) v.findViewById(R.id.tvSvlLiContent);
            TextView tvN = (TextView) v.findViewById(R.id.tvSvlLiType);
            TextView tvCa = (TextView) v.findViewById(R.id.tvSvlLiCameTime);
            TextView tvF = (TextView) v.findViewById(R.id.tvSvlLiFinishTime);
            Item item = items.get(i);
            tvC.setText(item.content);
            tvN.setText(item.type);
            tvCa.setText(item.cameTime);
            tvF.setText(item.finishTime);
            return v;
        }
    }
}
