package com.example.demo.Repository;

import com.example.demo.Model.Playlist;
import com.example.demo.Model.PlaylistSong;
import com.example.demo.Model.PlaylistSongId;
import com.example.demo.Model.Song;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, PlaylistSongId> {

    @Modifying
    @Query("DELETE FROM PlaylistSong ps WHERE ps.playlist.id = :playlistId")
    void deleteByPlaylistId(@Param("playlistId") Long playlistId);

    @Modifying
    @Query("DELETE FROM PlaylistSong ps WHERE ps.song.id = :songId")
    void deleteBySongId(@Param("songId") Long songId);

    @Modifying
    @Query("DELETE FROM PlaylistSong ps WHERE ps.song.id IN :songIds")
    void deleteBySongIdIn(@Param("songIds") List<Long> songIds);

    @Modifying
    @Transactional
    @Query("DELETE FROM PlaylistSong ps WHERE ps.playlist.id IN :playlistIds")
    void deleteByPlaylistIdIn(@Param("playlistIds") List<Long> playlistIds);;

    @Query("SELECT ps.song FROM PlaylistSong ps WHERE ps.playlist.id = :playlistId")
    Set<Song> findSongsByPlaylistId(@Param("playlistId") Long playlistId);

    @Query("SELECT ps.playlist FROM PlaylistSong ps WHERE ps.song.id = :songId")
    Set<Playlist> findPlaylistsBySongId(@Param("songId") Long songId);
}
