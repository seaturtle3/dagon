package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "faq_category")
@Data
@NoArgsConstructor
public class FAQCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String name;

    @Column(name = "display_order")
    @org.hibernate.annotations.ColumnDefault("0")
    private Integer displayOrder;

    public FAQCategory(String name, Integer displayOrder) {
        this.name = name;
        this.displayOrder = displayOrder;
    }
}