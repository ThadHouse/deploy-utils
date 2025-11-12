package org.wpilib.deployutils.deploy.sessions;

public interface IPSessionController extends SessionController {
    String getHost();
    int getPort();
}
