package com.hqumath.nativedemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hqumath.nativedemo.databinding.ActivityMainBinding;
import com.hqumath.nativedemo.utils.ByteUtil;
import com.hqumath.nativedemo.utils.CommonUtil;
import com.hqumath.nativedemo.utils.LogUtil;

import org.freedesktop.audiocodec.AudioCodec;
import org.freedesktop.demo.Demo;

import java.util.ArrayList;
import java.util.List;

/**
 * ****************************************************************
 * 作    者: Created by gyd
 * 创建时间: 2023/9/5 16:24
 * 文件描述:
 * 注意事项:
 * ****************************************************************
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final String TAG = "Native";
    private final int REQUEST_RECORD_AUDIO = 0x01;//请求录音权限
    private Activity mContext;
    private List<byte[]> audioData = new ArrayList<>();

    private AudioRecordHelper audioRecordHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CommonUtil.init(this);
        //事件监听
        initListener();
        //初始化数据
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    CommonUtil.toast("录音权限被您拒绝，请您到设置页面手动授权");
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        //录音并编码
        binding.btnEncode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //检查录音权限
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                            //清空缓存
                            audioData.clear();
                            //开始录音
                            audioRecordHelper.start();
                        } else {
                            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                            audioRecordHelper.stop();
                        }
                        break;
                }
                return true;
            }
        });
//        binding.btnEncode.setOnClickListener(v -> {
//            //检查录音权限
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
//
//
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
//            }
//
////            byte[] data1 = ByteUtil.hexToBytes("010203040506010203040506010203040506");
////            LogUtil.d(" data1=" + ByteUtil.bytesToHexWithSpace(data1));
////            byte[] data2 = new byte[data1.length];
////            int len2 = AudioCodec.g711Encode(data1, data2, data1.length, 1);
////            LogUtil.d("g711Encode: len=" + len2 + " data=" + ByteUtil.bytesToHexWithSpace(data2));
////            byte[] data3 = new byte[len2 * 2];
////            int len3 = AudioCodec.g711Decode(data2, data3, len2, 1);
////            LogUtil.d("g711Decode: len=" + len3 + " data=" + ByteUtil.bytesToHexWithSpace(data3));
//
//        });

        //加解密
        binding.btnDecode.setOnClickListener(v -> {
//            byte[] data = new byte[240];//每次收到的数据240B
//            byte[] encodeData = new byte[240];//编码后的数据240B
//            byte[] decodeData = new byte[1600];//解码后大小480B
//
//            int encodeLen = AudioCodec.g711Encode(data, encodeData, data.length, 1);
//            int decodeLen = AudioCodec.g711Decode(data, decodeData, data.length, 1);
//            binding.tvResult.setText("原始长度=" + data.length + "\n编码后长度=" + encodeLen + "\n解码后长度=" + decodeLen);
//
//            int encodeLen = AudioCodec.g723Encode(data, encodeData, data.length);
//            int decodeLen = AudioCodec.g723Decode(data, decodeData, data.length);
//            binding.tvResult.setText("原始长度=" + data.length + "\n编码后长度=" + encodeLen + "\n解码后长度=" + decodeLen);

        });
    }

    private void initData() {
        audioRecordHelper = new AudioRecordHelper();
        audioRecordHelper.init(new AudioRecordHelper.AudioRecordListener() {
            @Override
            public void onAudioRecord(byte[] data1, int len1) {
                //byte[] data1 = ByteUtil.subByte(data, 0, length);
                //编码
                byte[] data2 = new byte[len1];
                int len2 = AudioCodec.g711Encode(data1, data2, len1, 1);
                data2 = ByteUtil.subByte(data2, 0, len2);
                //存入列表
                audioData.add(data2);
            }
        });
    }



}
