package net.bigmir.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "business_plans")
public class BusinessPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToMany(mappedBy = "plan", cascade = {CascadeType.MERGE,CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.DETACH})
//    private List<Task> tasks = new LinkedList<>();

    @OneToOne(mappedBy = "plan", cascade = {CascadeType.MERGE,CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.DETACH})
    @JoinColumn(name = "startup_id")
    private StartUp startUp;


}
