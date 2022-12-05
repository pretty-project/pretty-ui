
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns iso.engines.item-editor.core.helpers
    (:require [iso.engines.engine-handler.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; iso.engines.engine-handler.core.helpers
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
  ; (default-item-path :my-editor)
  ; =>
  ; [:engines :engine-handler/edited-items :my-editor]
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
  ; (default-suggestions-path :my-editor)
  ; =>
  ; [:engines :engine-handler/suggestions :my-editor]
  ;
  ; @return (vector)
  [editor-id]
  (default-data-path editor-id :suggestions))
