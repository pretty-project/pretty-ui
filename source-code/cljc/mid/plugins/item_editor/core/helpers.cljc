
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.item-editor.core.helpers
    (:require [mid.plugins.plugin-handler.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.plugins.plugin-handler.core.helpers
(def component-id      core.helpers/component-id)
(def default-data-path core.helpers/default-data-path)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-item-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @example
  ;  (core.helpers/default-item-path :my-editor)
  ;  =>
  ;  [:plugins :plugin-handler/edited-items :my-editor]
  ;
  ; @return (vector)
  [editor-id]
  (default-data-path editor-id :edited-items))

(defn default-suggestions-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @example
  ;  (core.helpers/default-suggestions-path :my-editor)
  ;  =>
  ;  [:plugins :plugin-handler/suggestions :my-editor]
  ;
  ; @return (vector)
  [editor-id]
  (default-data-path editor-id :suggestions))
