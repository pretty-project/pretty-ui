
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.events
    (:require [plugins.plugin-handler.core.events :as core.events]
              [re-frame.api                       :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.events
(def store-plugin-props! core.events/store-plugin-props!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) lister-props
  ;
  ; @return (map)
  [db [_ lister-id lister-props]]
  (r store-plugin-props! db lister-id lister-props))
