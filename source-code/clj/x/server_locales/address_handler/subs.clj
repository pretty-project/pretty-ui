
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-locales.address-handler.subs
    (:require [re-frame.api                             :as r]
              [x.server-locales.address-handler.helpers :as address-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-ordered-address
  ; @param (string) zip-code
  ; @param (string) country
  ; @param (string) city
  ; @param (string) address
  ; @param (keyword) locale-id
  ;
  ; @example
  ;  (r locales/address->ordered-address db "537" "US" "Bradford" "537 Paper Street" :en)
  ;  =>
  ;  "US, 537 Paper Street, Bradford, 19806"
  ;
  ; @return (string)
  [db [_ zip-code country city address locale-id]]
  (address-handler.helpers/address->ordered-address zip-code country city address locale-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:locales/get-ordered-address "537" "US" "Bradford" "537 Paper Street" :en]
(r/reg-sub :locales/get-ordered-address get-ordered-address)
