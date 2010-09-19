/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */

package org.jclouds.filesystem.integration;

import java.io.IOException;
import java.util.Properties;

import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.BlobStoreContextFactory;
import org.jclouds.blobstore.integration.TransientBlobStoreTestInitializer;
import org.jclouds.blobstore.integration.internal.BaseBlobStoreIntegrationTest;
import org.jclouds.logging.log4j.config.Log4JLoggingModule;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

import org.jclouds.filesystem.reference.FilesystemConstants;
import org.jclouds.filesystem.utils.TestUtils;

/**
 * 
 * @author Adrian Cole
 */
public class FilesystemTestInitializer extends TransientBlobStoreTestInitializer {

   @Override
   protected BlobStoreContext createLiveContext(Module configurationModule, String url, String app,
            String identity, String key) throws IOException {
      BaseBlobStoreIntegrationTest.SANITY_CHECK_RETURNED_BUCKET_NAME = true;

      Properties prop = new Properties();
      prop.setProperty(FilesystemConstants.PROPERTY_BASEDIR, TestUtils.TARGET_BASE_DIR);
      return new  BlobStoreContextFactory().createContext(
            "filesystem",
            ImmutableSet.of(configurationModule, new Log4JLoggingModule()),
            prop);
   }

}