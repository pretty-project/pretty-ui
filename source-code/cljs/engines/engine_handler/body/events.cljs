
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.body.events
    (:require [map.api :refer [dissoc-in]]))



;; -- Body-props events -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-body-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ engine-id body-props]]
  (assoc-in db [:engines :engine-handler/body-props engine-id] body-props))

(defn remove-body-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  (dissoc-in db [:engines :engine-handler/body-props engine-id]))

(defn update-body-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ engine-id body-props]]
  (update-in db [:engines :engine-handler/body-props engine-id] merge body-props))
