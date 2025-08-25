package com.example.demo.Controller;

import com.example.demo.Model.Playlist;
import com.example.demo.Model.PlaylistSong;
import com.example.demo.Model.Song;
import com.example.demo.Service.PlaylistSongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.Set;

@Tag(name = "Playlist-Song Controller", description = "Manage songs in playlists and playlists containing songs")
@RestController
@RequestMapping("/api/playlistSong")
public class PlaylistSongController {

    @Autowired
    private PlaylistSongService playlistSongService;

    @Operation(summary = "Add a song to a playlist", description = "Add the specified song to the given playlist by ID")
    @PostMapping("/playlist/{playlistId}/song/{songId}")
    public ResponseEntity<PlaylistSong> addSongToPlaylist(
            @Parameter(description = "ID of the playlist", required = true) @PathVariable Long playlistId,
            @Parameter(description = "ID of the song to add", required = true) @PathVariable Long songId) {
        PlaylistSong result = playlistSongService.addSongToPlaylist(playlistId, songId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Remove a song from a playlist", description = "Remove the specified song from the given playlist by ID")
    @DeleteMapping("/playlist/{playlistId}/song/{songId}")
    public ResponseEntity<String> removeSongFromPlaylist(
            @Parameter(description = "ID of the playlist", required = true) @PathVariable Long playlistId,
            @Parameter(description = "ID of the song to remove", required = true) @PathVariable Long songId) {
        playlistSongService.removeSongFromPlaylist(playlistId, songId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get songs by playlist", description = "Retrieve all songs from the specified playlist")
    @GetMapping("/playlist/{playlistId}/songs")
    public ResponseEntity<Set<Song>> getSongsByPlaylist(
            @Parameter(description = "ID of the playlist", required = true)
            @PathVariable Long playlistId) {
        Set<Song> songs = playlistSongService.getSongsByPlaylist(playlistId);
        return ResponseEntity.ok(songs);
    }

    @Operation(summary = "Get playlists by song", description = "Retrieve all playlists that contain the specified song")
    @GetMapping("/song/{songId}/playlists")
    public ResponseEntity<Set<Playlist>> getPlaylistsBySong(
            @Parameter(description = "ID of the song", required = true)
            @PathVariable Long songId) {
        Set<Playlist> playlists = playlistSongService.getPlaylistsBySong(songId);
        return ResponseEntity.ok(playlists);
    }
}

