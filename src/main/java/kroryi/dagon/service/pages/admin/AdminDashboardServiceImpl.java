package kroryi.dagon.service.pages.admin;

import kroryi.dagon.repository.PartnerApplicationRepository;
import kroryi.dagon.repository.PartnerRepository;
import kroryi.dagon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {
    private final UserRepository userRepository;
    private final PartnerApplicationRepository applicationRepository;
    private final PartnerRepository partnerRepository;

    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public long getTotalApplications() {
        return applicationRepository.count();
    }

    @Override
    public long getApprovedPartners() {
        return partnerRepository.count();
    }





}
