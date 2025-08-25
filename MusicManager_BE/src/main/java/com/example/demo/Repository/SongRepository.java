package com.example.demo.Repository;

import com.example.demo.Model.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    Page<Song> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
