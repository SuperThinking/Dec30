package straightforwardapps.dec30;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Permission;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    int y;
    String[] x2;
    ListView tv;
    WebView wv;
    TextView snack_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        snack_layout = (TextView) findViewById(R.id.snack_layout);
        tv = (ListView) findViewById(R.id.tv);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        btask x = new btask();
        x.execute("https://www.gadgetsnow.com/tech-news");
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied to use Internet", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////

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

                Elements e = document.select("span");
                e = e.attr("class", "w_tle");
                String s = e.html();
                document = Jsoup.parse(s);
                e = document.select("a");
                List<String> ss = e.eachAttr("href");
                x2 = ss.toArray(new String[0]);
                return e.html();
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
            snack_layout.setVisibility(View.GONE);
            //Snackbar.make(snack_layout, s, Snackbar.LENGTH_SHORT).show();

            String[] x = s.split("\n");
            final String[] x1 = new String[x.length-31];
            for(int i=0; i<x.length-31; i++)
            {
                x1[i] = (i+1)+"]\t"+x[i];
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_custom, x1);
            tv.setAdapter(arrayAdapter);

            tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                    view.setSelected(true);
                    Intent i = new Intent(MainActivity.this, MainActivity2.class);
                    i.putExtra("link", x2[position]);
                    startActivity(i);
                }
            });
        }
    }



}
