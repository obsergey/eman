package org.osergey.dept.domain;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "dept", schema = "employee")
public class Employee {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept")
    private Dept dept;

    @Column(name = "name")
    private String name;

    @Column(name = "position")
    private String position;
}
