# Speech Codec

## 介绍
语音编解码器

ITU 推出G.7XX系列的speech codec, 目前广泛应用的有：G.711，G.723, G.726, G.729. 
每一种又有很多分支，如G.729就有G.729A, G.729B and G.729AB

介绍：https://blog.csdn.net/MyArrow/article/details/7845883
源码：https://fossies.org/dox/xmmp-0.6.1.1/g711_8c.html

## G.711
G.711就是语音模拟信号的一种非线性量化，细分有二种:G.711 A-law and G.711 u-law
性能参数：
• 采样率：8kHz
• 信息量：64kbps／channel
• 理论延迟：0.125msec
• 品质：MOS值4.10    

## G.723
G.723.1是一个双速率的语音编码器，是 ITU-T建议的应用于低速率多媒体服务中语音或其它音频信号的压缩算法

## G.726
G.726有四种码率：, 32, 24, 16 kbit/s

## G.729
G..729语音压缩编译码算法
• 采样率：8kHz
• 信息量：8kbps／channel
• 帧长：10msec
• 理论延迟：15msec
• 品质：MOS值3.9