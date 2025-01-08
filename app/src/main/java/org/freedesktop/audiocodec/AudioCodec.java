package org.freedesktop.audiocodec;

/**
 * ****************************************************************
 * 作    者: Created by gyd
 * 创建时间: 2023/9/5 17:45
 * 文件描述: 音频编解码
 * 注意事项:
 * ****************************************************************
 */
public class AudioCodec {
    ////////////////////////////////////native///////////////////////////////////
    static {
        System.loadLibrary("AudioCodec");
    }

    /**
     * g711编码 (编码后长度为1/2)
     *
     * @param in    输入
     * @param out   输出
     * @param inLen 输入长度
     * @param type  0:G711_A_LAW, 1:G711_U_LAW
     * @return 输出长度
     */
    public static native int g711Encode(byte[] in, byte[] out, int inLen, int type);

    /**
     * g711解码 (解码后长度为2倍)
     *
     * @param in    输入
     * @param out   输出
     * @param inLen 输入长度
     * @param type  0:G711_A_LAW, 1:G711_U_LAW
     * @return 输出长度
     */
    public static native int g711Decode(byte[] in, byte[] out, int inLen, int type);

    /**
     * g723编码 (编码后长度为1/5.3)
     *
     * @param in    输入
     * @param out   输出
     * @param inLen 输入长度
     * @return 输出长度
     */
    public static native int g723Encode(byte[] in, byte[] out, int inLen);

    /**
     * g723解码 (解码后长度为5.3倍)
     *
     * @param in    输入
     * @param out   输出
     * @param inLen 输入长度
     * @return 输出长度
     */
    public static native int g723Decode(byte[] in, byte[] out, int inLen);

}
