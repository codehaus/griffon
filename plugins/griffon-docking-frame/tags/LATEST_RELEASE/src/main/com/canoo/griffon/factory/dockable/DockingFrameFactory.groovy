/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.canoo.griffon.factory.dockable;

import groovy.swing.factory.BeanFactory;
import bibliothek.gui.dock.SplitDockStation
import bibliothek.gui.dock.DefaultDockable
import java.awt.Component
import java.awt.Container;
import java.awt.Window

/**
 * @author Per Junel
 * @author Christoph Lipp
 */
public class DockingFrameFactory extends BeanFactory {
	public static final String DELEGATE_PROPERTY_TITLE = "_delegateProperty:title";
	public static final String DEFAULT_DELEGATE_PROPERTY_TITLE = "title";
	public static final String STATION_INSTANCE = "stationInstance";
	public static final String CONTEXT_DATA_KEY = "DockStationFactoryData";
	
	public DockingFrameFactory() {
		super(SplitDockStation.class, false)
	}
	
	public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
		def dockController = builder.app.dockController
		def newStation = super.newInstance(builder, name, value, attributes)
		
		dockController.add(newStation)
		
		
		builder.context.dockStationFactoryClosure =
				{ FactoryBuilderSupport cBuilder, Object cNode, Map cAttributes ->
					if (builder.current == newStation.component) inspectChild(cBuilder, cNode, cAttributes)
				}
		builder.addAttributeDelegate(builder.context.dockStationFactoryClosure)
		
		builder.context[STATION_INSTANCE] = newStation
		builder.context[DELEGATE_PROPERTY_TITLE] = attributes.remove("titleProperty") ?: DEFAULT_DELEGATE_PROPERTY_TITLE
		
		return newStation.component;
	}
	
	public static void inspectChild(FactoryBuilderSupport builder, Object node, Map attributes) {
		if (!(node instanceof Component) || (node instanceof Window)) {
			return;
		}
		def name = attributes.remove(builder?.parentContext?.getAt(DELEGATE_PROPERTY_TITLE) ?: DEFAULT_DELEGATE_PROPERTY_TITLE)
		def dockControllerContext = builder.context.get(CONTEXT_DATA_KEY) ?: [:];
		if (dockControllerContext.isEmpty()) {
			builder.context.put(CONTEXT_DATA_KEY, dockControllerContext)
		}
		dockControllerContext.put(node, [name])
	}
	
	public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
		if (!(child instanceof Component) || (child instanceof Window)) {
			return;
		}
		def station = builder.parentContext[STATION_INSTANCE]
		try {
			def title = builder.context[CONTEXT_DATA_KEY]?.get(child) ?: [null]
			if (title[0] == null) {
				title[0] = child.name
			}
			station.addDockable(new DefaultDockable(child, title[0]))
		} catch (MissingPropertyException mpe) {
			station.add(child)
		}
	}
	
	public void onNodeCompleted( FactoryBuilderSupport builder, Object parent, Object node ) {
		super.onNodeCompleted (builder, parent, node)
		builder.removeAttributeDelegate(builder.context.dockStationFactoryClosure)
	}
}
