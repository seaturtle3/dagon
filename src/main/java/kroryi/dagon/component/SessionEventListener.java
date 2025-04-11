package kroryi.dagon.component;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SessionEventListener implements HttpSessionListener {

    private static final Logger logger = LoggerFactory.getLogger(SessionEventListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        logger.info("세션 생성: " + event.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        logger.info("세션 만료 또는 소멸: " + event.getSession().getId());
    }
}
