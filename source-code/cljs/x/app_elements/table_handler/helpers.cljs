

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.table-handler.helpers
    (:require [x.app-elements.engine.element :as element]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:horizontal-border (keyword)(opt)
  ;   :vertical-border (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:data-horizontal-border (keyword)
  ;   :data-vertical-border (keyword)}
  [element-id {:keys [horizontal-border vertical-border] :as element-props}]
  (cond-> (element/element-attributes element-id element-props)
          (some? horizontal-border) (assoc :data-horizontal-border horizontal-border)
          (some?   vertical-border) (assoc :data-vertical-border     vertical-border)))
