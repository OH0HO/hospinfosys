package tw.com.hismax.test.hospinfosys;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class Json2 extends Thread {
    String result2 = "";
    int view_no ,current_no= 0;
    String doctor_name,clinic_ps,prenatal_care = "";
    String _status_doc, _status = "";
    String location_code = "";
    String doctor_no = "";
    private int currentNo = 0;
    JSONArray JArray = new JSONArray();
    JSONObject jsonObj = new JSONObject();
    String test = "";
    private boolean haveData = false;
    private int curNo = 0;
    //衛教室
    String location_name;
    int duplicate_no, total_num, current_num, apn;

    public Json2(String chart_no) {
//        //若線上資料為陣列，則使用JSONArray
//        JSONArray jsonArray = null;
//        //若線上資料為單筆資料，則使用JSONObject
//        JSONObject jsonObj = null;


        // 透過HTTP連線取得回應
        try {
            Log.d("BEN", "Json2 Start !!");
            jsonObj.put("", test);
            JArray.put(jsonObj);

            HttpClient client = new DefaultHttpClient(); // for port 80 requests!
            Log.i("chart_no.toString()", chart_no.toString());
            HttpGet httpget = new HttpGet("http://61.219.152.220/rest/getTodayReg/get?chart_no=" + chart_no);

            HttpResponse response = client.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                this.result2 = EntityUtils.toString(entity);
                Log.d("Ben", "json2 = " + result2);
                this._status = new JSONArray(this.result2).getJSONObject(0).getString("_status");
                Log.d("Ben", "json2 status= " + this._status);
                if (_status.equals("success")){
                    this._status_doc = new JSONArray(this.result2).getJSONObject(0).getString("_status_doc");
                    this._status_doc = new String(_status_doc.getBytes("ISO-8859-1"), "UTF-8");
                    Log.i("處室代碼 beaconget", "處室代碼 : " + _status_doc.toString());
                    switch (_status_doc) {
                        case "REG": // 掛號門口
                            haveData = true;
                            this._status = new JSONArray(this.result2).getJSONObject(0).getString("_status");
                            this.view_no = new JSONArray(this.result2).getJSONObject(0).getInt("view_no");
                            this._status_doc = new JSONArray(this.result2).getJSONObject(0).getString("_status_doc");
                            this.location_code = new JSONArray(this.result2).getJSONObject(0).getString("location_code");
                            this.doctor_no = new JSONArray(this.result2).getJSONObject(0).getString("doctor_no");
                            this.doctor_name = new JSONArray(this.result2).getJSONObject(0).getString("doctor_name");
                            this.clinic_ps = new JSONArray(this.result2).getJSONObject(0).getString("clinic_ps");
                            this.current_no = new JSONArray(this.result2).getJSONObject(0).getInt("current_no");
                            this.prenatal_care = new JSONArray(this.result2).getJSONObject(0).getString("prenatal_care");
                            this.doctor_name = new String(doctor_name.getBytes("ISO-8859-1"), "UTF-8");   //亂碼變中文
                            this._status_doc = new String(_status_doc.getBytes("ISO-8859-1"), "UTF-8");
                            this.clinic_ps = new String(clinic_ps.getBytes("ISO-8859-1"), "UTF-8");
                            //***Ben -------s
                            this.setCurNo(new JSONArray(this.result2).getJSONObject(0).getInt("current_no"));

                            Log.d("doctor_name_json2", doctor_name.toString());
                            Log.d("_status_doc_json2", "掛號門口" + _status_doc.toString());
                            Log.d("clinic_ps_json2", clinic_ps.toString());
                            Log.d("view_no", "view_no : " + String.valueOf(view_no));
                            break;

                        case "PRE_DEPT_SCHEDULE": // 衛教室
                            haveData = true;
                            this.location_code = new JSONArray(this.result2).getJSONObject(0).getString("location_code");
                            this._status = new JSONArray(this.result2).getJSONObject(0).getString("_status");
                            this.total_num = new JSONArray(this.result2).getJSONObject(0).getInt("total_num");
                            this.current_num = new JSONArray(this.result2).getJSONObject(0).getInt("current_num");
                            this.apn = new JSONArray(this.result2).getJSONObject(0).getInt("apn");
                            this.location_name = new JSONArray(this.result2).getJSONObject(0).getString("location_name");
                            this._status_doc = new JSONArray(this.result2).getJSONObject(0).getString("_status_doc");
                            this.location_name = new String(location_name.getBytes("ISO-8859-1"), "UTF-8");
                            this._status_doc = new String(_status_doc.getBytes("ISO-8859-1"), "UTF-8");

                            Log.d("_status_doc_json2", "衛教室" + _status_doc.toString());
                            break;

                        case "DEPT_SCHEDULE": // 檢驗室
                            haveData = true;
                            this.location_code = new JSONArray(this.result2).getJSONObject(0).getString("location_code");
                            this._status = new JSONArray(this.result2).getJSONObject(0).getString("_status");
                            this.total_num = new JSONArray(this.result2).getJSONObject(0).getInt("total_num");
                            this.current_num = new JSONArray(this.result2).getJSONObject(0).getInt("current_num");
                            this.apn = new JSONArray(this.result2).getJSONObject(0).getInt("apn");
                            this.location_name = new JSONArray(this.result2).getJSONObject(0).getString("location_name");
                            this._status_doc = new JSONArray(this.result2).getJSONObject(0).getString("_status_doc");
                            this.location_name = new String(location_name.getBytes("ISO-8859-1"), "UTF-8");
                            this._status_doc = new String(_status_doc.getBytes("ISO-8859-1"), "UTF-8");

                            Log.d("_status_doc_json2", "檢驗室" + _status_doc.toString());
                            break;
                    }

                } else {
                    haveData = false;
                }
            } else {
                haveData = false;
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
        } catch (Exception e) {
            Log.e("MYAPP", "exception", e);
        } finally {
            //client.disconnect();
        }
    }

    //回傳值到Setting

    public String get_status_doc() {
        return this._status_doc;
    }

    public String getlocation_code() {
        return this.location_code;
    }

    public String getdoctor_no() {
        return this.doctor_no;
    }

    public String get_status() {
        return this._status;
    }

    public boolean isHaveData() {
        return haveData;
    }

    public String getprenatal_care() {
        return this.prenatal_care;
    }

    //---衛教室---

    public int gettotal_num() {
        return this.total_num;
    }

    public int getcurrent_num() {
        return this.current_num;
    }

    public int getapn() {
        return this.apn;
    }

    public String getlocation_name() {
        return location_name;
    }

    public void setCurNo(int curNo) {
        this.curNo = curNo;
    }
}

