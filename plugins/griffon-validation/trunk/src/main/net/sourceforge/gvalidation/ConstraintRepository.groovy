/*
 * Copyright 2010 the original author or authors.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.sourceforge.gvalidation

import net.sourceforge.gvalidation.validator.ClosureValidator
import net.sourceforge.gvalidation.validator.InetAddressValidator
import net.sourceforge.gvalidation.validator.UrlValidator
import net.sourceforge.gvalidation.validator.SizeValidator
import net.sourceforge.gvalidation.validator.RangeValidator
import net.sourceforge.gvalidation.validator.NotEqualValidator
import net.sourceforge.gvalidation.validator.MinSizeValidator
import net.sourceforge.gvalidation.validator.MinValidator
import net.sourceforge.gvalidation.validator.MaxSizeValidator
import net.sourceforge.gvalidation.validator.MaxValidator
import net.sourceforge.gvalidation.validator.MatchesValidator
import net.sourceforge.gvalidation.validator.InListValidator
import net.sourceforge.gvalidation.validator.CreditCardValidator
import net.sourceforge.gvalidation.validator.EmailValidator
import net.sourceforge.gvalidation.validator.BlankValidator
import net.sourceforge.gvalidation.validator.NullableValidator

/**
 * Repository class for managing and configure both built-in and custom constraints 
 *
 * @since 0.3
 * @author nick.zhu
 */
@Singleton
class ConstraintRepository {
    private def validators = Collections.unmodifiableMap([
            nullable: new NullableValidator(this),
            blank: new BlankValidator(this),
            email: new EmailValidator(this),
            creditCard: new CreditCardValidator(this),
            inList: new InListValidator(this),
            matches: new MatchesValidator(this),
            max: new MaxValidator(this),
            maxSize: new MaxSizeValidator(this),
            min: new MinValidator(this),
            minSize: new MinSizeValidator(this),
            notEqual: new NotEqualValidator(this),
            range: new RangeValidator(this),
            size: new SizeValidator(this),
            url: new UrlValidator(this),
            inetAddress: new InetAddressValidator(this),
            validator: new ClosureValidator(this)
    ])

    def getValidators() {
        def results = [:]

        results.putAll validators

        return Collections.unmodifiableMap(results)
    }

    def getValidator(name){
        return validators[name]
    }
}
