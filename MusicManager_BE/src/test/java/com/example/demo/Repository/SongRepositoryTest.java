package com.example.demo.Repository;

import com.example.demo.Model.Song;
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
class SongRepositoryTest {

    @Autowired
    SongRepository songRepository;

    private Song s1, s2, s3, s4, s5, s6;

    @BeforeEach
    void setup() {
        s1 = new Song("what do you mean", "Justin", "pop", "uploads/song1.mp3");
        s2 = new Song("Timber","PitBull",null,"uploads/song2.mp3");
        s3 = new Song("Hello","Adel", null, "uploads/song3.mp3");
        s4 = new Song("lock what you made me do","Taylor", null, "uploads/song4.mp3");
        s5 = new Song("Hot N Cold", "Katty",null, "uploads/song5.mp3");
        s6 = new Song("Hello", "SHINee", null, "uploads/song6.mp3");

        songRepository.saveAll(Set.of(s1, s2, s3, s4, s5, s6));
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        Pageable page = PageRequest.of(0, 10);
        Page<Song> songs = songRepository.findByNameContainingIgnoreCase("H", page);

        assertEquals(5, songs.stream().count());
    }
}