package tw.com.hismax.test.hospinfosys;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class Json_BeaconSet extends Thread {
    private String result = "";
    int view_no = 0;
    int seq_no_1, seq_no_2, seq_no_3, seq_no_4= 0;
    private String _status_doc ,pt_name_1,pt_name_2,pt_name_3,pt_name_4;
    private String _status = "";
    private String exceptViewTime="";

    JSONArray JArray = new JSONArray();
    JSONObject jsonObj = new JSONObject();
    String test = "";
    private boolean haveData = false;
    String url = "";


    public Json_BeaconSet(String inpData) {
//        //若線上資料為陣列，則使用JSONArray
//        JSONArray jsonArray = null;
//        //若線上資料為單筆資料，則使用JSONObject
//        JSONObject jsonObj = null;


        // 透過HTTP連線取得回應
        try {
            Log.d("BEN", "Json_BeaconSet Start !!");
            //***Ben: ?
            jsonObj.put("", test);
            JArray.put(jsonObj);

            HttpClient client = new DefaultHttpClient(); // for port 80 requests!
            Log.d("BEN", "input data=" + inpData);
            url = "http://61.219.152.220/rest/receiver_beacon/set";
            Log.d("BEN", "url = " + url);
            HttpPost httpost = new HttpPost(url);
            StringEntity se = new StringEntity(inpData);
            httpost.setEntity(se);

            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");
            HttpResponse responsePOST = client.execute(httpost);

            HttpEntity resEntity = responsePOST.getEntity();

            if (resEntity != null) {
                this.result = EntityUtils.toString(resEntity);
                Log.d("BEN", "Json_BeaconSet = " + result.toString());
                if (result.equals("")) {
                    haveData =false;
                } else {
                    this._status = new JSONArray(this.result).getJSONObject(0).getString("_status");
                    this._status_doc = new JSONArray(this.result).getJSONObject(0).getString("_status_doc");
                    Log.d("Ben", "json_BeaconSet status= " + this._status);
                    if (_status.equals("success")) {
                        haveData = true;

                        switch (_status_doc) {
                            case "REG": // 掛號門口
                                this.view_no = new JSONArray(this.result).getJSONObject(0).getInt("view_no");
                                this.exceptViewTime = new JSONArray(this.result).getJSONObject(0).getString("except_view_time");
                                Log.d("Json_BeaconSet","result : " + this.result);
                                break;

                            case "PRE_DEPT_SCHEDULE": // 衛教室
                                this.seq_no_1 = new JSONArray(this.result).getJSONObject(0).getInt("seq_no");
                                this.pt_name_1 = new JSONArray(this.result).getJSONObject(0).getString("pt_name");
                                this.pt_name_1 = new String(pt_name_1.getBytes("ISO-8859-1"), "UTF-8");

                                this.seq_no_2 = new JSONArray(this.result).getJSONObject(1).getInt("seq_no");
                                this.pt_name_2 = new JSONArray(this.result).getJSONObject(1).getString("pt_name");
                                this.pt_name_2 = new String(pt_name_2.getBytes("ISO-8859-1"), "UTF-8");

                                this.seq_no_3 = new JSONArray(this.result).getJSONObject(2).getInt("seq_no");
                                this.pt_name_3 = new JSONArray(this.result).getJSONObject(2).getString("pt_name");
                                this.pt_name_3 = new String(pt_name_3.getBytes("ISO-8859-1"), "UTF-8");

                                this.seq_no_4 = new JSONArray(this.result).getJSONObject(3).getInt("seq_no");
                                this.pt_name_4 = new JSONArray(this.result).getJSONObject(3).getString("pt_name");
                                this.pt_name_4 = new String(pt_name_4.getBytes("ISO-8859-1"), "UTF-8");
                                Log.d("Json_BeaconSet 衛教室","pt_name : " + this.pt_name_1+ this.pt_name_2+ this.pt_name_3+ this.pt_name_4);
                                break;

                            case "DEPT_SCHEDULE": // 檢驗室
                                this.seq_no_1 = new JSONArray(this.result).getJSONObject(0).getInt("seq_no");
                                this.pt_name_1 = new JSONArray(this.result).getJSONObject(0).getString("pt_name");
                                this.pt_name_1 = new String(pt_name_1.getBytes("ISO-8859-1"), "UTF-8");

                                this.seq_no_2 = new JSONArray(this.result).getJSONObject(1).getInt("seq_no");
                                this.pt_name_2 = new JSONArray(this.result).getJSONObject(1).getString("pt_name");
                                this.pt_name_2 = new String(pt_name_2.getBytes("ISO-8859-1"), "UTF-8");

                                this.seq_no_3 = new JSONArray(this.result).getJSONObject(2).getInt("seq_no");
                                this.pt_name_3 = new JSONArray(this.result).getJSONObject(2).getString("pt_name");
                                this.pt_name_3 = new String(pt_name_3.getBytes("ISO-8859-1"), "UTF-8");

                                this.seq_no_4 = new JSONArray(this.result).getJSONObject(3).getInt("seq_no");
                                this.pt_name_4 = new JSONArray(this.result).getJSONObject(3).getString("pt_name");
                                this.pt_name_4 = new String(pt_name_4.getBytes("ISO-8859-1"), "UTF-8");
                                Log.d("Json_BeaconSet 檢驗室","pt_name : " + this.pt_name_1+ this.pt_name_2+ this.pt_name_3+ this.pt_name_4);
                                break;

                        }


                    } else {
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
        }
    }


    public String getResult() {
        return result;
    }

    public String getExceptViewTime() {
        return exceptViewTime;
    }

}

