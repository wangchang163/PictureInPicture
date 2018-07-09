package test.com.pictureinpicture

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException

/**
 * Created by Thinkpad on 2018/7/9.
 */
object VideoUtils {

    private lateinit var player: MediaPlayer
    private var mSavedCurrentPosition: Int = 0
    /**
     * 开启video
     */
    fun startVideo(surface: SurfaceView,context:Context) {
        surface.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                openVideo(holder.surface,context)
            }

            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                player?.let { 
                    mSavedCurrentPosition=it.currentPosition
                    closeVideo()
                }
            }
        })

    }

    private fun closeVideo(){
        player.release()
    }

    private fun openVideo(surface: Surface,context: Context) {
        player = MediaPlayer()
        player?.let {
            it.setSurface(surface)
            it.reset()
            try {
               var fd:AssetFileDescriptor=context.resources.openRawResourceFd(R.raw.vid_bigbuckbunny)
                it.setDataSource(fd)
                it.setOnPreparedListener {
                    if (mSavedCurrentPosition>0){
                        it.seekTo(mSavedCurrentPosition)
                        mSavedCurrentPosition=0
                    }else{
                        play()
                    }
                }
                player.prepare()
            }catch (e:IOException){

            }
        }
    }

    private fun play() {
        if (player==null){
            return
        }
        player!!.start()

    }

    /**
     * 暂停video
     */
    fun pauseVideo() {
        if (player==null){
            return
        }
        player!!.pause()

    }
}