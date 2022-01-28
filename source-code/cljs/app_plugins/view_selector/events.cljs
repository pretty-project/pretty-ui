
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.6.8
; Compatibility: x4.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.view-selector.events
    (:require [x.app-core.api :as a :refer [r]]
              [app-plugins.view-selector.engine :as engine]
              [app-plugins.view-selector.subs   :as subs]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-current-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) view-id
  ;
  ; @return (map)
  [db [_ extension-id view-id]]
  (assoc-in db [extension-id :view-selector/meta-items :view-id] view-id))

(defn store-derived-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (let [derived-view-id (r subs/get-derived-view-id db extension-id)]
       (r set-current-view-id! db extension-id derived-view-id)))

(defn store-current-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:view-id (keyword)(opt)}
  ;
  ; @return (map)
  [db [_ extension-id {:keys [view-id]}]]
  (if (r subs/route-handled? db extension-id)
      (r store-derived-view-id! db extension-id)
      (r   set-current-view-id! db extension-id view-id)))

(defn load-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:view-id (keyword)(opt)}
  ;
  ; @return (map)
  [db [_ extension-id selector-props]]
  (r store-current-view-id! db extension-id selector-props))

(defn change-view!
  ; @param (keyword) extension-id
  ; @param (keyword) view-id
  ;
  ; @usage
  ;  (r view-selector/change-view! db :my-extension :my-view)
  ;
  ; @return (map)
  [db [_ extension-id view-id]]
  (r set-current-view-id! db extension-id view-id))

; @usage
;  [:view-selector/change-view! :my-extension :my-view]
(a/reg-event-db :view-selector/change-view! change-view!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/go-to!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) view-id
  ;
  ; @usage
  ;  [:view-selector/go-to! :my-extension :my-view]
  (fn [{:keys [db]} [_ extension-id view-id]]
      (if (r subs/route-handled? db extension-id)
          (let [target-route-string (engine/extended-route-string extension-id view-id)]
               [:router/go-to! target-route-string])
          [:view-selector/load-selector! extension-id {:view-id view-id}])))

(a/reg-event-fx
  :view-selector/load-selector!
  ; @param (keyword) extension-id
  ; @param (map)(opt) selector-props
  ;  {:view-id (keyword)(opt)}
  ;
  ; @usage
  ;  [:view-selector/load-selector! :my-extension]
  ;
  ; @usage
  ;  [:view-selector/load-selector! :my-extension {...}]
  ;
  ; @usage
  ;  [:view-selector/load-selector! :my-extension {:view-id "my-view"}]
  (fn [{:keys [db]} [_ extension-id selector-props]]
      (let [selector-label (r subs/get-meta-item db extension-id :label)]
           {:db (r load-selector! db extension-id selector-props)
            :dispatch-n [; XXX#3237
                         (if (r subs/set-title? db extension-id)
                             [:ui/set-title! selector-label])
                         (engine/load-extension-event extension-id)]})))
