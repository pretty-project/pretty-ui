
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.routes.events
    (:require [mid-fruits.candy                :refer [return]]
              [plugins.item-editor.core.events :as core.events]
              [plugins.item-editor.routes.subs :as routes.subs]
              [x.app-core.api                  :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-derived-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [derived-item-id (r routes.subs/get-derived-item-id db editor-id)]
       (assoc-in db [:plugins :plugin-handler/meta-items editor-id :item-id] derived-item-id)))

(defn store-derived-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (if-let [derived-view-id (r routes.subs/get-derived-view-id db editor-id)]
          (assoc-in db [:plugins :plugin-handler/meta-items editor-id :view-id] (keyword derived-view-id))
          (return   db)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (as-> db % (r core.events/reset-downloads! % editor-id)
             (r store-derived-item-id! % editor-id)
             (r store-derived-view-id! % editor-id)))
