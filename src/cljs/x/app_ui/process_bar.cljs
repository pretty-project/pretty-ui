
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.26
; Description:
; Version: v0.2.2
; Compatibility: x4.4.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.process-bar
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.css       :as css]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-elements.api   :as elements]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:process-status (integer)
  ;   :render-process-bar? (boolean)}
  [db _]
  (if-let [process-id (db/meta-item-path ::primary :process-id)]
          (let [process-status (r a/get-process-status db process-id)]
               {:process-status      (param process-status)
                :render-process-bar? (> process-status 0)})))

(a/reg-sub ::get-view-props get-view-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- listen-to-process!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) process-id
  ;
  ; @return (map)
  [db [_ process-id]]
  (assoc-in db (db/meta-item-path ::primary :process-id) process-id))

(a/reg-event-db :x.app-ui/listen-to-process! listen-to-process!)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- process-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:process-status (integer)(opt)
  ;   :render-process-bar? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [component-id {:keys [process-status render-process-bar?]}]
  (if (boolean render-process-bar?)
      [:div#x-app-process-bar
        [:div#x-app-process-bar--process-status {:style {:width (css/percent process-status)}}]]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [components/subscriber ::view {:component  #'process-bar
                                 :subscriber [::get-view-props]}])
