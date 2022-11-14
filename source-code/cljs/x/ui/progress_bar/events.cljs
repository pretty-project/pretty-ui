
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.progress-bar.events
    (:require [candy.api      :refer [return]]
              [mid-fruits.map :refer [dissoc-in]]
              [re-frame.api   :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn listen-to-process!
  ; @param (keyword) process-id
  ;
  ; @usage
  ;  (r listen-to-process! db :my-request)
  ;
  ; @return (map)
  [db [_ process-id]]
  (assoc-in db [:x.ui :progress-bar/meta-items :process-id] process-id))

(defn stop-listening-to-process!
  ; @param (keyword) process-id
  ;
  ; @usage
  ;  (r stop-listening-to-process! db :my-request)
  ;
  ; @return (map)
  [db [_ process-id]]
  ; Ha a progress-bar, még a process-id azonosítójú folyamatra figyelt, akkor befejezi a folyamat
  ; figyelését és az esetlegesen beállított fake-progress értéket is törli.
  (if-not (= process-id (get-in db [:x.ui :progress-bar/meta-items :process-id]))
          (return db)
          (->     db (dissoc-in [:x.ui :progress-bar/meta-items :process-id])
                     (dissoc-in [:x.ui :progress-bar/meta-items :fake-progress]))))

(defn fake-process!
  ; @param (integer) fake-progress
  ;
  ; @usage
  ;  (r fake-process! db 40)
  ;
  ; @return (map)
  [db [_ fake-progress]]
  (assoc-in db [:x.ui :progress-bar/meta-items :fake-progress] fake-progress))

(defn stop-faking-process!
  ; @usage
  ;  (r stop-faking-process! db)
  ;
  ; @return (map)
  [db [_ process-id]]
  (dissoc-in db [:x.ui :progress-bar/meta-items :fake-progress]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.ui/listen-to-process! :my-request]
(r/reg-event-db :x.ui/listen-to-process! listen-to-process!)

; @usage
;  [:x.ui/stop-listening-to-process! :my-request]
(r/reg-event-db :x.ui/stop-listening-to-process! stop-listening-to-process!)

; @usage
;  [:x.ui/fake-process! 40]
(r/reg-event-db :x.ui/fake-process! fake-process!)

; @usage
;  [:x.ui/stop-faking-process!]
(r/reg-event-db :x.ui/stop-faking-process! stop-faking-process!)
