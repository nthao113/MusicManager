package com.example.demo.Service;

import com.example.demo.Exception.NotFoundException;
import com.example.demo.Model.Playlist;
import com.example.demo.Model.PlaylistSong;
import com.example.demo.Model.PlaylistSongId;
import com.example.demo.Model.Song;
import com.example.demo.Repository.PlaylistRepository;
import com.example.demo.Repository.PlaylistSongRepository;
import com.example.demo.Repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlaylistSongServiceImpl implements PlaylistSongService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private PlaylistSongRepository playlistSongRepository;

    @Override
    public PlaylistSong addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new NotFoundException(playlistId, "Playlist"));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new NotFoundException(songId, "Song"));

        PlaylistSongId id = new PlaylistSongId(playlistId, songId);

        // Avoid duplicate inserts
        if (playlistSongRepository.existsById(id)) {
            throw new IllegalStateException("Song already exists in Playlist");
        }

        PlaylistSong ps = new PlaylistSong(playlist, song);

        return playlistSongRepository.save(ps);
    }

    @Override
    public void removeSongFromPlaylist(Long playlistId, Long songId) {
        PlaylistSongId id = new PlaylistSongId(playlistId, songId);
        if (!playlistSongRepository.existsById(id)) {
            throw new NotFoundException(songId, "Playlist-Song relation");
        }
        playlistSongRepository.deleteById(id);
    }

    @Override
    public Set<Song> getSongsByPlaylist(Long playlistId) {
        return playlistSongRepository.findSongsByPlaylistId(playlistId);
    }

    @Override
    public Set<Playlist> getPlaylistsBySong(Long songId) {
        return playlistSongRepository.findPlaylistsBySongId(songId);
    }
}

