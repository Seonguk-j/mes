package team_2p4p.mes.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "bom")
public class Bom {

    @Id
    @Column(name="bom_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "product_name")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material1", referencedColumnName = "item_id")
    private Item item1;

    @Column(nullable = false, name="material1_amount")
    private String material1_amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material2", referencedColumnName = "item_id")
    private Item item2;

    @Column(nullable = false, name="material2_amount")
    private String material2_amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material3", referencedColumnName = "item_id")
    private Item item3;

    @Column(nullable = false, name="material3_amount")
    private String material3_amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material4", referencedColumnName = "item_id")
    private Item item4;

    @Column(nullable = false, name="material4_amount")
    private String material4_amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material5", referencedColumnName = "item_id")
    private Item item5;

    @Column(nullable = false, name="material5_amount")
    private String material5_amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material6", referencedColumnName = "item_id")
    private Item item6;

    @Column(nullable = false, name="material6_amount")
    private String material6_amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material7", referencedColumnName = "item_id")
    private Item item7;

    @Column(nullable = false, name="material7_amount")
    private String material7_amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material8", referencedColumnName = "item_id")
    private Item item8;

    @Column(nullable = false, name="material8_amount")
    private String material8_amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material9", referencedColumnName = "item_id")
    private Item item9;

    @Column(nullable = false, name="material9_amount")
    private String material9_amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material10", referencedColumnName = "item_id")
    private Item item10;

    @Column(nullable = false, name="material10_amount")
    private String material10_amount;
}
