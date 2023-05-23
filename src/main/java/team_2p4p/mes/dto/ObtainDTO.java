//package team_2p4p.mes.dto;
//
//import java.time.LocalDateTime;
//import team_2p4p.mes.entity.Obtain;
//
//
//public class ObtainDTO {
//    private Long obtainId;
//    private Long itemId;
//    private Long customerId;
//    private Long obtainAmount;
//    private LocalDateTime obtainDate;
//    private LocalDateTime customerRequestDate;
//    private LocalDateTime expectDate;
//    private boolean obtainStat;
//    private LocalDateTime obtainStatDate;
//
//    // 기본 생성자
//    public ObtainDTO() {
//    }
//
//
//    public static ObtainDTO fromEntity(Obtain obtain) {
//        ObtainDTO dto = new ObtainDTO();
//        dto.setObtainId(obtain.getObtainId());
//        dto.setItemId(obtain.getItem().getItemId());
//        dto.setCustomerId(obtain.getCustomerId().getCustomerId());
//        dto.setObtainAmount(obtain.getObtainAmount());
//        dto.setObtainDate(obtain.getObtainDate());
//        dto.setCustomerRequestDate(obtain.getCustomerRequestDate());
//        dto.setExpectDate(obtain.getExpectDate());
//        dto.setObtainStat(obtain.isObtainStat());
//        dto.setObtainStatDate(obtain.getObtainStatDate());
//        return dto;
//    }
//    public Obtain toEntity() {
//        Obtain obtain = new Obtain();
//        obtain.setObtainId(this.obtainId);
//        obtain.setItemId(this.itemId);
//
//        return obtain;
//    }
//}
