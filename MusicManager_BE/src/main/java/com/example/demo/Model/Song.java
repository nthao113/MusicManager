package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "songs")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String artist;
    private String genre;
    private String filePath;

    @OneToMany(mappedBy = "song", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<PlaylistSong> songPlaylists = new HashSet<>();

    public Song(String name, String artist, String genre, String filePath) {
        this.name = name;
        this.artist = artist;
        this.genre = genre;
        this.filePath = filePath;
    }
}