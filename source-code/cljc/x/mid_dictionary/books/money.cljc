
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.books.money)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:currency
           {:en "Currency"
            :hu "Pénznem"}
           :current-price
           {:en "Current price"
            :hu "Árfolyam"}
           :default-currency
           {:en "Default currency"
            :hu "Alapértelmezett pénznem"}
           :default-current-price
           {:en "Default current price"
            :hu "Alapértelmezett árfolyam"}
           :default-vat-value
           {:en "Default VAT value"
            :hu "Alapértelmezett ÁFA érték"}
           :market-price
           {:en "Market price"
            :hu "Árfolyam"}
           :market-price-history
           {:en "Market price history"
            :hu "Árfolyamtörténet"}
           :primary-currency
           {:en "Primary currency"
            :hu "Elsődleges pénznem"}
           :secondary-currency
           {:en "Secondary currency"
            :hu "Másodlagos pénznem"}
           :vat
           {:en "VAT"
            :hu "ÁFA"}
           :vat-value
           {:en "VAT value"
            :hu "ÁFA érték"}})
