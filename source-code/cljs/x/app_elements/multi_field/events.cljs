
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.multi-field.events
    (:require [re-frame.api              :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn decrease-field-count!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @return (map)
  [db [_ group-id group-props field-dex]]
  (r engine/decrease-input-count! db group-id group-props field-dex))

(defn increase-field-count!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @return (map)
  [db [_ group-id group-props field-dex]]
  (r engine/increase-input-count! db group-id group-props field-dex))
