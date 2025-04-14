package kroryi.dagon.repository;


import kroryi.dagon.entity.ApiKeyCallbackUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiKeyCallbackUrlRepository extends JpaRepository<ApiKeyCallbackUrl, Long> {
    List<ApiKeyCallbackUrl> findByApiKey_Key(String apiKey);
}