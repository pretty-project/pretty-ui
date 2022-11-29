
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.core.events
    (:require [engines.engine-handler.core.events :as core.events]
              [re-frame.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.core.events
(def store-engine-props! core.events/store-engine-props!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-handler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) handler-props
  ;
  ; @return (map)
  [db [_ handler-id handler-props]]
  (r store-engine-props! db handler-id handler-props))
