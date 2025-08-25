package com.example.demo.Repository;

import com.example.demo.Model.Playlist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PlaylistRepositoryTest {

    @Autowired
    PlaylistRepository playlistRepository;

    private Playlist p1, p2;

    @BeforeEach
    void setup() {
        p1 = new Playlist("list1");
        p2 = new Playlist("list2");

        playlistRepository.saveAll(Set.of(p1, p2));
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        Pageable page = PageRequest.of(0, 10);
        Page<Playlist> playlists = playlistRepository.findByNameContainingIgnoreCase("list", page);

        assertEquals(2, playlists.stream().count());
    }

}