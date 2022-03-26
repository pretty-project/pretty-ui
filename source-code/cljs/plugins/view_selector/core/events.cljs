
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.events
    (:require [mid-fruits.candy                    :refer [return]]
              [plugins.view-selector.core.subs     :as core.subs]
              [plugins.view-selector.routes.events :as routes.events]
              [plugins.view-selector.transfer.subs :as transfer.subs]
              [x.app-core.api                      :refer [r]]
              [x.app-ui.api                        :as ui]))



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
  (assoc-in db [:plugins :view-selector/meta-items selector-id :view-id] view-id))



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
       (assoc-in db [:plugins :view-selector/meta-items selector-id :view-id] derived-view-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ;
  ; @return (map)
  [db [_ selector-id]]
  (as-> db % (r store-derived-view-id!          % selector-id)
             (r routes.events/set-parent-route! % selector-id)))
