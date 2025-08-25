package com.example.demo.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "playlist_song")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistSong {

    @EmbeddedId
    private PlaylistSongId id = new PlaylistSongId();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("playlistId")
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("songId")
    private Song song;

    private LocalDateTime createdAt = LocalDateTime.now();

    public PlaylistSong(Playlist playlist, Song song) {
        this.playlist = playlist;
        this.song = song;
        this.id = new PlaylistSongId(playlist.getId(), song.getId());
    }
}

