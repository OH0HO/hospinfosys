package tw.com.hismax.test.hospinfosys;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.File;


public class Setting extends Activity {
    public static File file;
    TextView name, birth, chart ,editRegisterId; //姓名，生日，病歷號碼 ，Register Id，
    EditText value = null; //使用者的身分證
    Button login, login_home;
    int chart_no;
    Boolean shareboolean = false;
    Json1 json1;
    //***Ben ------------
    String _birth_date, _pt_name, _id_no, _result; //Json1抓出來的值
    int _chart_no; //Json1抓出來的值
    String _regId; //GCM的值
    String _fromMenu = "SELF";
    //-------------------
//    String b_uuid = "D5E9DBE2-D9F7-4564-A6C8-57A38C5FA6F0";
    private Handler mUI_Handler;


    private Runnable registerDeviceRun = new Runnable() {

        public void run() {
            json1 = new Json1(_id_no, _regId);
            Setting.this._result  = json1.getString();
            Setting.this._chart_no = json1.getchart_no();
            Setting.this._birth_date = json1.getbirth_date();
            Setting.this._pt_name = json1.getpt_name();
            //Ben ------ Move to Object
            Setting.this.patient.setIdNo(value.getText().toString());
            Setting.this.patient.setChartNo(json1.getchart_no());
            Setting.this.patient.setPtName(json1.getpt_name());
            Setting.this.patient.setBirthDay(json1.getbirth_date());
            //patient.saveToFile();
            Setting.this.patient.writeShareData();
            //---- Refresh ------
            mUI_Handler.post(runnableShow2Screen);
        }

    };

    //---Ben --------S
    PatientInfoObj patient;
    //---------------E

    private void findComponent() {
        name = (TextView) findViewById(R.id.textView15);
        birth = (TextView) findViewById(R.id.textView19);
        chart = (TextView) findViewById(R.id.textView20);

        editRegisterId = (TextView) findViewById(R.id.editRegisterId);

        value = (EditText) findViewById(R.id.id_no);
        login_home = (Button) findViewById(R.id.button_login);
        login = (Button) findViewById(R.id.button);
        login_home.setOnClickListener(new ClickButton());
        login.setOnClickListener(new ClickButton());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        //***Ben: 檢查 GCM 服務是否可用----
        if (checkPlayServices()) {
            Log.d("BEN", "GCM 服務不可用");
        }
        //***Ben: 檢查是否為來自 Menu.java
        Log.d("BEN", "1. fromMenu=" + _fromMenu);
        try {
            Intent it = this.getIntent();
            _fromMenu = it.getStringExtra("FROM");   //取不到時為 null
        } catch (Exception e) {
            _fromMenu = "";
            Log.d("BEN", "Self Start....");
        }
        Log.d("BEN", "2. fromMenu=" + _fromMenu);
        findComponent();

        patient = (PatientInfoObj)getApplicationContext();
        if (patient.readShareData()) {
            //有資料
            if (_fromMenu != null){
                _chart_no = patient.getChartNo();
                _pt_name = patient.getPtName();
                _birth_date = patient.getBirthDay();
                _id_no = patient.getIdNo();
                shareboolean = true;
                moveToComponent();
            } else {
                SendIntent();
                return;
            }
        }
        //沒有資料, show this screen
        GetClientRegistrationId getClientRegistrationId = new GetClientRegistrationId(this);
        getClientRegistrationId.openGCM();
        _regId = getClientRegistrationId.getRegistrationId();
        Log.i("regid = ", _regId);
        //Ben取得
        // APA91bEJOQznACdSjOLAE2oGaPiaBAq2GZ59MK6UWaDWZWI8H0SIlw3amuVQpnuRgqoXugtklgbVlDCFNOEBunmUxqv6SLC8VGkgBDZOBO4f80EUc205UpH8Qhyl56snEdJaKN_443Ge
        editRegisterId.setText(_regId);

        mUI_Handler = new Handler();
    }

    public void SendIntent() {
        Intent it = new Intent();
        it.setClass(Setting.this, Mainpage.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("Boolean",shareboolean);
        it.putExtras(bundle);
        startActivity(it);
        Setting.this.finish();
    }

    class ClickButton implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button: {
                    if (value.getText().toString().equals("")) {
                        Toast.makeText(Setting.this, "請輸入您的身分證字號", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("Ben", "ID_NO="+ value.getEditableText().toString());
                        if (view == login) {
                            _id_no      = value.getEditableText().toString();

                            //***Ben : call Json1 註冊病患手機id -----------
                            Thread mThread = new Thread(registerDeviceRun);
                            mThread.start();

                        }
                    }
                    break;
                }
                case R.id.button_login: {
                    if (view == login_home) {

                        SendIntent();

                    }
                }

            }
        }
    }
    //***Ben :
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //GooglePlayServicesUtil.getErrorDialog(resultCode,
                //        this,PLAY_SERVICES_RESOLUTION_REQUEST).show();
                Log.i("BEN", "This device is  supported.");
            } else {
                Log.i("BEN", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public Runnable runnableShow2Screen = new Runnable() {
        public void run() {
            moveToComponent();
        }
    };

    private void moveToComponent(){
        name.setText(_pt_name);
        birth.setText(_birth_date);
        chart.setText(String.valueOf( _chart_no));
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

        AlertDialog.Builder ad = new AlertDialog.Builder(Setting.this); //創建訊息方塊

        ad.setTitle("離開");

        ad.setMessage("確定要離開?");

        ad.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則退出應用程式

            public void onClick(DialogInterface dialog, int i) {

                Setting.this.finish();//關閉activity

            }

        });

        ad.setNegativeButton("否", new DialogInterface.OnClickListener() { //按"否",則不執行任何操作

            public void onClick(DialogInterface dialog, int i) {

            }

        });

        ad.show();//顯示訊息視窗

    }

}





