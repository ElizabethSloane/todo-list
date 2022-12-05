package main.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@RequiredArgsConstructor
public class Task {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Setter
    @Getter
    @Column(name = "creation_time")
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private LocalDateTime creationTime;

    @Setter
    @Getter
    @Column(name = "is_done")
    private boolean isDone;

    @Setter
    @Getter
    private String title;

    @Setter
    @Getter
    private String description;

}
