
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns iso.engines.item-preview.core.helpers
    (:require [iso.engines.engine-handler.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; iso.engines.engine-handler.core.helpers
(def component-id      core.helpers/component-id)
(def default-data-path core.helpers/default-data-path)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-items-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ;
  ; @example
  ; (default-items-path :my-preview)
  ; =>
  ; [:engines :engine-handler/downloaded-items :my-preview]
  ;
  ; @return (vector)
  [preview-id]
  (default-data-path preview-id :downloaded-items))
