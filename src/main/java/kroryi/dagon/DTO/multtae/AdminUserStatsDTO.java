package kroryi.dagon.DTO.multtae;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserStatsDTO {
    private Long totalUsers;
    private Long approvedPartners;
    private Long pendingPartners;
    private Long rejectedPartners;
}
