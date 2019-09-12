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


public class Json_BeaconGet_line_list extends Thread {
    private String result = "";
    String _status = "";
    JSONArray JArray = new JSONArray();
    JSONObject jsonObj = new JSONObject();
    String test = "";
    private boolean haveData = false;
    String url = "";

    int seq_no_1, seq_no_2, seq_no_3, seq_no_4 = 0;
    private String pt_name_1, pt_name_2, pt_name_3, pt_name_4;
    int i;
    int list_fin_1 = 0, list_fin_2 = 0, list_fin_3 = 0, list_fin_4 = 0;
    String status_1 = "", status_2 = "", status_3 = "", status_4 = "";
    int list_c; //等於 C 時 i 的值
    int diff_valu;// 等於 * 的 i 值 和等於 C 時 i 的差距
    int test_self, test_now;

    public Json_BeaconGet_line_list(String chart_no, String location_code, String _processType) {
//        InputStream is = null;
//        String result = "";
//        //若線上資料為陣列，則使用JSONArray
//        JSONArray jsonArray = null;
//        //若線上資料為單筆資料，則使用JSONObject
//        JSONObject jsonObj = null;

        if (!_processType.equals("REG")) {

            // 透過HTTP連線取得回應
            try {
                Log.d("BEN", "Json_BeaconGet＿line_list Start !!");
                jsonObj.put("", test);
                JArray.put(jsonObj);
                StringEntity se = new StringEntity(JArray.toString());

                HttpClient client = new DefaultHttpClient(); // for port 80 requests!
                Log.d("BEN", "chart_no = " + chart_no);
                url = "http://61.219.152.220/rest/getDeptList/get?location_code=" + location_code + "&chart_no=" + chart_no;
                // - - 衛教室排隊清單 - -
                Log.d("BEN", "url = " + url);
                HttpGet httpget = new HttpGet(url);
                //httpget.setEntity(se);

                HttpResponse response = client.execute(httpget);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    this.result = EntityUtils.toString(entity);
                    Log.d("BEN", "Json_BeaconGet＿line_list = " + result.toString());
                    if (result.equals("\"\"")) {
                        //有資料但己處理過
                        haveData = true;

                    } else {
                        this._status = new JSONArray(this.result).getJSONObject(0).getString("_status");
                        Log.d("Ben", "Json_BeaconGet＿line_list status= " + this._status);
                        if (_status.equals("success")) {
                            haveData = true;
                            JSONArray dataArray = new JSONArray(result);
                            int[] seq_no = new int[dataArray.length()];
                            String[] pt_name = new String[dataArray.length()];
                            String[] eff_flag = new String[dataArray.length()];
                            String[] mark = new String[dataArray.length()];
                            String[] _status = new String[dataArray.length()];
                            String[] _status_doc = new String[dataArray.length()];

                            //因為data陣列裡面有好多個JSON物件，因此利用for迴圈來將資料抓取出來
                            //因為不知道data陣列裡有多少物件，因此我們用.length()這個方法來取得物件的數量
                            for (i = 0; i < dataArray.length(); i++) {
                                //接下來這兩行在做同一件事情，就是把剛剛JSON陣列裡的物件抓取出來
                                //並取得裡面的字串資料
                                //dataArray.getJSONObject(i)這段是在講抓取data陣列裡的第i個JSON物件
                                //抓取到JSON物件之後再利用.getString(“欄位名稱”)來取得該項value
                                //這裡的欄位名稱就是剛剛前面所提到的name:value的name
                                seq_no[i] = dataArray.getJSONObject(i).getInt("seq_no");
                                pt_name[i] = dataArray.getJSONObject(i).getString("pt_name");
                                eff_flag[i] = dataArray.getJSONObject(i).getString("eff_flag");
                                _status[i] = dataArray.getJSONObject(i).getString("_status");
                                _status_doc[i] = dataArray.getJSONObject(i).getString("_status_doc");
                                mark[i] = dataArray.getJSONObject(i).getString("mark");

                                if (mark[i].equals("*")) {
                                    test_self = i;
                                }
                                if (mark[i].equals("C")) {
                                    test_now = i;
                                }

                                Log.d("i等於", "i" + String.valueOf(i));
                            }
                            Log.d("i 值分別等於多少"," test_self " + test_self + " test_now " + test_now);


                            list_fin_1 = test_self;
                            list_fin_2 = test_self - 1;
                            list_fin_3 = test_self - 2;
                            list_fin_4 = test_self - 3;

                            this.seq_no_1 = new JSONArray(this.result).getJSONObject(list_fin_4).getInt("seq_no");
                            this.pt_name_1 = new JSONArray(this.result).getJSONObject(list_fin_4).getString("pt_name");
                            this.pt_name_1 = new String(pt_name_1.getBytes("ISO-8859-1"), "UTF-8");

                            this.seq_no_2 = new JSONArray(this.result).getJSONObject(list_fin_3).getInt("seq_no");
                            this.pt_name_2 = new JSONArray(this.result).getJSONObject(list_fin_3).getString("pt_name");
                            this.pt_name_2 = new String(pt_name_2.getBytes("ISO-8859-1"), "UTF-8");

                            this.seq_no_3 = new JSONArray(this.result).getJSONObject(list_fin_2).getInt("seq_no");
                            this.pt_name_3 = new JSONArray(this.result).getJSONObject(list_fin_2).getString("pt_name");
                            this.pt_name_3 = new String(pt_name_3.getBytes("ISO-8859-1"), "UTF-8");

                            this.seq_no_4 = new JSONArray(this.result).getJSONObject(list_fin_1).getInt("seq_no");
                            this.pt_name_4 = new JSONArray(this.result).getJSONObject(list_fin_1).getString("pt_name");
                            this.pt_name_4 = new String(pt_name_4.getBytes("ISO-8859-1"), "UTF-8");
                            Log.d("list_fin_1", "list_fin_1 : " + list_fin_1 + "i : " + String.valueOf(i));

                            //判斷目前的服務的號碼和自己號碼差多少
                            diff_valu = test_self - test_now; //自己 - 目前
                            Log.d("diff_valu", "diff_valu的值: " + diff_valu + " list_c " + list_c + " 123 " + list_fin_1);
                            switch (diff_valu) {
                                case 0:
                                    status_1 = "已看完";
                                    status_2 = "已看完";
                                    status_3 = "已看完";
                                    status_4 = "看診中";
                                    break;
                                case 1:
                                    status_1 = "已看完";
                                    status_2 = "已看完";
                                    status_3 = "看診中";
                                    status_4 = "未看";
                                    break;
                                case 2:
                                    status_1 = "已看完";
                                    status_2 = "看診中";
                                    status_3 = "未看";
                                    status_4 = "未看";
                                    break;
                                case 3:
                                    status_1 = "看診中";
                                    status_2 = "未看";
                                    status_3 = "未看";
                                    status_4 = "未看";
                                    break;
                                default:
                                    status_1 = "未看";
                                    status_2 = "未看";
                                    status_3 = "未看";
                                    status_4 = "未看";
                                    break;
                            }
                            Log.d("status 看診狀況", "123" + status_1 + status_2 + status_3 + status_4);

                            Log.d("i等於2", "i" + String.valueOf(i));

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
                //client.disconnect();
            }
        }
    }

    //回傳值到Setting

    public int getseq_no_1() {
        return seq_no_1;
    }

    public String getpt_name_1() {
        return pt_name_1;
    }

    public int getseq_no_2() {
        return seq_no_2;
    }

    public String getpt_name_2() {
        return pt_name_2;
    }

    public int getseq_no_3() {
        return seq_no_3;
    }

    public String getpt_name_3() {
        return pt_name_3;
    }

    public int getseq_no_4() {
        return seq_no_4;
    }

    public String getpt_name_4() {
        return pt_name_4;
    }

    public String getstatus_1() {
        return status_1;
    }

    public String getstatus_2() {
        return status_2;
    }

    public String getstatus_3() {
        return status_3;
    }

    public String getstatus_4() {
        return status_4;
    }

}

