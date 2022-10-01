
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-picker.core.events
    (:require [plugins.plugin-handler.core.events :as core.events]
              [re-frame.api                       :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.events
(def store-plugin-props! core.events/store-plugin-props!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-picker!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;
  ; @return (map)
  [db [_ picker-id picker-props]]
  (r store-plugin-props! db picker-id picker-props))
