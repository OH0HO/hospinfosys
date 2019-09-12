package tw.com.hismax.test.hospinfosys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by OH HO on 2016/7/2.
 */
public class Menu extends AppCompatActivity {

    String doctor_no ,pt_name ,doctor_name, clinic_ps;
    int view_no, chart_no;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        //Ben
        //Bundle bundle = this.getIntent().getExtras();
        //chart_no = bundle.getString("chart_no");

        Button btn1 = (Button) findViewById(R.id.button2);
        Button btn2 = (Button) findViewById(R.id.button3);
        Button btn3 = (Button) findViewById(R.id.button4);
        Button btn4 = (Button) findViewById(R.id.button5);
        Button btn5 = (Button) findViewById(R.id.button6);
        Button btn7 = (Button) findViewById(R.id.button7);


        btn1.setOnClickListener(new ClickButton());
        btn2.setOnClickListener(new ClickButton());
        btn3.setOnClickListener(new ClickButton());
        btn4.setOnClickListener(new ClickButton());
        btn5.setOnClickListener(new ClickButton());
        //Ben
        btn7.setOnClickListener(new ClickButton());
    }

    class ClickButton implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.button2: {
                    Toast.makeText(Menu.this, "主畫面", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent();
                    it.setClass(Menu.this, Mainpage.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("Boolean",true);
                    it.putExtras(bundle);
                    startActivity(it);
                    Log.e("ohho", "menu button main on " + doctor_name + clinic_ps + doctor_no + view_no + pt_name);
                    Menu.this.finish();
                    break;
                }
                case R.id.button3: {
                    Toast.makeText(Menu.this, "衛教清單", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent();
                    it.setClass(Menu.this, Web_list.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("Boolean",true);
                    it.putExtras(bundle);
                    startActivity(it);
                    Menu.this.finish();
                    break;

                }
                case R.id.button4: {
                    Toast.makeText(Menu.this, "就醫記錄", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent();
                    it.setClass(Menu.this, Web_record.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("Boolean",true);
                    it.putExtras(bundle);
                    startActivity(it);
                    Menu.this.finish();
                    break;

                }
                case R.id.button5: {
                    Toast.makeText(Menu.this, "訊息查詢", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent();
                    it.setClass(Menu.this, QueryMsg.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("Boolean",true);
                    it.putExtras(bundle);
                    startActivity(it);
                    Menu.this.finish();
                    break;

                }
                case R.id.button6: {
                    Toast.makeText(Menu.this, "基本設定", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent();
                    it.setClass(Menu.this, Setting.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("Boolean",true);
                    it.putExtras(bundle);
                    it.putExtra("FROM", "MENU");
                    startActivity(it);
                    Menu.this.finish();
                    break;

                }
                case R.id.button7: {
                    Toast.makeText(Menu.this, "醫院介紹", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent();
                    it.setClass(Menu.this, HospitalInfo.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("Boolean",true);
                    it.putExtras(bundle);
                    startActivity(it);
                    Menu.this.finish();
                    break;

                }

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

        AlertDialog.Builder ad = new AlertDialog.Builder(Menu.this); //創建訊息方塊

        ad.setTitle("離開");

        ad.setMessage("確定要離開?");

        ad.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則退出應用程式

            public void onClick(DialogInterface dialog, int i) {

                Menu.this.finish();//關閉activity

            }

        });

        ad.setNegativeButton("否", new DialogInterface.OnClickListener() { //按"否",則不執行任何操作

            public void onClick(DialogInterface dialog, int i) {

            }

        });

        ad.show();//顯示訊息視窗

    }
}
