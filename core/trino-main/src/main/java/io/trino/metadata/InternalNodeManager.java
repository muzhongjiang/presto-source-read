/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.metadata;

import io.trino.connector.CatalogName;

import java.util.Set;
import java.util.function.Consumer;
/**
 * 系统内部节点管理
 */
public interface InternalNodeManager
{
    Set<InternalNode> getNodes(NodeState state);

    Set<InternalNode> getActiveConnectorNodes(CatalogName catalogName);

    InternalNode getCurrentNode();

    /**获取Coordinator节点*/
    Set<InternalNode> getCoordinators();

    /**获取所有节点*/
    AllNodes getAllNodes();

    /**刷新节点*/
    void refreshNodes();

    void addNodeChangeListener(Consumer<AllNodes> listener);

    void removeNodeChangeListener(Consumer<AllNodes> listener);
}
