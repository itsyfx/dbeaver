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
package org.jkiss.dbeaver.model.lsm.sql.dialect;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.jkiss.dbeaver.Log;
import org.jkiss.dbeaver.model.impl.AbstractDescriptor;
import org.jkiss.dbeaver.model.lsm.LSMAnalyzer;
import org.jkiss.dbeaver.model.sql.SQLDialect;
import org.osgi.framework.Bundle;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


public class LSMDialectRegistry {
    
    static final String EXTENSION_ID = "org.jkiss.dbeaver.lsm.dialectSyntax"; //$NON-NLS-1$
    
    private static final Log log = Log.getLog(LSMDialectRegistry.class);

    private static LSMDialectRegistry instance = null;

    /**
     * Returns an instance of this singleton
     */
    public static synchronized LSMDialectRegistry getInstance() {
        if (instance == null) {
            instance = new LSMDialectRegistry();
            instance.loadExtensions(Platform.getExtensionRegistry());
        }
        return instance;
    }

    private final Map<Class<? extends SQLDialect>, LSMAnalyzer> knownLsmAnalyzerByDialects = new HashMap<>();

    private LSMDialectRegistry() {
    }

    private void loadExtensions(IExtensionRegistry registry) {
        IConfigurationElement[] extConfigs = registry.getConfigurationElementsFor(EXTENSION_ID);
        
        Stream.of(extConfigs).filter(e -> "lsmDialect".equals(e.getName())).forEach(this::registerLsmDialect);
    }
    
    private void registerLsmDialect(IConfigurationElement dialectElt) {
        Bundle bundle = Platform.getBundle(dialectElt.getContributor().getName());
        try {
            LSMAnalyzer analyzer = (LSMAnalyzer)dialectElt.createExecutableExtension("analyzerClass");
            for (IConfigurationElement sqlDialectRef: dialectElt.getChildren("appliesTo")) {
                String dialectClassName = sqlDialectRef.getAttribute("dialectClass");
                Class<? extends SQLDialect> dialectType = AbstractDescriptor.getObjectClass(bundle, dialectClassName, SQLDialect.class);
                knownLsmAnalyzerByDialects.put(dialectType, analyzer);
            }
        } catch (CoreException e) {
            log.error("Failed to register LSM dialect " + dialectElt.getAttribute("analyzerClass"), e);
        }
    }

    public LSMAnalyzer getAnalyzerForDialect(SQLDialect dialect) {
        Class<?> dialectClass = dialect.getClass();
        LSMAnalyzer analyzer;
        do {
            analyzer = knownLsmAnalyzerByDialects.get(dialectClass);
            dialectClass = dialectClass.getSuperclass();
        } while (analyzer == null && dialectClass != null);
        return analyzer;
    }
}