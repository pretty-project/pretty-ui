
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.events
    (:require [plugins.plugin-handler.core.events :as core.events]
              [re-frame.api                       :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.events
(def store-plugin-props! core.events/store-plugin-props!)



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
  (r store-plugin-props! db browser-id browser-props))
