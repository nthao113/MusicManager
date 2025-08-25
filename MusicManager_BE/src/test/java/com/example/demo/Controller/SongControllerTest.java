package com.example.demo.Controller;

import com.example.demo.Model.Song;
import com.example.demo.Security.JwtTestConfig;
import com.example.demo.Security.TestSecurityConfig;
import com.example.demo.Service.SongService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SongController.class)
@Import({TestSecurityConfig.class, JwtTestConfig.class})
class SongControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SongService songService;

    private Song s1, s2, s3, s4, s5, s6;

    @BeforeEach
    void setup() {
        s1 = new Song(1L,"what do you mean", "Justin", null, "uploads/song1.mp3", new HashSet<>());
        s2 = new Song(2L, "Timber","PitBull",null,"uploads/song2.mp3", new HashSet<>());
        s3 = new Song(3L, "Hello","Adel", null, "uploads/song3.mp3", new HashSet<>());
        s4 = new Song(4L, "lock what you made me do","Taylor", null, "uploads/song4.mp3", new HashSet<>());
        s5 = new Song(5L, "Hot N Cold", "Katty",null, "uploads/song5.mp3", new HashSet<>());
        s6 = new Song(6L, "Hello", "SHINee", null, "uploads/song6.mp3", new HashSet<>());
    }

    @Test
    void testFindById() throws Exception{
        Mockito.when(songService.findById(2L)).thenReturn(s2);

        mockMvc.perform(get("/api/songs/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L));
    }

    @Test
    void testFindAll() throws Exception{
        Page<Song> songs = new PageImpl<>(List.of(s1, s2, s5));
        Pageable page = PageRequest.of(0, 3);
        Mockito.when(songService.findAll(page)).thenReturn(songs);

        mockMvc.perform(get("/api/songs?page=0&size=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].name").value("what do you mean"));
    }

    @Test
    void testFindByNameContainingIgnoreCase() throws Exception{
        Page<Song> songs = new PageImpl<>(List.of(s3, s6));
        Pageable page = PageRequest.of(0, 3);

        Mockito.when(songService.findByNameContainingIgnoreCase("Hello", page)).thenReturn(songs);

        mockMvc.perform(get("/api/songs/name/Hello?page=0&size=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void testCreateSong() throws Exception{
        Mockito.when(songService.createSong(s4)).thenReturn(s4);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/songs")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(s4)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value("lock what you made me do"));
    }

    @Test
    void testUpdateSong() throws Exception{
        Mockito.when(songService.updateSong(4L, s4)).thenReturn(s4);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(put("/api/songs/4")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(s4)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value("lock what you made me do"));
    }

    @Test
    void testDeleteSong() throws Exception{
        mockMvc.perform(delete("/api/songs/3"));

        verify(songService, times(1)).deleteSongById(3L);
    }

    @Test
    void testDeletePlaylistAllById() throws Exception{
        List<Long> ids = List.of(s3.getId(), s2.getId());
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/songs/delete-multi")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isNoContent());

        verify(songService, times(1)).deleteSongAllById(ids);
    }
}