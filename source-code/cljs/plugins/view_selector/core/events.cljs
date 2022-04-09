
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.events
    (:require [mid-fruits.candy                    :refer [return]]
              [plugins.plugin-handler.core.events  :as core.events]
              [plugins.view-selector.core.subs     :as core.subs]
              [plugins.view-selector.transfer.subs :as transfer.subs]
              [x.app-core.api                      :refer [r]]
              [x.app-ui.api                        :as ui]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.events
(def set-meta-item!     core.events/set-meta-item!)
(def remove-meta-items! core.events/remove-meta-items!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn change-view!
  ; @param (keyword) selector-id
  ; @param (keyword) view-id
  ;
  ; @usage
  ;  (r view-selector/change-view! :my-selector :my-view)
  ;
  ; @return (map)
  [db [_ selector-id view-id]]
  (assoc-in db [:plugins :plugin-handler/meta-items selector-id :view-id] view-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-derived-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ;
  ; @return (map)
  [db [_ selector-id]]
  (let [derived-view-id (r core.subs/get-derived-view-id db selector-id)]
       (assoc-in db [:plugins :plugin-handler/meta-items selector-id :view-id] derived-view-id)))



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
