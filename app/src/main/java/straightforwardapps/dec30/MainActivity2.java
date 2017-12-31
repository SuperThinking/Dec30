package straightforwardapps.dec30;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity2 extends AppCompatActivity {


    TextView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        wv = (TextView) findViewById(R.id.wv);
        String link = getIntent().getStringExtra("link");
        btask x = new btask();
        x.execute("https://www.gadgetsnow.com/tech-news"+link);
    }


    public class btask extends AsyncTask<String, String[], String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.connect();
                String result = "";
                InputStream in = new BufferedInputStream(conn.getInputStream());
                if (in != null)
                {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    String line = "";

                    while ((line = bufferedReader.readLine()) != null)
                    {
                        result += line;
                    }
                }

                Document document = Jsoup.connect(strings[0])
                        .header("Accept-Encoding", "gzip, deflate")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                        .maxBodySize(0)
                        .timeout(600000)
                        .get();

                Elements e = document.select("meta");
                List<String> e1 = e.eachAttr("content");
                String[] x = e1.toArray(new String[0]);
                return x[2];
            }


            catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Bhai, Internet Chalu Karne Ka Kya Loge?";
        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            wv.setText(s);

        }
    }
}
