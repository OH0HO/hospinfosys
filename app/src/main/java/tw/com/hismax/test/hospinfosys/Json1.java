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


public class Json1 extends Thread {

    HttpClient client;
    String result = "";
    int chart_no = 0;
    String pt_name;
    String birth_date;
    JSONArray JArray = new JSONArray();
    JSONObject jsonObj = new JSONObject();

    public Json1(String id_no, String registration_id) {

        try {

            jsonObj.put("id_no", id_no);
            Log.e("id_no : ", id_no);
            jsonObj.put("registration_id", registration_id);
            jsonObj.put("eff_flag", "Y");
            Log.e("id_no", id_no.toString());  //你弄出的JSonArray轉成字串
//將JsonObject put進去JsonArray

            JArray.put(jsonObj);
            StringEntity se = new StringEntity(JArray.toString());

            Log.e("你弄的JsonArray", JArray.toString());  //你弄出的JSonArray轉成字串

            client = new DefaultHttpClient();
            HttpPost httpost = new HttpPost("http://61.219.152.220/rest/device_register/");
            httpost.setEntity(se);

            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");

            HttpResponse responsePOST = client.execute(httpost);
            HttpEntity resEntity = responsePOST.getEntity();

            if (resEntity != null) {
                this.result = EntityUtils.toString(resEntity);
            }

            this.chart_no = new JSONObject(this.result).getInt("chart_no");
            this.birth_date = new JSONObject(this.result).getString("birth_date");
            this.pt_name = new JSONObject(this.result).getString("pt_name");
            this.pt_name = new String(pt_name.getBytes("ISO-8859-1"), "UTF-8");   //亂碼變中文

            Log.e("chart_no  + 4birth_date", chart_no + birth_date.toString());  //你弄出的JSonArray轉成字串

            Log.e("chart_no+birth_date", "chart_no" + "birth_date");

            Log.e("chart_no", chart_no + pt_name.toString());


            /*writeStream(outputPost);
            outputPost.flush();
            outputPost.close();

            client.setFixedLengthStreamingMode(outputPost.getBytes().length);
            client.setChunkedStreamingMode(0);*/


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

    //回傳值到MainActivit
    public String getString() {
        return this.result;
    }

    public String getpt_name() {
        return this.pt_name;
    }

    public int getchart_no() {
        return this.chart_no;
    }

    public String getbirth_date() {
        return this.birth_date;
    }

}




