package com.example.demo.Controller;

import com.example.demo.Model.Song;
import com.example.demo.Service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@Tag(name = "Song Controller", description = "Operations related to songs")
public class SongController {

    @Autowired
    private SongService songService;

    @Operation(summary = "Get song by ID", description = "Retrieve a song by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<Song> findById(
            @Parameter(description = "ID of the song to be fetched")
            @PathVariable Long id) {
        Song song = songService.findById(id);
        return ResponseEntity.ok(song);
    }

    @Operation(summary = "Get songs by name", description = "Retrieve a list of songs that match the given name")
    @GetMapping("name/{name}")
    public ResponseEntity<Page<Song>> findByNameContainingIgnoreCase(
            @Parameter(description = "Name of the song to search for")
            @PathVariable String name,
            Pageable pageable) {
        Page<Song> songs = songService.findByNameContainingIgnoreCase(name, pageable);
        return ResponseEntity.ok(songs);
    }

    @Operation(summary = "Get all songs", description = "Retrieve a list of all songs")
    @GetMapping
    public ResponseEntity<Page<Song>> findAll(Pageable pageable) {
        Page<Song> songs = songService.findAll(pageable);
        return ResponseEntity.ok(songs);
    }

    @Operation(summary = "Create a new song", description = "Create a new song with the provided information")
    @PostMapping
    public ResponseEntity<Song> createSong(
            @RequestBody Song song) {
        Song created = songService.createSong(song);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update song by ID", description = "Update an existing song by its ID")
    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(
            @Parameter(description = "ID of the song to be updated")
            @PathVariable Long id,
            @RequestBody Song song) {
        Song updated = songService.updateSong(id, song);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete song by ID", description = "Delete a song by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSongById(
            @Parameter(description = "ID of the song to be deleted")
            @PathVariable Long id) {
        songService.deleteSongById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete multiple songs by Id", description = "This endpoint allows you to delete multiple songs by Id")
    @PostMapping("/delete-multi")
    public ResponseEntity<Void> deletePlaylistAllById(
             @Parameter(description = "List of song Ids to be deleted", required = true)
             @RequestBody List<Long> ids) {
        songService.deleteSongAllById(ids);
        return ResponseEntity.noContent().build();
    }
}

