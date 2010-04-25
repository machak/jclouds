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
package org.jclouds.gogrid.compute.functions;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.InetAddress;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeState;
import org.jclouds.compute.domain.internal.NodeMetadataImpl;
import org.jclouds.domain.Credentials;
import org.jclouds.domain.Location;
import org.jclouds.gogrid.domain.Server;
import org.jclouds.gogrid.services.GridServerClient;

import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * @author Oleksiy Yarmula
 */
@Singleton
public class ServerToNodeMetadata implements Function<Server, NodeMetadata> {
   private final Map<String, NodeState> serverStateToNodeState;
   private final GridServerClient client;
   private final Location location;
   private final Map<String, ? extends Image> images;

   @Inject
   ServerToNodeMetadata(Map<String, NodeState> serverStateToNodeState, GridServerClient client,
            Map<String, ? extends Image> images, Location location) {
      this.serverStateToNodeState = checkNotNull(serverStateToNodeState, "serverStateToNodeState");
      this.client = checkNotNull(client, "client");
      this.images = checkNotNull(images, "images");
      this.location = checkNotNull(location, "location");
   }

   @Override
   public NodeMetadata apply(Server from) {
      String tag = CharMatcher.JAVA_LETTER.retainFrom(from.getName());
      Set<InetAddress> ipSet = ImmutableSet.of(from.getIp().getIp());
      NodeState state = serverStateToNodeState.get(from.getState().getName());
      Credentials creds = client.getServerCredentialsList().get(from.getName());
      return new NodeMetadataImpl(from.getId() + "", from.getName(), location, null, ImmutableMap
               .<String, String> of(), tag, images.get(from.getImage().getId() + ""), state, ipSet,
               ImmutableList.<InetAddress> of(), ImmutableMap.<String, String> of(), creds);
   }
}