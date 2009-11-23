package org.infinispan.config;

import org.testng.annotations.Test;
import org.infinispan.test.SingleCacheManagerTest;
import org.infinispan.manager.CacheManager;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.util.concurrent.IsolationLevel;

import java.util.Properties;

/**
 * /**
 * Tests that string property replacement works properly when parsing
 * a config file.
 *
 * @author Mircea.Markus@jboss.com
 */
@Test (groups = "functional", testName = "config.StringPropertyReplacementTest")
public class StringPropertyReplacementTest extends SingleCacheManagerTest {


   protected CacheManager createCacheManager() throws Exception {
      System.setProperty("test.property.asyncListenerMaxThreads","2");
      System.setProperty("test.property.IsolationLevel","READ_COMMITTED");
      System.setProperty("test.property.writeSkewCheck","true");
      System.setProperty("test.property.SyncCommitPhase","true");
      return new DefaultCacheManager("configs/string-property-replaced.xml");
   }

   public void testGlobalConfig() {
      Properties asyncListenerExecutorProperties = cacheManager.getGlobalConfiguration().getAsyncListenerExecutorProperties();
      asyncListenerExecutorProperties.get("maxThreads").equals("2");

      Properties transportProps = cacheManager.getGlobalConfiguration().getTransportProperties();
      assert transportProps.get("configurationFile").equals("config-samples/jgroups-tcp.xml");
   }

   public void testDefaultCache() {
      Configuration configuration = cacheManager.getCache().getConfiguration();
      assert configuration.getIsolationLevel().equals(IsolationLevel.READ_COMMITTED);
      assert !configuration.isWriteSkewCheck();      
      assert configuration.isSyncCommitPhase();
   }
}

