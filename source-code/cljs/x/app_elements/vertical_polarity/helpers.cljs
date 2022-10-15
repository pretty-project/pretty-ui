
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.vertical-polarity.helpers
    (:require [x.app-elements.element.helpers :as element.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn polarity-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ;  {:style (map)(opt)}
  ;
  ; @return (map)
  ;  {:style (map)}
  [_ {:keys [style]}]
  {:style style})

(defn polarity-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [polarity-id polarity-props]
  (merge (element.helpers/element-default-attributes polarity-id polarity-props)
         (element.helpers/element-indent-attributes  polarity-id polarity-props)
         {}))
