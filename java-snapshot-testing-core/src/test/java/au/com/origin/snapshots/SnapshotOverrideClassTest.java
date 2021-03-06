package au.com.origin.snapshots;

import au.com.origin.snapshots.config.BaseSnapshotConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static au.com.origin.snapshots.SnapshotMatcher.start;
import static au.com.origin.snapshots.SnapshotMatcher.validateSnapshots;

public class SnapshotOverrideClassTest extends SnapshotSuperClassTest {

  @BeforeAll
  static void beforeAll() {
    start(new BaseSnapshotConfig());
  }

  @AfterAll
  static void afterAll() {
    validateSnapshots();
  }

  @Override
  public String getName() {
    return "anyName";
  }
}
