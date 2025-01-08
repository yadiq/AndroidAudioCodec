package com.hqumath.nativedemo.media;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.hqumath.nativedemo.app.AppExecutors;

import java.util.ArrayList;
import java.util.List;


/**
 * 手机播放
 */
public class AudioPlayHelper {

    private boolean isRunning = false;//音频播放线程状态
    private List<byte[]> decodeData = new ArrayList<>();//解码数据

    private AudioTrack audioTrk;//音频播放

    public AudioPlayHelper() {
        initAudioTrack();
    }

    public void setData(List<byte[]> decodeData) {
        this.decodeData = decodeData;
    }

    public void start() {
        if (!isRunning) {
            isRunning = true;
            AppExecutors.getInstance().workThread().execute(() -> {
                //Log.d(TAG, "AudioPlay 线程开始");
                for (int i = 0; i < decodeData.size(); i++) {
                    byte[] data = decodeData.get(i);
                    if (!isRunning)
                        break;
                    try {
                        audioTrk.play();
                        audioTrk.write(data, 0, data.length);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                isRunning = false;
                //Log.d(TAG, "AudioPlay 线程结束");
            });
        }
    }

    public void stop() {
        isRunning = false;
    }

    public void close() {
        if (audioTrk != null) {
            audioTrk.release();
            audioTrk = null;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    //音频播放器初始化
    private void initAudioTrack() {
        //采样频率，每秒声音样本的次数。
        int sampleRate = 8000;
        //声道数，每个时刻有几份信息。单声道
        int channelConfig = AudioFormat.CHANNEL_OUT_MONO;
        //和录制的一样的
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        //扬声器播放
        int streamType = AudioManager.STREAM_MUSIC;
        //流模式
        int mode = AudioTrack.MODE_STREAM;
        //最小缓冲区大小
        int recBufSize = AudioTrack.getMinBufferSize(
                sampleRate,
                channelConfig,
                audioFormat);
        audioTrk = new AudioTrack(
                streamType,
                sampleRate,
                channelConfig,
                audioFormat,
                recBufSize,
                mode);
        audioTrk.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
    }
}
