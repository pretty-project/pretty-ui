
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.chip-group.events
    (:require [mid-fruits.vector         :as vector]
              [x.app-core.api            :as a :refer [r]]
              [x.app-db.api              :as db]
              [x.app-elements.engine.api :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-chip!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:value-path (vector)}
  ; @param (integer) chip-dex
  ;
  ; @return (map)
  [db [_ _ {:keys [value-path]} chip-dex]]
  (r db/apply-item! db value-path vector/remove-nth-item chip-dex))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.chip-group/delete-chip! delete-chip!)
