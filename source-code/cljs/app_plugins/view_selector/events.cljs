
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.6.0
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.view-selector.events
    (:require [mid-fruits.map :refer [dissoc-in]]
              [x.app-core.api :as a :refer [r]]
              [app-plugins.view-selector.engine :as engine]
              [app-plugins.view-selector.subs   :as subs]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;
  ; @return (map)
  [db [_ extension-id selector-props]]
  (let [derived-view-id (r subs/get-derived-view db extension-id selector-props)]
       (-> db (dissoc-in [extension-id :view-selector/meta-items])
              (assoc-in  [extension-id :view-selector/meta-items :view-id] derived-view-id))))

(defn change-view!
  ; @param (keyword) extension-id
  ; @param (keyword) view-id
  ;
  ; @usage
  ;  (r view-selector/change-view! db :my-extension :my-view)
  ;
  ; @return (map)
  [db [_ extension-id view-id]]
  (assoc-in db [extension-id :view-selector/meta-items :view-id] view-id))

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
  (fn [_ [_ extension-id view-id]]
      (let [target-route-string (engine/extended-route-string extension-id view-id)]
           [:router/go-to! target-route-string])))

(a/reg-event-fx
  :view-selector/load-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:allowed-view-ids (keywords in vector)(opt)
  ;   :default-view-id (keyword)(opt)}
  (fn [{:keys [db]} [_ extension-id selector-props]]
      {:db (r load-selector! db extension-id selector-props)
       :dispatch (engine/load-event extension-id)}))
