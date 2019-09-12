package tw.com.hismax.test.hospinfosys;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Json_BeaconGet extends Thread {
    private String result = "";
    int view_no = 0;
    private int curNo = 0;
    String doctor_name ="", clinic_ps="", prenatal_care = "";
    String _status_doc="", _status = "";
    String location_code = "";
    String doctor_no = "";
    JSONArray JArray = new JSONArray();
    JSONObject jsonObj = new JSONObject();
    String test = "";
    private boolean haveData = false;
    String url = "";
    //衛教室
    String location_name;
    int current_no=0, total_num=0, current_num=0, apn=0;

    public Json_BeaconGet(String b_uuid, String chart_no) {

//        //若線上資料為陣列，則使用JSONArray
//        JSONArray jsonArray = null;
//        //若線上資料為單筆資料，則使用JSONObject
//        JSONObject jsonObj = null;


        // 透過HTTP連線取得回應
        try {
            Log.d("BEN", "Json_BeaconGet Start !!");
            jsonObj.put("", test);
            JArray.put(jsonObj);
            HttpClient client = new DefaultHttpClient(); // for port 80 requests!
            Log.d("BEN", "b_uuid=" + b_uuid);
            Log.d("BEN", "chart_no = " + chart_no);
            url = "http://61.219.152.220/rest/receiver_beacon/get?beacon_uuid=" + b_uuid + "&chart_no=" + chart_no;
//            url = "http://61.219.152.220/rest/receiver_beacon/get?beacon_uuid=39EC2745-4E96-455A-B80A-03B604BF677D&chart_no=" + chart_no;
            //***Ben : for Local Server
            //url = "http://127.0.0.1:8000/rest/receiver_beacon/get?beacon_uuid=" + b_uuid + "&chart_no=" + chart_no;
            Log.d("BEN", "url = " + url);
            HttpGet httpget = new HttpGet(url);

            HttpResponse response = client.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                this.result = EntityUtils.toString(entity);
                Log.d("BEN", "Json_BeaconGet = " + result.toString());
                if (result.equals("\"\"")) {
                    //有資料但己處理過
                    haveData = true;
                } else {
                    this._status = new JSONArray(this.result).getJSONObject(0).getString("_status");
//                    Log.d("Ben", "json_BeaconGet status= " + this._status);
                    if (_status.equals("success")) {
                        this._status_doc = new JSONArray(this.result).getJSONObject(0).getString("_status_doc");
                        this._status_doc = new String(_status_doc.getBytes("ISO-8859-1"), "UTF-8");
                        Log.i("處室代碼 beaconget", "處室代碼 : " + _status_doc.toString());
                        switch (_status_doc) {
                            case "REG": // 掛號門口
                                haveData = true;
                                this._status = new JSONArray(this.result).getJSONObject(0).getString("_status");
                                this.view_no = new JSONArray(this.result).getJSONObject(0).getInt("view_no");
                                this._status_doc = new JSONArray(this.result).getJSONObject(0).getString("_status_doc");
                                this.location_code = new JSONArray(this.result).getJSONObject(0).getString("location_code");
                                this.doctor_no = new JSONArray(this.result).getJSONObject(0).getString("doctor_no");
                                this.doctor_name = new JSONArray(this.result).getJSONObject(0).getString("doctor_name");
                                this.clinic_ps = new JSONArray(this.result).getJSONObject(0).getString("clinic_ps");
                                this.current_no = new JSONArray(this.result).getJSONObject(0).getInt("current_no");
                                this.prenatal_care = new JSONArray(this.result).getJSONObject(0).getString("prenatal_care");
                                this.doctor_name = new String(doctor_name.getBytes("ISO-8859-1"), "UTF-8");   //亂碼變中文
                                this._status_doc = new String(_status_doc.getBytes("ISO-8859-1"), "UTF-8");
                                this.clinic_ps = new String(clinic_ps.getBytes("ISO-8859-1"), "UTF-8");
                                //***Ben -------s
                                this.setCurNo(new JSONArray(this.result).getJSONObject(0).getInt("current_no"));

                                Log.d("doctor_name_json2", doctor_name.toString());
                                Log.d("_status_doc_json2", "掛號門口" + _status_doc.toString());
                                Log.d("clinic_ps_json2", clinic_ps.toString());
                                Log.d("view_no", "view_no : " + String.valueOf(view_no));
                                break;

                            case "PRE_DEPT_SCHEDULE": // 衛教室
                                haveData = true;
                                this.location_code = new JSONArray(this.result).getJSONObject(0).getString("location_code");
                                this._status = new JSONArray(this.result).getJSONObject(0).getString("_status");
                                this.total_num = new JSONArray(this.result).getJSONObject(0).getInt("total_num");
                                this.current_num = new JSONArray(this.result).getJSONObject(0).getInt("current_num");
                                this.apn = new JSONArray(this.result).getJSONObject(0).getInt("apn");
                                this.location_name = new JSONArray(this.result).getJSONObject(0).getString("location_name");
                                this._status_doc = new JSONArray(this.result).getJSONObject(0).getString("_status_doc");
                                this.location_name = new String(location_name.getBytes("ISO-8859-1"), "UTF-8");
                                this._status_doc = new String(_status_doc.getBytes("ISO-8859-1"), "UTF-8");

                                Log.d("_status_doc_json2", "衛教室" + _status_doc.toString());
                                break;

                            case "DEPT_SCHEDULE": // 檢驗室
                                haveData = true;
                                this.location_code = new JSONArray(this.result).getJSONObject(0).getString("location_code");
                                this._status = new JSONArray(this.result).getJSONObject(0).getString("_status");
                                this.total_num = new JSONArray(this.result).getJSONObject(0).getInt("total_num");
                                this.current_num = new JSONArray(this.result).getJSONObject(0).getInt("current_num");
                                this.apn = new JSONArray(this.result).getJSONObject(0).getInt("apn");
                                this.location_name = new JSONArray(this.result).getJSONObject(0).getString("location_name");
                                this._status_doc = new JSONArray(this.result).getJSONObject(0).getString("_status_doc");
                                this.location_name = new String(location_name.getBytes("ISO-8859-1"), "UTF-8");
                                this._status_doc = new String(_status_doc.getBytes("ISO-8859-1"), "UTF-8");

                                Log.d("_status_doc_json2", "檢驗室" + _status_doc.toString());
                                break;
                        }
                    } else {
                        this._status = new JSONArray(this.result).getJSONObject(0).getString("_status");
                        this._status_doc = new JSONArray(this.result).getJSONObject(0).getString("_status_doc");
                        this._status_doc = new String(_status_doc.getBytes("ISO-8859-1"), "UTF-8");
                        Log.d("錯誤訊息 beaconget", "錯誤訊息 : " + _status_doc.toString());
                        haveData = false;
                    }
                }
            } else {
                Log.d("BEN", "Get Data : ok");
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

    public String get_status() {
        return this._status;
    }

    public int getview_no() {
        return this.view_no;
    }

    public void setCurNo(int curNo) {
        this.curNo = curNo;
    }

    public String getprenatal_care() {
        return this.prenatal_care;
    }

    public String get_status_doc() {
        return _status_doc;
    }

    public String getdoctor_no() {
        return this.doctor_no;
    }

    public String getlocation_code() {
        return this.location_code;
    }

    public String getdoctor_name() {
        return this.doctor_name;
    }

    public String getclinic_ps() {
        return this.clinic_ps;
    }


    //**Ben:
    public boolean isHaveData() {
        return haveData;
    }

    public String getResult() {
        return result;
    }

    //---衛教室---
    public int getcurrent_no() {
        return this.current_no;
    }

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


}

