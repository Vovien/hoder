package com.holderzone.intelligencepos.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by tcw on 2017/6/16.
 */

public class BaiduTTS implements SpeechSynthesizerListener {

    /**
     * 单例
     */
    private static BaiduTTS sInstance;

    /**
     * 实例
     */
    private SpeechSynthesizer mSpeechSynthesizer;

    /**
     * 资源存放路径
     */
    private static final String SAMPLE_DIR_NAME = "baiduTTS";

    /**
     * 声学模型文件 男声 中文
     */
    private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";

    /**
     * 声学模型文件 女声 中文
     */
    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";

    /**
     * 文本模型文件 中文
     */
    private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";

    /**
     * 声学模型文件 男声 英文
     */
    private static final String ENGLISH_SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male_en.dat";

    /**
     * 声学模型文件 女声 英文
     */
    private static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";

    /**
     * 文本模型文件 英文
     */
    private static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 资源文件路径（包括模型文件和License）
     */
    private String mSampleDirPath;

    /**
     * 是否初始化成功
     */
    private boolean mInitialSucceed = true;

    public static BaiduTTS getInstance() {
        if (sInstance == null) {
            synchronized (BaiduTTS.class) {
                if (sInstance == null) {
                    sInstance = new BaiduTTS();
                }
            }
        }
        return sInstance;
    }

    private BaiduTTS() {
    }

    private MediaPlayer mediaPlayer;

    public void init(Context context) {
        mContext = context;
        mediaPlayer = MediaPlayer.create(mContext, R.raw.dingdong);

        initialEnv();

        initialTts();
    }

    private void initialEnv() {
        if (mSampleDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString();
            mSampleDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME;
        }
        makeDir(mSampleDirPath);
        copyFromAssetsToSdcard(false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + ENGLISH_SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath + "/" + ENGLISH_TEXT_MODEL_NAME);
    }

    private void initialTts() {
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(mContext);
        mSpeechSynthesizer.setSpeechSynthesizerListener(this);
        // 文本模型文件路径 (离线引擎使用)
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/"
                + TEXT_MODEL_NAME);
        // 声学模型文件路径 (离线引擎使用)
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/"
                + SPEECH_FEMALE_MODEL_NAME);
        // 请替换为语音开发者平台上注册应用得到的App ID (离线授权)
        mSpeechSynthesizer.setAppId(Config.BAIDU_TTS_APP_ID/*这里只是为了让Demo运行使用的APPID,请替换成自己的id。*/);
//        // 请替换为语音开发者平台注册应用得到的apikey和secretkey (在线授权)
//        mSpeechSynthesizer.setApiKey(MY_API_KEY,
//                MY_SECRET_KEY/*这里只是为了让Demo正常运行使用APIKey,请替换成自己的APIKey*/);
//        // 发音人（在线引擎），可用参数为0,1,2,3。。。（服务器端会动态增加，各值含义参考文档，以文档说明为准。0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
//        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 合成策略
        // MIX_MODE_DEFAULT（wifi在线，非wifi离线；）
        // MIX_MODE_HIGH_SPEED_NETWORK（wifi、4G、3G在线，其他离线；）
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE（默认在线，1.2秒超时即切换到离线）
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI（wifi在线，1.2秒超时即切换到离线）
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 音量（播放时）
        mSpeechSynthesizer.setStereoVolume(1.0f, 1.0f);
        // 音量（合成时），取值范围[0,9]
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 语速，取值范围[0,9]
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        // 语调，取值范围[0,9]
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
        // 合成引擎速度优化等级，取值范围[0,2]，值越大速度越快（离线引擎）
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOCODER_OPTIM_LEVEL, "2");
        // 初始化tts
        mSpeechSynthesizer.initTts(TtsMode.MIX);
        // 加载离线英文资源（提供离线英文合成功能）
        mSpeechSynthesizer.loadEnglishModel(mSampleDirPath + "/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath
                + "/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME);

    }

    public void resume() {
        mSpeechSynthesizer.resume();
    }

    public void speak(String text) {
        if (!mInitialSucceed) {
            mInitialSucceed = true;
            mSpeechSynthesizer.release();
            initialTts();
        }
        mediaPlayer.start();
        new Handler(Looper.getMainLooper()).postDelayed(() -> mSpeechSynthesizer.speak(text), 1000);
    }

    public void pause() {
        mSpeechSynthesizer.pause();
    }

    public void stop() {
        mSpeechSynthesizer.stop();
    }

    public void release() {
        mediaPlayer.release();
        mediaPlayer = null;
        mSpeechSynthesizer.release();
    }

    /**
     * 创建文件夹
     *
     * @param dirPath
     */
    private void makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 将sample工程需要的资源文件拷贝到SD卡中使用（授权文件为临时授权文件，请注册正式授权）
     *
     * @param isCover 是否覆盖已存在的目标文件
     * @param source
     * @param dest
     */
    private void copyFromAssetsToSdcard(boolean isCover, String source, String dest) {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = mContext.getResources().getAssets().open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onSynthesizeStart(String s) {

    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {

    }

    @Override
    public void onSynthesizeFinish(String s) {

    }

    @Override
    public void onSpeechStart(String s) {

    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {

    }

    @Override
    public void onSpeechFinish(String s) {

    }

    @Override
    public void onError(String s, SpeechError speechError) {
        mInitialSucceed = false;
    }
}
