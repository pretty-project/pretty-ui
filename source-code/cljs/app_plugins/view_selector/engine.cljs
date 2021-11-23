
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
(def request-id      engine/request-id)
(def render-event-id engine/render-event-id)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-derived-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:allowed-views (keywords in vector)(opt)
  ;   :default-view (keyword)(opt)}
  ;
  ; @return (keyword)
  ;  A view-id forrásából (route-path param) származó adat. Annak hiánya esetén a default-view.
  [db [_ extension-id {:keys [allowed-views default-view]}]]
  (if-let [derived-view (r router/get-current-route-path-param db :selected-view)]
          (let [derived-view (keyword derived-view)]
               (if (or (not (vector?          allowed-views))
                       (vector/contains-item? allowed-views derived-view))
                   ; If allowed-views is NOT in use,
                   ; or allowed-views is in use & derived-view is allowed ...
                   (return derived-view)
                   ; If allowed-views is in use & derived view is NOT allowed ...
                   (return default-view)))
          (return default-view)))

(defn get-selected-view
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r view-selector/get-selected-view db :my-extension)
  ;
  ; @return (keyword)
  [db [_ extension-id]]
  (get-in db [extension-id :selector-meta :selected-view]))

(defn get-body-props
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r view-selector/get-body-props db :my-extension)
  ;
  ; @return (map)
  ;  {:selected-view (keyword)}
  [db [_ extension-id]]
  {:selected-view (r get-selected-view db extension-id)})

(defn get-header-props
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r view-selector/get-header-props db :my-extension)
  ;
  ; @return (map)
  ;  {:selected-view (keyword)}
  [db [_ extension-id]]
  {:selected-view (r get-selected-view db extension-id)})



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
  (assoc-in db [extension-id :selector-meta :selected-view] view-id))

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
  ;  {:allowed-views (keywords in vector)(opt)
  ;   :default-view (keyword)(opt)}
  (fn [{:keys [db]} [_ extension-id selector-props]]
      (let [derived-view (r get-derived-view db extension-id selector-props)]
           {:db         (-> db (dissoc-in [extension-id :selector-meta])
                               (assoc-in  [extension-id :selector-meta :selected-view] derived-view))
            :dispatch-n [[:x.app-ui/listen-to-process! (request-id extension-id)]
                         [:x.app-ui/set-header-title!  (param      extension-id)]
                         [:x.app-ui/set-window-title!  (param      extension-id)]
                         (render-event extension-id)]})))
