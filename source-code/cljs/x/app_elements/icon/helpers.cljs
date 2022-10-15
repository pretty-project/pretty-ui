
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.icon.helpers
    (:require [x.app-elements.element.helpers :as element.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;  {:style (map)(opt)}
  ;
  ; @return (map)
  ;  {:style (map)}
  [_ {:keys [style]}]
  {:style style})

(defn icon-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [icon-id icon-props]
  (merge (element.helpers/element-default-attributes icon-id icon-props)
         (element.helpers/element-indent-attributes  icon-id icon-props)
         {}))
