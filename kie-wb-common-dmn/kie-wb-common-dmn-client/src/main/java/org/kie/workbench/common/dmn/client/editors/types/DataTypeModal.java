/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.dmn.client.editors.types;

import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.workbench.common.dmn.client.editors.types.common.DataTypeFactory;
import org.kie.workbench.common.dmn.client.editors.types.common.ItemDefinitionUtils;
import org.kie.workbench.common.dmn.client.editors.types.listview.DataTypeList;
import org.kie.workbench.common.dmn.client.editors.types.persistence.DataTypeStore;
import org.kie.workbench.common.dmn.client.editors.types.persistence.ItemDefinitionStore;
import org.uberfire.ext.editor.commons.client.file.popups.elemental2.Elemental2Modal;

@ApplicationScoped
public class DataTypeModal extends Elemental2Modal<DataTypeModal.View> {

    private static final String MODAL_WIDTH = "800px";

    private final DataTypeList treeList;

    private final ItemDefinitionUtils itemDefinitionUtils;

    private final DataTypeFactory dataTypeFactory;

    private final ItemDefinitionStore definitionStore;

    private final DataTypeStore dataTypeStore;

    @Inject
    public DataTypeModal(final View view,
                         final DataTypeList treeList,
                         final ItemDefinitionUtils itemDefinitionUtils,
                         final DataTypeFactory dataTypeFactory,
                         final ItemDefinitionStore definitionStore,
                         final DataTypeStore dataTypeStore) {
        super(view);

        this.treeList = treeList;
        this.itemDefinitionUtils = itemDefinitionUtils;
        this.dataTypeFactory = dataTypeFactory;
        this.definitionStore = definitionStore;
        this.dataTypeStore = dataTypeStore;
    }

    @PostConstruct
    public void setup() {
        super.setup();
        setWidth(MODAL_WIDTH);
        getView().setup(treeList);
    }

    public void show() {
        cleanDataTypeStore();
        loadDataTypes();
        superShow();
    }

    void cleanDataTypeStore() {
        definitionStore.clear();
        dataTypeStore.clear();
    }

    void loadDataTypes() {
        treeList.setupItems(itemDefinitionUtils
                                    .all()
                                    .stream()
                                    .map(dataTypeFactory::makeStandardDataType)
                                    .collect(Collectors.toList()));
    }

    void superShow() {
        super.show();
    }

    public interface View extends Elemental2Modal.View<DataTypeModal> {

        void setup(final DataTypeList treeGrid);
    }
}
