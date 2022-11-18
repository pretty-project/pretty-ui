
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.locales.address-handler.subs
    (:require [re-frame.api                      :as r :refer [r]]
              [x.locales.address-handler.helpers :as address-handler.helpers]
              [x.locales.language-handler.subs   :as language-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-ordered-address
  ; @param (string) zip-code
  ; @param (string) country
  ; @param (string) city
  ; @param (string) address
  ;
  ; @usage
  ;  (r address->ordered-address db "537" "US" "Bradford" "537 Paper Street")
  ;
  ; @return (string)
  [db [_ zip-code country city address]]
  (let [selected-language (r language-handler.subs/get-selected-language db)]
       (address-handler.helpers/address->ordered-address zip-code country city address selected-language)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.locales/get-ordered-address "537" "US" "Bradford" "537 Paper Street"]
(r/reg-sub :x.locales/get-ordered-address get-ordered-address)