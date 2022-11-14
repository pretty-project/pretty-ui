
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.progress-screen.events
    (:require [mid-fruits.map :refer [dissoc-in]]
              [re-frame.api   :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn lock-screen!
  ; @param (keyword) process-id
  ;
  ; @usage
  ;  (r lock-screen! db process-id)
  ;
  ; @return (map)
  [db [_ process-id]]
  (assoc-in db [:x.ui :progress-screen/meta-items :process-locks process-id] true))

(defn unlock-screen!
  ; @param (keyword) process-id
  ;
  ; @usage
  ;  (r unlock-screen! db process-id)
  ;
  ; @return (map)
  [db [_ process-id]]
  (dissoc-in db [:x.ui :progress-screen/meta-items :process-locks process-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.ui/lock-screen! :my-process]
(r/reg-event-db :x.ui/lock-screen! lock-screen!)

; @usage
;  [:x.ui/unlock-screen! :my-process]
(r/reg-event-db :x.ui/unlock-screen! unlock-screen!)
