package com.example.demo.Service;

import com.example.demo.Model.Playlist;
import com.example.demo.Model.PlaylistSong;
import com.example.demo.Model.Song;

import java.util.Set;

public interface PlaylistSongService {
    PlaylistSong addSongToPlaylist(Long playlistId, Long songId);
    void removeSongFromPlaylist(Long playlistId, Long songId);
    Set<Song> getSongsByPlaylist(Long playlistId);
    Set<Playlist> getPlaylistsBySong(Long songId);
}


