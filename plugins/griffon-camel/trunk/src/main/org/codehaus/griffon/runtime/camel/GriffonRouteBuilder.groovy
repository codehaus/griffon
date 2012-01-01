/* 
 * Copyright 2010 the original author or authors.
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
package org.codehaus.griffon.runtime.camel

import org.apache.camel.builder.RouteBuilder

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class GriffonRouteBuilder extends RouteBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(GriffonRouteBuilder)
    final Closure routes

    GriffonRouteBuilder(Closure routes) {
        this.routes = routes
    }

    void configure() {
        routes.delegate = this
        routes.resolveStrategy = Closure.DELEGATE_FIRST
        LOG.debug "Configuring routes using ${routes.class.name}"
        routes()
    }
}
