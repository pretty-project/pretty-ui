
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.engine-handler.header.subs
    (:require [re-frame.api :refer [r]]))



;; -- Header-props subscriptions ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-header-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) prop-key
  ;
  ; @return (*)
  [db [_ engine-id prop-key]]
  (get-in db [:engines :engine-handler/header-props engine-id prop-key]))

(defn header-props-stored?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (some? (get-in db [:engines :engine-handler/header-props engine-id])))



;; -- Header lifecycles subscriptions -----------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-mount?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (boolean)
  [db [_ engine-id]]
  (r header-props-stored? db engine-id))
