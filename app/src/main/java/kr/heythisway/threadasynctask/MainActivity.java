package kr.heythisway.threadasynctask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final int SET_DONE = 1;
    TextView textView;
    ProgressDialog progress;

    // thread 에서 호출하기 위한 handler
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
                runAsync();
            }
        });
        // 화면에 진행상태를 표시
        // 프로그래스 다이얼로 정의
        progress = new ProgressDialog(this);
        progress.setTitle("진행중...");
        progress.setMessage("진행중 표시되는 메시지입니다");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void setDone() {
        textView.setText("Done!!!");
        // 프로그래스 창을 해제
        progress.dismiss();
    }

    private void runAsync(){

        new AsyncTask<String, Integer, Float>(){
            // 제네릭 타입 1 - doInBackground 의 인자
            //            2 - onProgressUpdate 의 인자
            //            3 - doInBackground 의 리턴타입 ,
            // doInBackground 가 호출되기 전에 먼저 호출된다.
            @Override
            protected void onPreExecute() {
                progress.show();
            }

            @Override
            protected Float doInBackground(String... params) { // 데이터를 처리...
                // 10초 후에
                try {
                    for(int i=0 ; i<10 ; i++) {
                        publishProgress(i*10); // <- onProgressUpdate 를 주기적으로 업데이트 해준다.
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 1000.4f;
            }

            // doInBackground 가 처리되고 나서 호출된다.
            @Override
            protected void onPostExecute(Float result) {
                Log.e("AsyncTask","doInBackground의 결과값="+result);
                // 결과값을 메인 UI 에 세팅하는 로직을 여기에 작성한다.
                setDone();
                progress.dismiss();
            }

            // 주기적으로 doInBackground 에서 호출이 가능한 함수
            @Override
            protected void onProgressUpdate(Integer... values) {
                progress.setMessage("진행율 = " +values[0]+ "%");
            }
        }.execute("안녕", "하세요"); // <- doInBackground 를 실행

    }

    private void runThread() {
        // 프로그래스 창을 호출
        progress.show();
        CustomThread thread = new CustomThread(handler);
        thread.start();
    }
}

class CustomThread extends Thread {
    Handler handler;
    public CustomThread(Handler handler){
        this.handler = handler;
    }

    @Override
    public void run() {
        // 10초 후에
        try {
            Thread.sleep(10000);
            // Main UI 에 현재 thread 가 접근할 수 없으므로
            // handler 를 통해 호출해준다.
            handler.sendEmptyMessage(MainActivity.SET_DONE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}