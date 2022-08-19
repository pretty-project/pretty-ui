

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.progress-bar.events
    (:require [mid-fruits.candy :refer [param return]]
              [mid-fruits.map   :refer [dissoc-in]]
              [x.app-core.api   :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn listen-to-process!
  ; @param (keyword) process-id
  ;
  ; @usage
  ;  (r ui/listen-to-process! db :my-request)
  ;
  ; @return (map)
  [db [_ process-id]]
  (assoc-in db [:ui :progress-bar/meta-items :process-id] process-id))

(defn stop-listening-to-process!
  ; @param (keyword) process-id
  ;
  ; @usage
  ;  (r ui/stop-listening-to-process! db :my-request)
  ;
  ; @return (map)
  [db [_ process-id]]
  ; Ha a progress-bar, még a process-id azonosítójú folyamatra figyelt, akkor befejezi a folyamat
  ; figyelését és az esetlegesen beállított fake-progress értéket is törli.
  (if-not (= process-id (get-in db [:ui :progress-bar/meta-items :process-id]))
          (return db)
          (->     db (dissoc-in [:ui :progress-bar/meta-items :process-id])
                     (dissoc-in [:ui :progress-bar/meta-items :fake-progress]))))

(defn fake-process!
  ; @param (integer) fake-progress
  ;
  ; @usage
  ;  (r ui/fake-process! db 40)
  ;
  ; @return (map)
  [db [_ fake-progress]]
  (assoc-in db [:ui :progress-bar/meta-items :fake-progress] fake-progress))

(defn stop-faking-process!
  ; @usage
  ;  (r ui/stop-faking-process! db)
  ;
  ; @return (map)
  [db [_ process-id]]
  (dissoc-in db [:ui :progress-bar/meta-items :fake-progress]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:ui/listen-to-process! :my-request]
(a/reg-event-db :ui/listen-to-process! listen-to-process!)

; @usage
;  [:ui/stop-listening-to-process! :my-request]
(a/reg-event-db :ui/stop-listening-to-process! stop-listening-to-process!)

; @usage
;  [:ui/fake-process! 40]
(a/reg-event-db :ui/fake-process! fake-process!)

; @usage
;  [:ui/stop-faking-process!]
(a/reg-event-db :ui/stop-faking-process! stop-faking-process!)
