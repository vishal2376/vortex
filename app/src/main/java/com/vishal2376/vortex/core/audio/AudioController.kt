package com.vishal2376.vortex.core.audio

/**
 * Author: Vishal Singh (vishal2376)
 */

import android.content.Context
import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import androidx.annotation.RawRes
import kotlin.math.sqrt

class AudioController(private val context: Context) {
	private var mediaPlayer: MediaPlayer? = null
	private var visualizer: Visualizer? = null

	private var currentMagnitude = 0f

	fun play(@RawRes resId: Int) {
		release()

		try {
			mediaPlayer = MediaPlayer.create(context, resId).apply {
				isLooping = true
				start()
			}

			val sessionId = mediaPlayer?.audioSessionId ?: return

			visualizer = Visualizer(sessionId).apply {
				captureSize = Visualizer.getCaptureSizeRange()[1]
				setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
					override fun onWaveFormDataCapture(
						vis: Visualizer?,
						waveform: ByteArray?,
						rate: Int,
					) {
						waveform?.let { updateMagnitude(it) }
					}

					override fun onFftDataCapture(vis: Visualizer?, fft: ByteArray?, rate: Int) {}
				}, Visualizer.getMaxCaptureRate() / 2, true, false)
				enabled = true
			}
		} catch (e: Exception) {
			release()
		}
	}

	fun updateMagnitude(waveform: ByteArray) {
		var sum = 0.0
		for (byte in waveform) {
			val amplitude = byte.toFloat() / 128f
			sum += amplitude * amplitude
		}
		val rms = sqrt(sum / waveform.size).toFloat()

		val boost = 1.5f
		val rawTarget = (rms * boost)
		val target = (rawTarget * rawTarget).coerceIn(0f, 1f)

		val dynamicSmoothing = if (target > currentMagnitude) 0.1f else 0.6f

		currentMagnitude = (currentMagnitude * dynamicSmoothing) + (target * (1 - dynamicSmoothing))
	}

	fun getAmplitude(): Float = currentMagnitude

	fun release() {
		visualizer?.enabled = false
		visualizer?.release()
		visualizer = null
		mediaPlayer?.stop()
		mediaPlayer?.release()
		mediaPlayer = null
		currentMagnitude = 0f
	}
}