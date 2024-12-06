package dev.arbjerg.ukulele.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager
import com.sedmelluq.discord.lavaplayer.track.*
import dev.lavalink.youtube.YoutubeAudioSourceManager
import dev.lavalink.youtube.track.YoutubeAudioTrack
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.DataInput
import java.io.DataOutput
import java.io.IOException

class YtDlpAudioSourceManager : AudioSourceManager {
    private val log: Logger = LoggerFactory.getLogger(Player::class.java)

    private val youtubeAudioSourceManager: YoutubeAudioSourceManager
    private val localAudioSourceManager: LocalAudioSourceManager

    init {
        youtubeAudioSourceManager = YoutubeAudioSourceManager(true)
        localAudioSourceManager = LocalAudioSourceManager()
    }

    override fun shutdown() {
        youtubeAudioSourceManager.shutdown()
        localAudioSourceManager.shutdown()
    }

    override fun getSourceName(): String {
        return "yt-dlp"
    }

    override fun loadItem(manager: AudioPlayerManager, reference: AudioReference): AudioItem? {
        val ytAudioItem = youtubeAudioSourceManager.loadItem(manager, reference)
        if (ytAudioItem == null) {
            log.debug("null track from youtubeAudioSourceManager?")
            return null
        } else if (ytAudioItem is YoutubeAudioTrack) {
            log.info("Found track at {}", ytAudioItem.info.uri)
        } else if (ytAudioItem is BasicAudioPlaylist) {
            if (ytAudioItem.tracks.isNotEmpty()) {
                val at = ytAudioItem.tracks[0]
                log.info("Found track at {}", at.info.uri)
            }
        } else {
            log.warn(
                "Cannot submit unknown item to result handler: {} w/ type {}",
                ytAudioItem,
                (ytAudioItem.javaClass.name)
            )
        }

        return localAudioSourceManager.loadItem(manager, reference)
    }

    override fun isTrackEncodable(track: AudioTrack): Boolean {
        // return localAudioSourceManager.isTrackEncodable(track);
        return true // ??? seems like what others do?
    }

    @Throws(IOException::class)
    override fun encodeTrack(track: AudioTrack, output: DataOutput) {
        // blank in dev.lavalink.youtube.YoutubeAudioSourceManager
    }

    @Throws(IOException::class)
    override fun decodeTrack(trackInfo: AudioTrackInfo, input: DataInput): AudioTrack {
        return localAudioSourceManager.decodeTrack(trackInfo, input)
    }
}
