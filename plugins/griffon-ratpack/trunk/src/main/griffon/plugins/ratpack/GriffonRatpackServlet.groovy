package griffon.plugins.ratpack

import javax.activation.MimetypesFileTypeMap
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.mortbay.jetty.servlet.Context
import org.mortbay.jetty.servlet.ServletHolder

import com.bleedingwolf.ratpack.TemplateRenderer
import com.bleedingwolf.ratpack.RatpackApp

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Andres Almiray
 */
class GriffonRatpackServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(GriffonRatpackServlet)

    RatpackApp app
	MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap()
    
    void init() {
		mimetypesFileTypeMap.addMimeTypes(RatpackApp.class.getResourceAsStream('mime.types').text)
    }
    
    void service(HttpServletRequest req, HttpServletResponse res) {   
        def verb = req.method
        def path = req.pathInfo
    
        def renderer = new GriffonTemplateRenderer(app.config.templateRoot)
        
        def handler = app.getHandler(verb, path)
        def output = ''
        
        if(handler) {                
            handler.delegate.renderer = renderer
            handler.delegate.request = req
            handler.delegate.response = res
            
            try {
                output = handler.call()
            } catch(RuntimeException ex) {
                log.error('Caught Exception ' + ex, ex)
                
                res.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                output = renderer.renderException(ex, req)
            }            
        } else if(app.config.public && staticFileExists(path)){        
            output = serveStaticFile(res, path)
        } else {
            res.status = HttpServletResponse.SC_NOT_FOUND
            
            output = renderer.renderError(
                title: 'Page Not Found',   // TODO i18n
                message: 'Page Not Found', // TODO i18n
                metadata: [
                    'Request Method': req.method.toUpperCase(),
                    'Request URL': req.requestURL,
                ]
            )
        }
        
        output = convertOutputToByteArray(output)
            
        def contentLength = output.length
        res.setHeader('Content-Length', contentLength.toString())
        
        def stream = res.getOutputStream()
        stream.write(output)
        stream.flush()
        stream.close()
        
        log.info("[${res.status}] ${verb} ${path}")
    }
    
    private boolean staticFileExists(path) {        
		!path.endsWith('/') && staticFileFrom(path) != null
    }
    
    private def serveStaticFile(response, path) {        
		URL url = staticFileFrom(path)
		response.setHeader('Content-Type', mimetypesFileTypeMap.getContentType(url.toString()))
        url.openStream().bytes
    }
    
    private URL staticFileFrom(path) {
        try {
            return Thread.currentThread().contextClassLoader.getResource([app.config.public, path].join('/'))
        } catch(Exception e) {
            return null
        }
    }
    
    private byte[] convertOutputToByteArray(output) {
        if(output instanceof String)
            output = output.getBytes()
        else if(output instanceof GString)
            output = output.toString().getBytes()
        return output
    }
    
    static void configure(RatpackApp app, Context context) {
        context.addServlet(new ServletHolder(new GriffonRatpackServlet(app: app)), app.config.context + '/*')
        log.info("Added ratpack application at ${context.contextPath}${app.config.context}")
    }
}
