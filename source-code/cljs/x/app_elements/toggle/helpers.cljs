
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.toggle.helpers
    (:require [x.app-elements.element.helpers :as element.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ;  {:style (map)(opt)}
  ;
  ; @return (map)
  ;  {:style (map)}
  [_ {:keys [style]}]
  {:style style})

(defn toggle-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [toggle-id toggle-props]
  (merge (element.helpers/element-default-attributes toggle-id toggle-props)
         (element.helpers/element-indent-attributes  toggle-id toggle-props)
         {}))
