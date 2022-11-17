
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.body.events
    (:require [engines.engine-handler.body.subs   :as body.subs]
              [engines.engine-handler.core.events :as core.events]
              [engines.engine-handler.core.subs   :as core.subs]
              [map.api                            :refer [dissoc-in]]))
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
