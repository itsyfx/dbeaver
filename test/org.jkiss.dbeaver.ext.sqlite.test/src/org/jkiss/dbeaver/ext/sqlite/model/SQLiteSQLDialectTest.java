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
package org.jkiss.dbeaver.ext.sqlite.model;

import org.jkiss.dbeaver.model.struct.DBSTypedObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SQLiteSQLDialectTest {

    @Mock
    DBSTypedObject mockAttribute;

    @Test
    public void quoteInfinities() {
        final SQLiteSQLDialect dialect = new SQLiteSQLDialect();
        Assert.assertEquals("'Infinity'", dialect.escapeScriptValue(mockAttribute, Float.POSITIVE_INFINITY, ""));
        Assert.assertEquals("'-Infinity'", dialect.escapeScriptValue(mockAttribute, Float.NEGATIVE_INFINITY, ""));
        Assert.assertEquals("'Infinity'", dialect.escapeScriptValue(mockAttribute, Double.POSITIVE_INFINITY, ""));
        Assert.assertEquals("'-Infinity'", dialect.escapeScriptValue(mockAttribute, Double.NEGATIVE_INFINITY, ""));
    }

    @Test
    public void quoteNaNs() {
        final SQLiteSQLDialect dialect = new SQLiteSQLDialect();
        Assert.assertEquals("'NaN'", dialect.escapeScriptValue(mockAttribute, Float.NaN, ""));
        Assert.assertEquals("'NaN'", dialect.escapeScriptValue(mockAttribute, Float.POSITIVE_INFINITY - Float.POSITIVE_INFINITY, ""));
        Assert.assertEquals("'NaN'", dialect.escapeScriptValue(mockAttribute, Double.NaN, ""));
        Assert.assertEquals("'NaN'", dialect.escapeScriptValue(mockAttribute, Double.NEGATIVE_INFINITY * 0, ""));
    }
}
