package au.com.origin.snapshots.junit5;

import au.com.origin.snapshots.SnapshotConfig;

import java.lang.reflect.Method;

public class JUnit5SnapshotConfig implements SnapshotConfig {

  @Override
  public Class<?> getTestClass() {
    throw new RuntimeException("You forgot to add @ExtendWith(SnapshotExtension.class) to the class");
  }

  @Override
  public Method getTestMethod(Class<?> testClass) {
    throw new RuntimeException("You forgot to add @ExtendWith(SnapshotExtension.class) to the class");
  }

}
