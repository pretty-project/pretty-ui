
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.01
; Description:
; Version: v0.1.8
; Compatibility: x4.4.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-locales.country-list)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def COUNTRIES
     {:AD
      {:capital "Andorra la Vella"
       :continent "EU"
       :currency "EUR"
       :languages ["ca"]
       :name "Andorra"
       :native "Andorra"
       :phone "376"}
      :AE
      {:capital "Abu Dhabi"
       :continent "AS"
       :currency "AED"
       :languages ["ar"]
       :name "United Arab Emirates"
       :native "دولة الإمارات العربية المتحدة"
       :phone "971"}
      :AF
      {:capital "Kabul"
       :continent "AS"
       :currency "AFN"
       :languages ["ps" "uz" "tk"]
       :name "Afghanistan"
       :native "افغانستان"
       :phone "93"}
      :AG
      {:capital "Saint John's"
       :continent "NA"
       :currency "XCD"
       :languages ["en"]
       :name "Antigua and Barbuda"
       :native "Antigua and Barbuda"
       :phone "1268"}
      :AI
      {:capital "The Valley"
       :continent "NA"
       :currency "XCD"
       :languages ["en"]
       :name "Anguilla"
       :native "Anguilla"
       :phone "1264"}
      :AL
      {:capital "Tirana"
       :continent "EU"
       :currency "ALL"
       :languages ["sq"]
       :name "Albania"
       :native "Shqipëria"
       :phone "355"}
      :AM
      {:capital "Yerevan"
       :continent "AS"
       :currency "AMD"
       :languages ["hy" "ru"]
       :name "Armenia"
       :native "Հայաստան"
       :phone "374"}
      :AO
      {:capital "Luanda"
       :continent "AF"
       :currency "AOA"
       :languages ["pt"]
       :name "Angola"
       :native "Angola"
       :phone "244"}
      :AQ
      {:capital ""
       :continent "AN"
       :currency ""
       :languages []
       :name "Antarctica"
       :native "Antarctica"
       :phone "672"}
      :AR
      {:capital "Buenos Aires"
       :continent "SA"
       :currency "ARS"
       :languages ["es" "gn"]
       :name "Argentina"
       :native "Argentina"
       :phone "54"}
      :AS
      {:capital "Pago Pago"
       :continent "OC"
       :currency "USD"
       :languages ["en" "sm"]
       :name "American Samoa"
       :native "American Samoa"
       :phone "1684"}
      :AT
      {:capital "Vienna"
       :continent "EU"
       :currency "EUR"
       :languages ["de"]
       :name "Austria"
       :native "Österreich"
       :phone "43"}
      :AU
      {:capital "Canberra"
       :continent "OC"
       :currency "AUD"
       :languages ["en"]
       :name "Australia"
       :native "Australia"
       :phone "61"}
      :AW
      {:capital "Oranjestad"
       :continent "NA"
       :currency "AWG"
       :languages ["nl" "pa"]
       :name "Aruba"
       :native "Aruba"
       :phone "297"}
      :AX
      {:capital "Mariehamn"
       :continent "EU"
       :currency "EUR"
       :languages ["sv"]
       :name "Åland"
       :native "Åland"
       :phone "358"}
      :AZ
      {:capital "Baku"
       :continent "AS"
       :currency "AZN"
       :languages ["az"]
       :name "Azerbaijan"
       :native "Azərbaycan"
       :phone "994"}
      :BA
      {:capital "Sarajevo"
       :continent "EU"
       :currency "BAM"
       :languages ["bs" "hr" "sr"]
       :name "Bosnia and Herzegovina"
       :native "Bosna i Hercegovina"
       :phone "387"}
      :BB
      {:capital "Bridgetown"
       :continent "NA"
       :currency "BBD"
       :languages ["en"]
       :name "Barbados"
       :native "Barbados"
       :phone "1246"}
      :BD
      {:capital "Dhaka"
       :continent "AS"
       :currency "BDT"
       :languages ["bn"]
       :name "Bangladesh"
       :native "Bangladesh"
       :phone "880"}
      :BE
      {:capital "Brussels"
       :continent "EU"
       :currency "EUR"
       :languages ["nl" "fr" "de"]
       :name "Belgium"
       :native "België"
       :phone "32"}
      :BF
      {:capital "Ouagadougou"
       :continent "AF"
       :currency "XOF"
       :languages ["fr" "ff"]
       :name "Burkina Faso"
       :native "Burkina Faso"
       :phone "226"}
      :BG
      {:capital "Sofia"
       :continent "EU"
       :currency "BGN"
       :languages ["bg"]
       :name "Bulgaria"
       :native "България"
       :phone "359"}
      :BH
      {:capital "Manama"
       :continent "AS"
       :currency "BHD"
       :languages ["ar"]
       :name "Bahrain"
       :native "‏البحرين"
       :phone "973"}
      :BI
      {:capital "Bujumbura"
       :continent "AF"
       :currency "BIF"
       :languages ["fr" "rn"]
       :name "Burundi"
       :native "Burundi"
       :phone "257"}
      :BJ
      {:capital "Porto-Novo"
       :continent "AF"
       :currency "XOF"
       :languages ["fr"]
       :name "Benin"
       :native "Bénin"
       :phone "229"}
      :BL
      {:capital "Gustavia"
       :continent "NA"
       :currency "EUR"
       :languages ["fr"]
       :name "Saint Barthélemy"
       :native "Saint-Barthélemy"
       :phone "590"}
      :BM
      {:capital "Hamilton"
       :continent "NA"
       :currency "BMD"
       :languages ["en"]
       :name "Bermuda"
       :native "Bermuda"
       :phone "1441"}
      :BN
      {:capital "Bandar Seri Begawan"
       :continent "AS"
       :currency "BND"
       :languages ["ms"]
       :name "Brunei"
       :native "Negara Brunei Darussalam"
       :phone "673"}
      :BO
      {:capital "Sucre"
       :continent "SA"
       :currency "BOB,BOV"
       :languages ["es" "ay" "qu"]
       :name "Bolivia"
       :native "Bolivia"
       :phone "591"}
      :BQ
      {:capital "Kralendijk"
       :continent "NA"
       :currency "USD"
       :languages ["nl"]
       :name "Bonaire"
       :native "Bonaire"
       :phone "5997"}
      :BR
      {:capital "Brasília"
       :continent "SA"
       :currency "BRL"
       :languages ["pt"]
       :name "Brazil"
       :native "Brasil"
       :phone "55"}
      :BS
      {:capital "Nassau"
       :continent "NA"
       :currency "BSD"
       :languages ["en"]
       :name "Bahamas"
       :native "Bahamas"
       :phone "1242"}
      :BT
      {:capital "Thimphu"
       :continent "AS"
       :currency "BTN,INR"
       :languages ["dz"]
       :name "Bhutan"
       :native "ʼbrug-yul"
       :phone "975"}
      :BV
      {:capital ""
       :continent "AN"
       :currency "NOK"
       :languages ["no" "nb" "nn"]
       :name "Bouvet Island"
       :native "Bouvetøya"
       :phone "47"}
      :BW
      {:capital "Gaborone"
       :continent "AF"
       :currency "BWP"
       :languages ["en" "tn"]
       :name "Botswana"
       :native "Botswana"
       :phone "267"}
      :BY
      {:capital "Minsk"
       :continent "EU"
       :currency "BYN"
       :languages ["be" "ru"]
       :name "Belarus"
       :native "Белару́сь"
       :phone "375"}
      :BZ
      {:capital "Belmopan"
       :continent "NA"
       :currency "BZD"
       :languages ["en" "es"]
       :name "Belize"
       :native "Belize"
       :phone "501"}
      :CA
      {:capital "Ottawa"
       :continent "NA"
       :currency "CAD"
       :languages ["en" "fr"]
       :name "Canada"
       :native "Canada"
       :phone "1"}
      :CC
      {:capital "West Island"
       :continent "AS"
       :currency "AUD"
       :languages ["en"]
       :name "Cocos [Keeling] Islands"
       :native "Cocos (Keeling) Islands"
       :phone "61"}
      :CD
      {:capital "Kinshasa"
       :continent "AF"
       :currency "CDF"
       :languages ["fr" "ln" "kg" "sw" "lu"]
       :name "Democratic Republic of the Congo"
       :native "République démocratique du Congo"
       :phone "243"}
      :CF
      {:capital "Bangui"
       :continent "AF"
       :currency "XAF"
       :languages ["fr" "sg"]
       :name "Central African Republic"
       :native "Ködörösêse tî Bêafrîka"
       :phone "236"}
      :CG
      {:capital "Brazzaville"
       :continent "AF"
       :currency "XAF"
       :languages ["fr" "ln"]
       :name "Republic of the Congo"
       :native "République du Congo"
       :phone "242"}
      :CH
      {:capital "Bern"
       :continent "EU"
       :currency "CHE,CHF,CHW"
       :languages ["de" "fr" "it"]
       :name "Switzerland"
       :native "Schweiz"
       :phone "41"}
      :CI
      {:capital "Yamoussoukro"
       :continent "AF"
       :currency "XOF"
       :languages ["fr"]
       :name "Ivory Coast"
       :native "Côte d'Ivoire"
       :phone "225"}
      :CK
      {:capital "Avarua"
       :continent "OC"
       :currency "NZD"
       :languages ["en"]
       :name "Cook Islands"
       :native "Cook Islands"
       :phone "682"}
      :CL
      {:capital "Santiago"
       :continent "SA"
       :currency "CLF,CLP"
       :languages ["es"]
       :name "Chile"
       :native "Chile"
       :phone "56"}
      :CM
      {:capital "Yaoundé"
       :continent "AF"
       :currency "XAF"
       :languages ["en" "fr"]
       :name "Cameroon"
       :native "Cameroon"
       :phone "237"}
      :CN
      {:capital "Beijing"
       :continent "AS"
       :currency "CNY"
       :languages ["zh"]
       :name "China"
       :native "中国"
       :phone "86"}
      :CO
      {:capital "Bogotá"
       :continent "SA"
       :currency "COP"
       :languages ["es"]
       :name "Colombia"
       :native "Colombia"
       :phone "57"}
      :CR
      {:capital "San José"
       :continent "NA"
       :currency "CRC"
       :languages ["es"]
       :name "Costa Rica"
       :native "Costa Rica"
       :phone "506"}
      :CU
      {:capital "Havana"
       :continent "NA"
       :currency "CUC,CUP"
       :languages ["es"]
       :name "Cuba"
       :native "Cuba"
       :phone "53"}
      :CV
      {:capital "Praia"
       :continent "AF"
       :currency "CVE"
       :languages ["pt"]
       :name "Cape Verde"
       :native "Cabo Verde"
       :phone "238"}
      :CW
      {:capital "Willemstad"
       :continent "NA"
       :currency "ANG"
       :languages ["nl" "pa" "en"]
       :name "Curacao"
       :native "Curaçao"
       :phone "5999"}
      :CX
      {:capital "Flying Fish Cove"
       :continent "AS"
       :currency "AUD"
       :languages ["en"]
       :name "Christmas Island"
       :native "Christmas Island"
       :phone "61"}
      :CY
      {:capital "Nicosia"
       :continent "EU"
       :currency "EUR"
       :languages ["el" "tr" "hy"]
       :name "Cyprus"
       :native "Κύπρος"
       :phone "357"}
      :CZ
      {:capital "Prague"
       :continent "EU"
       :currency "CZK"
       :languages ["cs" "sk"]
       :name "Czech Republic"
       :native "Česká republika"
       :phone "420"}
      :DE
      {:capital "Berlin"
       :continent "EU"
       :currency "EUR"
       :languages ["de"]
       :name "Germany"
       :native "Deutschland"
       :phone "49"}
      :DJ
      {:capital "Djibouti"
       :continent "AF"
       :currency "DJF"
       :languages ["fr" "ar"]
       :name "Djibouti"
       :native "Djibouti"
       :phone "253"}
      :DK
      {:capital "Copenhagen"
       :continent "EU"
       :currency "DKK"
       :languages ["da"]
       :name "Denmark"
       :native "Danmark"
       :phone "45"}
      :DM
      {:capital "Roseau"
       :continent "NA"
       :currency "XCD"
       :languages ["en"]
       :name "Dominica"
       :native "Dominica"
       :phone "1767"}
      :DO
      {:capital "Santo Domingo"
       :continent "NA"
       :currency "DOP"
       :languages ["es"]
       :name "Dominican Republic"
       :native "República Dominicana"
       :phone "1809,1829,1849"}
      :DZ
      {:capital "Algiers"
       :continent "AF"
       :currency "DZD"
       :languages ["ar"]
       :name "Algeria"
       :native "الجزائر"
       :phone "213"}
      :EC
      {:capital "Quito"
       :continent "SA"
       :currency "USD"
       :languages ["es"]
       :name "Ecuador"
       :native "Ecuador"
       :phone "593"}
      :EE
      {:capital "Tallinn"
       :continent "EU"
       :currency "EUR"
       :languages ["et"]
       :name "Estonia"
       :native "Eesti"
       :phone "372"}
      :EG
      {:capital "Cairo"
       :continent "AF"
       :currency "EGP"
       :languages ["ar"]
       :name "Egypt"
       :native "مصر‎"
       :phone "20"}
      :EH
      {:capital "El Aaiún"
       :continent "AF"
       :currency "MAD,DZD,MRU"
       :languages ["es"]
       :name "Western Sahara"
       :native "الصحراء الغربية"
       :phone "212"}
      :ER
      {:capital "Asmara"
       :continent "AF"
       :currency "ERN"
       :languages ["ti" "ar" "en"]
       :name "Eritrea"
       :native "ኤርትራ"
       :phone "291"}
      :ES
      {:capital "Madrid"
       :continent "EU"
       :currency "EUR"
       :languages ["es" "eu" "ca" "gl" "oc"]
       :name "Spain"
       :native "España"
       :phone "34"}
      :ET
      {:capital "Addis Ababa"
       :continent "AF"
       :currency "ETB"
       :languages ["am"]
       :name "Ethiopia"
       :native "ኢትዮጵያ"
       :phone "251"}
      :FI
      {:capital "Helsinki"
       :continent "EU"
       :currency "EUR"
       :languages ["fi" "sv"]
       :name "Finland"
       :native "Suomi"
       :phone "358"}
      :FJ
      {:capital "Suva"
       :continent "OC"
       :currency "FJD"
       :languages ["en" "fj" "hi" "ur"]
       :name "Fiji"
       :native "Fiji"
       :phone "679"}
      :FK
      {:capital "Stanley"
       :continent "SA"
       :currency "FKP"
       :languages ["en"]
       :name "Falkland Islands"
       :native "Falkland Islands"
       :phone "500"}
      :FM
      {:capital "Palikir"
       :continent "OC"
       :currency "USD"
       :languages ["en"]
       :name "Micronesia"
       :native "Micronesia"
       :phone "691"}
      :FO
      {:capital "Tórshavn"
       :continent "EU"
       :currency "DKK"
       :languages ["fo"]
       :name "Faroe Islands"
       :native "Føroyar"
       :phone "298"}
      :FR
      {:capital "Paris"
       :continent "EU"
       :currency "EUR"
       :languages ["fr"]
       :name "France"
       :native "France"
       :phone "33"}
      :GA
      {:capital "Libreville"
       :continent "AF"
       :currency "XAF"
       :languages ["fr"]
       :name "Gabon"
       :native "Gabon"
       :phone "241"}
      :GB
      {:capital "London"
       :continent "EU"
       :currency "GBP"
       :languages ["en"]
       :name "United Kingdom"
       :native "United Kingdom"
       :phone "44"}
      :GD
      {:capital "St. George's"
       :continent "NA"
       :currency "XCD"
       :languages ["en"]
       :name "Grenada"
       :native "Grenada"
       :phone "1473"}
      :GE
      {:capital "Tbilisi"
       :continent "AS"
       :currency "GEL"
       :languages ["ka"]
       :name "Georgia"
       :native "საქართველო"
       :phone "995"}
      :GF
      {:capital "Cayenne"
       :continent "SA"
       :currency "EUR"
       :languages ["fr"]
       :name "French Guiana"
       :native "Guyane française"
       :phone "594"}
      :GG
      {:capital "St. Peter Port"
       :continent "EU"
       :currency "GBP"
       :languages ["en" "fr"]
       :name "Guernsey"
       :native "Guernsey"
       :phone "44"}
      :GH
      {:capital "Accra"
       :continent "AF"
       :currency "GHS"
       :languages ["en"]
       :name "Ghana"
       :native "Ghana"
       :phone "233"}
      :GI
      {:capital "Gibraltar"
       :continent "EU"
       :currency "GIP"
       :languages ["en"]
       :name "Gibraltar"
       :native "Gibraltar"
       :phone "350"}
      :GL
      {:capital "Nuuk"
       :continent "NA"
       :currency "DKK"
       :languages ["kl"]
       :name "Greenland"
       :native "Kalaallit Nunaat"
       :phone "299"}
      :GM
      {:capital "Banjul"
       :continent "AF"
       :currency "GMD"
       :languages ["en"]
       :name "Gambia"
       :native "Gambia"
       :phone "220"}
      :GN
      {:capital "Conakry"
       :continent "AF"
       :currency "GNF"
       :languages ["fr" "ff"]
       :name "Guinea"
       :native "Guinée"
       :phone "224"}
      :GP
      {:capital "Basse-Terre"
       :continent "NA"
       :currency "EUR"
       :languages ["fr"]
       :name "Guadeloupe"
       :native "Guadeloupe"
       :phone "590"}
      :GQ
      {:capital "Malabo"
       :continent "AF"
       :currency "XAF"
       :languages ["es" "fr"]
       :name "Equatorial Guinea"
       :native "Guinea Ecuatorial"
       :phone "240"}
      :GR
      {:capital "Athens"
       :continent "EU"
       :currency "EUR"
       :languages ["el"]
       :name "Greece"
       :native "Ελλάδα"
       :phone "30"}
      :GS
      {:capital "King Edward Point"
       :continent "AN"
       :currency "GBP"
       :languages ["en"]
       :name "South Georgia and the South Sandwich Islands"
       :native "South Georgia"
       :phone "500"}
      :GT
      {:capital "Guatemala City"
       :continent "NA"
       :currency "GTQ"
       :languages ["es"]
       :name "Guatemala"
       :native "Guatemala"
       :phone "502"}
      :GU
      {:capital "Hagåtña"
       :continent "OC"
       :currency "USD"
       :languages ["en" "ch" "es"]
       :name "Guam"
       :native "Guam"
       :phone "1671"}
      :GW
      {:capital "Bissau"
       :continent "AF"
       :currency "XOF"
       :languages ["pt"]
       :name "Guinea-Bissau"
       :native "Guiné-Bissau"
       :phone "245"}
      :GY
      {:capital "Georgetown"
       :continent "SA"
       :currency "GYD"
       :languages ["en"]
       :name "Guyana"
       :native "Guyana"
       :phone "592"}
      :HK
      {:capital "City of Victoria"
       :continent "AS"
       :currency "HKD"
       :languages ["zh" "en"]
       :name "Hong Kong"
       :native "香港"
       :phone "852"}
      :HM
      {:capital ""
       :continent "AN"
       :currency "AUD"
       :languages ["en"]
       :name "Heard Island and McDonald Islands"
       :native "Heard Island and McDonald Islands"
       :phone "61"}
      :HN
      {:capital "Tegucigalpa"
       :continent "NA"
       :currency "HNL"
       :languages ["es"]
       :name "Honduras"
       :native "Honduras"
       :phone "504"}
      :HR
      {:capital "Zagreb"
       :continent "EU"
       :currency "HRK"
       :languages ["hr"]
       :name "Croatia"
       :native "Hrvatska"
       :phone "385"}
      :HT
      {:capital "Port-au-Prince"
       :continent "NA"
       :currency "HTG,USD"
       :languages ["fr" "ht"]
       :name "Haiti"
       :native "Haïti"
       :phone "509"}
      :HU
      {:capital "Budapest"
       :continent "EU"
       :currency "HUF"
       :languages ["hu"]
       :name "Hungary"
       :native "Magyarország"
       :phone "36"}
      :ID
      {:capital "Jakarta"
       :continent "AS"
       :currency "IDR"
       :languages ["id"]
       :name "Indonesia"
       :native "Indonesia"
       :phone "62"}
      :IE
      {:capital "Dublin"
       :continent "EU"
       :currency "EUR"
       :languages ["ga" "en"]
       :name "Ireland"
       :native "Éire"
       :phone "353"}
      :IL
      {:capital "Jerusalem"
       :continent "AS"
       :currency "ILS"
       :languages ["he" "ar"]
       :name "Israel"
       :native "יִשְׂרָאֵל"
       :phone "972"}
      :IM
      {:capital "Douglas"
       :continent "EU"
       :currency "GBP"
       :languages ["en" "gv"]
       :name "Isle of Man"
       :native "Isle of Man"
       :phone "44"}
      :IN
      {:capital "New Delhi"
       :continent "AS"
       :currency "INR"
       :languages ["hi" "en"]
       :name "India"
       :native "भारत"
       :phone "91"}
      :IO
      {:capital "Diego Garcia"
       :continent "AS"
       :currency "USD"
       :languages ["en"]
       :name "British Indian Ocean Territory"
       :native "British Indian Ocean Territory"
       :phone "246"}
      :IQ
      {:capital "Baghdad"
       :continent "AS"
       :currency "IQD"
       :languages ["ar" "ku"]
       :name "Iraq"
       :native "العراق"
       :phone "964"}
      :IR
      {:capital "Tehran"
       :continent "AS"
       :currency "IRR"
       :languages ["fa"]
       :name "Iran"
       :native "ایران"
       :phone "98"}
      :IS
      {:capital "Reykjavik"
       :continent "EU"
       :currency "ISK"
       :languages ["is"]
       :name "Iceland"
       :native "Ísland"
       :phone "354"}
      :IT
      {:capital "Rome"
       :continent "EU"
       :currency "EUR"
       :languages ["it"]
       :name "Italy"
       :native "Italia"
       :phone "39"}
      :JE
      {:capital "Saint Helier"
       :continent "EU"
       :currency "GBP"
       :languages ["en" "fr"]
       :name "Jersey"
       :native "Jersey"
       :phone "44"}
      :JM
      {:capital "Kingston"
       :continent "NA"
       :currency "JMD"
       :languages ["en"]
       :name "Jamaica"
       :native "Jamaica"
       :phone "1876"}
      :JO
      {:capital "Amman"
       :continent "AS"
       :currency "JOD"
       :languages ["ar"]
       :name "Jordan"
       :native "الأردن"
       :phone "962"}
      :JP
      {:capital "Tokyo"
       :continent "AS"
       :currency "JPY"
       :languages ["ja"]
       :name "Japan"
       :native "日本"
       :phone "81"}
      :KE
      {:capital "Nairobi"
       :continent "AF"
       :currency "KES"
       :languages ["en" "sw"]
       :name "Kenya"
       :native "Kenya"
       :phone "254"}
      :KG
      {:capital "Bishkek"
       :continent "AS"
       :currency "KGS"
       :languages ["ky" "ru"]
       :name "Kyrgyzstan"
       :native "Кыргызстан"
       :phone "996"}
      :KH
      {:capital "Phnom Penh"
       :continent "AS"
       :currency "KHR"
       :languages ["km"]
       :name "Cambodia"
       :native "Kâmpŭchéa"
       :phone "855"}
      :KI
      {:capital "South Tarawa"
       :continent "OC"
       :currency "AUD"
       :languages ["en"]
       :name "Kiribati"
       :native "Kiribati"
       :phone "686"}
      :KM
      {:capital "Moroni"
       :continent "AF"
       :currency "KMF"
       :languages ["ar" "fr"]
       :name "Comoros"
       :native "Komori"
       :phone "269"}
      :KN
      {:capital "Basseterre"
       :continent "NA"
       :currency "XCD"
       :languages ["en"]
       :name "Saint Kitts and Nevis"
       :native "Saint Kitts and Nevis"
       :phone "1869"}
      :KP
      {:capital "Pyongyang"
       :continent "AS"
       :currency "KPW"
       :languages ["ko"]
       :name "North Korea"
       :native "북한"
       :phone "850"}
      :KR
      {:capital "Seoul"
       :continent "AS"
       :currency "KRW"
       :languages ["ko"]
       :name "South Korea"
       :native "대한민국"
       :phone "82"}
      :KW
      {:capital "Kuwait City"
       :continent "AS"
       :currency "KWD"
       :languages ["ar"]
       :name "Kuwait"
       :native "الكويت"
       :phone "965"}
      :KY
      {:capital "George Town"
       :continent "NA"
       :currency "KYD"
       :languages ["en"]
       :name "Cayman Islands"
       :native "Cayman Islands"
       :phone "1345"}
      :KZ
      {:capital "Astana"
       :continent "AS"
       :currency "KZT"
       :languages ["kk" "ru"]
       :name "Kazakhstan"
       :native "Қазақстан"
       :phone "76,77"}
      :LA
      {:capital "Vientiane"
       :continent "AS"
       :currency "LAK"
       :languages ["lo"]
       :name "Laos"
       :native "ສປປລາວ"
       :phone "856"}
      :LB
      {:capital "Beirut"
       :continent "AS"
       :currency "LBP"
       :languages ["ar" "fr"]
       :name "Lebanon"
       :native "لبنان"
       :phone "961"}
      :LC
      {:capital "Castries"
       :continent "NA"
       :currency "XCD"
       :languages ["en"]
       :name "Saint Lucia"
       :native "Saint Lucia"
       :phone "1758"}
      :LI
      {:capital "Vaduz"
       :continent "EU"
       :currency "CHF"
       :languages ["de"]
       :name "Liechtenstein"
       :native "Liechtenstein"
       :phone "423"}
      :LK
      {:capital "Colombo"
       :continent "AS"
       :currency "LKR"
       :languages ["si" "ta"]
       :name "Sri Lanka"
       :native "śrī laṃkāva"
       :phone "94"}
      :LR
      {:capital "Monrovia"
       :continent "AF"
       :currency "LRD"
       :languages ["en"]
       :name "Liberia"
       :native "Liberia"
       :phone "231"}
      :LS
      {:capital "Maseru"
       :continent "AF"
       :currency "LSL,ZAR"
       :languages ["en" "st"]
       :name "Lesotho"
       :native "Lesotho"
       :phone "266"}
      :LT
      {:capital "Vilnius"
       :continent "EU"
       :currency "EUR"
       :languages ["lt"]
       :name "Lithuania"
       :native "Lietuva"
       :phone "370"}
      :LU
      {:capital "Luxembourg"
       :continent "EU"
       :currency "EUR"
       :languages ["fr" "de" "lb"]
       :name "Luxembourg"
       :native "Luxembourg"
       :phone "352"}
      :LV
      {:capital "Riga"
       :continent "EU"
       :currency "EUR"
       :languages ["lv"]
       :name "Latvia"
       :native "Latvija"
       :phone "371"}
      :LY
      {:capital "Tripoli"
       :continent "AF"
       :currency "LYD"
       :languages ["ar"]
       :name "Libya"
       :native "‏ليبيا"
       :phone "218"}
      :MA
      {:capital "Rabat"
       :continent "AF"
       :currency "MAD"
       :languages ["ar"]
       :name "Morocco"
       :native "المغرب"
       :phone "212"}
      :MC
      {:capital "Monaco"
       :continent "EU"
       :currency "EUR"
       :languages ["fr"]
       :name "Monaco"
       :native "Monaco"
       :phone "377"}
      :MD
      {:capital "Chișinău"
       :continent "EU"
       :currency "MDL"
       :languages ["ro"]
       :name "Moldova"
       :native "Moldova"
       :phone "373"}
      :ME
      {:capital "Podgorica"
       :continent "EU"
       :currency "EUR"
       :languages ["sr" "bs" "sq" "hr"]
       :name "Montenegro"
       :native "Црна Гора"
       :phone "382"}
      :MF
      {:capital "Marigot"
       :continent "NA"
       :currency "EUR"
       :languages ["en" "fr" "nl"]
       :name "Saint Martin"
       :native "Saint-Martin"
       :phone "590"}
      :MG
      {:capital "Antananarivo"
       :continent "AF"
       :currency "MGA"
       :languages ["fr" "mg"]
       :name "Madagascar"
       :native "Madagasikara"
       :phone "261"}
      :MH
      {:capital "Majuro"
       :continent "OC"
       :currency "USD"
       :languages ["en" "mh"]
       :name "Marshall Islands"
       :native "M̧ajeļ"
       :phone "692"}
      :MK
      {:capital "Skopje"
       :continent "EU"
       :currency "MKD"
       :languages ["mk"]
       :name "North Macedonia"
       :native "Северна Македонија"
       :phone "389"}
      :ML
      {:capital "Bamako"
       :continent "AF"
       :currency "XOF"
       :languages ["fr"]
       :name "Mali"
       :native "Mali"
       :phone "223"}
      :MM
      {:capital "Naypyidaw"
       :continent "AS"
       :currency "MMK"
       :languages ["my"]
       :name "Myanmar [Burma]"
       :native "မြန်မာ"
       :phone "95"}
      :MN
      {:capital "Ulan Bator"
       :continent "AS"
       :currency "MNT"
       :languages ["mn"]
       :name "Mongolia"
       :native "Монгол улс"
       :phone "976"}
      :MO
      {:capital ""
       :continent "AS"
       :currency "MOP"
       :languages ["zh" "pt"]
       :name "Macao"
       :native "澳門"
       :phone "853"}
      :MP
      {:capital "Saipan"
       :continent "OC"
       :currency "USD"
       :languages ["en" "ch"]
       :name "Northern Mariana Islands"
       :native "Northern Mariana Islands"
       :phone "1670"}
      :MQ
      {:capital "Fort-de-France"
       :continent "NA"
       :currency "EUR"
       :languages ["fr"]
       :name "Martinique"
       :native "Martinique"
       :phone "596"}
      :MR
      {:capital "Nouakchott"
       :continent "AF"
       :currency "MRU"
       :languages ["ar"]
       :name "Mauritania"
       :native "موريتانيا"
       :phone "222"}
      :MS
      {:capital "Plymouth"
       :continent "NA"
       :currency "XCD"
       :languages ["en"]
       :name "Montserrat"
       :native "Montserrat"
       :phone "1664"}
      :MT
      {:capital "Valletta"
       :continent "EU"
       :currency "EUR"
       :languages ["mt" "en"]
       :name "Malta"
       :native "Malta"
       :phone "356"}
      :MU
      {:capital "Port Louis"
       :continent "AF"
       :currency "MUR"
       :languages ["en"]
       :name "Mauritius"
       :native "Maurice"
       :phone "230"}
      :MV
      {:capital "Malé"
       :continent "AS"
       :currency "MVR"
       :languages ["dv"]
       :name "Maldives"
       :native "Maldives"
       :phone "960"}
      :MW
      {:capital "Lilongwe"
       :continent "AF"
       :currency "MWK"
       :languages ["en" "ny"]
       :name "Malawi"
       :native "Malawi"
       :phone "265"}
      :MX
      {:capital "Mexico City"
       :continent "NA"
       :currency "MXN"
       :languages ["es"]
       :name "Mexico"
       :native "México"
       :phone "52"}
      :MY
      {:capital "Kuala Lumpur"
       :continent "AS"
       :currency "MYR"
       :languages ["ms"]
       :name "Malaysia"
       :native "Malaysia"
       :phone "60"}
      :MZ
      {:capital "Maputo"
       :continent "AF"
       :currency "MZN"
       :languages ["pt"]
       :name "Mozambique"
       :native "Moçambique"
       :phone "258"}
      :NA
      {:capital "Windhoek"
       :continent "AF"
       :currency "NAD,ZAR"
       :languages ["en" "af"]
       :name "Namibia"
       :native "Namibia"
       :phone "264"}
      :NC
      {:capital "Nouméa"
       :continent "OC"
       :currency "XPF"
       :languages ["fr"]
       :name "New Caledonia"
       :native "Nouvelle-Calédonie"
       :phone "687"}
      :NE
      {:capital "Niamey"
       :continent "AF"
       :currency "XOF"
       :languages ["fr"]
       :name "Niger"
       :native "Niger"
       :phone "227"}
      :NF
      {:capital "Kingston"
       :continent "OC"
       :currency "AUD"
       :languages ["en"]
       :name "Norfolk Island"
       :native "Norfolk Island"
       :phone "672"}
      :NG
      {:capital "Abuja"
       :continent "AF"
       :currency "NGN"
       :languages ["en"]
       :name "Nigeria"
       :native "Nigeria"
       :phone "234"}
      :NI
      {:capital "Managua"
       :continent "NA"
       :currency "NIO"
       :languages ["es"]
       :name "Nicaragua"
       :native "Nicaragua"
       :phone "505"}
      :NL
      {:capital "Amsterdam"
       :continent "EU"
       :currency "EUR"
       :languages ["nl"]
       :name "Netherlands"
       :native "Nederland"
       :phone "31"}
      :NO
      {:capital "Oslo"
       :continent "EU"
       :currency "NOK"
       :languages ["no" "nb" "nn"]
       :name "Norway"
       :native "Norge"
       :phone "47"}
      :NP
      {:capital "Kathmandu"
       :continent "AS"
       :currency "NPR"
       :languages ["ne"]
       :name "Nepal"
       :native "नपल"
       :phone "977"}
      :NR
      {:capital "Yaren"
       :continent "OC"
       :currency "AUD"
       :languages ["en" "na"]
       :name "Nauru"
       :native "Nauru"
       :phone "674"}
      :NU
      {:capital "Alofi"
       :continent "OC"
       :currency "NZD"
       :languages ["en"]
       :name "Niue"
       :native "Niuē"
       :phone "683"}
      :NZ
      {:capital "Wellington"
       :continent "OC"
       :currency "NZD"
       :languages ["en" "mi"]
       :name "New Zealand"
       :native "New Zealand"
       :phone "64"}
      :OM
      {:capital "Muscat"
       :continent "AS"
       :currency "OMR"
       :languages ["ar"]
       :name "Oman"
       :native "عمان"
       :phone "968"}
      :PA
      {:capital "Panama City"
       :continent "NA"
       :currency "PAB,USD"
       :languages ["es"]
       :name "Panama"
       :native "Panamá"
       :phone "507"}
      :PE
      {:capital "Lima"
       :continent "SA"
       :currency "PEN"
       :languages ["es"]
       :name "Peru"
       :native "Perú"
       :phone "51"}
      :PF
      {:capital "Papeetē"
       :continent "OC"
       :currency "XPF"
       :languages ["fr"]
       :name "French Polynesia"
       :native "Polynésie française"
       :phone "689"}
      :PG
      {:capital "Port Moresby"
       :continent "OC"
       :currency "PGK"
       :languages ["en"]
       :name "Papua New Guinea"
       :native "Papua Niugini"
       :phone "675"}
      :PH
      {:capital "Manila"
       :continent "AS"
       :currency "PHP"
       :languages ["en"]
       :name "Philippines"
       :native "Pilipinas"
       :phone "63"}
      :PK
      {:capital "Islamabad"
       :continent "AS"
       :currency "PKR"
       :languages ["en" "ur"]
       :name "Pakistan"
       :native "Pakistan"
       :phone "92"}
      :PL
      {:capital "Warsaw"
       :continent "EU"
       :currency "PLN"
       :languages ["pl"]
       :name "Poland"
       :native "Polska"
       :phone "48"}
      :PM
      {:capital "Saint-Pierre"
       :continent "NA"
       :currency "EUR"
       :languages ["fr"]
       :name "Saint Pierre and Miquelon"
       :native "Saint-Pierre-et-Miquelon"
       :phone "508"}
      :PN
      {:capital "Adamstown"
       :continent "OC"
       :currency "NZD"
       :languages ["en"]
       :name "Pitcairn Islands"
       :native "Pitcairn Islands"
       :phone "64"}
      :PR
      {:capital "San Juan"
       :continent "NA"
       :currency "USD"
       :languages ["es" "en"]
       :name "Puerto Rico"
       :native "Puerto Rico"
       :phone "1787,1939"}
      :PS
      {:capital "Ramallah"
       :continent "AS"
       :currency "ILS"
       :languages ["ar"]
       :name "Palestine"
       :native "فلسطين"
       :phone "970"}
      :PT
      {:capital "Lisbon"
       :continent "EU"
       :currency "EUR"
       :languages ["pt"]
       :name "Portugal"
       :native "Portugal"
       :phone "351"}
      :PW
      {:capital "Ngerulmud"
       :continent "OC"
       :currency "USD"
       :languages ["en"]
       :name "Palau"
       :native "Palau"
       :phone "680"}
      :PY
      {:capital "Asunción"
       :continent "SA"
       :currency "PYG"
       :languages ["es" "gn"]
       :name "Paraguay"
       :native "Paraguay"
       :phone "595"}
      :QA
      {:capital "Doha"
       :continent "AS"
       :currency "QAR"
       :languages ["ar"]
       :name "Qatar"
       :native "قطر"
       :phone "974"}
      :RE
      {:capital "Saint-Denis"
       :continent "AF"
       :currency "EUR"
       :languages ["fr"]
       :name "Réunion"
       :native "La Réunion"
       :phone "262"}
      :RO
      {:capital "Bucharest"
       :continent "EU"
       :currency "RON"
       :languages ["ro"]
       :name "Romania"
       :native "România"
       :phone "40"}
      :RS
      {:capital "Belgrade"
       :continent "EU"
       :currency "RSD"
       :languages ["sr"]
       :name "Serbia"
       :native "Србија"
       :phone "381"}
      :RU
      {:capital "Moscow"
       :continent "EU"
       :currency "RUB"
       :languages ["ru"]
       :name "Russia"
       :native "Россия"
       :phone "7"}
      :RW
      {:capital "Kigali"
       :continent "AF"
       :currency "RWF"
       :languages ["rw" "en" "fr"]
       :name "Rwanda"
       :native "Rwanda"
       :phone "250"}
      :SA
      {:capital "Riyadh"
       :continent "AS"
       :currency "SAR"
       :languages ["ar"]
       :name "Saudi Arabia"
       :native "العربية السعودية"
       :phone "966"}
      :SB
      {:capital "Honiara"
       :continent "OC"
       :currency "SBD"
       :languages ["en"]
       :name "Solomon Islands"
       :native "Solomon Islands"
       :phone "677"}
      :SC
      {:capital "Victoria"
       :continent "AF"
       :currency "SCR"
       :languages ["fr" "en"]
       :name "Seychelles"
       :native "Seychelles"
       :phone "248"}
      :SD
      {:capital "Khartoum"
       :continent "AF"
       :currency "SDG"
       :languages ["ar" "en"]
       :name "Sudan"
       :native "السودان"
       :phone "249"}
      :SE
      {:capital "Stockholm"
       :continent "EU"
       :currency "SEK"
       :languages ["sv"]
       :name "Sweden"
       :native "Sverige"
       :phone "46"}
      :SG
      {:capital "Singapore"
       :continent "AS"
       :currency "SGD"
       :languages ["en" "ms" "ta" "zh"]
       :name "Singapore"
       :native "Singapore"
       :phone "65"}
      :SH
      {:capital "Jamestown"
       :continent "AF"
       :currency "SHP"
       :languages ["en"]
       :name "Saint Helena"
       :native "Saint Helena"
       :phone "290"}
      :SI
      {:capital "Ljubljana"
       :continent "EU"
       :currency "EUR"
       :languages ["sl"]
       :name "Slovenia"
       :native "Slovenija"
       :phone "386"}
      :SJ
      {:capital "Longyearbyen"
       :continent "EU"
       :currency "NOK"
       :languages ["no"]
       :name "Svalbard and Jan Mayen"
       :native "Svalbard og Jan Mayen"
       :phone "4779"}
      :SK
      {:capital "Bratislava"
       :continent "EU"
       :currency "EUR"
       :languages ["sk"]
       :name "Slovakia"
       :native "Slovensko"
       :phone "421"}
      :SL
      {:capital "Freetown"
       :continent "AF"
       :currency "SLL"
       :languages ["en"]
       :name "Sierra Leone"
       :native "Sierra Leone"
       :phone "232"}
      :SM
      {:capital "City of San Marino"
       :continent "EU"
       :currency "EUR"
       :languages ["it"]
       :name "San Marino"
       :native "San Marino"
       :phone "378"}
      :SN
      {:capital "Dakar"
       :continent "AF"
       :currency "XOF"
       :languages ["fr"]
       :name "Senegal"
       :native "Sénégal"
       :phone "221"}
      :SO
      {:capital "Mogadishu"
       :continent "AF"
       :currency "SOS"
       :languages ["so" "ar"]
       :name "Somalia"
       :native "Soomaaliya"
       :phone "252"}
      :SR
      {:capital "Paramaribo"
       :continent "SA"
       :currency "SRD"
       :languages ["nl"]
       :name "Suriname"
       :native "Suriname"
       :phone "597"}
      :SS
      {:capital "Juba"
       :continent "AF"
       :currency "SSP"
       :languages ["en"]
       :name "South Sudan"
       :native "South Sudan"
       :phone "211"}
      :ST
      {:capital "São Tomé"
       :continent "AF"
       :currency "STN"
       :languages ["pt"]
       :name "São Tomé and Príncipe"
       :native "São Tomé e Príncipe"
       :phone "239"}
      :SV
      {:capital "San Salvador"
       :continent "NA"
       :currency "SVC,USD"
       :languages ["es"]
       :name "El Salvador"
       :native "El Salvador"
       :phone "503"}
      :SX
      {:capital "Philipsburg"
       :continent "NA"
       :currency "ANG"
       :languages ["nl" "en"]
       :name "Sint Maarten"
       :native "Sint Maarten"
       :phone "1721"}
      :SY
      {:capital "Damascus"
       :continent "AS"
       :currency "SYP"
       :languages ["ar"]
       :name "Syria"
       :native "سوريا"
       :phone "963"}
      :SZ
      {:capital "Lobamba"
       :continent "AF"
       :currency "SZL"
       :languages ["en" "ss"]
       :name "Swaziland"
       :native "Swaziland"
       :phone "268"}
      :TC
      {:capital "Cockburn Town"
       :continent "NA"
       :currency "USD"
       :languages ["en"]
       :name "Turks and Caicos Islands"
       :native "Turks and Caicos Islands"
       :phone "1649"}
      :TD
      {:capital "N'Djamena"
       :continent "AF"
       :currency "XAF"
       :languages ["fr" "ar"]
       :name "Chad"
       :native "Tchad"
       :phone "235"}
      :TF
      {:capital "Port-aux-Français"
       :continent "AN"
       :currency "EUR"
       :languages ["fr"]
       :name "French Southern Territories"
       :native "Territoire des Terres australes et antarctiques fr"
       :phone "262"}
      :TG
      {:capital "Lomé"
       :continent "AF"
       :currency "XOF"
       :languages ["fr"]
       :name "Togo"
       :native "Togo"
       :phone "228"}
      :TH
      {:capital "Bangkok"
       :continent "AS"
       :currency "THB"
       :languages ["th"]
       :name "Thailand"
       :native "ประเทศไทย"
       :phone "66"}
      :TJ
      {:capital "Dushanbe"
       :continent "AS"
       :currency "TJS"
       :languages ["tg" "ru"]
       :name "Tajikistan"
       :native "Тоҷикистон"
       :phone "992"}
      :TK
      {:capital "Fakaofo"
       :continent "OC"
       :currency "NZD"
       :languages ["en"]
       :name "Tokelau"
       :native "Tokelau"
       :phone "690"}
      :TL
      {:capital "Dili"
       :continent "OC"
       :currency "USD"
       :languages ["pt"]
       :name "East Timor"
       :native "Timor-Leste"
       :phone "670"}
      :TM
      {:capital "Ashgabat"
       :continent "AS"
       :currency "TMT"
       :languages ["tk" "ru"]
       :name "Turkmenistan"
       :native "Türkmenistan"
       :phone "993"}
      :TN
      {:capital "Tunis"
       :continent "AF"
       :currency "TND"
       :languages ["ar"]
       :name "Tunisia"
       :native "تونس"
       :phone "216"}
      :TO
      {:capital "Nuku'alofa"
       :continent "OC"
       :currency "TOP"
       :languages ["en" "to"]
       :name "Tonga"
       :native "Tonga"
       :phone "676"}
      :TR
      {:capital "Ankara"
       :continent "AS"
       :currency "TRY"
       :languages ["tr"]
       :name "Turkey"
       :native "Türkiye"
       :phone "90"}
      :TT
      {:capital "Port of Spain"
       :continent "NA"
       :currency "TTD"
       :languages ["en"]
       :name "Trinidad and Tobago"
       :native "Trinidad and Tobago"
       :phone "1868"}
      :TV
      {:capital "Funafuti"
       :continent "OC"
       :currency "AUD"
       :languages ["en"]
       :name "Tuvalu"
       :native "Tuvalu"
       :phone "688"}
      :TW
      {:capital "Taipei"
       :continent "AS"
       :currency "TWD"
       :languages ["zh"]
       :name "Taiwan"
       :native "臺灣"
       :phone "886"}
      :TZ
      {:capital "Dodoma"
       :continent "AF"
       :currency "TZS"
       :languages ["sw" "en"]
       :name "Tanzania"
       :native "Tanzania"
       :phone "255"}
      :UA
      {:capital "Kyiv"
       :continent "EU"
       :currency "UAH"
       :languages ["uk"]
       :name "Ukraine"
       :native "Україна"
       :phone "380"}
      :UG
      {:capital "Kampala"
       :continent "AF"
       :currency "UGX"
       :languages ["en" "sw"]
       :name "Uganda"
       :native "Uganda"
       :phone "256"}
      :UM
      {:capital ""
       :continent "OC"
       :currency "USD"
       :languages ["en"]
       :name "U.S. Minor Outlying Islands"
       :native "United States Minor Outlying Islands"
       :phone "1"}
      :US
      {:capital "Washington D.C."
       :continent "NA"
       :currency "USD,USN,USS"
       :languages ["en"]
       :name "United States"
       :native "United States"
       :phone "1"}
      :UY
      {:capital "Montevideo"
       :continent "SA"
       :currency "UYI,UYU"
       :languages ["es"]
       :name "Uruguay"
       :native "Uruguay"
       :phone "598"}
      :UZ
      {:capital "Tashkent"
       :continent "AS"
       :currency "UZS"
       :languages ["uz" "ru"]
       :name "Uzbekistan"
       :native "O‘zbekiston"
       :phone "998"}
      :VA
      {:capital "Vatican City"
       :continent "EU"
       :currency "EUR"
       :languages ["it" "la"]
       :name "Vatican City"
       :native "Vaticano"
       :phone "379"}
      :VC
      {:capital "Kingstown"
       :continent "NA"
       :currency "XCD"
       :languages ["en"]
       :name "Saint Vincent and the Grenadines"
       :native "Saint Vincent and the Grenadines"
       :phone "1784"}
      :VE
      {:capital "Caracas"
       :continent "SA"
       :currency "VES"
       :languages ["es"]
       :name "Venezuela"
       :native "Venezuela"
       :phone "58"}
      :VG
      {:capital "Road Town"
       :continent "NA"
       :currency "USD"
       :languages ["en"]
       :name "British Virgin Islands"
       :native "British Virgin Islands"
       :phone "1284"}
      :VI
      {:capital "Charlotte Amalie"
       :continent "NA"
       :currency "USD"
       :languages ["en"]
       :name "U.S. Virgin Islands"
       :native "United States Virgin Islands"
       :phone "1340"}
      :VN
      {:capital "Hanoi"
       :continent "AS"
       :currency "VND"
       :languages ["vi"]
       :name "Vietnam"
       :native "Việt Nam"
       :phone "84"}
      :VU
      {:capital "Port Vila"
       :continent "OC"
       :currency "VUV"
       :languages ["bi" "en" "fr"]
       :name "Vanuatu"
       :native "Vanuatu"
       :phone "678"}
      :WF
      {:capital "Mata-Utu"
       :continent "OC"
       :currency "XPF"
       :languages ["fr"]
       :name "Wallis and Futuna"
       :native "Wallis et Futuna"
       :phone "681"}
      :WS
      {:capital "Apia"
       :continent "OC"
       :currency "WST"
       :languages ["sm" "en"]
       :name "Samoa"
       :native "Samoa"
       :phone "685"}
      :XK
      {:capital "Pristina"
       :continent "EU"
       :currency "EUR"
       :languages ["sq" "sr"]
       :name "Kosovo"
       :native "Republika e Kosovës"
       :phone "377,381,383,386"}
      :YE
      {:capital "Sana'a"
       :continent "AS"
       :currency "YER"
       :languages ["ar"]
       :name "Yemen"
       :native "اليَمَن"
       :phone "967"}
      :YT
      {:capital "Mamoudzou"
       :continent "AF"
       :currency "EUR"
       :languages ["fr"]
       :name "Mayotte"
       :native "Mayotte"
       :phone "262"}
      :ZA
      {:capital "Pretoria"
       :continent "AF"
       :currency "ZAR"
       :languages ["af" "en" "nr" "st" "ss" "tn" "ts" "ve" "xh" "zu"]
       :name "South Africa"
       :native "South Africa"
       :phone "27"}
      :ZM
      {:capital "Lusaka"
       :continent "AF"
       :currency "ZMW"
       :languages ["en"]
       :name "Zambia"
       :native "Zambia"
       :phone "260"}
      :ZW
      {:capital "Harare"
       :continent "AF"
       :currency "USD,ZAR,BWP,GBP,AUD,CNY,INR,JPY"
       :languages ["en" "sn" "nd"]
       :name "Zimbabwe"
       :native "Zimbabwe"
       :phone "263"}})
