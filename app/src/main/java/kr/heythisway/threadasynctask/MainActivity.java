package kr.heythisway.threadasynctask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 *
 */
public class MainActivity extends AppCompatActivity {
    TextView textView;
    public static final int SET_DONE = 1;

    // 핸들러
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_DONE:
                    setDone();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run();
            }
        });
    }

    public void run() {
        new Thread() {
            public void run() {
                try {
                    // 10초 후에
                    Thread.sleep(10000);
                    handler.sendEmptyMessage(SET_DONE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void setDone() {
        textView.setText("Done!!!");
    }
}
