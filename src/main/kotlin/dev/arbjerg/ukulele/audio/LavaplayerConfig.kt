package dev.arbjerg.ukulele.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import dev.lavalink.youtube.YoutubeAudioSourceManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LavaplayerConfig {
    @Bean
    fun playerManager(): AudioPlayerManager {
        val apm = DefaultAudioPlayerManager()

        // Add the new YoutubeAudioSourceManager
        apm.registerSourceManager(YoutubeAudioSourceManager(true))

        // Then add the rest of the source managers
        AudioSourceManagers.registerRemoteSources(apm)

        // TODO: Is there a way to take out the original YoutubeAudioSourceManager?
        // apm.sourceManagers.removeAll { audioSourceManager -> audioSourceManager is com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager }

        return apm
    }
}
