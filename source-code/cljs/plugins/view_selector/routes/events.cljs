
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.routes.events
    (:require [plugins.plugin-handler.routes.events :as routes.events]
              [x.app-core.api                       :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.routes.events
(def store-derived-view-id! routes.events/store-derived-view-id!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ;
  ; @return (map)
  [db [_ selector-id]]
  (r store-derived-view-id! db selector-id))
