
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.books.products)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:automatic-price
           {:en "Automatic price"
            :hu "Automatikus ár"}
           :automatic-pricing
           {:en "Automatic pricing"
            :hu "Automatikus árazás"}
           :dealer-margin
           {:en "Dealer margin"
            :hu "Kereskedői árrés"}
           :dealer-rebate
           {:en "Dealer debate"
            :hu "Kereskedői engedmény"}
           :discount-price
           {:en "Discount price"
            :hu "Kedvezményes ár"}
           :list-price
           {:en "List price"
            :hu "Listaár"}
           :manual-price
           {:en "Manual price"
            :hu "Egyedi ár"}
           :manual-pricing
           {:en "Manual pricing"
            :hu "Egyedi árazás"}
           :manufacturer-price
           {:en "Manufacturer price"
            :hu "Gyártói ár"}
           :net-discount-price
           {:en "Net discount price"
            :hu "Nettó kedvezményes ár"}
           :net-list-price
           {:en "Net list price"
            :hu "Nettó listaár"}
           :net-total-price
           {:en "Net total price"
            :hu "Nettó végösszeg"}
           :net-unique-price
           {:en "Net unique price"
            :hu "Nettó egyedi ár"}
           :gross-discount-price
           {:en "Gross discount price"
            :hu "Bruttó kedvezményes ár"}
           :gross-list-price
           {:en "Gross list price"
            :hu "Bruttó listaár"}
           :gross-total-price
           {:en "Gross total price"
            :hu "Bruttó végösszeg"}
           :gross-unique-price
           {:en "Gross unique price"
            :hu "Bruttó egyedi ár"}
           :price
           {:en "Price"
            :hu "Ár"}
           :prices
           {:en "Prices"
            :hu "Árak"}
           :pricing
           {:en "Pricing"
            :hu "Árazás"}
           :product
           {:en "Product"
            :hu "Termék"}
           :products
           {:en "Products"
            :hu "Termékek"}
           ; Angol nyelvű árajánlatokon a "Megnevezés" kifejezés "Description"
           ; kifejezésként szerepel.
           :product-description
           {:en "Description"
            :hu "Megnevezés"}
           :total-price
           {:en "Total price"
            :hu "Végösszeg"}
           :transport-cost
           {:en "Transport cost"
            :hu "Szállítási költség"}
           :unique-price
           {:en "Unique price"
            :hu "Egyedi ár"}
           :unique-pricing
           {:en "Unique pricing"
            :hu "Egyedi árazás"}})
