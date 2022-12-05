
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.header.events
    (:require [map.api :refer [dissoc-in]]))



;; -- Header-props events -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-header-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (map) header-props
  ;
  ; @return (map)
  [db [_ engine-id header-props]]
  (assoc-in db [:engines :engine-handler/header-props engine-id] header-props))

(defn remove-header-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  (dissoc-in db [:engines :engine-handler/header-props engine-id]))

(defn update-header-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (map) header-props
  ;
  ; @return (map)
  [db [_ engine-id header-props]]
  (update-in db [:engines :engine-handler/header-props engine-id] merge header-props))
