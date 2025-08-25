package com.example.demo.Repository;

import com.example.demo.Model.Playlist;
import com.example.demo.Model.Song;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface
PlaylistRepository extends JpaRepository<Playlist, Long> {

    Page<Playlist> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

}
