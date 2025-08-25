package com.example.demo.Controller;

import com.example.demo.Model.Playlist;
import com.example.demo.Service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@Tag(name = "Playlist Controller", description = "Manage music playlists")
@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    //This is for security testing
    @GetMapping("/protected")
    public ResponseEntity<String> protectedEndpoint() {
        return ResponseEntity.ok("Access Granted");
    }

    @Operation(summary = "Get playlist by ID", description = "Retrieve a playlist by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<Playlist> findById(
            @Parameter(description = "ID of the playlist", required = true)
            @PathVariable Long id) {
        Playlist playlist = playlistService.findById(id);
        return ResponseEntity.ok(playlist);
    }

    @Operation(summary = "Get playlists by name", description = "Retrieve playlists matching the given name")
    @GetMapping("/name/{name}")
    public ResponseEntity<Page<Playlist>> findByNameContainingIgnoreCase(
            @Parameter(description = "Name of the playlist to search", required = true)
            @PathVariable String name,
            Pageable pageable) {
        Page<Playlist> playlists = playlistService.findByNameContainingIgnoreCase(name, pageable);
        return ResponseEntity.ok(playlists);
    }

    @Operation(summary = "Get all playlists", description = "Retrieve a list of all playlists")
    @GetMapping
    public ResponseEntity<Page<Playlist>> findAll(Pageable pageable) {
        Page<Playlist> playlists = playlistService.findAll(pageable);
        return ResponseEntity.ok(playlists);
    }

    @Operation(summary = "Create a new playlist", description = "Create a new playlist with provided information")
    @PostMapping
    public ResponseEntity<Playlist> createPlaylist(
            @Parameter(description = "Playlist object to be created", required = true)
            @RequestBody Playlist playlist) {
        Playlist created = playlistService.createPlaylist(playlist);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update playlist by ID", description = "Update an existing playlist using its ID")
    @PutMapping("/{id}")
    public ResponseEntity<Playlist> updatePlaylist(
            @Parameter(description = "ID of the playlist to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated playlist object", required = true)
            @RequestBody Playlist playlist) {
        Playlist updated = playlistService.updatePlaylist(id, playlist);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete playlist by ID", description = "Delete a playlist using its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylistById(
            @Parameter(description = "ID of the playlist to delete", required = true)
            @PathVariable Long id) {
        playlistService.deletePlaylistById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete multiple playlists by Id", description = "This endpoint allows you to delete multiple playlists by Id")
    @PostMapping("/delete-multi")
    public ResponseEntity<Void> deletePlaylistAllById(
            @Parameter(description = "List of playlist Ids to be deleted", required = true)
            @RequestBody List<Long> ids){
        playlistService.deletePlaylistAllById(ids);
        return ResponseEntity.noContent().build();
    }
}


