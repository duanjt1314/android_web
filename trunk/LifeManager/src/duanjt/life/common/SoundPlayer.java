package duanjt.life.common;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * 自定义封装声音播放类
 * 
 * @author 段江涛
 * 
 */
public class SoundPlayer {
	SoundPool mSoundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
	int msound = 0, sysSound = 0;
	Context context;

	/**
	 * 播放扫描到工具的声音
	 */
	public void PlayInfend() {
		mSoundPool.play(msound, 1, 1, 0, 0, 1);

	}

	/**
	 * 播放未读取到工具的声音
	 */
	public void PlayEmpty() {
		mSoundPool.play(sysSound, 1, 1, 0, 0, 1);
	}
}
