/*
* Copyright 2009-2010 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the 'License');
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an 'AS IS' BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import twitter4j.TwitterFactory;
import twitter.TwitterAccess as TA

class TwitterGriffonAddon {

	private static final Log logger = LogFactory.getLog(TwitterGriffonAddon)
	
 /* adds application event handlers */
    def events = [
	 /* If user hasn't beign authenticated yet it can throw an event to get authenticated. If
	  * authentication succeed then a loginSucceed event will be thrown otherwise a 
	  * loginFailed will be thrown instead */
        "loginRequested": {username,password -> 
			def at 
			def exception
		 /* Trying to log on */
			try{ 
				at = TA.getAccessToken(username,password)
			 /* If everything goes ok we send the access token */
				app.event("loginSucceed",[new TwitterFactory().getInstance(at)])
			} catch (ex){
			 /* Otherwise we send the failure cause */
				app.event("loginFailed",[ex])
			}
		},
	/* Every time a user wants to use his authentication credentials to do anything on his behalf it
	 * should throw this event. If everithing goes fine then a credentialsFound event will be
	 * thrown. If credentials are not stored then a credentialsNotFound will be thrown */
		"credentialsRequested": {
			def at
			def exception
		 /* Getting the access the stored token */ 
			try{ 
				at = TA.loadAccessToken()
				if (at){
					app.event("credentialsFound",[new TwitterFactory().getInstance(at)])
				} else {
					app.event("credentialsNotFound",[ex])
				}
			} catch (ex){
				app.event("credentialsNotFound",[ex])
			}
		},
	 /* Maybe at any time the user may want to use another username/password */
		"credentialsDeletion":{
			try{
				TA.deleteAccessToken()
				app.event("credentialsDeletionSuceed")
			} catch (ex){
				app.event("credentialsDeletionFailed",[ex])
			}
		},
	 /* A Twitter4j instance will be injected in every controller by default */
		NewInstance: { klass, type, instance ->
			/* If there's no configuration property about twitter then twitter
			 * instance only will be injected into controllers */
			   def types = app.config.griffon?.twitter?.injectInto ?: ['controller']
			   if (!types.contains(type)) return
			   def mc = app.artifactManager.findGriffonClass(klass).metaClass
			   def at
			   try{ at = TA.loadAccessToken() } catch (ex){
				   logger.info("Injecting twitter instance without credentials")
			   }
			   mc.twitter = at ? 
			   	new TwitterFactory().getInstance(at) : 
				new TwitterFactory().getInstance() 
		}
    ]
}
