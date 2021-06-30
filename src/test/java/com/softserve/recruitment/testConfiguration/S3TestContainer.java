package com.softserve.recruitment.testConfiguration;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import java.util.function.Consumer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class S3TestContainer {
  private static final String DATABASE_IMAGE = "findify/s3mock:latest";
  private static final int PORT = 8001;
  private static final GenericContainer DATABASE_CONTAINER = new GenericContainer(DockerImageName.parse(DATABASE_IMAGE))
      .withExposedPorts(PORT)
      .withCreateContainerCmdModifier((Consumer<CreateContainerCmd>) cmd -> cmd.withHostConfig(
          new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(PORT), new ExposedPort(PORT)))
      ));
  static {
    DATABASE_CONTAINER.start();
  }
}
