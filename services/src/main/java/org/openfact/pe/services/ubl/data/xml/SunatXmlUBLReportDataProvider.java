/*******************************************************************************
 * Copyright 2016 Sistcoop, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.openfact.pe.services.ubl.data.xml;

import org.jboss.logging.Logger;
import org.openfact.models.DocumentModel;
import org.openfact.models.OpenfactSession;
import org.openfact.report.ReportDataProvider;
import org.openfact.ubl.UBLReportDataProvider;
import org.openfact.ubl.data.xml.XmlSupportedAttribute;

public class SunatXmlUBLReportDataProvider implements UBLReportDataProvider {

    private static final Logger logger = Logger.getLogger(org.openfact.ubl.data.xml.XmlUBLReportDataProvider.class);

    protected OpenfactSession session;

    public SunatXmlUBLReportDataProvider(OpenfactSession session) {
        this.session = session;
    }

    @Override
    public int getProviderPriority() {
        return 0;
    }

    @Override
    public ReportDataProvider<DocumentModel> document() {
        return new ReportDataProvider<DocumentModel>() {
            @Override
            public Object getFieldValue(DocumentModel document, String fieldName) {
                SunatXmlSupportedAttribute attribute = SunatXmlSupportedAttribute.fromString(fieldName.toUpperCase());
                if (attribute != null) {
                    return attribute.asObject(document.getXmlAsJSONObject());
                }
                return null;
            }

            @Override
            public void close() {
            }
        };
    }

    @Override
    public void close() {
    }
}
