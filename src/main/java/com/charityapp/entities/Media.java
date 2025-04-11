package com.charityapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // IMAGE ou VIDEO

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Date dateUpload;

    @ManyToOne
    @JoinColumn(name = "action_de_charite_id", nullable = false)
    private ActionDeCharite actionDeCharite;

    @PrePersist
    protected void onCreate() {
        dateUpload = new Date();
    }
} 