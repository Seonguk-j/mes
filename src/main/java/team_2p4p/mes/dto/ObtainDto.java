package team_2p4p.mes.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

//게시글 요청(Request) 클래스
@Getter @Setter
public class ObtainDto {

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemName;        // 상품명

    @NotBlank(message = "수량은 필수 입력 값입니다.")
    private Long obtainAmount;      // 수량

    @NotBlank(message = "납기일은 필수 입력 값입니다.")
    private LocalDateTime obtainDate;    // 납기일

}
