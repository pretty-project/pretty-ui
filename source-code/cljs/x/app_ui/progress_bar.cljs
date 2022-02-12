
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.26
; Description:
; Version: v0.8.8
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.progress-bar
    (:require [mid-fruits.candy :refer [param return]]
              [mid-fruits.css   :as css]
              [mid-fruits.map   :refer [dissoc-in]]
              [x.app-core.api   :as a :refer [r]]
              [x.app-db.api     :as db]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-progress-bar-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:fake-progress (integer)
  ;   :process-failured? (boolean)
  ;   :process-progress (integer)}
  [db _]
  (let [fake-progress (get-in db (db/meta-item-path :ui/progress-bar :fake-progress) 0)]
       (if-let [process-id (get-in db (db/meta-item-path :ui/progress-bar :process-id))]
               (let [process-progress (r a/get-process-progress db process-id)]
                    {:process-failured? (r a/process-failured? db process-id)
                     :process-progress  (max fake-progress process-progress)})
               {:process-progress fake-progress})))

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
  (assoc-in db (db/meta-item-path :ui/progress-bar :process-id) process-id))

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
  (if (= process-id (get-in db (db/meta-item-path :ui/progress-bar :process-id)))
      (dissoc-in db (db/meta-item-path :ui/progress-bar :process-id))
      (return    db)))

; @usage
;  [:ui/stop-listening-to-process! :my-request]
(a/reg-event-db :ui/stop-listening-to-process! stop-listening-to-process!)

(defn fake-process!
  ; @param (integer) fake-progress
  ;
  ; @usage
  ;  (r ui/fake-process! db 40)
  ;
  ; @return (map)
  [db [_ fake-progress]]
  (assoc-in db (db/meta-item-path :ui/progress-bar :fake-progress) fake-progress))

; @usage
;  [:ui/fake-process! 40]
(a/reg-event-db :ui/fake-process! fake-process!)

(defn stop-faking-process!
  ; @usage
  ;  (r ui/stop-faking-process! db)
  ;
  ; @return (map)
  [db [_ process-id]]
  (dissoc-in db (db/meta-item-path :ui/progress-bar :fake-progress)))

; @usage
;  [:ui/stop-faking-process!]
(a/reg-event-db :ui/stop-faking-process! stop-faking-process!)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- progress-bar-process-progress
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (hiccup)
  []
  (let [{:keys [process-failured? process-progress]} @(a/subscribe [:ui/get-progress-bar-props])]
       [:div#x-app-progress-bar--process-progress {:style {:height (case process-progress 0 "0" "6px")
                                                           :width  (css/percent process-progress)}
                                                   :data-failured  (boolean     process-failured?)}]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (hiccup)
  []
  ; Ahhoz, hogy a [:ui/fake-process! ...] esemény használatával beállított hamis folyamatjelző állapota
  ; átmenetesen jelenjen meg (CSS transition), az elemnek a DOM-fában kell lennie az érték beállításakor!
  [:div#x-app-progress-bar [progress-bar-process-progress]])
