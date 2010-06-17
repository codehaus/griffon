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

package griffon.countries

/**
 * @author Andres.Almiray
 */
class Country implements Comparable {
    public static final Country AD = new Country('AD', "Andorra")
    public static final Country AE = new Country('AE', "United Arab Emirates")
    public static final Country AF = new Country('AF', "Afghanistan")
    public static final Country AG = new Country('AG', "Antigua and Barbuda")
    public static final Country AI = new Country('AI', "Anguilla")
    public static final Country AL = new Country('AL', "Albania")
    public static final Country AM = new Country('AM', "Armenia")
    public static final Country AN = new Country('AN', "Netherlands Antilles")
    public static final Country AO = new Country('AO', "Angola")
    public static final Country AQ = new Country('AQ', "Antarctica")
    public static final Country AR = new Country('AR', "Argentina")
    public static final Country AS = new Country('AS', "American Samoa")
    public static final Country AT = new Country('AT', "Austria")
    public static final Country AU = new Country('AU', "Australia")
    public static final Country AW = new Country('AW', "Aruba")
    public static final Country AX = new Country('AX', "Aland Islands")
    public static final Country AZ = new Country('AZ', "Azerbaijan")
    public static final Country BA = new Country('BA', "Bosnia and Herzegovina")
    public static final Country BB = new Country('BB', "Barbados")
    public static final Country BD = new Country('BD', "Bangladesh")
    public static final Country BE = new Country('BE', "Belgium")
    public static final Country BF = new Country('BF', "Burkina Faso")
    public static final Country BG = new Country('BG', "Bulgaria")
    public static final Country BH = new Country('BH', "Bahrain")
    public static final Country BI = new Country('BI', "Burundi")
    public static final Country BJ = new Country('BJ', "Benin")
    public static final Country BL = new Country('BL', "Saint Barthelemy")
    public static final Country BM = new Country('BM', "Bermuda")
    public static final Country BN = new Country('BN', "Brunei Darussalam")
    public static final Country BO = new Country('BO', "Bolivia")
    public static final Country BR = new Country('BR', "Brazil")
    public static final Country BS = new Country('BS', "Bahamas")
    public static final Country BT = new Country('BT', "Bhutan")
    public static final Country BV = new Country('BV', "Bouvet Island")
    public static final Country BW = new Country('BW', "Botswana")
    public static final Country BY = new Country('BY', "Belarus")
    public static final Country BZ = new Country('BZ', "Belize")
    public static final Country CA = new Country('CA', "Canada")
    public static final Country CC = new Country('CC', "Cocos (Keeling) Islands")
    public static final Country CD = new Country('CD', "Democratic Republic of the Congo")
    public static final Country CF = new Country('CF', "Central African Republic")
    public static final Country CG = new Country('CG', "Congo")
    public static final Country CH = new Country('CH', "Switzerland")
    public static final Country CI = new Country('CI', "Cote d'Ivoire")
    public static final Country CK = new Country('CK', "Cook Islands")
    public static final Country CL = new Country('CL', "Chile")
    public static final Country CM = new Country('CM', "Cameroon")
    public static final Country CN = new Country('CN', "China")
    public static final Country CO = new Country('CO', "Colombia")
    public static final Country CR = new Country('CR', "Costa Rica")
    public static final Country CU = new Country('CU', "Cuba")
    public static final Country CV = new Country('CV', "Cape Verde")
    public static final Country CX = new Country('CX', "Christmas Island")
    public static final Country CY = new Country('CY', "Cyprus")
    public static final Country CZ = new Country('CZ', "Czech Republic")
    public static final Country DE = new Country('DE', "Germany")
    public static final Country DJ = new Country('DJ', "Djibouti")
    public static final Country DK = new Country('DK', "Denmark")
    public static final Country DM = new Country('DM', "Dominica")
    public static final Country DO = new Country('DO', "Dominican Republic")
    public static final Country DZ = new Country('DZ', "Algeria")
    public static final Country EC = new Country('EC', "Ecuador")
    public static final Country EE = new Country('EE', "Estonia")
    public static final Country EG = new Country('EG', "Egypt")
    public static final Country EH = new Country('EH', "Western Sahara")
    public static final Country ER = new Country('ER', "Eritrea")
    public static final Country ES = new Country('ES', "Spain")
    public static final Country ET = new Country('ET', "Ethiopia")
    public static final Country FI = new Country('FI', "Finland")
    public static final Country FJ = new Country('FJ', "Fiji")
    public static final Country FK = new Country('FK', "Falkland Islands (Malvinas)")
    public static final Country FM = new Country('FM', "Federated States of Micronesia")
    public static final Country FO = new Country('FO', "Faroe Islands")
    public static final Country FR = new Country('FR', "France")
    public static final Country GA = new Country('GA', "Gabon")
    public static final Country GB = new Country('GB', "United Kingdom")
    public static final Country GD = new Country('GD', "Grenada")
    public static final Country GE = new Country('GE', "Georgia")
    public static final Country GF = new Country('GF', "French Guiana")
    public static final Country GG = new Country('GG', "Guernsey")
    public static final Country GH = new Country('GH', "Ghana")
    public static final Country GI = new Country('GI', "Gibraltar")
    public static final Country GL = new Country('GL', "Greenland")
    public static final Country GM = new Country('GM', "Gambia")
    public static final Country GN = new Country('GN', "Guinea")
    public static final Country GP = new Country('GP', "Guadeloupe")
    public static final Country GQ = new Country('GQ', "Equatorial Guinea")
    public static final Country GR = new Country('GR', "Greece")
    public static final Country GS = new Country('GS', "South Georgia and the South Sandwich Islands")
    public static final Country GT = new Country('GT', "Guatemala")
    public static final Country GU = new Country('GU', "Guam")
    public static final Country GW = new Country('GW', "Guinea-Bissau")
    public static final Country GY = new Country('GY', "Guyana")
    public static final Country HK = new Country('HK', "Hong Kong")
    public static final Country HM = new Country('HM', "Heard Island and McDonald Islands")
    public static final Country HN = new Country('HN', "Honduras")
    public static final Country HR = new Country('HR', "Croatia")
    public static final Country HT = new Country('HT', "Haiti")
    public static final Country HU = new Country('HU', "Hungary")
    public static final Country ID = new Country('ID', "Indonesia")
    public static final Country IE = new Country('IE', "Ireland")
    public static final Country IL = new Country('IL', "Israel")
    public static final Country IM = new Country('IM', "Isle of Man")
    public static final Country IN = new Country('IN', "India")
    public static final Country IO = new Country('IO', "British Indian Ocean Territory")
    public static final Country IQ = new Country('IQ', "Iraq")
    public static final Country IR = new Country('IR', "Islamic Republic of Iran")
    public static final Country IS = new Country('IS', "Iceland")
    public static final Country IT = new Country('IT', "Italy")
    public static final Country JE = new Country('JE', "Jersey")
    public static final Country JM = new Country('JM', "Jamaica")
    public static final Country JO = new Country('JO', "Jordan")
    public static final Country JP = new Country('JP', "Japan")
    public static final Country KE = new Country('KE', "Kenya")
    public static final Country KG = new Country('KG', "Kyrgyzstan")
    public static final Country KH = new Country('KH', "Cambodia")
    public static final Country KI = new Country('KI', "Kiribati")
    public static final Country KM = new Country('KM', "Comoros")
    public static final Country KN = new Country('KN', "Saint Kitts and Nevis")
    public static final Country KP = new Country('KP', "Democratic People's Republic of Korea")
    public static final Country KR = new Country('KR', "Republic of Korea")
    public static final Country KW = new Country('KW', "Kuwait")
    public static final Country KY = new Country('KY', "Cayman Islands")
    public static final Country KZ = new Country('KZ', "Kazakhstan")
    public static final Country LA = new Country('LA', "Lao People's Democratic Republic")
    public static final Country LB = new Country('LB', "Lebanon")
    public static final Country LC = new Country('LC', "Saint Lucia")
    public static final Country LI = new Country('LI', "Liechtenstein")
    public static final Country LK = new Country('LK', "Sri Lanka")
    public static final Country LR = new Country('LR', "Liberia")
    public static final Country LS = new Country('LS', "Lesotho")
    public static final Country LT = new Country('LT', "Lithuania")
    public static final Country LU = new Country('LU', "Luxembourg")
    public static final Country LV = new Country('LV', "Latvia")
    public static final Country LY = new Country('LY', "Libyan Arab Jamahiriya")
    public static final Country MA = new Country('MA', "Morocco")
    public static final Country MC = new Country('MC', "Monaco")
    public static final Country MD = new Country('MD', "Republic of Moldova")
    public static final Country ME = new Country('ME', "Montenegro")
    public static final Country MF = new Country('MF', "Saint Martin (French part)")
    public static final Country MG = new Country('MG', "Madagascar")
    public static final Country MH = new Country('MH', "Marshall Islands")
    public static final Country MK = new Country('MK', "Macedonia")
    public static final Country ML = new Country('ML', "Mali")
    public static final Country MM = new Country('MM', "Myanmar")
    public static final Country MN = new Country('MN', "Mongolia")
    public static final Country MO = new Country('MO', "Macao")
    public static final Country MP = new Country('MP', "Northern Mariana Islands")
    public static final Country MQ = new Country('MQ', "Martinique")
    public static final Country MR = new Country('MR', "Mauritania")
    public static final Country MS = new Country('MS', "Montserrat")
    public static final Country MT = new Country('MT', "Malta")
    public static final Country MU = new Country('MU', "Mauritius")
    public static final Country MV = new Country('MV', "Maldives")
    public static final Country MW = new Country('MW', "Malawi")
    public static final Country MX = new Country('MX', "Mexico")
    public static final Country MY = new Country('MY', "Malaysia")
    public static final Country MZ = new Country('MZ', "Mozambique")
    public static final Country NA = new Country('NA', "Namibia")
    public static final Country NC = new Country('NC', "New Caledonia")
    public static final Country NE = new Country('NE', "Niger")
    public static final Country NF = new Country('NF', "Norfolk Island")
    public static final Country NG = new Country('NG', "Nigeria")
    public static final Country NI = new Country('NI', "Nicaragua")
    public static final Country NL = new Country('NL', "Netherlands")
    public static final Country NO = new Country('NO', "Norway")
    public static final Country NP = new Country('NP', "Nepal")
    public static final Country NR = new Country('NR', "Nauru")
    public static final Country NU = new Country('NU', "Niue")
    public static final Country NZ = new Country('NZ', "New Zealand")
    public static final Country OM = new Country('OM', "Oman")
    public static final Country PA = new Country('PA', "Panama")
    public static final Country PE = new Country('PE', "Peru")
    public static final Country PF = new Country('PF', "French Polynesia")
    public static final Country PG = new Country('PG', "Papua New Guinea")
    public static final Country PH = new Country('PH', "Philippines")
    public static final Country PK = new Country('PK', "Pakistan")
    public static final Country PL = new Country('PL', "Poland")
    public static final Country PM = new Country('PM', "Saint Pierre and Miquelon")
    public static final Country PN = new Country('PN', "Pitcairn")
    public static final Country PR = new Country('PR', "Puerto Rico")
    public static final Country PS = new Country('PS', "Occupied Palestinian Territory")
    public static final Country PT = new Country('PT', "Portugal")
    public static final Country PW = new Country('PW', "Palau")
    public static final Country PY = new Country('PY', "Paraguay")
    public static final Country QA = new Country('QA', "Qatar")
    public static final Country RE = new Country('RE', "Reunion")
    public static final Country RO = new Country('RO', "Romania")
    public static final Country RS = new Country('RS', "Serbia")
    public static final Country RU = new Country('RU', "Russian Federation")
    public static final Country RW = new Country('RW', "Rwanda")
    public static final Country SA = new Country('SA', "Saudi Arabia")
    public static final Country SB = new Country('SB', "Solomon Islands")
    public static final Country SC = new Country('SC', "Seychelles")
    public static final Country SD = new Country('SD', "Sudan")
    public static final Country SE = new Country('SE', "Sweden")
    public static final Country SG = new Country('SG', "Singapore")
    public static final Country SH = new Country('SH', "Ascension and Tristan da Cunha Saint Helena")
    public static final Country SI = new Country('SI', "Slovenia")
    public static final Country SJ = new Country('SJ', "Svalbard and Jan Mayen")
    public static final Country SK = new Country('SK', "Slovakia")
    public static final Country SL = new Country('SL', "Sierra Leone")
    public static final Country SM = new Country('SM', "San Marino")
    public static final Country SN = new Country('SN', "Senegal")
    public static final Country SO = new Country('SO', "Somalia")
    public static final Country SR = new Country('SR', "Suriname")
    public static final Country ST = new Country('ST', "Sao Tome and Principe")
    public static final Country SV = new Country('SV', "El Salvador")
    public static final Country SY = new Country('SY', "Syrian Arab Republic")
    public static final Country SZ = new Country('SZ', "Swaziland")
    public static final Country TC = new Country('TC', "Turks and Caicos Islands")
    public static final Country TD = new Country('TD', "Chad")
    public static final Country TF = new Country('TF', "French Southern Territories")
    public static final Country TG = new Country('TG', "Togo")
    public static final Country TH = new Country('TH', "Thailand")
    public static final Country TJ = new Country('TJ', "Tajikistan")
    public static final Country TK = new Country('TK', "Tokelau")
    public static final Country TL = new Country('TL', "Timor-Leste")
    public static final Country TM = new Country('TM', "Turkmenistan")
    public static final Country TN = new Country('TN', "Tunisia")
    public static final Country TO = new Country('TO', "Tonga")
    public static final Country TR = new Country('TR', "Turkey")
    public static final Country TT = new Country('TT', "Trinidad and Tobago")
    public static final Country TV = new Country('TV', "Tuvalu")
    public static final Country TW = new Country('TW', "Taiwan")
    public static final Country TZ = new Country('TZ', "United Republic of Tanzania")
    public static final Country UA = new Country('UA', "Ukraine")
    public static final Country UG = new Country('UG', "Uganda")
    public static final Country UM = new Country('UM', "United States Minor Outlying Islands")
    public static final Country US = new Country('US', "United States")
    public static final Country UY = new Country('UY', "Uruguay")
    public static final Country UZ = new Country('UZ', "Uzbekistan")
    public static final Country VA = new Country('VA', "Holy See (Vatican City State)")
    public static final Country VC = new Country('VC', "Saint Vincent and the Grenadines")
    public static final Country VE = new Country('VE', "Venezuela")
    public static final Country VG = new Country('VG', "Virgin Islands, British")
    public static final Country VI = new Country('VI', "Virgin Islands, U.S.")
    public static final Country VN = new Country('VN', "Viet Nam")
    public static final Country VU = new Country('VU', "Vanuatu")
    public static final Country WF = new Country('WF', "Wallis and Futuna")
    public static final Country WS = new Country('WS', "Samoa")
    public static final Country YE = new Country('YE', "Yemen")
    public static final Country YT = new Country('YT', "Mayotte")
    public static final Country ZA = new Country('ZA', "South Africa")
    public static final Country ZM = new Country('ZM', "Zambia")
    public static final Country ZW = new Country('ZW', "Zimbabwe")

    public static final Country[] COUNTRIES = [
        AD, AE, AF, AG, AI, AL, AM, AN, AO, AQ,
        AR, AS, AT, AU, AW, AX, AZ, BA, BB, BD,
        BE, BF, BG, BH, BI, BJ, BL, BM, BN, BO,
        BR, BS, BT, BV, BW, BY, BZ, CA, CC, CD,
        CF, CG, CH, CI, CK, CL, CM, CN, CO, CR,
        CU, CV, CX, CY, CZ, DE, DJ, DK, DM, DO,
        DZ, EC, EE, EG, EH, ER, ES, ET, FI, FJ,
        FK, FM, FO, FR, GA, GB, GD, GE, GF, GG,
        GH, GI, GL, GM, GN, GP, GQ, GR, GS, GT,
        GU, GW, GY, HK, HM, HN, HR, HT, HU, ID,
        IE, IL, IM, IN, IO, IQ, IR, IS, IT, JE,
        JM, JO, JP, KE, KG, KH, KI, KM, KN, KP,
        KR, KW, KY, KZ, LA, LB, LC, LI, LK, LR,
        LS, LT, LU, LV, LY, MA, MC, MD, ME, MF,
        MG, MH, MK, ML, MM, MN, MO, MP, MQ, MR,
        MS, MT, MU, MV, MW, MX, MY, MZ, NA, NC,
        NE, NF, NG, NI, NL, NO, NP, NR, NU, NZ,
        OM, PA, PE, PF, PG, PH, PK, PL, PM, PN,
        PR, PS, PT, PW, PY, QA, RE, RO, RS, RU,
        RW, SA, SB, SC, SD, SE, SG, SH, SI, SJ,
        SK, SL, SM, SN, SO, SR, ST, SV, SY, SZ,
        TC, TD, TF, TG, TH, TJ, TK, TL, TM, TN,
        TO, TR, TT, TV, TW, TZ, UA, UG, UM, US,
        UY, UZ, VA, VC, VE, VG, VI, VN, VU, WF,
        WS, YE, YT, ZA, ZM, ZW
    ]

    final String code
    final String name

    Country(String code, String name) {
        this.code = code
        this.name = name
    }

    String toString() {
        "$name [$code]"
    }

    boolean equals(Object o) {
        if(o == null) return false
        if(o == this) return true
        if(!(o instanceof Country)) return false
        code == o.code && name == o.name 
    }

    int hashCode() {
        (code.hashCode()*37) + (name.hashCode()*31)
    }

    int compareTo(Object o) {
        if (!(o instanceof Country)) {
            throw new ClassCastException("Expected type was ${Country} but got ${o?.getClass()}")
        }
        code <=> o.code
    }

    static Country resolveByCode(String code) {
        if(!code || code.size() != 2) {
            throw new IllegalArgumentException("Code '${code}' is not a valid ISO 3166-1 alpha-2 code")
        }

        Country country = COUNTRIES.find { it.code == code.toUpperCase() }
        if(!country) {
            throw new IllegalArgumentException("Code '${code}' is not a valid ISO 3166-1 alpha-2 code")
        }

        country
    }
}
