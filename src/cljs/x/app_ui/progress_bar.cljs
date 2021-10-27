
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.26
; Description:
; Version: v0.2.2
; Compatibility: x4.4.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.progress-bar
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
  ;  {:process-progress (integer)
  ;   :render-progress-bar? (boolean)}
  [db _]
  (if-let [process-id (get-in db (db/meta-item-path ::primary :process-id))]
          (let [process-progress (r a/get-process-progress db process-id)
                process-activity (r a/get-process-activity db process-id)]
               {:process-progress     (param process-progress)
                :render-progress-bar? (or (= :active process-activity)
                                          (= :idle   process-activity))})))

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

(defn- progress-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:process-progress (integer)(opt)
  ;   :render-progress-bar? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [component-id {:keys [process-progress render-progress-bar?] :as view-props}]
  (if (boolean render-progress-bar?)
      [:div#x-app-progress-bar
        [:div#x-app-progress-bar--process-progress {:style {:width (css/percent process-progress)}}]]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [components/subscriber ::view {:component  #'progress-bar
                                 :subscriber [::get-view-props]}])
