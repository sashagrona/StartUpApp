package net.bigmir.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "startup_id")
    private StartUp startUp;

    private String path;

    private Double size;

    public Document(String name, StartUp startUp, String path, Double size) {
        this.name = name;
        this.startUp = startUp;
        this.path = path;
        this.size = size;
    }
}
