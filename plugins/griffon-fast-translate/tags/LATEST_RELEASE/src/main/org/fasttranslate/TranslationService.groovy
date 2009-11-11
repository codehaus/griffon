package org.fasttranslate

import org.gparallelizer.Parallelizer
import com.google.api.translate.Language;
import com.google.api.translate.Translate;

/**
* This class provides fast human language translation, backed by Google Translate. 
* 
* @author Hamlet D'Arcy
*/ 
class TranslationService {
 
    static def MAX_REQUEST_SIZE = 1024

    /**
    * Creates the service. 
    */ 
    public TranslationService() {
        Translate.setHttpReferrer('http://griffon.codehaus.org/')
    }
    
    /**
    * Gets a list of all supported languages. 
    * 
    * @return a String list of all supported languages
    */ 
    List<String> getLanguages() {
        Language.values().collect { it.name() }
    }

    /**
    * Translates the specified text from one language to another. 
    * @param text   the text to translate
    * @param lang1  the language to translate from
    * @param lang2  the language to translate to
    */ 
    String translate(String text, String fromLanguage, String toLanguage) {
        try {                
            Parallelizer.withParallelizer(5) {
                def requestParts = divideRequest(MAX_REQUEST_SIZE, text)
                requestParts.collectAsync { 
                    Translate.execute(
                        it, 
                        Enum.valueOf(Language, fromLanguage), 
                        Enum.valueOf(Language, toLanguage)) 
                }.join()
            }
        } catch(Throwable t) {
            t.printStackTrace()
            t.message
        }                
    }

    /**
    * Provides service to translate several pieces of text in several languages. 
    * The parameter is a list of Map in the form: 
    * <code>
    *   [ [text: 'text to translate...', from: 'language', to: 'language', then: { result -> do something with result...}], 
    *   [ [text: 'other to translate...', from: 'language', to: 'language', then: { result -> do something else with result...}] ]
    * </code>
    * The 'text' key specifies the text to translate. 
    * The 'from' key specifes the langauge to translate from. 
    * The 'to' key specifies the langauge to translate to. 
    * The 'then' key is a closure that will receive the result of the translation. 
    * 
    * This allows you to translate several blocks of text in several lanuages in parallel. 
    * There is no guarantee which order the result closures will be called. 
    * 
    * @param translationRequests
    *       A list of Maps specifying the translation information
    */ 
    void translate(List<Map> translationRequests) {
        
        translationRequests.each {
            def textToTranslate = it['text']        
            def fromLanguage = it['from']        
            def toLanguage = it['to']        
            def event = it['then']
            
            event(translate(textToTranslate, fromLanguage, toLanguage))
        }                            
    }

    /**
    * Provides service to translate a piece of text into several languages. 
    * The 2nd parameter is a list of Map in the form: 
    * <code>
    *   [ [from: 'language', to: 'language', then: { result -> do something with result...}], 
    *   [ [from: 'language', to: 'language', then: { result -> do something else with result...}] ]
    * </code>
    * The 'from' key specifes the langauge to translate from. 
    * The 'to' key specifies the langauge to translate to. 
    * The 'then' key is a closure that will receive the result of the translation. 
    * 
    * This allows you to translate several blocks of text in several lanuages in parallel. 
    * There is no guarantee which order the result closures will be called. 
    * 
    * @param textToTranslate
    *       the text to translate
    * @param translationRequests
    *       A list of Maps specifying the translation information
    */ 
    void translate(String textToTranslate, List<Map> translationRequests) {
        
        translationRequests.each {
            def fromLanguage = it['from']        
            def toLanguage = it['to']        
            def event = it['then']
            
            event(translate(textToTranslate, fromLanguage, toLanguage))
        }                            
    }

    def divideRequest(maxLength, source) {
        
        def sentences = source.split("\\.")
        
        def result = sentences.inject([], { acc, e -> 
            if (e.length() > maxLength) { 
                acc << (e + '.')
            } else if (acc.size() == 0) {
                acc << (e + '.')
            } else {
                def lastElement = acc.pop()
                if (lastElement.size() + e.size() > maxLength) {
                    acc << lastElement
                    acc << (e + '.')
                } else {
                    acc << (lastElement + e + '.')
                }
            }
        }) 
        def endsInPeriod = source.endsWith('.')
        if (!endsInPeriod) {
            def lastElement = result.size() - 1 
            def x = result[lastElement]
            if (x.endsWith('.')) {
                result[lastElement] = x.substring(0, x.size() - 1)
            }
        }
        result
    }
 }
