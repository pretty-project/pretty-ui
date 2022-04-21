
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.routes.events
    (:require [plugins.plugin-handler.routes.events :as routes.events]
              [x.app-core.api                       :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.routes.events
(def store-derived-item-id! routes.events/store-derived-item-id!)
(def store-derived-view-id! routes.events/store-derived-view-id!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (as-> db % (r store-derived-item-id! % editor-id)
             (r store-derived-view-id! % editor-id)))
