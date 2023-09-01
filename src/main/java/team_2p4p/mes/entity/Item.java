package team_2p4p.mes.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "ITEM")
public class Item {

    @Id
    @Column(name="ITEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(nullable = false, name="ITEM_CODE")
    private String itemCode;

    @Column(name="item_name")
    private String itemName;

    @Column(nullable = false, name="item_stat")
    private String itemStat;


}
