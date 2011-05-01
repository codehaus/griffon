package twitter

import java.io.FileNotFoundException;

import griffon.test.*
import groovy.mock.interceptor.*
import twitter4j.auth.AccessToken

/**
 * Some concept testing
 * 
 * @author mgg
 *
 */
class TwitterAccessTests extends GriffonUnitTestCase {

	AccessToken accessToken
	
    /* 
     * Initializing the accessToken
     * */
    @Override
	protected void setUp() {
	 /* The token string should have the userid separated from the rest of the 
	  * token by an '-'. If not a ArrayIndexOutOfBoundException will be thrown */
		accessToken = new AccessToken("00001-23423123123","23423123123")
	}

	void testTwitterAccess() {
	 /* ---------------------------------------------------------------------- */
	 /* ----------------------- MOCK ESPECTATIONS ---------------------------- */
	 /* ---------------------------------------------------------------------- */
		def mockContextClass = new MockFor(TwitterAccess)
	 /* We try to load access token */		
		mockContextClass.demand.getAccessToken {username,password->
			if (!username && password){
				throw new IllegalArgumentException("Username And Password are mandatory")
			}
			accessToken
		}
	 /* First time we call storeAccessToken we're gonna do it well */
		mockContextClass.demand.storeAccessToken{at->}
	 /* Just the opposite this time */
		mockContextClass.demand.storeAccessToken{at->
			if (!at) throw new IllegalArgumentException("AccessToken can't be null")			
		}
		mockContextClass.demand.loadAccessToken{
			accessToken
		}
	 /* Time for deleting the accessToken */
		mockContextClass.demand.deleteAccessToken{ /* Do nothing */ }
	 /* And finally if we continue accessing the accessToken we'll get a surprise */
		mockContextClass.demand.loadAccessToken{
			throw new FileNotFoundException("Already deleted")	
		}		
	 /* ---------------------------------------------------------------------- */
	 /* ---------------------------- ASSERTIONS ------------------------------ */
	 /* ---------------------------------------------------------------------- */
		mockContextClass.use {
		 /* ----------------------- GETTING ACCESSTOKEN ---------------------------- */ 
		 /* The getAccessToken() should return an AccessToken object */
			AccessToken result = TwitterAccess.getAccessToken("username","password")
		 /* Some assertions about the retrieved token */
			assert result instanceof AccessToken
			assert result.getUserId() == 1L
			assert result.getToken() == "00001-23423123123"
			assert result.getTokenSecret() == "23423123123"
		 /* ----------------------- STORING ACCESSTOKEN ---------------------------- */
		 /* Twitter access can only be called */
			TwitterAccess.storeAccessToken(result)
			shouldFail { 
				TwitterAccess.storeAccessToken(null)
			}
		 /* ----------------------- LOADING FROM DISK ------------------------------ */
			def possibleSameAccessToken = TwitterAccess.loadAccessToken()
			assert possibleSameAccessToken == result
		 /* ----------------------- DELETING ACCESSTOKEN --------------------------- */
			TwitterAccess.deleteAccessToken()
			shouldFail {
				TwitterAccess.loadAccessToken()
			}
		}
    }
}
