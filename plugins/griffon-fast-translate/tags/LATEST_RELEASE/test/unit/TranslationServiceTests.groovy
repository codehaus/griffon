import org.gparallelizer.Parallelizer

import org.fasttranslate.TranslationService

class TranslationServiceTests extends GroovyTestCase {

    def service 
    
    void setUp() {
        service = new TranslationService()
    }
    void testTranslate() {
        
        def result = service.translate('Hello World', 'ENGLISH', 'GERMAN')
        assert 'Hallo Welt' == result  
    }

    void testTranslate_DSL() {
        
        def result1, result2
        service.translate([
            [text: 'Hallo Welt', 
             from: 'GERMAN', 
               to: 'ENGLISH', 
             then: { result -> result1 = result}], 
            
            [text: 'Hello World', 
             from: 'ENGLISH', 
               to: 'GERMAN', 
             then: { result -> result2 = result}]]
        )
        assert 'Hello World' == result1  
        assert 'Hallo Welt'  == result2  
    }

    void testTranslate_DSL_WIth_StringParm() {
        
        def result1, result2
        service.translate('Hello. How are you?', [
            [from: 'ENGLISH', 
               to: 'SPANISH', 
             then: { result -> result1 = result}], 
            
            [from: 'ENGLISH', 
               to: 'GERMAN', 
             then: { result -> result2 = result}]]
        )
        assert 'Hola. ¿Cómo estás?' == result1  
        assert 'Hallo. Wie geht es dir?'  == result2
    }
    
    void testLanguages() {
        def langs = service.languages
        assert langs == ['AUTO_DETECT', 'AFRIKAANS', 'ALBANIAN', 'AMHARIC', 'ARABIC', 'ARMENIAN', 'AZERBAIJANI', 'BASQUE', 'BELARUSIAN', 'BENGALI', 'BIHARI', 'BULGARIAN', 'BURMESE', 'CATALAN', 'CHEROKEE', 'CHINESE', 'CHINESE_SIMPLIFIED', 'CHINESE_TRADITIONAL', 'CROATIAN', 'CZECH', 'DANISH', 'DHIVEHI', 'DUTCH', 'ENGLISH', 'ESPERANTO', 'ESTONIAN', 'FILIPINO', 'FINNISH', 'FRENCH', 'GALACIAN', 'GEORGIAN', 'GERMAN', 'GREEK', 'GUARANI', 'GUJARATI', 'HEBREW', 'HINDI', 'HUNGARIAN', 'ICELANDIC', 'INDONESIAN', 'INUKTITUT', 'IRISH', 'ITALIAN', 'JAPANESE', 'KANNADA', 'KAZAKH', 'KHMER', 'KOREAN', 'KURDISH', 'KYRGYZ', 'LAOTHIAN', 'LATVIAN', 'LITHUANIAN', 'MACEDONIAN', 'MALAY', 'MALAYALAM', 'MALTESE', 'MARATHI', 'MONGOLIAN', 'NEPALI', 'NORWEGIAN', 'ORIYA', 'PASHTO', 'PERSIAN', 'POLISH', 'PORTUGUESE', 'PUNJABI', 'ROMANIAN', 'RUSSIAN', 'SANSKRIT', 'SERBIAN', 'SINDHI', 'SINHALESE', 'SLOVAK', 'SLOVENIAN', 'SPANISH', 'SWAHILI', 'SWEDISH', 'TAJIK', 'TAMIL', 'TAGALOG', 'TELUGU', 'THAI', 'TIBETAN', 'TURKISH', 'UKRANIAN', 'URDU', 'UZBEK', 'UIGHUR', 'VIETNAMESE', 'WELSH', 'YIDDISH']
    }
}
