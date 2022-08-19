
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.progress-screen.events
    (:require [mid-fruits.map :refer [dissoc-in]]
              [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn lock-screen!
  ; @param (keyword) process-id
  ;
  ; @usage
  ;  (r ui/lock-screen! db process-id)
  ;
  ; @return (map)
  [db [_ process-id]]
  (assoc-in db [:ui :progress-screen/meta-items :process-locks process-id] true))

(defn unlock-screen!
  ; @param (keyword) process-id
  ;
  ; @usage
  ;  (r ui/unlock-screen! db process-id)
  ;
  ; @return (map)
  [db [_ process-id]]
  (dissoc-in db [:ui :progress-screen/meta-items :process-locks process-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:ui/lock-screen! :my-process]
(a/reg-event-db :ui/lock-screen! lock-screen!)

; @usage
;  [:ui/unlock-screen! :my-process]
(a/reg-event-db :ui/unlock-screen! unlock-screen!)
