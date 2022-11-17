
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.locales.address-handler.helpers
    (:require [string.api :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn address->ordered-address
  ; @param (string) zip-code
  ; @param (string) country
  ; @param (string) city
  ; @param (string) address
  ; @param (keyword) locale-id
  ;
  ; @example
  ;  (address->ordered-address "19806" "US" "Bradford" "537 Paper Street" :en)
  ;  =>
  ;  "US, 537 Paper Street, Bradford, 19806"
  ;
  ; @example
  ;  (address->ordered-address "1025" "HU" "Budapest" "Minta utca 123." :hu)
  ;  =>
  ;  "HU, 1025 Budapest, Minta utca 123."
  ;
  ; @return (string)
  [zip-code country city address locale-id]
  (case locale-id :en (string/join [country address city zip-code] ", " {:join-empty? false})
                  :hu (let [area (string/trim (str zip-code " " city))]
                           (string/join [country area address] ", " {:join-empty? false}))))
