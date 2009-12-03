/*
* Copyright 2009 the original author or authors.
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

package griffon.spring.artifact

import griffon.core.ArtifactInfo
// import grails.spring.BeanBuilder
// import griffon.spring.factory.support.ArtifactFactoryBean;

/**
 * @author Andres Almiray (aalmiray)
 */
class SpringDomainArtifactHandler extends SpringArtifactHandlerAdapter {
    SpringDomainArtifactHandler() {
        super("domain")
    }

    void initialize(ArtifactInfo[] artifacts) {
        super.initialize(artifacts)
        if(!artifacts) return
        // registerArtifactBeans(artifacts)
    }

    public ArtifactInfo findArtifact(String name) {
        String simpleName = null
        if(name.length() == 1) {
            simpleName = name.toLowerCase()
        } else {
            simpleName = name[0].toLowerCase() + name[1..-1]
        }
        for(ArtifactInfo artifactInfo : artifacts) {
            if(artifactInfo.simpleName == simpleName) return artifactInfo
        }
        return null
    }
/*
    protected void registerArtifactBeans(ArtifactInfo[] artifacts) {
        doWithBeanBuilder { 
            artifacts.each { art ->
                "${art.simpleName}"(ArtifactFactoryBean) { bean ->
                    bean.scope = "singleton"
                    bean.autowire = "byName"
                    artifact = art
                }
            }
        }
    }
*/
}
