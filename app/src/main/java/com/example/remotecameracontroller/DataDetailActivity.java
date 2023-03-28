package com.example.remotecameracontroller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import network_resurce.ModbusGetInfo;
import network_resurce.ModbusInfoConnection;
import network_resurce.OnvifPTZControllerConnection;

public class DataDetailActivity extends Activity {
    public final static String TAG = "RemoteCameraConrtoller";
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 1;
    private WebView webView = null;
    private Uri uri1;//파일 재생 위치 : String, Uri, FileDescriptor, AssetFileDescriptor 지원 함
    private Uri uri2;//mod url
    private static final boolean USE_TEXTURE_VIEW = false;//API 24 이상 android.view.SurfaceView 사용권장/USE_TEXTURE_VIEW 가 true = android.view.TextureView
    private static final boolean ENABLE_SUBTITLES = true;// ENABLE_SUBTITLES가 true이면 자막 ON
    private VLCVideoLayout mVideoLayout = null;// 비디오 레이아웃
    private LibVLC mLibVLC = null;// LibVLC 클래스
    private MediaPlayer mMediaPlayer = null;// 미디어 컨트롤러
    Timer timer;


    private void requestReadExternalStoragePermission() {// 퍼미션 요청 함수
        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
    }

    @Override// 퍼미션 결과 콜백 함수
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE_PERMISSION) {
            if (permissions.length != 1 || grantResults.length != 1 ||
                    grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "external storage read permission not granted.");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_detail);
        Intent intent = getIntent();
        uri1 = Uri.parse(intent.getStringExtra("url"));
        uri2 = Uri.parse(intent.getStringExtra("modUrl"));
        System.out.println("------------------------------------------------"+uri2);

        //퍼미션 체크하기
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (savedInstanceState == null)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                requestReadExternalStoragePermission();//퍼미션 요청 하기

        // VLC 옵션
        final ArrayList<String> args = new ArrayList<>();
        args.add("-vv");
        mLibVLC = new LibVLC(this, args);// LibVLC 클래스 생성
        mMediaPlayer = new MediaPlayer(mLibVLC);// 미디어 컨트롤러 생성
        mVideoLayout = findViewById(R.id.video_layout);// 비디오 재생 레이아웃

        //webview 영역
        webView = findViewById(R.id.webview);
        webView.getSettings().setDomStorageEnabled(true);// 로컬 스토리지 (localStorage) 사용여부
        webView.setWebViewClient(new WebViewClient());  // 새 창 띄우기 않기
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setLoadWithOverviewMode(true);  // WebView 화면크기에 맞추도록 설정 - setUseWideViewPort 와 같이 써야함
        webView.getSettings().setUseWideViewPort(true);  // wide viewport 설정 - setLoadWithOverviewMode 와 같이 써야함
        webView.getSettings().setJavaScriptEnabled(true); // 자바스크립트 사용여부
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attachViews: 비디오 레이아웃 View 에 연결 // surfaceFrame(VLCVideoLayout): VLCVideoLayout 비디오가 출력될 레이아웃 변수 // dm(DisplayManager): 렌더링 전환용 변수 옵션 // subtitles(boolean): 자막 활성/비활성 // textureView(boolean): View 선택
        mMediaPlayer.attachViews(mVideoLayout, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW);
        //Media 미디어 로드 // ILibVLC(ILibVLC): LibVLC 클래스 변수 // path(String, Uri, FileDescriptor, AssetFileDescriptor): 미디어 객체
        final Media media = new Media(mLibVLC, uri1);
        mMediaPlayer.setMedia(media);// 미디어 컨트롤러 클래스에 미디어 적용
        media.release();
        mMediaPlayer.play();// 재생 시작
        webView.loadUrl(new UrlList().controllerWebPageUrl());//웹페이지 호출

        new ModbusInfoConnection(uri2.toString());
        new OnvifPTZControllerConnection(uri1.toString());//webview 조작 연결/ post로 선택된 ip 카메라의 rtsp보내기
        try {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    new ModbusGetInfo();
                }
            }, 0, 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();// 미디어 컨트롤러 제거
        mLibVLC.release();// VLC 제거
        timer.cancel(); //webview Get 정지
        webView.destroy();//webview 종료
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.stop();// 재생 중지
        mMediaPlayer.detachViews();// 연결된 View 제거
    }
}
