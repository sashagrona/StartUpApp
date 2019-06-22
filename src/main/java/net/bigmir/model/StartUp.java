package net.bigmir.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bigmir.dto.StartUpDTO;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "startups")
public class StartUp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //There is the idea that it can't be two startups with same names
    @Column(unique = true)
    private String name;

    private String idea;

    @OneToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.DETACH})
    @JoinColumn(name = "business_plan_id")
    private BusinessPlan plan;

    @ManyToMany(mappedBy = "startUps", cascade = {CascadeType.MERGE,CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.DETACH})
    private Set<SimpleUser> users = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "creator_id")
    private SimpleUser creator;

    //for money it's better to use BigDecimal, but in the case it needs only approximate startCapital, so cents are not necessary
    private Long startCapital;

    private String location;


    @OneToMany(mappedBy = "startUp")
    List<Document> documents = new LinkedList<>();


    public StartUp(String name, String idea) {
        this.name = name;
        this.idea = idea;
    }

    public StartUp(BusinessPlan plan, Long startCapital, String location) {
        this.plan = plan;
        this.startCapital = startCapital;
        this.location = location;

    }

    public StartUp(StartUpBuilder builder) {
        this.name = builder.name;
        this.idea = builder.idea;
        this.plan = builder.plan;
        this.startCapital = builder.startCapital;
        this.location = builder.location;
    }

    public static StartUp of(String name, String idea, BusinessPlan plan, Long startCapital, String location) {
        return new StartUpBuilder()
                .mergeByName(name)
                .addIdea(idea)
                .addPlan(plan)
                .addStartCapital(startCapital)
                .addLocation(location)
                .build();
    }

    public static StartUp of(String name, String idea) {
        return new StartUp.StartUpBuilder()
                .mergeByName(name)
                .addIdea(idea)
                .build();
    }


    public StartUpDTO toDTO() {
        StartUpDTO startUpDTO = StartUpDTO.of(name, idea);
        startUpDTO.setCreator(this.creator);
        return startUpDTO;
    }

    public static StartUp fromDTO(StartUpDTO startUpDTO) {
        return StartUp.of(startUpDTO.getName(), startUpDTO.getIdea());
    }

    //builder pattern
    public static class StartUpBuilder {
        private String name;
        private String idea;
        private BusinessPlan plan;
        private Long startCapital;
        private String location;

        public StartUpBuilder mergeByName(String name) {
            if (!name.equals("")) {
                this.name = name;
            }
            return this;
        }

        public StartUpBuilder addIdea(String idea) {
            this.idea = idea;
            return this;
        }

        public StartUpBuilder addPlan(BusinessPlan plan) {
            this.plan = plan;
            return this;
        }

        public StartUpBuilder addStartCapital(Long startCapital) {
            this.startCapital = startCapital;
            return this;
        }

        public StartUpBuilder addLocation(String location) {
            this.location = location;
            return this;
        }

        public StartUp build() {
            return new StartUp(this);
        }
    }
}
