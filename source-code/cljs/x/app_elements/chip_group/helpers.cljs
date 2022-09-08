
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.chip-group.helpers
    (:require [x.app-elements.engine.api :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-group-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  [group-id group-props]
  (merge (engine/element-default-attributes group-id group-props)
         (engine/element-indent-attributes  group-id group-props)
         {}))
