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

package org.kie.workbench.common.stunner.bpmn.client.forms.filters;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.kie.workbench.common.forms.adf.engine.shared.FormElementFilter;
import org.kie.workbench.common.stunner.core.client.api.SessionManager;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvasHandler;
import org.kie.workbench.common.stunner.core.graph.Element;
import org.kie.workbench.common.stunner.core.graph.Node;
import org.kie.workbench.common.stunner.core.graph.content.definition.Definition;
import org.kie.workbench.common.stunner.core.graph.util.GraphUtils;
import org.kie.workbench.common.stunner.forms.client.formFilters.StunnerFormElementFilterProvider;

public class CatchingIntermediateEventFilterProvider implements StunnerFormElementFilterProvider {

    private final SessionManager sessionManager;
    private final Supplier<Class<?>> definitionSupplier;

    public CatchingIntermediateEventFilterProvider(final SessionManager sessionManager,
                                                   final Class<?> intermediateEventClass) {
        this.sessionManager = sessionManager;
        this.definitionSupplier = () -> intermediateEventClass;
    }

    @Override
    public Class<?> getDefinitionType() {
        return definitionSupplier.get();
    }

    @Override
    public Collection<FormElementFilter> provideFilters(final String uuid,
                                                        final Element<? extends Definition<?>> element,
                                                        final Object definition) {
        final Predicate predicate = o -> isBoundaryEvent(uuid);
        final FormElementFilter isInterruptingFilter = new FormElementFilter("executionSet.cancelActivity",
                                                                             predicate);
        return Arrays.asList(isInterruptingFilter);
    }

    private boolean isBoundaryEvent(final String uuid) {
        final AbstractCanvasHandler canvasHandler = (AbstractCanvasHandler) sessionManager.getCurrentSession().getCanvasHandler();
        final Node node = canvasHandler.getGraphIndex().getNode(uuid);
        return GraphUtils.isDockedNode(node);
    }
}
