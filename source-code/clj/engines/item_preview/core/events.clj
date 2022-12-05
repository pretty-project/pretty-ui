
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-preview.core.events
    (:require [engines.engine-handler.core.events :as core.events]
              [re-frame.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.core.events
(def store-engine-props! core.events/store-engine-props!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-preview!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;
  ; @return (map)
  [db [_ preview-id preview-props]]
  (r store-engine-props! db preview-id preview-props))
