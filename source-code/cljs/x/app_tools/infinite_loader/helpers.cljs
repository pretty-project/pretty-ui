

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.infinite-loader.helpers
    (:require [mid-fruits.keyword :as keyword]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn loader-id->observer-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ;
  ; @example
  ;  (infinite-loader.helpers/loader-id->observer-id :my-loader)
  ;  =>
  ;  :my-loader--observer
  ;
  ; @return (keyword)
  [loader-id]
  (keyword/append loader-id :observer "--"))
