
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-viewer.core.events
    (:require [engines.engine-handler.core.events :as core.events]
              [re-frame.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.core.events
(def store-engine-props! core.events/store-engine-props!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-viewer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) viewer-props
  ;
  ; @return (map)
  [db [_ viewer-id viewer-props]]
  (r store-engine-props! db viewer-id viewer-props))
