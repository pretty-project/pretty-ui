
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.22
; Description: 'Failure is success in progress' – Albert Einstein
; Version: v0.3.8
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.process-handler
    (:require [mid-fruits.map           :refer [dissoc-in]]
              [mid-fruits.math          :as math]
              [mid-fruits.vector        :as vector]
              [x.app-core.event-handler :as event-handler :refer [r]]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name process-status
;  :ready    - Folyamattal kapcsolatos státuszváltozás nem történt
;  :prepare  - Folyamat előkészítése
;  :progress - Folyamatban
;  :failure  - Folyamat befejezve (sikertelen)
;  :success  - Folyamat befejezve (sikeres)
;  :blocked  - Folyamat blokkolva (nem indítható/újraindítható)
;
; @name process-activity
;  :inactive - Folyamattal kapcsolatos aktivitás nem történt (folyamat indítható)
;  :active   - Folyamatban (folyamat nem újraindítható)
;  :idle     - Folyamat befejezve (üresjárati idő – folyamat újraindítható)
;  :stalled  - Folyamat befejezve (lezárva        – folyamat újraindítható)
;
; @name process-progress
;  0% - 100%



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; ; @constant (ms)
; (def IDLE-TIMEOUT 2000)
;
; (a/reg-event-fx
;   :lets-rock!
;   {:dispatch-n [[:core/reg-process-status-event! :my-process :failure [:->my-process-failured]
;                 [:core/reg-process-status-event! :my-process :success [:->my-process-successed]
;                 [:initialize-something!]]})
;
; (a/reg-event-fx
;   :initialize-something!
;   {:dispatch-n [[:core/set-process-status! :my-process :prepare]
;                 [:start-something!]]})
;
; (a/reg-event-fx
;   :start-something!
;   {:dispatch-n [[:core/set-process-status!   :my-process :progress]
;                 [:core/set-process-activity! :my-process :active]
;                 [:do-something!]]})
;
; (a/reg-event-fx
;  :do-something!
;  (fn [{:keys [db]} _])
;      (if (r something-success? db))
;          {:dispatch [:->something-successed]}
;          {:dispatch [:->something->failured]})
;
; (a/reg-event-fx
;   :->something-successed
;   {:dispatch-n     [[:core/set-process-status!   :my-process :success]
;                     [:core/set-process-activity! :my-process :idle]]
;    :dispatch-later [{:ms       IDLE-TIMEOUT
;                      :dispatch [:core/set-process-activity! :my-process :stalled]}]})
;
; (a/reg-event-fx
;   :->something-failured
;   {:dispatch-n     [[:core/set-process-status!   :my-process :failure]
;                     [:core/set-process-activity! :my-process :idle]]
;    :dispatch-later [{:ms       IDLE-TIMEOUT
;                      :dispatch [:core/set-process-activity! :my-process :stalled]}]})



;; -- Process status subscriptions --------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-process-status
  ; @param (keyword) process-id
  ;
  ; @return (keyword)
  [db [_ process-id]]
  (let [process-status (get-in db [:core/processes :data-items process-id :status])]
       (or process-status :ready)))

(defn process-ready?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-status (r get-process-status db process-id)]
       (= process-status :ready)))

(defn process-preparing?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-status (r get-process-status db process-id)]
       (= process-status :prepare)))

(defn process-in-progress?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-status (r get-process-status db process-id)]
       (= process-status :progress)))

(defn process-success?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-status (r get-process-status db process-id)]
       (= process-status :success)))

(defn process-failured?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-status (r get-process-status db process-id)]
       (= process-status :failure)))

(defn process-blocked?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-status (r get-process-status db process-id)]
       (= process-status :blocked)))



;; -- Process activity subscriptions ------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-process-activity
  ; @param (keyword) process-id
  ;
  ; @return (keyword)
  [db [_ process-id]]
  (let [process-activity (get-in db [:core/processes :data-items process-id :activity])]
       (or process-activity :inactive)))

(defn process-inactive?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-activity (r get-process-activity db process-id)]
       (= process-activity :inactive)))

(defn process-active?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-activity (r get-process-activity db process-id)]
       (= process-activity :active)))

(defn process-idle?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-activity (r get-process-activity db process-id)]
       (= process-activity :idle)))

(defn process-stalled?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-activity (r get-process-activity db process-id)]
       (= process-activity :stalled)))



;; -- Process progress subscriptions ------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-process-progress
  ; @param (keyword) process-id
  ;
  ; @return (percent)
  [db [_ process-id]]
  (let [process-progress (get-in db [:core/processes :data-items process-id :progress])]
       (or process-progress 0)))

(defn process-done?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (= 100 (r get-process-progress db process-id)))



;; -- Process control subscriptions -------------------------------------------
;; ----------------------------------------------------------------------------

(defn start-process?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (not (or (r process-active?  db process-id)
           (r process-blocked? db process-id))))



;; -- Process state subscriptions ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-process-state
  ; @param (keyword) process-id
  ;
  ; @usage
  ;  (r a/get-process-state db :my-process)
  ;
  ; @return (map)
  ;  {:process-status (keyword)
  ;   :process-activity (keyword)
  ;   :process-progress (percent)}
  [db [_ process-id]]
  {:process-status   (r get-process-status   db process-id)
   :process-activity (r get-process-activity db process-id)
   :process-progress (r get-process-progress db process-id)})

; @usage
;  [:core/get-process-state :my-process]
(event-handler/reg-sub :core/get-process-state get-process-state)



;; -- Process events subscriptions --------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-process-progress-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) process-id
  ; @param (percent) process-progress
  ;
  ; @return (vector)
  [db [_ process-id process-progress]]
  (get-in db [:core/processes :data-items process-id :progress-events process-progress]))

(defn- get-process-status-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) process-id
  ; @param (keyword) process-status
  ;
  ; @return (vector)
  [db [_ process-id process-status]]
  (get-in db [:core/processes :data-items process-id :status-events process-status]))

(defn- get-process-activity-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) process-id
  ; @param (keyword) process-activity
  ;
  ; @return (vector)
  [db [_ process-id process-activity]]
  (get-in db [:core/processes :data-items process-id :activity-events process-activity]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-process-progress!
  ; WARNING#4067
  ;  A set-process-*! események adatbázis függvényként való használatakor
  ;  nem történnek meg a hozzárendelt process-*-event események.
  ;
  ;  As DB-event:     (r a/set-process-progress! db :my-process)
  ;  As effect-event: [:core/set-process-progress!  :my-process]
  ;
  ; @param (keyword) process-id
  ; @param (percent) process-progress
  ;
  ; @return (map)
  [db [_ process-id process-progress]]
  (assoc-in db [:core/processes :data-items process-id :progress] process-progress))

(defn set-process-status!
  ; WARNING#4067
  ;
  ; @param (keyword) process-id
  ; @param (keyword) process-status
  ;  :prepare, :progress, :failure, :success
  ;
  ; @return (map)
  [db [_ process-id process-status]]
  (assoc-in db [:core/processes :data-items process-id :status] process-status))

(defn set-process-activity!
  ; WARNING#4067
  ;
  ; @param (keyword) process-id
  ; @param (keyword) process-activity
  ;  :active, :idle, :stalled, ...
  ;
  ; @return (map)
  [db [_ process-id process-activity]]
  (assoc-in db [:core/processes :data-items process-id :activity] process-activity))

(defn- reg-process-status-event!
  ; @param (keyword) process-id
  ; @param (keyword) process-status
  ; @param (metamorphic-event) event
  ;
  ; @usage
  ;  (r a/reg-process-status-event! db :my-process :success [:do-something!])
  ;
  ; @return (map)
  [db [_ process-id process-status event]]
  (update-in db [:core/processes :data-items process-id :status-events process-status]
             vector/conj-item event))

; @usage
;  [:core/reg-process-status-event! :my-process :success [:do-something!]]
(event-handler/reg-event-db :core/reg-process-status-event! reg-process-status-event!)

(defn- reg-process-activity-event!
  ; @param (keyword) process-id
  ; @param (keyword) process-activity
  ; @param (metamorphic-event) event
  ;
  ; @usage
  ;  (r a/reg-process-activity-event! db :my-process :stalled [:do-something!])
  ;
  ; @return (map)
  [db [_ process-id process-activity event]]
  (update-in db [:core/processes :data-items process-id :activity-events process-activity]
             vector/conj-item event))

; @usage
;  [:core/reg-process-activity-event! :my-process :stalled [:do-something!]]
(event-handler/reg-event-db :core/reg-process-activity-event! reg-process-activity-event!)

(defn- reg-process-progress-event!
  ; @param (keyword) process-id
  ; @param (percent) process-progress
  ; @param (metamorphic-event) event
  ;
  ; @usage
  ;  (r a/reg-process-progress-event! db :my-process 100 [:do-something!])
  ;
  ;
  ; @return (map)
  [db [_ process-id process-progress event]]
  (update-in db [:core/processes :data-items process-id :progress-events process-progress]
             vector/conj-item event))

; @usage
;  [:core/reg-process-progress-event! :my-process 100 [:do-something!]]
(event-handler/reg-event-db :core/reg-process-progress-event! reg-process-progress-event!)

(defn- clear-process!
  ; @param (keyword) process-id
  ;
  ; @usage
  ;  [:core/clear-process! :my-process]
  ;
  ; @return (map)
  [db [_ process-id]]
  (dissoc-in db [:core/processes :data-items process-id]))

(event-handler/reg-event-db :core/clear-process! clear-process!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :core/set-process-progress!
  ; @param (keyword) process-id
  ; @param (percent) process-progress
  ;
  ; @usage
  ;  [:core/set-process-progress! :my-process 100]
  (fn [{:keys [db]} [_ process-id process-progress]]
      (let [process-progress (math/floor process-progress)]
           (if-let [process-events (r get-process-progress-events db process-id process-progress)]
                   {:db (r set-process-progress! db process-id process-progress)
                    :dispatch-n process-events}
                   {:db (r set-process-progress! db process-id process-progress)}))))

(event-handler/reg-event-fx
  :core/set-process-status!
  ; @param (keyword) process-id
  ; @param (keyword) process-status
  ;
  ; @usage
  ;  [:core/set-process-status! :my-process :success]
  (fn [{:keys [db]} [_ process-id process-status]]
      (if-let [status-events (r get-process-status-events db process-id process-status)]
              {:db (r set-process-status! db process-id process-status)
               :dispatch-n status-events}
              {:db (r set-process-status! db process-id process-status)})))

(event-handler/reg-event-fx
  :core/set-process-activity!
  ; @param (keyword) process-id
  ; @param (keyword) process-activity
  ;
  ; @usage
  ;  [:core/set-process-activity! :my-process :stalled]
  (fn [{:keys [db]} [_ process-id process-activity]]
      (if-let [activity-events (r get-process-activity-events db process-id process-activity)]
              {:db (r set-process-activity! db process-id process-activity)
               :dispatch-n activity-events}
              {:db (r set-process-activity! db process-id process-activity)})))
