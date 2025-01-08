package com.hqumath.nativedemo.media;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.hqumath.nativedemo.app.AppExecutors;

/**
 * 手机拾音
 */
public class AudioRecordHelper {
    public static final String TAG = "AudioRecordHelper";

    private boolean isRunning = false;//音频录制线程状态
    private int minBufferSize;//最小缓冲区大小
    private byte[] receiveData;//每次收到的数据240B

    private AudioRecord audioRec;//录音机
    private AudioRecordListener listener;

    public void init(AudioRecordListener listener) {
        this.listener = listener;
        initAudioRecord();
    }

    public void start() {
        //未授予录音权限，无法播放
        try {
            if (audioRec != null)
                audioRec.startRecording();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (!isRunning) {
            isRunning = true;
            AppExecutors.getInstance().workThread().execute(() -> {
                //Log.d(TAG, "Record 线程开始");
                while (isRunning) {
                    //读取
                    int reader = audioRec.read(receiveData, 0, receiveData.length);
                    if (reader <= 0) continue;
                    if (listener != null)
                        listener.onAudioRecord(receiveData, reader);
                }
                //Log.d(TAG, "Record 线程结束");
            });
        }
    }

    public void stop() {
        try {
            if (audioRec != null)
                audioRec.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        isRunning = false;
    }

    public void close() {
        if (audioRec != null) {
            audioRec.release();
            audioRec = null;
        }
    }

    //音频采集
    private void initAudioRecord() {
        //采样频率，每秒声音样本的次数。
        int sampleRate = 8000;
        //声道数，每个时刻有几份信息。单声道
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        //采样位数，将样本幅度量化，类似声卡的分辨率。
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        //最小缓冲区大小
        minBufferSize = AudioRecord.getMinBufferSize(
                sampleRate,
                channelConfig,
                audioFormat);
        receiveData = new byte[minBufferSize];//每次收到的数据
        //录音机
        audioRec = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelConfig,
                audioFormat,
                minBufferSize);
        /*//声学回声消除器 AcousticEchoCanceler 消除了从远程捕捉到音频信号上的信号的作用
        if (AcousticEchoCanceler.isAvailable()) {
            AcousticEchoCanceler aec = AcousticEchoCanceler.create(audioRec.getAudioSessionId());
            if (aec != null) {
                aec.setEnabled(true);
            }
        }
        //自动增益控制 AutomaticGainControl 自动恢复正常捕获的信号输出
        if (AutomaticGainControl.isAvailable()) {
            AutomaticGainControl agc = AutomaticGainControl.create(audioRec.getAudioSessionId());
            if (agc != null) {
                agc.setEnabled(true);
            }
        }
        //噪声抑制器 NoiseSuppressor 可以消除被捕获信号的背景噪音
        if (NoiseSuppressor.isAvailable()) {
            NoiseSuppressor nc = NoiseSuppressor.create(audioRec.getAudioSessionId());
            if (nc != null) {
                nc.setEnabled(true);
            }
        }*/
    }

    public interface AudioRecordListener {
        void onAudioRecord(byte[] data, int length);
    }
}
