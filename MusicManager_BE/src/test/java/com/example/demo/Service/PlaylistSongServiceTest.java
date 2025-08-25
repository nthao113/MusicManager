package com.example.demo.Service;

import com.example.demo.Model.Playlist;
import com.example.demo.Model.PlaylistSong;
import com.example.demo.Model.PlaylistSongId;
import com.example.demo.Model.Song;
import com.example.demo.Repository.PlaylistRepository;
import com.example.demo.Repository.PlaylistSongRepository;
import com.example.demo.Repository.SongRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
class PlaylistSongServiceTest {

    @Autowired
    private PlaylistSongService playlistSongService;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private PlaylistSongRepository playlistSongRepository;

    private Playlist p1, p2;
    private Song s1, s2, s3, s4, s5, s6;

    @BeforeEach
    void setup() {
        p1 = playlistRepository.save(new Playlist("list1"));
        p2 = playlistRepository.save(new Playlist("list2"));

        s1 = songRepository.save(new Song("what do you mean", "Justin", "pop", "uploads/song1.mp3"));
        s2 = songRepository.save(new Song("Timber","PitBull",null,"uploads/song2.mp3"));
        s3 = songRepository.save(new Song("Hello","Adel", null, "uploads/song3.mp3"));
        s4 = songRepository.save(new Song("lock what you made me do","Taylor", null, "uploads/song4.mp3"));
        s5 = songRepository.save(new Song("Hot N Cold", "Katty",null, "uploads/song5.mp3"));
        s6 = songRepository.save(new Song("Hello", "SHINee", null, "uploads/song6.mp3"));
    }

    @Test
    void testAddSongToPlaylist() {
        PlaylistSong ps = playlistSongService.addSongToPlaylist(p1.getId(), s3.getId());

        assertNotNull(ps);
        assertEquals(p1.getId(), ps.getPlaylist().getId());
        assertEquals(s3.getId(), ps.getSong().getId());

        assertTrue(playlistSongRepository.existsById(new PlaylistSongId(p1.getId(), s3.getId())));
    }

    @Test
    void testRemoveSongFromPlaylist() {
        playlistSongService.addSongToPlaylist(p2.getId(), s5.getId());
        assertTrue(playlistSongRepository.existsById(new PlaylistSongId(p2.getId(), s5.getId())));

        playlistSongService.removeSongFromPlaylist(p2.getId(), s5.getId());
        assertFalse(playlistSongRepository.existsById(new PlaylistSongId(p2.getId(), s5.getId())));
    }

    @Test
    @Transactional
    void testGetSongsByPlaylist() {
        playlistSongService.addSongToPlaylist(p1.getId(), s2.getId());
        assertTrue(playlistSongRepository.existsById(new PlaylistSongId(p1.getId(), s2.getId())));

        Set<Song> songs = playlistSongService.getSongsByPlaylist(p1.getId());

        assertEquals(1, songs.size());
        assertTrue(songs.stream().anyMatch(s -> s.getId().equals(s2.getId())));
    }

    @Test
    void testGetPlaylistsBySong() {
        playlistSongService.addSongToPlaylist(p2.getId(), s3.getId());
        playlistSongService.addSongToPlaylist((p1.getId()), s3.getId());

        Set<Playlist> playlists = playlistSongService.getPlaylistsBySong(s3.getId());

        assertEquals(2, playlists.size());
    }
}
