
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.file-editor.core.events
    (:require [plugins.plugin-handler.core.events :as core.events]
              [x.server-core.api                  :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.events
(def store-plugin-props! core.events/store-plugin-props!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;
  ; @return (map)
  [db [_ editor-id editor-props]]
  (r store-plugin-props! db editor-id editor-props))
