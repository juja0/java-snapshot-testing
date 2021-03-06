package au.com.origin.snapshots;

import au.com.origin.snapshots.junit4.JUnit4SnapshotConfig;
import org.junit.After;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class Junit4SnapshotConfigTest {

  @After
  public void after() {
    System.clearProperty(SnapshotConfig.JVM_UPDATE_SNAPSHOTS_PARAMETER);
  }

  @Test
  public void shouldNotUpdateSnapshotNotPassed() {
    SnapshotConfig snapshotConfig = new JUnit4SnapshotConfig();
    assertThat(snapshotConfig.updateSnapshot().isPresent()).isFalse();
  }

  @Test
  public void shouldUpdateSnapshotPassed() {
    System.setProperty(SnapshotConfig.JVM_UPDATE_SNAPSHOTS_PARAMETER, "example");
    SnapshotConfig snapshotConfig = new JUnit4SnapshotConfig();
    assertThat(snapshotConfig.updateSnapshot().get()).isEqualTo("example");
  }

}
