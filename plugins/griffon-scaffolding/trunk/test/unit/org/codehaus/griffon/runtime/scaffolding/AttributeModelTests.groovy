/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codehaus.griffon.runtime.scaffolding

import static griffon.util.GriffonClassUtils.getPropertyDescriptor
import griffon.test.*
import java.beans.*
import griffon.plugins.scaffolding.*
import griffon.plugins.scaffolding.editors.*
import org.codehaus.groovy.runtime.GStringImpl
import javax.swing.*

/**
 * @author Andres Almiray
 */
class AttributeModelTests extends GriffonUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSmoke() {
        Book book1 = new Book(title: 'Title1', author: 'Author1')
        ValueObject<Book> bean = new ValueObject<Book>()
        AttributeModel<Book, String> titleAttr = new DefaultAttributeModel<Book, String>(Book, bean, 'title')
        assert titleAttr.value == null

        bean.value = book1
        assert titleAttr.value == 'Title1'

        titleAttr.value = 'Title2'
        assert titleAttr.value == 'Title2'
        assert book1.title == 'Title2'

        Book book2 = new Book(title: 'Title3', author: 'Author3')
        bean.value = book2
        assert titleAttr.value == 'Title3'
        assert book2.title == 'Title3'
        assert book1.title == 'Title2'

        titleAttr.value = 'Title4'
        assert titleAttr.value == 'Title4'
        assert book2.title == 'Title4'
        assert book1.title == 'Title2'

        bean.value = null
        assert titleAttr.value == null
    }

    void testMore() {
        BookBeanModel model = new BookBeanModel()
        assert model
        assert model.title
        assert model.author
        assert !model.value
        assert !model.title.value
        assert !model.author.value

        assert model.modelDescriptor.name == 'Book'
        assert model.modelDescriptor.displayName == 'Book'
        assert model.modelDescriptor.description == 'Book'
        assert model.title.modelDescriptor.name == 'Title'
        assert model.title.modelDescriptor.displayName == 'Title'
        assert model.title.modelDescriptor.description == 'Title'

        assert model.modelAttributes['title'] == model.title
        assert model.modelAttributes['author'] == model.author

        Book book1 = new Book(title: 'Title1', author: 'Author1')
        model.value = book1
        assert model.title.value == book1.title
        assert model.author.value == book1.author

        model.value = null
        assert !model.title.value
        assert !model.author.value
    }

    void testDefaultBeanModel() {
        shouldFail(IllegalArgumentException) { new DefaultBeanModel(Author, null) }
        shouldFail(IllegalArgumentException) { new DefaultBeanModel(Author, new String[0]) }
        shouldFail(IllegalArgumentException) { new DefaultBeanModel(Author, ['class'] as String[]) }
        shouldFail(IllegalArgumentException) { new DefaultBeanModel(Author, [null] as String[]) }

        BeanModel<Author> model = new DefaultBeanModel<Author>(Author)
        assert model.modelAttributes
        assert model.modelAttributes['name'] == model.name
        assert !model.name.value
        assert !model.getName().value

        Author author = new Author(name: 'Duke') 
        model.value = author
        assert model.name.value == 'Duke'

        model.value = null
        assert !model.name.value
    }

    void testExcludes() {
        BeanModel model = Class.forName('org.codehaus.griffon.runtime.scaffolding.CompanyBeanModel').newInstance()
        assert model.modelAttributes.size() == 1
        assert model.modelAttributes['name'] == model.name
    }

    void testBinding() {
        BookBeanModel model1 = new BookBeanModel()
        Book book1 = new Book(title: 'Title1', author: 'Author1')
        model1.value = book1

        BookBeanModel model2 = new BookBeanModel()
        Book book2 = new Book(title: 'Title2', author: 'Author2')
        model2.value = book2

        assert model1.title.value == book1.title
        assert book1.title == 'Title1'
        Binder.bind(model1.title, model2.title)
        assert model1.title.value == book2.title
        assert book1.title == book2.title
        
        model2.title.value = 'Title3'
        assert model1.title.value == book2.title
        assert book1.title == book2.title
    }

    void testMutualBinding() {
        BookBeanModel model1 = new BookBeanModel()
        Book book1 = new Book(title: 'Title1', author: 'Author1')
        model1.value = book1

        BookBeanModel model2 = new BookBeanModel()
        Book book2 = new Book(title: 'Title2', author: 'Author2')
        model2.value = book2

        assert model1.title.value == book1.title
        assert book1.title == 'Title1'
        Binder.bind(target: model1.title, source: model2.title, mutual: true)
        assert model1.title.value == book2.title
        assert book1.title == book2.title
        
        model2.title.value = 'Title3'
        assert model1.title.value == book2.title
        assert book1.title == book2.title
        
        model1.title.value = 'Title4'
        assert model1.title.value == book2.title
        assert model2.title.value == book1.title
        assert book2.title == book1.title
    }

    void testCustomizedBinding() {
        BookBeanModel model1 = new BookBeanModel()
        Book book1 = new Book(title: 'Title1', author: 'Author1')
        model1.value = book1

        BookBeanModel model2 = new BookBeanModel()
        Book book2 = new Book(title: 'Title2', author: 'Author2')
        model2.value = book2

        PropertyEditorManager.registerEditor(org.codehaus.groovy.runtime.GStringImpl, StringEditor)
        assert model1.title.value == book1.title
        assert book1.title == 'Title1'
        Binder.bind(target: model1.title,
                    source: model2.title,
                    reader: {m -> "SOURCE(${m.value})"},
                    writer: {m, v -> m.value = "TARGET($v)"})
        assert model1.title.value == "TARGET(SOURCE(${book2.title}))"
        assert book1.title == "TARGET(SOURCE(${book2.title}))"
        
        model2.title.value = 'Title3'
        assert model1.title.value == "TARGET(SOURCE(${book2.title}))"
        assert book1.title == "TARGET(SOURCE(${book2.title}))"
    }

    void testBeanBinding() {
        BookBeanModel model1 = new BookBeanModel()
        Book book1 = new Book(title: 'Title1', author: 'Author1')
        model1.value = book1

        BookBeanModel model2 = new BookBeanModel()
        Book book2 = new Book(title: 'Title2', author: 'Author2')
        model2.value = book2

        assert model1.title.value == book1.title
        Binder.bind(model1, model2)
        assert model1.value != model2.value
        assert model1.title.value == book2.title
        assert book1.title == book2.title
        
        model2.title.value = 'Title3'
        assert model1.value != model2.value
        assert model1.title.value == book2.title
        assert book1.title == book2.title
        
        Book book4 = new Book(title: 'Title4', author: 'Author4')
        model2.value = book4
        assert model1.value != model2.value
        assert model2.title.value == book4.title
        assert model1.title.value == book4.title
        assert model2.author.value == book4.author
        assert model1.author.value == book4.author
    }

    void testMutualBeanBinding() {
        BookBeanModel model1 = new BookBeanModel()
        Book book1 = new Book(title: 'Title1', author: 'Author1')
        model1.value = book1

        BookBeanModel model2 = new BookBeanModel()
        Book book2 = new Book(title: 'Title2', author: 'Author2')
        model2.value = book2

        assert model1.title.value == book1.title
        Binder.bind(target: model1, source: model2, mutual: true)
        assert model1.value != model2.value
        assert model1.title.value == book2.title
        assert book1.title == book2.title
        
        model2.title.value = 'Title3'
        assert model1.value != model2.value
        assert model1.title.value == book2.title
        assert book1.title == book2.title
        
        Book book4 = new Book(title: 'Title4', author: 'Author4')
        model2.value = book4
        assert model1.value != model2.value
        assert model2.title.value == book4.title
        assert model1.title.value == book4.title
        assert model2.author.value == book4.author
        assert model1.author.value == book4.author
    }

    void testCompositeBinding() {
        BookBeanModel model1 = new BookBeanModel()
        Book book1 = new Book(title: 'Title1', author: 'Author1')
        model1.value = book1

        BookBeanModel model2 = new BookBeanModel()
        Book book2 = new Book(title: 'Title2', author: 'Author2')
        model2.value = book2

        BookBeanModel model3 = new BookBeanModel()
        Book book3 = new Book(title: 'Title3', author: 'Author3')
        model3.value = book3

        Model composite = new CommandCompositeModel([model2, model3].title, {models.value.join(' ')})

        assert model1.title.value == book1.title
        assert book1.title == 'Title1'
        Binder.bind(model1.title, composite)
        assert model1.title.value == [book2.title, book3.title].join(' ')
        assert book1.title == [book2.title, book3.title].join(' ')
        
        model3.title.value = 'Title4'
        assert book3.title == 'Title4'
        assert model1.title.value == [book2.title, book3.title].join(' ')
        assert book1.title == [book2.title, book3.title].join(' ')
    }

    public void testBeanBind() {
        JCheckBox button = new JCheckBox('Foo')

        BookBeanModel model1 = new BookBeanModel()
        Book book1 = new Book(title: 'Title1', author: 'Author1')
        model1.value = book1
        
        Binder.beanBind(bean: button, model: model1.title, property: 'label')
        assert button.label == book1.title
        
        model1.title.value = 'Title2'
        assert button.label == book1.title
    }
}
