#include <string.h>
#include <jni.h>
#include <android/log.h>

#include <stdlib.h>
#include "codec/g72x.h"

//打印日志 LOGI("failed to open file: %s %d", "string", 0);
#define  LOG_TAG    "JNI_log"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)


/**
 * g711编码
 * @param env
 * @param thiz
 * @param in 输入
 * @param out 输出
 * @param inLen 输入长度
 * @param type 0:G711_A_LAW, 1:G711_U_LAW
 * @return 输出长度
 */
JNIEXPORT jint JNICALL
Java_org_freedesktop_audiocodec_AudioCodec_g711Encode(JNIEnv *env, jobject thiz, jbyteArray in,
                                                      jbyteArray out, jint inLen, jint type) {
    jbyte *cin = (*env)->GetByteArrayElements(env, in, NULL);
    jbyte *cout = (*env)->GetByteArrayElements(env, out, NULL);
    if (cin == NULL || cout == NULL) {
        return 0;
    }
    int len = g711_encode(cin, cout, inLen, type);

    (*env)->ReleaseByteArrayElements(env, in, cin, 0);
    (*env)->ReleaseByteArrayElements(env, out, cout, 0);
    return len;
}

/**
 * g711解码
 * @param env
 * @param thiz
 * @param in 输入
 * @param out 输出
 * @param inLen 输入长度
 * @param type 0:G711_A_LAW, 1:G711_U_LAW
 * @return 输出长度
 */
JNIEXPORT jint JNICALL
Java_org_freedesktop_audiocodec_AudioCodec_g711Decode(JNIEnv *env, jobject thiz, jbyteArray in,
                                                        jbyteArray out, jint inLen, jint type) {
    jbyte *cin = (*env)->GetByteArrayElements(env, in, NULL);
    jbyte *cout = (*env)->GetByteArrayElements(env, out, NULL);
    if (cin == NULL || cout == NULL) {
        return 0;
    }
    int len = g711_decode(cin, cout, inLen, type);

    (*env)->ReleaseByteArrayElements(env, in, cin, 0);
    (*env)->ReleaseByteArrayElements(env, out, cout, 0);
    return len;
}

/*JNIEXPORT void JNICALL
Java_org_freedesktop_audiocodec_AudioCodec_g723EncodeInit(JNIEnv *env, jobject thiz) {
    g72x_init_encode_state();
}

JNIEXPORT void JNICALL
Java_org_freedesktop_audiocodec_AudioCodec_g723DecodeInit(JNIEnv *env, jobject thiz) {
    g72x_init_decode_state();
}*/

/**
 * g723编码
 * @param env
 * @param thiz
 * @param in 输入
 * @param out 输出
 * @param inLen 输入长度
 * @return 输出长度
 */
JNIEXPORT jint JNICALL
Java_org_freedesktop_audiocodec_AudioCodec_g723Encode(JNIEnv *env, jobject thiz, jbyteArray in,
                                                      jbyteArray out, jint inLen) {
    jbyte *cin = (*env)->GetByteArrayElements(env, in, NULL);
    jbyte *cout = (*env)->GetByteArrayElements(env, out, NULL);
    if (cin == NULL || cout == NULL) {
        return 0;
    }
    int len = g723_encode(cin, cout, inLen);

    (*env)->ReleaseByteArrayElements(env, in, cin, 0);
    (*env)->ReleaseByteArrayElements(env, out, cout, 0);
    return len;
}

/**
 * g723解码
 * @param env
 * @param thiz
 * @param in 输入
 * @param out 输出
 * @param inLen 输入长度
 * @return 输出长度
 */
JNIEXPORT jint JNICALL
Java_org_freedesktop_audiocodec_AudioCodec_g723Decode(JNIEnv *env, jobject thiz, jbyteArray in,
                                                      jbyteArray out, jint inLen) {
    jbyte *cin = (*env)->GetByteArrayElements(env, in, NULL);
    jbyte *cout = (*env)->GetByteArrayElements(env, out, NULL);
    if (cin == NULL || cout == NULL) {
        return 0;
    }
    int len = g723_decode(cin, cout, inLen);

    (*env)->ReleaseByteArrayElements(env, in, cin, 0);
    (*env)->ReleaseByteArrayElements(env, out, cout, 0);
    return len;
}