
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.body.subs
    (:require [re-frame.api :refer [r]]))



;; -- Body-props subscriptions ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-body-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) prop-key
  ;
  ; @return (*)
  [db [_ engine-id prop-key]]
  (get-in db [:engines :engine-handler/body-props engine-id prop-key]))

(defn body-props-stored?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (some? (get-in db [:engines :engine-handler/body-props engine-id])))



;; -- Body lifecycles subscriptions -------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-did-mount?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (r body-props-stored? db engine-id))
