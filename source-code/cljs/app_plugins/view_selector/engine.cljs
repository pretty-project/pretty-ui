
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.4.2
; Compatibility: x4.4.6



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
(def request-id   engine/request-id)
(def render-event engine/render-event)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-derived-view-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:allowed-view-ids (keywords in vector)(opt)
  ;   :default-view-id (keyword)(opt)}
  ;
  ; @return (keyword)
  ;  A view-id forrásából (route-path param) származó adat. Annak hiánya esetén a default-view-id
  [db [_ extension-id {:keys [allowed-view-ids default-view-id]}]]
  (if-let [derived-view-id (r router/get-current-route-path-param db :view-id)]
          (let [derived-view-id (keyword derived-view-id)]
               (if (or (not (vector?          allowed-view-ids))
                       (vector/contains-item? allowed-view-ids derived-view-id))
                   ; If allowed-view-ids is NOT in use,
                   ; or allowed-view-ids is in use & derived-view-id is allowed ...
                   (return derived-view-id)
                   ; If allowed-view-ids is in use & derived-view-id is NOT allowed ...
                   (return default-view-id)))
          (return default-view-id)))

(defn get-selected-view-id
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r view-selector/get-selected-view-id db :my-extension)
  ;
  ; @return (keyword)
  [db [_ extension-id]]
  (get-in db [extension-id :view-meta :view-id]))

(defn get-body-props
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r view-selector/get-body-props db :my-extension)
  ;
  ; @return (map)
  ;  {:view-id (keyword)}
  [db [_ extension-id]]
  {:view-id (r get-selected-view-id db extension-id)})

(defn get-header-props
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r view-selector/get-header-props db :my-extension)
  ;
  ; @return (map)
  ;  {:view-id (keyword)}
  [db [_ extension-id]]
  {:view-id (r get-selected-view-id db extension-id)})



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
  :view-selector/load!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:allowed-view-ids (keywords in vector)(opt)
  ;   :default-view-id (keyword)(opt)}
  (fn [{:keys [db]} [_ extension-id selector-props]]
      (let [derived-view-id (r get-derived-view-id db extension-id selector-props)]
           {:db         (-> db (dissoc-in [extension-id :view-meta])
                               (assoc-in  [extension-id :view-meta :view-id] derived-view-id))
            :dispatch-n [[:ui/listen-to-process! (request-id extension-id)]
                         [:ui/set-header-title!  (param      extension-id)]
                         [:ui/set-window-title!  (param      extension-id)]
                         (render-event extension-id)]})))
