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
import com.hqumath.nativedemo.media.AudioPlayHelper;
import com.hqumath.nativedemo.media.AudioRecordHelper;
import com.hqumath.nativedemo.utils.ByteUtil;
import com.hqumath.nativedemo.utils.CommonUtil;

import org.freedesktop.audiocodec.AudioCodec;

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
    private List<byte[]> pcmData = new ArrayList<>();//原始数据
    private List<byte[]> encodeData = new ArrayList<>();//编码数据
    private List<byte[]> decodeData = new ArrayList<>();//解码数据

    private AudioRecordHelper audioRecordHelper;
    private AudioPlayHelper audioPlayHelper;

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
        if (audioRecordHelper != null) {
            audioRecordHelper.stop();
            audioRecordHelper.close();
            audioRecordHelper = null;
        }
        if (audioPlayHelper != null) {
            audioPlayHelper.stop();
            audioPlayHelper.close();
            audioPlayHelper = null;
        }
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
        binding.btnRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //检查录音权限
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                            binding.btnRecord.setText("录音中...");
                            //清空缓存
                            binding.tvPcm.setText("");
                            binding.tvEncode.setText("");
                            pcmData.clear();
                            encodeData.clear();
                            //开始录音
                            if (audioRecordHelper != null)
                                audioRecordHelper.start();
                        } else {
                            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        binding.btnRecord.setText("录音");
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                            if (audioRecordHelper != null)
                                audioRecordHelper.stop();
                        }
                        break;
                }
                return true;
            }
        });
        binding.btnEncode1.setOnClickListener(v -> {
            //清空缓存
            binding.tvEncode.setText("");
            encodeData.clear();
            //编码
            for (byte[] data1 : pcmData) {
                byte[] data2 = new byte[data1.length];
                int len2 = AudioCodec.g711Encode(data1, data2, data1.length, 1);
                byte[] data3 = ByteUtil.subByte(data2, 0, len2);
                encodeData.add(data3);
            }
            binding.tvEncode.setText("编码数据:\n" + ByteUtil.bytesToHexWithSpace(encodeData.get(encodeData.size() - 1)));
        });
        binding.btnPlay.setOnClickListener(v -> {
            //停止播放
            if (audioPlayHelper != null && audioPlayHelper.isRunning()) {
                audioPlayHelper.stop();
                return;
            }
            //解码
            for (byte[] data1 : encodeData) {
                byte[] data2 = new byte[data1.length * 2];
                int len2 = AudioCodec.g711Decode(data1, data2, data1.length, 1);
                byte[] data3 = ByteUtil.subByte(data2, 0, len2);
                decodeData.add(data3);
            }
            //播放
            if (audioPlayHelper != null) {
                audioPlayHelper.setData(decodeData);
                audioPlayHelper.start();
            }
        });

//        //加解密
//        binding.btnDecode.setOnClickListener(v -> {
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
//
//        });
    }

    private void initData() {
        audioRecordHelper = new AudioRecordHelper();
        audioRecordHelper.init(new AudioRecordHelper.AudioRecordListener() {
            @Override
            public void onAudioRecord(byte[] data1, int len1) {
                byte[] data2 = ByteUtil.subByte(data1, 0, len1);
                pcmData.add(data2);
                binding.getRoot().post(() -> {
                    binding.tvPcm.setText("原始数据:\n" + ByteUtil.bytesToHexWithSpace(pcmData.get(pcmData.size() - 1)));
                });
            }
        });
        audioPlayHelper = new AudioPlayHelper();
    }
}
