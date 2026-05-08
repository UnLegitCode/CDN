package ru.unlegit.cdn.coordinator.eventlistener;

import com.netflix.appinfo.InstanceInfo;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.unlegit.cdn.coordinator.service.CdnCoordinatorService;
import ru.unlegit.cdn.coordinator.util.ServiceId;

@Component
@AllArgsConstructor
public final class EurekaEventListener {

    private final CdnCoordinatorService nodeService;

    @EventListener
    public void onInstanceRegistration(EurekaInstanceRegisteredEvent event) {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        ServiceId serviceId = ServiceId.get(instanceInfo.getAppName());

        if (serviceId == ServiceId.NODE) {
            nodeService.updateNode(instanceInfo);
        }
    }

    @EventListener
    public void onInstanceRenew(EurekaInstanceRenewedEvent event) {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        ServiceId serviceId = ServiceId.get(instanceInfo.getAppName());

        if (serviceId == ServiceId.NODE) {
            nodeService.updateNode(instanceInfo);
        }
    }

    @EventListener
    public void onInstanceCancellation(EurekaInstanceCanceledEvent event) {
        ServiceId serviceId = ServiceId.get(event.getAppName());

        if (serviceId == ServiceId.NODE) {
            nodeService.removeNode(event.getServerId());
        }
    }
}