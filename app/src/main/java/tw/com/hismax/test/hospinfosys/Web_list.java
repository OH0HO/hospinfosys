package tw.com.hismax.test.hospinfosys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

/**
 * Created by OH HO on 2016/6/27.
 */
public class Web_list extends AppCompatActivity {
    Button login_home;
    int chart_no;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_list);

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.requestFocus();
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.loadUrl("http://61.219.152.220/geteducation/");

        login_home = (Button) findViewById(R.id.button_login);
        login_home.setOnClickListener(new ClickButton());
        }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
    class ClickButton implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Log.d("BEN", "Click....");
            Intent it = new Intent();
            it.setClass(Web_list.this, Mainpage.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("Boolean",true);
            it.putExtras(bundle);
            startActivity(it);
            Web_list.this.finish();
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
        AlertDialog.Builder ad = new AlertDialog.Builder(Web_list.this); //創建訊息方塊
        ad.setTitle("離開");
        ad.setMessage("確定要離開?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則退出應用程式
            public void onClick(DialogInterface dialog, int i) {
                Web_list.this.finish();//關閉activity
            }
        });

        ad.setNegativeButton("否", new DialogInterface.OnClickListener() { //按"否",則不執行任何操作
            public void onClick(DialogInterface dialog, int i) {
            }
        });

        ad.show();//顯示訊息視窗

    }
}
