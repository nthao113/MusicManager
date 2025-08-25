package com.example.demo.Controller;

import com.example.demo.Model.Playlist;
import com.example.demo.Security.JwtTestConfig;
import com.example.demo.Security.TestSecurityConfig;
import com.example.demo.Service.PlaylistService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PlaylistController.class)
@Import({TestSecurityConfig.class, JwtTestConfig.class})
class PlaylistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaylistService playlistService;

    private Playlist p1, p2, p3;

    @BeforeEach
    void setup() {
        p1 = new Playlist(1L, "playlist1", new HashSet<>());
        p2 = new Playlist(2L, "playlist2", new HashSet<>());
        p3 = new Playlist(3L, "playlist3", new HashSet<>());
    }

    @Test
    void testFindById() throws Exception {
        Mockito.when(playlistService.findById(2L)).thenReturn(p2);

        mockMvc.perform(get("/api/playlists/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L));
    }

    @Test
    void testFindAll() throws Exception {
        Page<Playlist> playlists = new PageImpl<>(List.of(p1, p2, p3));
        Pageable page = PageRequest.of(0, 3);

        Mockito.when(playlistService.findAll(page)).thenReturn(playlists);

        mockMvc.perform(get("/api/playlists?page=0&size=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].name").value("playlist1"));
    }

    @Test
    void testFindByNameContainingIgnoreCase() throws Exception{
        Page<Playlist> playlists = new PageImpl<>(List.of(p1, p2, p3));
        Pageable page = PageRequest.of(0, 10);

        Mockito.when(playlistService.findByNameContainingIgnoreCase("playlist", page)).thenReturn(playlists);

        mockMvc.perform(get("/api/playlists/name/playlist?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    void testCreatePlaylist() throws Exception{
        Mockito.when(playlistService.createPlaylist(p3)).thenReturn(p3);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/playlists")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(p3)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("playlist3"));

        verify(playlistService, times(1)).createPlaylist(p3);
    }

    @Test
    void testUpdatePlaylist() throws Exception{
        Mockito.when(playlistService.updatePlaylist(3L, p3)).thenReturn(p3);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(put("/api/playlists/3")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(p3)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("playlist3"));
    }

    @Test
    void testDeletePlaylistById() throws Exception{
        mockMvc.perform(delete("/api/playlists/3"));

        verify(playlistService, times(1)).deletePlaylistById(3L);
    }

    @Test
    void testDeletePlaylistAllById() throws Exception{
        List<Long> ids = List.of(p3.getId(), p2.getId());
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/playlists/delete-multi")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isNoContent());

        verify(playlistService, times(1)).deletePlaylistAllById(ids);
    }

}