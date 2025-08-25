package com.example.demo.Service;

import com.example.demo.Exception.NotFoundException;
import com.example.demo.Model.Playlist;
import com.example.demo.Model.Song;
import com.example.demo.Repository.PlaylistRepository;
import com.example.demo.Repository.PlaylistSongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistSongRepository playlistSongRepository;

    //CRUD
    public Playlist createPlaylist(Playlist playlist){
        return playlistRepository.save(playlist);
    }

    public Playlist findById(Long id){
        return playlistRepository.findById(id).orElse(null);
    }

    public Page<Playlist> findByNameContainingIgnoreCase(String name, Pageable pageable){
        return playlistRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<Playlist> findAll(Pageable pageable){
        return playlistRepository.findAll(pageable);
    }

    public Playlist updatePlaylist(Long id, Playlist playlist){
        Playlist updatedPlaylist = playlistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, "Playlist" ));

        updatedPlaylist.setName(playlist.getName());

        return playlistRepository.save(updatedPlaylist);
    }

    @Transactional
    public void deletePlaylistById(Long id){
        playlistSongRepository.deleteByPlaylistId(id); // delete previous child row
        playlistRepository.deleteById(id);
    }

    @Transactional
    public void deletePlaylistAllById(List<Long> ids) {
        playlistSongRepository.deleteByPlaylistIdIn(ids);
        playlistRepository.deleteAllById(ids);
    }
}
