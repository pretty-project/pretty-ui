
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.engine-handler.body.events
    (:require [mid-fruits.map                     :refer [dissoc-in]]
              [plugins.engine-handler.body.subs   :as body.subs]
              [plugins.engine-handler.core.events :as core.events]
              [plugins.engine-handler.core.subs   :as core.subs]))
             ;[re-frame.api                       :refer [r]]



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
