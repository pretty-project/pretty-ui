
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.vertical-line.helpers
    (:require [x.app-elements.element.helpers :as element.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn line-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  ;  {:style (map)(opt)}
  ;
  ; @return (map)
  ;  {:style (map)}
  [_ {:keys [style]}]
  {:style style})

(defn line-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [line-id line-props]
  (merge (element.helpers/element-default-attributes line-id line-props)
         (element.helpers/element-indent-attributes  line-id line-props)
         {}))
