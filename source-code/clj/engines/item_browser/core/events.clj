
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.core.events
    (:require [engines.engine-handler.core.events :as core.events]
              [re-frame.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.core.events
(def store-engine-props! core.events/store-engine-props!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) browser-props
  ;
  ; @return (map)
  [db [_ browser-id browser-props]]
  (r store-engine-props! db browser-id browser-props))
