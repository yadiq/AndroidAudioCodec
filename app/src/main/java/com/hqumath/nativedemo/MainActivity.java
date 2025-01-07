package com.hqumath.nativedemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hqumath.nativedemo.databinding.ActivityMainBinding;
import com.hqumath.nativedemo.utils.ByteUtil;
import com.hqumath.nativedemo.utils.CommonUtil;
import com.hqumath.nativedemo.utils.LogUtil;

import org.freedesktop.audiocodec.AudioCodec;
import org.freedesktop.demo.Demo;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CommonUtil.init(this);

        //网络请求
        binding.btnHttp.setOnClickListener(v -> {
            byte[] data1 = ByteUtil.hexToBytes("010203040506");
            LogUtil.d(" data1=" + ByteUtil.bytesToHexWithSpace(data1));
            byte[] data2 = new byte[data1.length];
            int len2 = AudioCodec.g711Encode(data1, data2, data1.length, 1);
            LogUtil.d("g711Encode: len=" + len2 + " data=" + ByteUtil.bytesToHexWithSpace(data2));
            byte[] data3 = new byte[len2 * 2];
            int len3 = AudioCodec.g711Decode(data2, data3, len2, 1);
            LogUtil.d("g711Decode: len=" + len3 + " data=" + ByteUtil.bytesToHexWithSpace(data3));

        });

        //加解密
        binding.btnEncrypt.setOnClickListener(v -> {
            LogUtil.d("测试日志");
//            String plaintext = "123abc";
//            String key = "12345678901234567890123456789012";
//            String iv = "1234567890123456";
//            String result = Demo.encryptTest(plaintext, key, iv);
//            binding.tv1.setText("aes256cbc加密:\n  明文: " + plaintext + "\n  密钥: " + key + "\n  偏移量: " + iv + "\n  结果: " + result + "\n更多加解密方式，见日志: \n  aes256gcm、aes256cbc、aes128ecb、\n  sha1、hmacSHA256");
        });

    }
}
