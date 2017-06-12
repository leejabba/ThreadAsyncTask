# 버튼 클릭 후 5초 후 메시지 보내기
스레드를 이용해 구현한다.

1. 스레드 구현
2. 핸들러 구현
3. 스레드 호출

## 1. 스레드 구현
Thread 클래스를 extends한 클래스를 정의한다.
새로 정의한 클래스는 반드시 run() 메서드를 오버라이드 해야한다.

run 메서드에서는 5초 동안 슬립한 후에 TextView 위젯에 setText 하면 된다. 하지만 스레드에서는 위젯을 직접 컨트롤 할 수 없기때문에 핸들러에 메시지를 보내야 한다. 

메시지를 받은 핸들러는 메인스레드에서 위젯을 컨트롤 할 수 있기 때문이다.

```java
class CustomThread extends Thread {
	// 메인에 정의된 핸들러를 받아내는 부분 구현
	Handler handler;

	// 생성자를 통해 핸들러를 받는다.
	public CustomThread (Handler handler) {
		this.handler = handler;
	}

	// run 메서드 오버라이드
	@Override
	public void run() {
		// 5초 후에 핸들러에 메시지를 보낸다.
		try {
			Thread.sleep(5000);
			handler.sendEmptyMessage(1);
		} catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
}
```

* * *

## 2. 핸들러 구현

**[MainActivity.java]**
```java
Handler handler = new Handler() {
	@Override
    public void handleMessage(Message msg) {
    	switch (msg.what) {
    		case 1:
    			textView.setText("Done!!!");
    			break;
    	}
    }
}
```

* * *

## 3. 스레드 호출

```java
CustomThread thread = new CustomThread(handler);
thread.start();
```
