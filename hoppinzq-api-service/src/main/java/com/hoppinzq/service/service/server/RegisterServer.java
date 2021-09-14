package com.hoppinzq.service.service.server;



import com.hoppinzq.service.service.ServiceWrapper;

import java.util.List;

public interface RegisterServer {

    void insertService(ServiceWrapper serviceWrapper);

    void insertServices(List<ServiceWrapper> serviceWrappers);
}
