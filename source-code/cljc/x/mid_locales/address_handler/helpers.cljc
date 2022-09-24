
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-locales.address-handler.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn address->ordered-address
  ; @param (string) zip-code
  ; @param (string) country
  ; @param (string) city
  ; @param (string) address
  ; @param (keyword) locale-id
  ;
  ; @usage
  ;  (locales/address->ordered-address "537" "US" "Bradford" "537 Paper Street" :en)
  ;
  ; @return (string)
  [zip-code country city address locale-id]
  (case locale-id :en (str country ", " address  ", " city ", " zip-code)
                  :hu (str country ", " zip-code " "  city ", " address)))
