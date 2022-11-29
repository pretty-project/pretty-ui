
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns iso.engines.item-handler.core.helpers
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
  ; @param (keyword) handler-id
  ;
  ; @example
  ;  (default-item-path :my-handler)
  ;  =>
  ;  [:engines :engine-handler/handled-items :my-handler]
  ;
  ; @return (vector)
  [handler-id]
  (default-data-path handler-id :handled-items))

(defn default-suggestions-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @example
  ;  (default-suggestions-path :my-handler)
  ;  =>
  ;  [:engines :engine-handler/suggestions :my-handler]
  ;
  ; @return (vector)
  [handler-id]
  (default-data-path handler-id :suggestions))
