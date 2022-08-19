
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.value-editor.core.helpers
    (:require [mid.plugins.plugin-handler.core.helpers :as core.helpers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.plugins.plugin-handler.core.helpers
(def component-id      core.helpers/component-id)
(def default-data-path core.helpers/default-data-path)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-edit-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @example
  ;  (core.helpers/default-edit-path :my-editor)
  ;  =>
  ;  [:plugins :plugin-handler/edited-item :my-editor]
  ;
  ; @return (vector)
  [editor-id]
  (default-data-path editor-id :edited-item))
