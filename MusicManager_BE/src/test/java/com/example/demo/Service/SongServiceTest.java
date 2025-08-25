package com.example.demo.Service;

import com.example.demo.Model.Song;
import com.example.demo.Repository.PlaylistSongRepository;
import com.example.demo.Repository.SongRepository;
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
class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @Mock
    private PlaylistSongRepository playlistSongRepository;

    @InjectMocks
    private SongService songService;

    private Song s1, s2, s3, s4, s5, s6;

    @BeforeEach
    void setup() {
        s1 = new Song(1L,"what do you mean", "Justin", "pop", "uploads/song1.mp3", null);
        s2 = new Song(2L, "Timber","PitBull",null,"uploads/song2.mp3", null);
        s3 = new Song(3L, "Hello","Adel", null, "uploads/song3.mp3", null);
        s4 = new Song(4L, "lock what you made me do","Taylor", null, "uploads/song4.mp3", null);
        s5 = new Song(5L, "Hot N Cold", "Katty",null, "uploads/song5.mp3", null);
        s6 = new Song(6L, "Hello", "SHINee", null, "uploads/song6.mp3", null);
    }

    @Test
    void testCreateSong() {
        when(songRepository.save(any(Song.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Song actual = songService.createSong(s1);
        assertEquals(s1, actual);
    }

    @Test
    void testFindById() {
        when(songRepository.findById(2L)).thenReturn(Optional.of(s2));

        Song actual = songService.findById(2L);
        assertEquals(s2, actual);
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        Page<Song> songs = new PageImpl<>(List.of(s3,s6));
        Pageable page = PageRequest.of(0, 2);

        when(songRepository.findByNameContainingIgnoreCase("Hello", page)).thenReturn(songs);

        Page<Song> actual = songService.findByNameContainingIgnoreCase("Hello", page );
        assertEquals(songs, actual);
    }

    @Test
    void testFindAll() {
        Page<Song> songs = new PageImpl<>(List.of(s1, s2, s3));
        Pageable page = PageRequest.of(0, 3);

        when(songRepository.findAll(page)).thenReturn(songs);

        Page<Song> actual = songService.findAll(page);
        assertEquals(songs, actual);
    }

    @Test
    void testUpdateSong() {
        Song s = new Song(5L, "Die young", "Keysha", null,"uploads/song.mp3", null);

        when(songRepository.findById(5L)).thenReturn(Optional.of(s5));
        when(songRepository.save(any(Song.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Song actual = songService.updateSong(5L, s);
        assertEquals(s, actual);
    }

    @Test
    void testDeleteSong() {
        songService.deleteSongById(4L);

        verify(playlistSongRepository, times(1)).deleteBySongId(4L);
        verify(songRepository, times(1)).deleteById(4L);
    }

    @Test
    void testDeleteSongAll() {
        List<Long> ids = List.of(4L, 3L);
        songService.deleteSongAllById(ids);

        verify(playlistSongRepository, times(1)).deleteBySongIdIn(ids);
        verify(songRepository, times(1)).deleteAllById(ids);
    }
}