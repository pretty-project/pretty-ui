
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.blank.helpers
    (:require [x.app-core.api                 :as a]
              [x.app-elements.element.helpers :as element.helpers]
              [x.app-environment.api          :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blank-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ;  {:style (map)(opt)}
  ;
  ; @return (map)
  ;  {:style (map)}
  [_ {:keys [style]}]
  {:style style})

(defn blank-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [blank-id blank-props]
  (merge (element.helpers/element-default-attributes blank-id blank-props)
         (element.helpers/element-indent-attributes  blank-id blank-props)
         {}))