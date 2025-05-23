package kroryi.dagon.service;

import kroryi.dagon.entity.Inquiry;
import kroryi.dagon.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    public Inquiry findById(Long id) {
        return inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("inquiry not found"));
    }

    public List<Inquiry> findAll() {
        return inquiryRepository.findAll();
    }
}
