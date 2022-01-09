
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

(defn- get-progress-bar-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:process-progress (integer)
  ;   :render-progress-bar? (boolean)}
  [db _]
  (if-let [process-id (get-in db (db/meta-item-path :ui/progress-bar :process-id))]
          (let [process-activity (r a/get-process-activity db process-id)
                overlay-screen?  (get-in db (db/meta-item-path :ui/progress-bar :overlay-screen?))]
               {:process-failured?      (r a/process-failured? db process-id)
                :process-progress       (r a/get-process-progress db process-id)
                :render-progress-bar?   (or (= :active process-activity)
                                            (= :idle   process-activity))
                :render-screen-overlay? (and (boolean overlay-screen?)
                                             (or (= :active process-activity)
                                                 (= :idle   process-activity)))})))

(a/reg-sub :ui/get-progress-bar-props get-progress-bar-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- listen-to-process!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) process-id
  ; @param (map) options
  ;  {:overlay-screen? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  (r ui/listen-to-process! db :my-request)
  ;
  ; @usage
  ;  (r ui/listen-to-process! db :my-request {...})
  ;
  ; @return (map)
  [db [_ process-id options]]
  (assoc-in db (db/meta-item-path :ui/progress-bar)
               (merge {:process-id process-id}
                      (param       options))))

; @usage
;  [:ui/listen-to-process! :my-request]
;
; @usage
;  [:ui/listen-to-process! :my-request {...}]
(a/reg-event-db :ui/listen-to-process! listen-to-process!)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- progress-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;  {:process-failured? (boolean)(opt)
  ;   :process-progress (integer)(opt)
  ;   :render-progress-bar? (boolean)(opt)
  ;   :render-screen-overlay? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [process-failured? process-progress render-progress-bar? render-screen-overlay?]}]
  (if render-progress-bar? [:div#x-app-progress-bar
                             (if render-screen-overlay? [:div#x-app-progress-bar--screen-overlay])
                             [:div#x-app-progress-bar--process-progress {:style {:width (css/percent process-progress)}
                                                                         :data-failured (boolean process-failured?)}]]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [components/subscriber ::view 
                         {:component  #'progress-bar
                          :subscriber [:ui/get-progress-bar-props]}])
