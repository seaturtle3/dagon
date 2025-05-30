package kroryi.dagon.service;

import kroryi.dagon.DTO.multtae.AdminUserStatsDTO;

public interface AdminDashboardService {
    long getTotalUsers();
    long getTotalApplications();
    long getApprovedPartners();

}
