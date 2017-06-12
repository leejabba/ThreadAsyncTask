package kr.heythisway.threadasynctask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import static kr.heythisway.threadasynctask.MainActivity.SET_DONE;

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
                CustomThread thread = new CustomThread(handler);
                thread.start();
            }
        });
    }


    private void setDone() {
        textView.setText("Done!!!");
    }
}

// CustomThread 클래스 정의
class CustomThread extends Thread {
    Handler handler;

    CustomThread(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            handler.sendEmptyMessage(SET_DONE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}