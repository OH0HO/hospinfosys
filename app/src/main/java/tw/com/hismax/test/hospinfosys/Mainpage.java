package tw.com.hismax.test.hospinfosys;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.view.KeyEvent;
import android.widget.Toast;

import com.THLight.USBeacon.App.Lib.BatteryPowerData;
import com.THLight.USBeacon.App.Lib.USBeaconConnection;
import com.THLight.USBeacon.App.Lib.USBeaconServerInfo;
import com.THLight.USBeacon.App.Lib.iBeaconData;
import com.THLight.USBeacon.App.Lib.iBeaconScanManager;
import com.THLight.Util.THLLog;


import java.util.Date;

public class Mainpage extends Activity implements iBeaconScanManager.OniBeaconScan {

    Button butMenu, butCmd;
    TextView txtPtName, txtViewNo, txtDoctorName, txtClinicPs, txtDoc1, txtDoc2, txtDoc3, txtDoc4, txtDoc5, txtDoc6;
    TextView txtDoc1_1, txtDoc1_2, txtDoc1_5, txtDoc2_1, txtDoc2_2, txtDoc2_5, txtDoc3_1, txtDoc3_2, txtDoc3_5;
    TextView txtDoc4_1, txtDoc4_2, txtDoc4_5, txtDoc5_1, txtDoc5_2, txtDoc5_5, txtDoc6_1, txtDoc6_2, txtDoc6_5;
    Json2 json2;                //***Ben : http://61.219.152.220/rest/getTodayReg/get?chart_no=" + chart_no.toString()
    Json_BeaconGet beaconGet;   //***Ben : http://61.219.152.220/rest/receiver_beacon/get?beacon_uuid=" + b_uuid + "&chart_no=" + chart_no
    Json_BeaconSet beaconSet;   //***Ben : http://61.219.152.220/rest/receiver_beacon/set
    Json_BeaconGet_line_list beaconList; // "http://61.219.152.220/rest/getDeptList/get?location_code=20&chart_no=" + chart_no;
    String _doc1_2, _doc1_1, _doc1_5, _doc2_2, _doc2_1, _doc2_5, _doc3_2, _doc3_1, _doc3_5, _doc4_2, _doc4_1, _doc4_5, _doc5_2, _doc5_1, _doc5_5, _doc6_2, _doc6_1, _doc6_5;

    //---Ben --------S
    Handler mUI_Handler;
    //掛號資料 ----
    String _doctor_no = "", _doctor_name, _status_doc, _location_code, _status, _clinic_ps, prenatal_care; //從json2抓出來的資料 門診
    int _view_no, _current_no;//從json2抓出來的資料 門診
    String _location_name;//從beaconget抓出來的資料 衛教室
    int _total_num, _current_num, _apn;//從beaconget抓出來的資料 衛教室
    PatientInfoObj patient;
    int _chart_no;
    String _pt_name;
    String _json_BeaconGet_Result;
    String _json_BeaconSet_Result;
    String b_uuid;
    String _doc1 = "", _doc2 = "", _doc3 = "", _doc4 = "", _doc5 = "", _doc6 = "";
    Boolean butCmdVisible = false;
    boolean doubleclick = true;
    String _processType = "";
    String _exceptionTime = "";
    int notFoundBeaconCount = 0;
    int _seq_no_1 = 0, _seq_no_2 = 0, _seq_no_3 = 0, _seq_no_4 = 0;
    String _pt_name_1 = "", _pt_name_2 = "", _pt_name_3 = "", _pt_name_4 = "";
    String status_1 = "", status_2 = "", status_3 = "", status_4 = "";

    //---------------------
    Date curDate;
    int timeForScaning = 1000;              //scan持續時間
    int delaySec = 10000;                   //scan間隔時間
    boolean stopBln;                        //控制 每 delaySec 只發生 onScan 一次
    //String beaconUuid;                    //存放 Beacon uuid 之變數-> b_uuid
    private Handler mHandler = new Handler();
    //---------------E

    /**
     * scaner for scanning iBeacon around.
     */
    iBeaconScanManager miScaner = null;
    final String HTTP_API = "http://www.usbeacon.com.tw/api/func";

    BluetoothAdapter mBLEAdapter = BluetoothAdapter.getDefaultAdapter();
    final int REQ_ENABLE_BT = 2000;
    USBeaconConnection mBServer = new USBeaconConnection();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        //-------------------------
        // ----檢查網路是否已經開啟---- //
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Mainpage.CONNECTIVITY_SERVICE);
        if (null != cm) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (null == ni || (!ni.isConnected())) {
                dlgNetworkNotAvailable();
            } else {
                THLLog.d("debug", "NI not null");
                NetworkInfo niMobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (null != niMobile) {
                    boolean is3g = niMobile.isConnectedOrConnecting();
                    if (is3g) {
                    } else {
                        USBeaconServerInfo info = new USBeaconServerInfo();
                        info.serverUrl = HTTP_API;
                    }
                }
            }
        } else {
            THLLog.d("debug", "CM null");
        }
        // ----檢查藍芽是否已將開啟---- //
        if (!mBLEAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQ_ENABLE_BT);
            Log.e("開啟藍芽", "辨識");
        } else {
            Log.e("開啟藍芽", "已開");
        }
        //-------------------------
        //***Ben --- 取出 patient 資料-------s
        patient = (PatientInfoObj) getApplicationContext();

        _pt_name = patient.getPtName();
        _chart_no = patient.getChartNo();

        //***Ben ----取出元件 ---------------e
        butMenu = (Button) findViewById(R.id.but_menu);
        txtPtName = (TextView) findViewById(R.id.textView1); //病人名稱
        txtViewNo = (TextView) findViewById(R.id.textView10); //看診號碼
        txtDoctorName = (TextView) findViewById(R.id.textView9); //醫生姓名
        txtClinicPs = (TextView) findViewById(R.id.textView3); //診間號碼
        txtDoc1 = (TextView) findViewById(R.id.textView_Doc1);
        txtDoc2 = (TextView) findViewById(R.id.textView_Doc2);
        txtDoc3 = (TextView) findViewById(R.id.textView_Doc3);
        txtDoc4 = (TextView) findViewById(R.id.textView_Doc4);
        txtDoc5 = (TextView) findViewById(R.id.textView_Doc5);
        txtDoc6 = (TextView) findViewById(R.id.textView_Doc6);
        Textview_1_6();
        butCmd = (Button) findViewById(R.id.butCmd);

        //*** Ben --- set Value to 元件 -----
        txtPtName.setText(_pt_name);
        DisplayDocArea();
        butMenu.setOnClickListener(new ClickToMenu());
        butCmd.setOnClickListener(new ClickConform());
        mUI_Handler = new Handler();

        //如果其他activity傳回true那就執行releasesharePre 來 回復資料
        Bundle bundle = getIntent().getExtras();
        if (bundle.equals("\"\"")) {
            Log.d("ohho", "回傳的值沒東西 " + _doctor_name + _clinic_ps + _doctor_no + _view_no + _pt_name);
        } else {
            Boolean boolean1 = bundle.getBoolean("Boolean");
            if (boolean1) {
                releasesharePre();
            }
        }


        //***Ben : call Json2 取得今日看診資料 -----------
        Thread mThread = new Thread(mainRun);
        mThread.start();


//***Ben
        /** create instance of iBeaconScanManager. */
        miScaner = new iBeaconScanManager(this, this);
        stopBln = false;
        miScaner.startScaniBeacon(timeForScaning);
        //***Ben : 建立定時器, 每 delaySec 啟動一次 scan Beacon
        mHandler.removeCallbacks(onTimer);
        mHandler.postDelayed(onTimer, delaySec);
//------------

    }


    public void Textview_1_6() {
        txtDoc1_1 = (TextView) findViewById(R.id.textView_Doc1_1);
        txtDoc1_2 = (TextView) findViewById(R.id.textView_Doc1_2);
        txtDoc1_5 = (TextView) findViewById(R.id.textView_Doc1_5);
        txtDoc2_1 = (TextView) findViewById(R.id.textView_Doc2_1);
        txtDoc2_2 = (TextView) findViewById(R.id.textView_Doc2_2);
        txtDoc2_5 = (TextView) findViewById(R.id.textView_Doc2_5);
        txtDoc3_1 = (TextView) findViewById(R.id.textView_Doc3_1);
        txtDoc3_2 = (TextView) findViewById(R.id.textView_Doc3_2);
        txtDoc3_5 = (TextView) findViewById(R.id.textView_Doc3_5);
        txtDoc4_1 = (TextView) findViewById(R.id.textView_Doc4_1);
        txtDoc4_2 = (TextView) findViewById(R.id.textView_Doc4_2);
        txtDoc4_5 = (TextView) findViewById(R.id.textView_Doc4_5);
        txtDoc5_1 = (TextView) findViewById(R.id.textView_Doc5_1);
        txtDoc5_2 = (TextView) findViewById(R.id.textView_Doc5_2);
        txtDoc5_5 = (TextView) findViewById(R.id.textView_Doc5_5);
        txtDoc6_1 = (TextView) findViewById(R.id.textView_Doc6_1);
        txtDoc6_2 = (TextView) findViewById(R.id.textView_Doc6_2);
        txtDoc6_5 = (TextView) findViewById(R.id.textView_Doc6_5);
    }


    //***Ben : 定時器, 時間到所做之內容
    private Runnable onTimer = new Runnable() {
        @Override
        public void run() {
            //***Ben : 更新畫面內容
            //mBeaconUuid.setText(beaconUuid);
            //mScanTime.setText(formatter.format(curDate));

            //Ben***----- refresh screen -----
            // 當移除Beacon時清除畫面, 此段需觀察看看有無問題
            notFoundBeaconCount++;
            if (notFoundBeaconCount > 1) {
                _doc2 = "";
                _doc3 = "";
                butCmdVisible = false;
                mUI_Handler.post(runnableShow2Screen);
            }
            //--------------------------------

            //***Ben : 再重新開始 scan Beacon
            miScaner.startScaniBeacon(timeForScaning);
            stopBln = false;

            //***Ben : 設定下一次定時器啟動時間
            mHandler.postDelayed(this, delaySec);
        }
    };

    @Override
    public void onScaned(iBeaconData iBeacon) {
        //***Ben------s
        if (!stopBln) {
            synchronized (this) {
                //***Ben : 獲取當前時間
                notFoundBeaconCount = 0;
                stopBln = true;
                curDate = new Date(System.currentTimeMillis());

                Log.d("BEN", "onScaned : " + curDate);

                //*** Ben : 開始處理SCAN到訊號之事情
                b_uuid = iBeacon.beaconUuid.toString();
                Log.d("BEN", "Scan Beacon, UUID = " + b_uuid);

                //***Ben : call Json_BeaconGet 由Beacon觸發 取得今日看診資料 -----------
                Thread mThread = new Thread(getRegRecordRun);
                mThread.start();
                //mThreadHandler_get.post(getRegRecordRun);
                //*** Ben : 事情處理結束

                if (b_uuid.equals("")) {
                    _doc1_1 = "未發現掛號資料~~";

                }

            }
        } else {
            Log.d("BEN", "Scan Beacon, But not activate");
        }

        //this.b_uuid = iBeacon.beaconUuid.toString();
        //Log.d("BEACON", "Scan Beacon, UUID = " + b_uuid);
        //Benj暫時Mark
        //mThreadHandler_get.post(g1);
    }

    @Override
    public void onBatteryPowerScaned(BatteryPowerData batteryPowerData) {

    }

    class ClickToMenu implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.but_menu: {
                    Log.e("ohho", "按下按鈕 onClick  on " + _doctor_name + _clinic_ps + _doctor_no + _view_no + _pt_name);
                    Wrisharepre();
                    Intent it = new Intent();
                    it.setClass(Mainpage.this, Menu.class);
                    startActivity(it);
                    Mainpage.this.finish();
                    break;
                }
            }
        }
    }

    class ClickConform implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Log.d("ohho", "onclick " + doubleclick);
            if (doubleclick = true) {
                //***Ben : call Json_BeaconGet 由Beacon觸發 取得今日看診資料 -----------
                Thread mThread = new Thread(setBeaconProcessRun);
                mThread.start();
                doubleclick = false;
                //Toast.makeText(Mainpage.this, "enter", Toast.LENGTH_SHORT).show();
                Log.d("ohho", "按下按鈕");
            } else {
                Toast.makeText(Mainpage.this, "check double time", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public Runnable mainRun = new Runnable() {
        @Override
        public void run() {
            json2 = new Json2(String.valueOf(_chart_no));
            //------- Patient Register Record ----- Upper
            _status_doc = json2.get_status_doc();

            beaconGet = new Json_BeaconGet(b_uuid, String.valueOf(_chart_no));
            _status = beaconGet.get_status();
//            Log.d("處室代碼 mainpage", "處室代碼" + _status_doc.toString());
            // 這邊先把掛號室的資料抓出來 因為衛教室和科室那邊沒有這些資料 之後再放進去


            if (json2.isHaveData()) {
                switch (_status_doc) {

                    case "PRE_DEPT_SCHEDULE": // 衛教室
                        Log.i("case2", _status_doc.toString());
                        _location_code = json2.getlocation_code();
                        _status = json2.get_status();
                        _processType = json2.get_status_doc();
                        prenatal_care = json2.getprenatal_care();
                        _location_name = json2.getlocation_name();
                        _total_num = json2.gettotal_num();
                        _current_num = json2.getcurrent_num();
                        _apn = json2.getapn();
                        //------- Clinic Status ---- Bottom
                        _doc1 = "目前衛教室等待人數 : " + _total_num;
                        _doc2 = "目前已服務至 : " + _current_num;
                        _doc3 = "是否報到並加入排隊 ? ";
                        _doc4 = "";
                        _doc5 = "";
                        _doc6 = "";
                        _doc1_1 = _doc1_2 = _doc1_5 = "";
                        _doc2_1 = _doc2_2 = _doc2_5 = "";
                        _doc3_1 = _doc3_2 = _doc3_5 = "";
                        _doc4_1 = _doc4_2 = _doc4_5 = "";
                        _doc5_1 = _doc5_2 = _doc5_5 = "";
                        _doc6_1 = _doc6_2 = _doc6_5 = "";
                        butCmdVisible = true;
                        break;

                    case "DEPT_SCHEDULE": // 檢驗科
                        Log.i("case3", _status_doc.toString());
                        _doctor_no = json2.getdoctor_no();
                        _location_code = json2.getlocation_code();
                        _status = json2.get_status();
                        _processType = json2.get_status_doc();
                        prenatal_care = json2.getprenatal_care();
                        _location_name = json2.getlocation_name();
                        _total_num = json2.gettotal_num();
                        _current_num = json2.getcurrent_num();
                        _apn = json2.getapn();
                        //------- Clinic Status ---- Bottom
                        _doc1 = "目前檢驗科等待人數 : " + _total_num;
                        _doc2 = "目前已服務至 : " + _current_num;
                        _doc3 = "是否報到並加入排隊 ? ";
                        _doc4 = "";
                        _doc5 = "";
                        _doc6 = "";
                        _doc1_1 = _doc1_2 = _doc1_5 = "";
                        _doc2_1 = _doc2_2 = _doc2_5 = "";
                        _doc3_1 = _doc3_2 = _doc3_5 = "";
                        _doc4_1 = _doc4_2 = _doc4_5 = "";
                        _doc5_1 = _doc5_2 = _doc5_5 = "";
                        _doc6_1 = _doc6_2 = _doc6_5 = "";
                        butCmdVisible = true;
                        break;
                }
            } else {
                Log.e("沒有掛號資料", "mainRun");
                _doctor_no = "";
                _doctor_name = "";
                _current_no = 0;
                _clinic_ps = "";
                _view_no = 0;
                _doc1 = "沒有掛號資料 !!";
                _doc2 = "";
                _doc3 = "";
                _doc4 = "";
                _doc5 = "";
                _doc6 = "";
                _doc1_1 = _doc1_2 = _doc1_5 = "";
                _doc2_1 = _doc2_2 = _doc2_5 = "";
                _doc3_1 = _doc3_2 = _doc3_5 = "";
                _doc4_1 = _doc4_2 = _doc4_5 = "";
                _doc5_1 = _doc5_2 = _doc5_5 = "";
                _doc6_1 = _doc6_2 = _doc6_5 = "";
            }
            //Ben***----- refresh screen -----
            mUI_Handler.post(runnableShow2Screen);

        }
    };

    public Runnable getRegRecordRun = new Runnable() {
        public void run() {
            beaconGet = new Json_BeaconGet(b_uuid, String.valueOf(_chart_no));

            _status = beaconGet.get_status();
            Log.d("看錯誤訊息 Mainpage", "_status : " + _status.toString());
            if (_status.equals("error")) {  //判斷是不是 success 資料回傳正確
                _status_doc = beaconGet.get_status_doc();
                _doc1 = _status_doc;
                _doc2 = "";
                _doc3 = "";
                _doc4 = "";
                _doc5 = "";
                _doc6 = "";
                Log.d("錯誤訊息 Mainpage", "錯誤訊息 : " + _status_doc.toString());
                butCmdVisible = false;
            } else {
                if (beaconGet.isHaveData()) {
                    _json_BeaconGet_Result = beaconGet.getResult();
                    Log.e("test BeaconGet_Result", "_json_BeaconGet_Result" + _json_BeaconGet_Result.toString());
                    if (_json_BeaconGet_Result.equals("\"\"")) {
                        Log.d("BEN", "getRegRecordRun had Processed !! return 空字串.....");
                    } else {
                        _status_doc = beaconGet.get_status_doc();

                        switch (_status_doc) {

                            case "REG": // 掛號門口
                                Log.i("case1", _status_doc.toString());
                                _view_no = beaconGet.getview_no();
                                _doctor_name = beaconGet.getdoctor_name();
                                _clinic_ps = beaconGet.getclinic_ps();
                                _doctor_no = beaconGet.getdoctor_no();
                                _location_code = beaconGet.getlocation_code();
                                _status = beaconGet.get_status();
                                _processType = beaconGet.get_status_doc();
                                prenatal_care = beaconGet.getprenatal_care();
                                //------- Clinic Status ---- Bottom
                                _current_no = beaconGet.getcurrent_no();
                                _doc1 = "目前看診至 : " + _current_no;
                                _doc2 = "確定預約報到 ? ";
                                _doc3 = "";
                                _doc4 = "";
                                _doc5 = "";
                                _doc6 = "";
                                _doc1_1 = _doc1_2 = _doc1_5 = "";
                                _doc2_1 = _doc2_2 = _doc2_5 = "";
                                _doc3_1 = _doc3_2 = _doc3_5 = "";
                                _doc4_1 = _doc4_2 = _doc4_5 = "";
                                _doc5_1 = _doc5_2 = _doc5_5 = "";
                                _doc6_1 = _doc6_2 = _doc6_5 = "";
                                butCmdVisible = true;
//                                Log.d("排隊人數2 _current_no", "_current_no : " + _current_no);
                                break;

                            case "PRE_DEPT_SCHEDULE": // 衛教室
                                Log.i("case2", _status_doc.toString());
                                _location_code = beaconGet.getlocation_code();
                                _status = beaconGet.get_status();
                                _processType = beaconGet.get_status_doc();
                                prenatal_care = beaconGet.getprenatal_care();
                                _location_name = beaconGet.getlocation_name();
                                _total_num = beaconGet.gettotal_num();
                                _current_num = beaconGet.getcurrent_num();
                                _apn = beaconGet.getapn();

                                //------- Clinic Status ---- Bottom
                                _current_no = beaconGet.getcurrent_num();
                                _doc1 = "目前衛教室等待人數 : " + _total_num;
                                _doc2 = "目前已服務至 : " + _current_num;
                                _doc3 = "是否報到並加入排隊 ? ";
                                _doc4 = "";
                                _doc5 = "";
                                _doc6 = "";
                                _doc1_1 = _doc1_2 = _doc1_5 = "";
                                _doc2_1 = _doc2_2 = _doc2_5 = "";
                                _doc3_1 = _doc3_2 = _doc3_5 = "";
                                _doc4_1 = _doc4_2 = _doc4_5 = "";
                                _doc5_1 = _doc5_2 = _doc5_5 = "";
                                _doc6_1 = _doc6_2 = _doc6_5 = "";
                                butCmdVisible = true;
                                break;

                            case "DEPT_SCHEDULE": // 檢驗科
                                Log.i("case3", _status_doc.toString());
                                _location_code = beaconGet.getlocation_code();
                                _status = beaconGet.get_status();
                                _processType = beaconGet.get_status_doc();
                                prenatal_care = beaconGet.getprenatal_care();
                                _location_name = beaconGet.getlocation_name();
                                _total_num = beaconGet.gettotal_num();
                                _current_num = beaconGet.getcurrent_num();
                                _apn = beaconGet.getapn();

                                Log.i("case3 _location_code", "31和32的位子是哪裡: " + _location_code.toString());
                                if (_location_code.equals("31")) {
                                    //------- Clinic Status ---- Bottom
                                    _current_no = beaconGet.getcurrent_num();
                                    _doc1 = "目前檢驗科等待人數 : " + _total_num;
                                    _doc2 = "目前已服務至 : " + _current_num;
                                    _doc3 = "是否報到並加入排隊 ? ";
                                    _doc4 = "";
                                    _doc5 = "";
                                    _doc6 = "";
                                    _doc1_1 = _doc1_2 = _doc1_5 = "";
                                    _doc2_1 = _doc2_2 = _doc2_5 = "";
                                    _doc3_1 = _doc3_2 = _doc3_5 = "";
                                    _doc4_1 = _doc4_2 = _doc4_5 = "";
                                    _doc5_1 = _doc5_2 = _doc5_5 = "";
                                    _doc6_1 = _doc6_2 = _doc6_5 = "";
                                    butCmdVisible = true;
                                } else {
                                    //------- Clinic Status ---- Bottom
                                    _current_no = beaconGet.getcurrent_num();
                                    _doc1 = "目前超音波室等待人數 : " + _total_num;
                                    _doc2 = "目前已服務至 : " + _current_num;
                                    _doc3 = "是否報到並加入排隊 ? ";
                                    _doc4 = "";
                                    _doc5 = "";
                                    _doc6 = "";
                                    _doc1_1 = _doc1_2 = _doc1_5 = "";
                                    _doc2_1 = _doc2_2 = _doc2_5 = "";
                                    _doc3_1 = _doc3_2 = _doc3_5 = "";
                                    _doc4_1 = _doc4_2 = _doc4_5 = "";
                                    _doc5_1 = _doc5_2 = _doc5_5 = "";
                                    _doc6_1 = _doc6_2 = _doc6_5 = "";
                                    butCmdVisible = true;
                                }

                                break;
                        }
                    }
                } else {
                    Log.e("沒有掛號資料", "getRegRecordRun");
                    _doctor_no = "";
                    _doctor_name = "";
                    _current_no = 0;
                    _clinic_ps = "";
                    _view_no = 0;
                    _doc1 = "沒有掛號資料 !!";
                    _doc2 = "";
                    _doc3 = "";
                    _doc4 = "";
                    _doc5 = "";
                    _doc6 = "";
                    _doc1_1 = _doc1_2 = _doc1_5 = "";
                    _doc2_1 = _doc2_2 = _doc2_5 = "";
                    _doc3_1 = _doc3_2 = _doc3_5 = "";
                    _doc4_1 = _doc4_2 = _doc4_5 = "";
                    _doc5_1 = _doc5_2 = _doc5_5 = "";
                    _doc6_1 = _doc6_2 = _doc6_5 = "";
                    butCmdVisible = false;
                }
            }
            //Ben***----- refresh screen -----
            mUI_Handler.post(runnableShow2Screen);

            Wrisharepre();  //SharedPreferences
        }
    };

    public Runnable runnableShow2Screen = new Runnable() {
        public void run() {
            txtDoctorName.setText(_doctor_name);
            txtClinicPs.setText(_clinic_ps);
            txtViewNo.setText(String.valueOf(_view_no));
            Log.e("ohho", "runnableShow2Screen on " + _doctor_name + _clinic_ps + _doctor_no + _view_no);
            if (!_doctor_no.equals("")) {
                WebView myWebView = (WebView) findViewById(R.id.webview);
                myWebView.getSettings().setJavaScriptEnabled(true);
                myWebView.requestFocus();
                myWebView.setWebViewClient(new MyWebViewClient());
                myWebView.loadUrl("http://61.219.152.220/static/doctor_images/" + String.valueOf(_doctor_no) + ".jpeg");
            } else {
                //Toast.makeText(Mainpage.this, "沒有畫面", Toast.LENGTH_SHORT).show();
            }
            DisplayDocArea();

        }
    };

    private void DisplayDocArea() {
        txtDoc1.setText(_doc1);
        txtDoc1_1.setText(_doc1_1);
        txtDoc1_2.setText(_doc1_2);
        txtDoc1_5.setText(_doc1_5);
        txtDoc2.setText(_doc2);
        txtDoc2_1.setText(_doc2_1);
        txtDoc2_2.setText(_doc2_2);
        txtDoc2_5.setText(_doc2_5);
        txtDoc3.setText(_doc3);
        txtDoc3_1.setText(_doc3_1);
        txtDoc3_2.setText(_doc3_2);
        txtDoc3_5.setText(_doc3_5);
        txtDoc4.setText(_doc4);
        txtDoc4_1.setText(_doc4_1);
        txtDoc4_2.setText(_doc4_2);
        txtDoc4_5.setText(_doc4_5);
        txtDoc5.setText(_doc5);
        txtDoc5_1.setText(_doc5_1);
        txtDoc5_2.setText(_doc5_2);
        txtDoc5_5.setText(_doc5_5);
        txtDoc6.setText(_doc6);
        txtDoc6_1.setText(_doc6_1);
        txtDoc6_2.setText(_doc6_2);
        txtDoc6_5.setText(_doc6_5);

        if (butCmdVisible) {
            butCmd.setVisibility(View.VISIBLE);
//            _json_BeaconGet_Result = beaconGet.getResult();
//            Log.e("test BeaconGet_Result", "_json_BeaconGet_Result"+_json_BeaconGet_Result.toString());
//            _processType = beaconGet.get_status_doc();


            Log.d("test 科室編號 : ", "_processType : " + _processType.toString());
            switch (_processType) {
                case "REG":
                    butCmd.setText("預約報到");
                    break;
                case "PRE_DEPT_SCHEDULE":
                    butCmd.setText("衛教室報到排隊");
                    break;
                case "DEPT_SCHEDULE":
                    butCmd.setText("科室報到排隊");
                    break;
            }

        } else {
            butCmd.setVisibility(View.INVISIBLE);
        }
    }

    public Runnable setBeaconProcessRun = new Runnable() {
        public void run() {
            beaconSet = new Json_BeaconSet(_json_BeaconGet_Result);
            _processType = beaconGet.get_status_doc();
            if (!_processType.equals("REG")) {
                beaconList = new Json_BeaconGet_line_list(String.valueOf(_chart_no), _location_code, _processType);
                _pt_name_1 = beaconList.getpt_name_1();
                _pt_name_2 = beaconList.getpt_name_2();
                _pt_name_3 = beaconList.getpt_name_3();
                _pt_name_4 = beaconList.getpt_name_4();
                _seq_no_1 = beaconList.getseq_no_1();
                _seq_no_2 = beaconList.getseq_no_2();
                _seq_no_3 = beaconList.getseq_no_3();
                _seq_no_4 = beaconList.getseq_no_4();
                status_1 = beaconList.getstatus_1();
                status_2 = beaconList.getstatus_2();
                status_3 = beaconList.getstatus_3();
                status_4 = beaconList.getstatus_4();
            }
            _processType = beaconGet.get_status_doc();
            if (beaconGet.isHaveData()) {
                _json_BeaconSet_Result = beaconSet.getResult();
                if (_json_BeaconSet_Result.equals("\"\"")) {
                    Log.d("BEN", "setBeaconProcessRun had Processed !! return 空字串.....");
                } else {

                    Log.d("test 科室編號 set : ", "_processType : " + _processType.toString());
                    switch (_processType) {
                        case "REG":
                            //------- Clinic Status ---- Bottom
                            _exceptionTime = beaconSet.getExceptViewTime();
                            _doc1 = "預計看診時間為 : " + _exceptionTime;
                            _doc2 = "報到成功 !!";
                            _doc3 = "";
                            _doc4 = "";
                            _doc5 = "";
                            _doc6 = "";
                            _doc1_1 = _doc1_2 = _doc1_5 = "";
                            _doc2_1 = _doc2_2 = _doc2_5 = "";
                            _doc3_1 = _doc3_2 = _doc3_5 = "";
                            _doc4_1 = _doc4_2 = _doc4_5 = "";
                            _doc5_1 = _doc5_2 = _doc5_5 = "";
                            _doc6_1 = _doc6_2 = _doc6_5 = "";
                            prenatal_care = beaconGet.getprenatal_care();
                            if (prenatal_care.equals("Y")) {
                                _doc3 = "請您先到衛教室報到";
                            }
                            butCmdVisible = false;
                            break;

                        case "PRE_DEPT_SCHEDULE":
                            _doc1 = "";
                            _doc2 = "";
                            _doc3 = "";
                            _doc4 = "";
                            _doc5 = "";
                            _doc6 = "";
                            _doc1_1 = " 號碼 ";
                            _doc1_2 = " 姓   名 ";
                            _doc1_5 = "狀 態 ";
                            _doc2_1 = "  " + String.valueOf(_seq_no_1);
                            _doc2_2 = " " + _pt_name_1;
                            _doc2_5 = status_1;
                            _doc3_1 = "  " + String.valueOf(_seq_no_2);
                            _doc3_2 = " " + _pt_name_2;
                            _doc3_5 = status_2;
                            _doc4_1 = "  " + String.valueOf(_seq_no_3);
                            _doc4_2 = " " + _pt_name_3;
                            _doc4_5 = status_3;
                            _doc5_1 = "  " + String.valueOf(_seq_no_4);
                            _doc5_2 = " " + _pt_name_4;
                            _doc5_5 = status_4;
                            butCmdVisible = false;
                            break;

                        case "DEPT_SCHEDULE":
                            if (_pt_name_1.equals("")) {
                                _doc1 = "沒有掛號資料";
                            } else {
                                _doc1 = "";
                                _doc2 = "";
                                _doc3 = "";
                                _doc4 = "";
                                _doc5 = "";
                                _doc6 = "";
                                _doc1_1 = " 號碼 ";
                                _doc1_2 = " 姓   名 ";
                                _doc1_5 = "狀 態 ";
                                _doc2_1 = "  " + String.valueOf(_seq_no_1);
                                _doc2_2 = " " + _pt_name_1;
                                _doc2_5 = status_1;
                                _doc3_1 = "  " + String.valueOf(_seq_no_2);
                                _doc3_2 = " " + _pt_name_2;
                                _doc3_5 = status_2;
                                _doc4_1 = "  " + String.valueOf(_seq_no_3);
                                _doc4_2 = " " + _pt_name_3;
                                _doc4_5 = status_3;
                                _doc5_1 = "  " + String.valueOf(_seq_no_4);
                                _doc5_2 = " " + _pt_name_4;
                                _doc5_5 = status_4;
                            }
                            butCmdVisible = false;
                            break;
                    }
                }
            } else {
                Log.e("沒有掛號資料", "setBeaconProcessRun");
                _doctor_no = "";
                _doctor_name = "";
                _current_no = 0;
                _clinic_ps = "";
                _view_no = 0;
                _doc1 = "沒有掛號資料 !!";
                _doc2 = "";
                _doc3 = "";
                _doc4 = "";
                _doc5 = "";
                _doc6 = "";
                _doc1_1 = _doc1_2 = _doc1_5 = "";
                _doc2_1 = _doc2_2 = _doc2_5 = "";
                _doc3_1 = _doc3_2 = _doc3_5 = "";
                _doc4_1 = _doc4_2 = _doc4_5 = "";
                _doc5_1 = _doc5_2 = _doc5_5 = "";
                _doc6_1 = _doc6_2 = _doc6_5 = "";
                butCmdVisible = false;
            }

            //Ben***----- refresh screen -----
            mUI_Handler.post(runnableShow2Screen);

            Wrisharepre();  //SharedPreferences
        }
    };

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }


    // ----檢查網路是否已經開啟----
    public void dlgNetworkNotAvailable() {
        final AlertDialog dlg = new AlertDialog.Builder(Mainpage.this).create();

        dlg.setTitle("沒有網路連線");
        dlg.setMessage("請開啟您的網路");

        dlg.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dlg.dismiss();
                Mainpage.this.finish();
                Log.e("沒有網路", "關閉");
            }
        });
        dlg.show();
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
        AlertDialog.Builder ad = new AlertDialog.Builder(Mainpage.this); //創建訊息方塊
        ad.setTitle("離開");
        ad.setMessage("確定要離開?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則退出應用程式
            public void onClick(DialogInterface dialog, int i) {
                Mainpage.this.finish();//關閉activity
            }
        });

        ad.setNegativeButton("否", new DialogInterface.OnClickListener() { //按"否",則不執行任何操作
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        ad.show();//顯示訊息視窗
    }

    //把資料存到SharedPreferences裡面
    public void Wrisharepre() {
        //取得一個 SharedPreferences 物件讓目前的 Activity 使用，
        //產生的 SharedPreferences 檔案「可以讓其他 App 寫入」
        SharedPreferences spref = getPreferences(MODE_PRIVATE);

        //由 SharedPreferences 中取出 Editor 物件，透過 Editor 物件將資料存入
        SharedPreferences.Editor editor = spref.edit();
        Log.e("ohho", "sharePre2  on " + _doctor_name + _clinic_ps + _doctor_no + _view_no + _pt_name);
        //清除 SharedPreferences 檔案中所有資料
        editor.clear();


        //儲存字串型態的資料
        editor.putString("mPT_name", _pt_name);
        editor.putString("mDoctor_name", _doctor_name);
        editor.putString("mClinic_ps", _clinic_ps);
        editor.putString("mDoctor_no", _doctor_no);
        editor.putInt("mView_no", _view_no);

        editor.putString("m_doc1", _doc1);
        editor.putString("m_doc2", _doc2);
        editor.putString("m_doc3", _doc3);

        editor.putString("m_doc1_1", _doc1_1);
        editor.putString("m_doc1_2", _doc1_2);
        editor.putString("m_doc1_5", _doc1_5);

        editor.putString("m_doc2_1", _doc2_1);
        editor.putString("m_doc2_2", _doc2_2);
        editor.putString("m_doc2_5", _doc2_5);

        editor.putString("m_doc3_1", _doc3_1);
        editor.putString("m_doc3_2", _doc3_2);
        editor.putString("m_doc3_5", _doc3_5);

        editor.putString("m_doc4_1", _doc4_1);
        editor.putString("m_doc4_2", _doc4_2);
        editor.putString("m_doc4_5", _doc4_5);

        editor.putString("m_doc5_1", _doc5_1);
        editor.putString("m_doc5_2", _doc5_2);
        editor.putString("m_doc5_5", _doc5_5);

        editor.putBoolean("m_butCmdVisible", butCmdVisible);
        editor.putString("m_processType", _processType);
        //將目前對 SharedPreferences 的異動寫入檔案中
        //如果沒有呼叫 apply()，則異動的資料不會生效
        editor.apply();

        //將目前對 SharedPreferences 的異動寫入檔案中
        //如果沒有呼叫 commit()，則異動的資料不會生效
        editor.commit();
        //回傳 KEY_STRING 是否在在 SharedPreferences 檔案中
        boolean exists = spref.contains("mDoctor_name");
        Log.e("ohho", "sharePre  on " + _doctor_name + _clinic_ps + _doctor_no + _view_no + _pt_name + exists);

    }

    public void releasesharePre() {
        //取出 SharedPreferences 物件，若 SharedPreference 檔案不存在就會建立一個新的
        SharedPreferences spref = getPreferences(MODE_PRIVATE);

        //透過 KEY_STRING key 取出字串型態的資料，若資料不存在則回傳 null
        _pt_name = spref.getString("mPT_name", "");
        _doctor_name = spref.getString("mDoctor_name", "");
        _clinic_ps = spref.getString("mClinic_ps", "");
        _doctor_no = spref.getString("mDoctor_no", "");
        _view_no = spref.getInt("mView_no", 0);

        _doc1 = spref.getString("m_doc1", "");
        _doc2 = spref.getString("m_doc2", "");
        _doc3 = spref.getString("m_doc3", "");

        _doc1_1 = spref.getString("m_doc1_1", "");
        _doc1_2 = spref.getString("m_doc1_2", "");
        _doc1_5 = spref.getString("m_doc1_5", "");

        _doc2_1 = spref.getString("m_doc2_1", "");
        _doc2_2 = spref.getString("m_doc2_2", "");
        _doc2_5 = spref.getString("m_doc2_5", "");

        _doc3_1 = spref.getString("m_doc3_1", "");
        _doc3_2 = spref.getString("m_doc3_2", "");
        _doc3_5 = spref.getString("m_doc3_5", "");

        _doc4_1 = spref.getString("m_doc4_1", "");
        _doc4_2 = spref.getString("m_doc4_2", "");
        _doc4_5 = spref.getString("m_doc4_5", "");

        _doc5_1 = spref.getString("m_doc5_1", "");
        _doc5_2 = spref.getString("m_doc5_2", "");
        _doc5_5 = spref.getString("m_doc5_5", "");

        butCmdVisible = spref.getBoolean("m_butCmdVisible", false);
        _processType = spref.getString("m_processType", "");

        //回傳 mDoctor_name 是否在在 SharedPreferences 檔案中
        boolean exists = spref.contains("mDoctor_name");
        boolean exists1 = spref.contains("mClinic_ps");
        boolean exists2 = spref.contains("mClinic_ps");
        boolean exists3 = spref.contains("mDoctor_no");
        boolean exists4 = spref.contains("mView_no");
        Log.e("ohho", "releasesharePre1  on " + exists + exists1 + exists2 + exists3 + exists4);
        Log.e("ohho", "releasesharePre2  on " + _pt_name + _doctor_name + _clinic_ps + _doctor_no + _view_no);

        txtDoctorName.setText(_doctor_name);
        txtClinicPs.setText(_clinic_ps);
        txtViewNo.setText(String.valueOf(_view_no));

    }


}
