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
package twitter

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import twitter4j.Twitter
import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.auth.RequestToken

import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.DomNodeList
import com.gargoylesoftware.htmlunit.html.HtmlElement
import com.gargoylesoftware.htmlunit.html.HtmlForm
import com.gargoylesoftware.htmlunit.html.HtmlInput
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput

/**
 * Griffon Twitter Access
 * 
 * @author mgg
 *
 */
class TwitterAccess {
	
	private static final Logger logger = LoggerFactory.getLogger(TwitterAccess)
	
	/**
	 * Here we use HtmlUnit to create a conversation against the Twitter authorization service.
	 * 
	 * @param username
	 * @param password
	 * 
	 * @return AccessToken needed to access Twitter in our own behalf
	 * 
	 */
	static getAccessToken(username,password) throws TwitterException{
	 /* Getting the twitter instance with the application keys (look in application resources) */		
		Twitter twitter = new TwitterFactory().getInstance();
		RequestToken requestToken = twitter.getOAuthRequestToken();
		AccessToken accessToken = loadAccessToken();
	 /* If we haven't stored our accesstoken previously */
		if (null == accessToken) {
			if (logger.isDebugEnabled()){
				logger.debug "Accessing Twitter Authorization"
			}
		 /* I want always to be as much safe as I can */
			String requestUrl = requestToken.getAuthorizationURL().replace("http","https");
		 /* We act as if we were using a Firefox browser */
			WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3_6);
		 /* Last version of twitter has some unstable javascript */
			webClient.setJavaScriptEnabled(false)
			HtmlPage htmlPage =  webClient.getPage(requestUrl);
			HtmlForm htmlForm = htmlPage.getForms().get(0);
		 /* Setting username and password */
			if (logger.isDebugEnabled()){
				logger.debug "Filling out authentication form"
			}
			HtmlInput.class.cast(htmlForm.getElementById("username_or_email")).setValueAttribute(username)
			HtmlPasswordInput.class.cast(htmlForm.getElementById("password")).setValueAttribute(password)
			HtmlPage pinPage = HtmlSubmitInput.class.cast(htmlForm.getElementById("allow")).click()
		 /* Once the authentication form has been submitted then we try to get the oauth pin */
			DomNodeList<HtmlElement> codeElements = pinPage.getElementsByTagName("code")
			String pin = codeElements ? codeElements.get(0).getTextContent().trim() : "";
			try{
			 /* If we get the pin */
				if(pin.length() > 0){
					if (logger.isDebugEnabled()){
						logger.debug "Getting authentication pin"
					}
					accessToken = twitter.getOAuthAccessToken(requestToken, pin);
			 /* Otherwise we try anyway */
				}else{
					accessToken = twitter.getOAuthAccessToken();
				}
				if (accessToken){
					storeAccessToken(accessToken)
				} else {
					throw new TwitterException("Unable to get the access token.")
				}
		 /* If anything goes wrong */
			} catch (TwitterException te) {
				if(401 == te.getStatusCode()){
					logger.error("Unable to get the access token.");					
				}
			 /* We re-throw the exception */
				throw new TwitterException(te)
			}
		}
	 /* If everything went as expected we return the access token */
		return accessToken
	}
	
	/**
	 * If the user has been logged previously we can retrieve the stored access token 
	 * 
	 * @return
	 */
	static loadAccessToken() throws Exception{
		AccessToken at = null;
		String homeDir = System.getProperty("user.home")+"/.tweetagile";
		File file = new File(homeDir,"credentials.ser");
		if (file.exists()){
			if (logger.isDebugEnabled()){
				logger.debug "Loading authentication token..."
			}
			try{
				at = AccessToken.class.cast(new ObjectInputStream(new FileInputStream(file)).readObject());
			} catch (Exception ex){
			 /* If anything went wrong */
				throw new Exception(ex)
			}
		}
		if (logger.isDebugEnabled()){
			logger.debug "Authentication token Loaded"
		}
		return at;
	}
	
	static storeAccessToken(at) throws Exception{
		String homeDir = System.getProperty("user.home")+"/.tweetagile";
		File file = new File(homeDir,"credentials.ser");
		try{
			if (logger.isDebugEnabled()){
				logger.debug "Storing authentication token..."
			}
			new ObjectOutputStream(new FileOutputStream(file)).writeObject(at);
		} catch (Exception ex){
		 /* If anything went wrong */
			throw new Exception(ex)
		}
		if (logger.isDebugEnabled()){
			logger.debug "Authentication token stored"
		}
		return at;
	}
	
	/**
	 * Maybe user wants to use another credentials
	 * 
	 * @return
	 */
	static deleteAccessToken() throws Exception{
		String homeDir = System.getProperty("user.home")+"/.tweetagile";
		File file = new File(homeDir,"credentials.ser");
		if (!file.delete()){
			file.deleteOnExit()
		}
	}
}
