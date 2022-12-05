
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.ghost.helpers
    (:require [elements.element.helpers :as element.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [style]}]
  {:style style})

(defn ghost-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  ; {:height (keyword)}
  ;
  ; @return (map)
  ; {:data-border-radius (keyword)
  ;  :data-height (keyword)}
  [ghost-id {:keys [border-radius height] :as ghost-props}]
  (merge (element.helpers/element-default-attributes ghost-id ghost-props)
         (element.helpers/element-indent-attributes  ghost-id ghost-props)
         {:data-border-radius border-radius
          :data-height        height}))
