package com.softserve.recruitment.testConfiguration;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import java.time.Duration;
import java.util.function.Consumer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;

public abstract class S3TestContainer {
  private static final String DATABASE_IMAGE = "minio/minio";
  private static final int PORT = 9000;
  private static final String ACCESS_KEY = "test";
  private static final String SECRET_KEY = "testtesttest";

  private static final GenericContainer DATABASE_CONTAINER = new GenericContainer(DATABASE_IMAGE)
      .withEnv("MINIO_ACCESS_KEY", ACCESS_KEY)
      .withEnv("MINIO_SECRET_KEY", SECRET_KEY)
      .withCommand("server /data")
      .withExposedPorts(PORT)
      .waitingFor(new HttpWaitStrategy()
          .forPath("/minio/health/ready")
          .forPort(PORT)
          .withStartupTimeout(Duration.ofSeconds(10)))
      .withCreateContainerCmdModifier((Consumer<CreateContainerCmd>) cmd -> cmd.withHostConfig(
          new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(PORT), new ExposedPort(PORT)))
      ));


  static {
   DATABASE_CONTAINER.start();
  }
}
