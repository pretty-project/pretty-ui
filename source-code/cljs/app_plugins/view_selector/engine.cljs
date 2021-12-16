
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.4.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.view-selector.engine
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :refer [dissoc-in]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-router.api  :as router]
              [mid-plugins.view-selector.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.view-selector.engine
(def DEFAULT-VIEW-ID         engine/DEFAULT-VIEW-ID)
(def route-id                engine/route-id)
(def extended-route-id       engine/extended-route-id)
(def route-template          engine/route-template)
(def extended-route-template engine/extended-route-template)
(def route                   engine/route)
(def extended-route          engine/extended-route)
(def load-event              engine/load-event)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-derived-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:allowed-view-ids (keywords in vector)(opt)
  ;   :default-view-id (keyword)(opt)}
  ;
  ; @return (keyword)
  ;  A view-id forrásából (route-path param) származó adat.
  ;  A forrás hiánya esetén a default-view-id paraméter.
  ;  A default-view-id paraméter hiánya esetén a DEFAULT-VIEW-ID konstans.
  [db [_ extension-id {:keys [allowed-view-ids default-view-id]}]]
  (if-let [derived-view-id (r router/get-current-route-path-param db :view-id)]
          (let [derived-view-id (keyword derived-view-id)]
               (if (or (not (vector?          allowed-view-ids))
                       (vector/contains-item? allowed-view-ids derived-view-id))
                   ; If allowed-view-ids is NOT in use,
                   ; or allowed-view-ids is in use & derived-view-id is allowed ...
                   (return derived-view-id)
                   ; If allowed-view-ids is in use & derived-view-id is NOT allowed ...
                   (or default-view-id DEFAULT-VIEW-ID)))
          (or default-view-id DEFAULT-VIEW-ID)))

(defn get-selected-view
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r view-selector/get-selected-view db :my-extension)
  ;
  ; @return (keyword)
  [db [_ extension-id]]
  (get-in db [extension-id :view-meta :view-id]))

; @usage
;  [:view-selector/get-selected-view :my-extension]
(a/reg-sub :view-selector/get-selected-view get-selected-view)

(defn get-view-props
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r view-selector/get-view-props db :my-extension)
  ;
  ; @return (map)
  ;  {:view-id (keyword)}
  [db [_ extension-id]]
  {:view-id (r get-selected-view db extension-id)})

; @usage
;  [:view-selector/get-view-props :my-extension]
(a/reg-sub :view-selector/get-view-props get-view-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn change-view!
  ; @param (keyword) extension-id
  ; @param (keyword) view-id
  ;
  ; @usage
  ;  (r view-selector/change-view! db :my-extension :my-view)
  ;
  ; @return (map)
  [db [_ extension-id view-id]]
  (assoc-in db [extension-id :view-meta :view-id] view-id))

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
      (let [target-route (extended-route extension-id view-id)]
           [:router/go-to! target-route])))

(a/reg-event-fx
  :view-selector/initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:allowed-view-ids (keywords in vector)(opt)
  ;   :default-view-id (keyword)(opt)}
  (fn [{:keys [db]} [_ extension-id selector-props]]
      (let [derived-view-id (r get-derived-view db extension-id selector-props)]
           {:db       (-> db (dissoc-in [extension-id :view-meta])
                             (assoc-in  [extension-id :view-meta :view-id] derived-view-id))
            :dispatch (load-event extension-id)})))
