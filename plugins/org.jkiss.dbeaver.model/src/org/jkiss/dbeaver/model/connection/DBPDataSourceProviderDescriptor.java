/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2023 DBeaver Corp and others
 *
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
package org.jkiss.dbeaver.model.connection;

import org.jkiss.code.NotNull;
import org.jkiss.code.Nullable;
import org.jkiss.dbeaver.model.DBPImage;
import org.jkiss.dbeaver.model.DBPNamedObject;
import org.jkiss.dbeaver.model.navigator.meta.DBXTreeDescriptor;
import org.jkiss.dbeaver.model.sql.SQLDialectMetadata;

import java.util.List;

/**
 * Data source provider descriptor
 */
public interface DBPDataSourceProviderDescriptor extends DBPNamedObject {

    String getId();

    String getDescription();

    DBPImage getIcon();

    boolean isDriversManagable();

    List<? extends DBPDriver> getEnabledDrivers();

    String getPluginId();

    DBXTreeDescriptor getTreeDescriptor();

    @NotNull
    SQLDialectMetadata getScriptDialect();

    boolean isTemporary();

    @Nullable
    DBPDriver getDriver(@NotNull String id);

    List<? extends DBPDriver> getDrivers();

    DBPDataSourceProviderDescriptor getParentProvider();

    @NotNull
    List<DBPDataSourceProviderDescriptor> getChildrenProviders();

    // Returns true if this provider or one of parent providers has specified ID
    boolean matchesId(String id);
}
