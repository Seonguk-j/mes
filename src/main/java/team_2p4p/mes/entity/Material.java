package team_2p4p.mes.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "material")
public class Material {

    @Id
    @Column(name="material_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long materialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "order_id")
    private OrderMaterial orderMaterial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "item_id")
    private Item item;

    @Column(nullable = false, name="material_stock")
    private Long materialStock;

    @Column(nullable = false, name="warehouse_date")
    private LocalDateTime warehouseDate;

    @Column(nullable = false, name="material_stat")
    private int materialStat;

    public void updateMaterial(int materialStat, LocalDateTime warehouseDate){
        this.materialId = getMaterialId();
        this.orderMaterial = getOrderMaterial();
        this.item = getItem();
        this.materialStock = getMaterialStock();
        this.warehouseDate = warehouseDate;
        this.materialStat = materialStat;
    }
}
