package com.katevu.youtubeplayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

const val YOUTUBE_VIDEO_ID = "aG51brxM1kk"
const val YOUTUBE_PLAYLIST = "PLXtTjtWmQhg0N8akNvcdhtgzJtRtgu6Vf"

class YoutubeActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private val TAG = "YoutubeActivity"
    private val DIALOG_REQUEST_CODE = 1
    private val playerView by lazy { YouTubePlayerView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "OnCreate called")

        val layout = layoutInflater.inflate(R.layout.activity_youtube, null) as ConstraintLayout
        setContentView(layout)

//        val button1 = Button(this)
//        button1.layoutParams = ConstraintLayout.LayoutParams(600, 180)
//        button1.text = "Button added"
//        with(layout) {
//            button1.layoutParams = ConstraintLayout.LayoutParams(600, 180)
//            button1.text = "Button added"
//            addView(button1)
//        }

        playerView.layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        layout.addView(playerView)

        playerView.initialize(getString(R.string.GOOGLE_API_KEY), this)
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        youTubePlayer: YouTubePlayer?,
        wasRestore: Boolean
    ) {
        Log.d(TAG, "onInitializationSuccess: provider is ${provider?.javaClass}")
        Log.d(TAG, "onInitializationSuccess: youTubePlay is ${youTubePlayer?.javaClass}")
        Toast.makeText(this, "onInitializationSuccess", Toast.LENGTH_SHORT).show()

        youTubePlayer?.setPlaybackEventListener(playbackEventListener)
        youTubePlayer?.setPlayerStateChangeListener(playerStateChangeListener)

        if (!wasRestore) {
            youTubePlayer?.loadVideo(YOUTUBE_VIDEO_ID)
        } else {
            youTubePlayer?.play()
        }
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?,
        youTubeInitializationResult: YouTubeInitializationResult?
    ) {
        Log.d(TAG, "onInitializationFailure called")
        if (youTubeInitializationResult?.isUserRecoverableError == true) {
            youTubeInitializationResult.getErrorDialog(this, DIALOG_REQUEST_CODE)?.show()
        } else {
            val errorMessage =
                "Error when initializing the YoutubePlayer $youTubeInitializationResult"
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()

        }
    }

    private val playbackEventListener = object : YouTubePlayer.PlaybackEventListener {
        override fun onPlaying() {
            Toast.makeText(this@YoutubeActivity, "The video is playing!!!!", Toast.LENGTH_SHORT)
                .show()
        }

        override fun onPaused() {
            Toast.makeText(this@YoutubeActivity, "The video is on pause!!!", Toast.LENGTH_SHORT)
                .show()
        }

        override fun onStopped() {
            Toast.makeText(this@YoutubeActivity, "Video is stopped", Toast.LENGTH_SHORT).show()
        }

        override fun onBuffering(p0: Boolean) {
        }

        override fun onSeekTo(p0: Int) {
        }
    }

    private val playerStateChangeListener = object : YouTubePlayer.PlayerStateChangeListener {
        override fun onLoading() {
        }

        override fun onLoaded(p0: String?) {
        }

        override fun onAdStarted() {
            Toast.makeText(
                this@YoutubeActivity,
                "Ad is playing, be patient!!!!",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onVideoStarted() {
            Toast.makeText(this@YoutubeActivity, "Video has been started!!!", Toast.LENGTH_SHORT)
                .show()
        }

        override fun onVideoEnded() {
            Toast.makeText(this@YoutubeActivity, "This is the end of the video", Toast.LENGTH_SHORT)
                .show()
        }

        override fun onError(p0: YouTubePlayer.ErrorReason?) {
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult called with response code: $resultCode for request code $requestCode")

        if (requestCode == DIALOG_REQUEST_CODE) {
            intent?.toString()?.let { Log.d(TAG, it) }
            Log.d(TAG, intent?.extras.toString())
            playerView.initialize(getString(R.string.GOOGLE_API_KEY), this)

        }
    }
}