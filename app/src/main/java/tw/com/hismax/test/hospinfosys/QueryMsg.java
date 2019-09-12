package tw.com.hismax.test.hospinfosys;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class QueryMsg extends AppCompatActivity {
    Button login_home;
    PatientInfoObj patient;
    ListView lstMsg;
    LstAdapter adapter;
    List<MessageItem> msgList;
    int chart_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_msg);

        //***Ben --- 取出 patient 資料-------s
        patient = (PatientInfoObj)getApplicationContext();
        //demo------
        //patient.addMsgList(new MessageItem("2016/07/17 08:30:00","這是測試訊息這是測試訊息這是測試訊息這是測試訊息這是測試訊息"));
        //patient.addMsgList(new MessageItem("2016/07/17 08:30:00","這是測試訊息這是測試訊息這是測試訊息這是測試訊息這是測試訊息"));
        //patient.addMsgList(new MessageItem("2016/07/17 08:30:00","這是測試訊息這是測試訊息這是測試訊息這是測試訊息這是測試訊息"));
        //patient.addMsgList(new MessageItem("2016/07/17 08:30:00","這是測試訊息這是測試訊息這是測試訊息這是測試訊息這是測試訊息"));
        //patient.addMsgList(new MessageItem("2016/07/17 08:30:00","這是測試訊息這是測試訊息這是測試訊息這是測試訊息這是測試訊息"));
        //patient.addMsgList(new MessageItem("2016/07/17 08:30:00","這是測試訊息這是測試訊息這是測試訊息這是測試訊息這是測試訊息"));
        //patient.addMsgList(new MessageItem("2016/07/17 08:30:00","這是測試訊息這是測試訊息這是測試訊息這是測試訊息這是測試訊息"));
        //patient.addMsgList(new MessageItem("2016/07/17 08:30:00","這是測試訊息這是測試訊息這是測試訊息這是測試訊息這是測試訊息"));
        //patient.addMsgList(new MessageItem("2016/07/17 08:30:00","這是測試訊息這是測試訊息這是測試訊息這是測試訊息這是測試訊息"));
        //patient.addMsgList(new MessageItem("2016/07/17 08:30:00","這是測試訊息這是測試訊息這是測試訊息這是測試訊息這是測試訊息"));
        //----------
        msgList = patient.getMsgList();

        //_pt_name = patient.getPtName();
        //_chart_no =  patient.getChartNo();

        //***Ben ----取出元件 ---------------e
        login_home = (Button) findViewById(R.id.button_login);
        login_home.setOnClickListener(new ClickButton());
        lstMsg = (ListView) findViewById(R.id.listView_Msg);


        adapter = new LstAdapter(QueryMsg.this, msgList);
        lstMsg.setAdapter(adapter);
    }
    class ClickButton implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent it = new Intent();
            it.setClass(QueryMsg.this, Mainpage.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("Boolean",true);
            it.putExtras(bundle);
            startActivity(it);
            QueryMsg.this.finish();
        }
    }
    public class LstAdapter extends BaseAdapter{

        LayoutInflater _lstInflater;
        List<MessageItem> _lst;
        public LstAdapter(Context context, List<MessageItem> lst){
            this._lstInflater = LayoutInflater.from(context);
            this._lst = lst;
        }
        @Override
        public int getCount() {
            return _lst.size();
        }

        @Override
        public Object getItem(int i) {
            return _lst.get(i);
        }

        @Override
        public long getItemId(int i) {
            return _lst.indexOf(getItem(i));
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder holder = null;
            if (view == null){
                view = _lstInflater.inflate(R.layout.activity_msg_item, null);
                holder = new ViewHolder(
                        (TextView) view.findViewById(R.id.textView_Time),
                        (TextView) view.findViewById(R.id.textView_Message)
                );
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            MessageItem item = (MessageItem)getItem(i);
            holder.txtTime.setText(item.getDateTime());
            holder.txtMessage.setText(item.getMsgContext());

            return view;
        }

        private class ViewHolder {
            TextView txtTime, txtMessage;
            public ViewHolder(TextView time, TextView msg) {
                this.txtTime = time;
                this.txtMessage = msg;
            }

        }
    }

    // 確認是否要離開
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {   //確定按下退出鍵
            ConfirmExit(); //呼叫ConfirmExit()函數
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void ConfirmExit() {
        AlertDialog.Builder ad = new AlertDialog.Builder(QueryMsg.this); //創建訊息方塊
        ad.setTitle("離開");
        ad.setMessage("確定要離開?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則退出應用程式
            public void onClick(DialogInterface dialog, int i) {
                QueryMsg.this.finish();//關閉activity
            }
        });

        ad.setNegativeButton("否", new DialogInterface.OnClickListener() { //按"否",則不執行任何操作
            public void onClick(DialogInterface dialog, int i) {
            }
        });

        ad.show();//顯示訊息視窗

    }
}
