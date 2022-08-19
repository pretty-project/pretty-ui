
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.cookie-handler.prototypes
    (:require [mid-fruits.candy :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cookie-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  ; @param (map) cookie-props
  ;  {:cookie-type (keyword)(opt)
  ;   :max-age (integer)(opt)}
  ;
  ; @return (map)
  ;  {:cookie-type (keyword)
  ;   :max-age (integer)
  ;   :secure (boolean)}
  [cookie-props]
  (merge {:cookie-type :user-experience
          :max-age     -1}
         (param cookie-props)
         {:secure    true
          :same-site "strict"}))
