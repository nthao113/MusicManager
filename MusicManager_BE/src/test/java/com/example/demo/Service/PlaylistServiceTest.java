package com.example.demo.Service;

import com.example.demo.Model.Playlist;
import com.example.demo.Repository.PlaylistRepository;
import com.example.demo.Repository.PlaylistSongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceTest {

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private PlaylistSongRepository playlistSongRepository;

    @InjectMocks
    private PlaylistService playlistService;

    private Playlist p1, p2, p3;

    @BeforeEach
    void setup(){
        p1 = new Playlist(1L, "playlist1", null);
        p2 = new Playlist(2L, "playlist2", null);
        p3 = new Playlist(3L, "playlist3", null);
    }

    @Test
    void testCreatePlaylist() {
        when(playlistRepository.save(any(Playlist.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Playlist actual = playlistService.createPlaylist(p1);
        assertEquals(p1, actual);
    }

    @Test
    void testFindById() {
        when(playlistRepository.findById(2L)).thenReturn(Optional.of(p2));

        Playlist actual = playlistService.findById(2L);
        assertEquals(p2, actual);
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        Page<Playlist> playlists = new PageImpl<>(List.of(p1, p2, p3));
        Pageable page = PageRequest.of(0, 3);

        when(playlistRepository.findByNameContainingIgnoreCase("playlist", page)).thenReturn(playlists);

        Page<Playlist> actual = playlistService.findByNameContainingIgnoreCase("playlist", page);
        assertEquals(playlists, actual);
    }

    @Test
    void testFindAll() {
        Page<Playlist> playlists = new PageImpl<>(List.of(p1, p2, p3));
        Pageable page = PageRequest.of(0, 3);

        when(playlistRepository.findAll(page)).thenReturn(playlists);

        Page<Playlist> actual = playlistService.findAll(page);
        assertEquals(playlists, actual);
    }

    @Test
    void testUpdatePlaylist() {
        Playlist p = new Playlist(1L, "playlist--", null);

        when(playlistRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(playlistRepository.save(any(Playlist.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        Playlist actual = playlistService.updatePlaylist(1L, p);
        assertEquals(p, actual);
    }

    @Test
    void testDeletePlaylist() {
        playlistService.deletePlaylistById(4L);

        verify(playlistSongRepository, times(1)).deleteByPlaylistId(4L);
        verify(playlistRepository, times(1)).deleteById(4L);
    }

    @Test
    void testDeletePlaylistAll() {
        List<Long> ids = List.of(4L, 3L);
        playlistService.deletePlaylistAllById(ids);

        verify(playlistSongRepository, times(1)).deleteByPlaylistIdIn(ids);
        verify(playlistRepository, times(1)).deleteAllById(ids);
    }

}