import org.fasttranslate.TranslationService

class MultiplexingInputTests extends GroovyTestCase {

    def ctrl
    def input
        
    public void setUp() {
        ctrl = new TranslationService()
        input = 'Sentence one. Sentence two. Sentence three.'
    }
    
    public void testDividingInputWithNoSpillover() {
        def output = ctrl.divideRequest(100, input)
        assert output.size() ==  1
        assert output[0] == input    
    }
    
    public void testDividingInputWhereEverySentenceIsTooLong() {
        def output = ctrl.divideRequest(20, input)
        
        assert output.size() == 3

        assert output[0] == 'Sentence one.'
        assert output[1] == ' Sentence two.'
        assert output[2] == ' Sentence three.'
    
    }    
}
