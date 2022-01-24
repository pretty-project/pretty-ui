
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.26
; Description:
; Version: v0.6.8
; Compatibility: x4.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.progress-bar
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.css       :as css]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]))



;; -- Description -------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  A [:ui/listen-to-process! ...] esemény meghívásával regisztrált folyamatok
;  közül, a listából mindig annak az aktív folyamatnak az állapotát mutatja
;  a progress-bar, amelyik hamarabb került regisztrálásra.



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-progress-bar-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:process-failured? (boolean)
  ;   :process-progress (integer)}
  [db _]
  ; A process-list vektorban felsorolt process-id azonosítójú folyamatok közül az első
  ; aktív folyamat állapotával tér vissza.
  (if-let [process-list (get-in db (db/meta-item-path :ui/progress-bar :process-list))]
          (letfn [(f [process-id] (let [process-activity (r a/get-process-activity db process-id)]
                                       (case process-activity :active process-id :idle process-id nil)))]
                 (if-let [process-id (some f process-list)]
                         {:process-failured? (r a/process-failured?    db process-id)
                          :process-progress  (r a/get-process-progress db process-id)}))))

(a/reg-sub :ui/get-progress-bar-props get-progress-bar-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn listen-to-process!
  ; @param (keyword) process-id
  ;
  ; @usage
  ;  (r ui/listen-to-process! db :my-request)
  ;
  ; @return (map)
  [db [_ process-id]]
  (update-in db (db/meta-item-path :ui/progress-bar :process-list) vector/conj-item-once process-id))

; @usage
;  [:ui/listen-to-process! :my-request]
(a/reg-event-db :ui/listen-to-process! listen-to-process!)

(defn stop-listening-to-process!
  ; @param (keyword) process-id
  ;
  ; @usage
  ;  (r ui/stop-listening-to-process! db :my-request)
  ;
  ; @return (map)
  [db [_ process-id]]
  (update-in db (db/meta-item-path :ui/progress-bar :process-list) vector/remove-item process-id))

; @usage
;  [:ui/stop-listening-to-process! :my-request]
(a/reg-event-db :ui/stop-listening-to-process! stop-listening-to-process!)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- progress-bar-process-progress
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;  {:process-failured? (boolean)(opt)
  ;   :process-progress (integer)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [process-failured? process-progress]}]
  [:div#x-app-progress-bar--process-progress {:style {:width (css/percent process-progress)}
                                              :data-failured (boolean     process-failured?)}])

(defn- progress-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;  {:process-progress (integer)(opt)}
  ;
  ; @return (hiccup)
  [bar-id {:keys [process-progress] :as bar-props}]
  (if process-progress [:div#x-app-progress-bar [progress-bar-process-progress bar-id bar-props]]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [components/subscriber ::view
                         {:render-f   #'progress-bar
                          :subscriber [:ui/get-progress-bar-props]}])
