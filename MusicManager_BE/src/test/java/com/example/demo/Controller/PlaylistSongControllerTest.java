package com.example.demo.Controller;

import com.example.demo.Model.Playlist;
import com.example.demo.Model.PlaylistSong;
import com.example.demo.Model.Song;
import com.example.demo.Security.JwtTestConfig;
import com.example.demo.Security.TestSecurityConfig;
import com.example.demo.Service.PlaylistSongService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlaylistSongController.class)
@Import({TestSecurityConfig.class, JwtTestConfig.class})
class PlaylistSongControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaylistSongService playlistSongService;

    private Song s1, s2, s3, s4, s5, s6;
    private Playlist p1, p2, p3;

    @BeforeEach
    void setup(){
        p1 = new Playlist(1L, "playlist1", null);
        p2 = new Playlist(2L, "playlist2", null);
        p3 = new Playlist(3L, "playlist3", null);


        s1 = new Song(1L,"what do you mean", "Justin", "pop", "uploads/song1.mp3", null);
        s2 = new Song(2L, "Timber","PitBull","pop","uploads/song2.mp3", null);
        s3 = new Song(3L, "Hello","Adel", "pop", "uploads/song3.mp3", null);
        s4 = new Song(4L, "lock what you made me do","Taylor", "pop", "uploads/song4.mp3", null);
        s5 = new Song(5L, "Hot N Cold", "Katty","pop", "uploads/song5.mp3", null);
        s6 = new Song(6L, "Hello", "SHINee", "pop", "uploads/song6.mp3", null);
    }

    @Test
    void testAddSongToPlaylist() throws Exception {
        PlaylistSong ps = new PlaylistSong(p1, s2);

        Mockito.when(playlistSongService.addSongToPlaylist(1L, 2L)).thenReturn(ps);

        mockMvc.perform(post("/api/playlistSong/playlist/1/song/2"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.playlist.id").value(1))
                .andExpect(jsonPath("$.song.id").value(2));
    }


    @Test
    void testRemoveSongFromPlaylist() throws Exception{
        mockMvc.perform(delete("/api/playlistSong/playlist/1/song/2"))
                .andExpect(status().isNoContent());

        verify(playlistSongService, times(1)).removeSongFromPlaylist(1L, 2L);
    }

    @Test
    void testGetSongsByPlaylist() throws Exception{
        Set<Song> songs = new HashSet<>(List.of(s1, s2, s3));

        Mockito.when(playlistSongService.getSongsByPlaylist(2L)).thenReturn(songs);

        mockMvc.perform(get("/api/playlistSong/playlist/2/songs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void testGetPlaylistsBySong() throws Exception{
        Set<Playlist> playlists = new HashSet<>(List.of(p1, p2));

        Mockito.when(playlistSongService.getPlaylistsBySong(4L)).thenReturn(playlists);

        mockMvc.perform(get("/api/playlistSong/song/4/playlists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}