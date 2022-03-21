
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
  ; @param (keyword) extension-id
  ; @param (keyword) view-id
  ;
  ; @usage
  ;  (r view-selector/change-view! :my-extension :my-view)
  ;
  ; @return (map)
  [db [_ extension-id view-id]]
  (assoc-in db [:plugins :view-selector/meta-items extension-id :view-id] view-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-derived-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (let [derived-view-id (r core.subs/get-derived-view-id db extension-id)]
       (assoc-in db [:plugins :view-selector/meta-items extension-id :view-id] derived-view-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (as-> db % (r store-derived-view-id!          % extension-id)
             (r routes.events/set-parent-route! % extension-id)))
