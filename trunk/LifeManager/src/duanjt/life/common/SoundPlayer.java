package duanjt.life.common;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * �Զ����װ����������
 * 
 * @author �ν���
 * 
 */
public class SoundPlayer {
	SoundPool mSoundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
	int msound = 0, sysSound = 0;
	Context context;

	/**
	 * ����ɨ�赽���ߵ�����
	 */
	public void PlayInfend() {
		mSoundPool.play(msound, 1, 1, 0, 0, 1);

	}

	/**
	 * ����δ��ȡ�����ߵ�����
	 */
	public void PlayEmpty() {
		mSoundPool.play(sysSound, 1, 1, 0, 0, 1);
	}
}
