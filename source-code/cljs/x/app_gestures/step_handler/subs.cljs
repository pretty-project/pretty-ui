
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-gestures.step-handler.subs
    (:require [mid-fruits.candy  :refer [return]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn step-handler-inited?
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/step-handler-inited? db :my-handler)
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (let [handler-props (get-in db [:gestures :step-handler/data-items handler-id])]
       (some? handler-props)))

(defn autostep?
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/autostep? db :my-handler)
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (and (get-in db [:gestures :step-handler/data-items handler-id :autostep?])
       (not (get-in db [:gestures :step-handler/data-items handler-id :paused?]))))

(defn progressive-stepping?
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/progressive-stepping? db :my-handler)
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (let [step-duration (get-in db [:gestures :step-handler/data-items handler-id :step-duration])]
       (> step-duration 0)))

(defn get-step-timeout
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/get-step-timeout db :my-handler)
  ;
  ; @return (integer)
  [db [_ handler-id]]
  (let [step-duration (get-in db [:gestures :step-handler/data-items handler-id :step-duration])
        step-interval (get-in db [:gestures :step-handler/data-items handler-id :step-interval])]
       (+ step-duration step-interval)))

(defn get-steps
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/get-steps db :my-handler)
  ;
  ; @return (vector)
  [db [_ handler-id]]
  (get-in db [:gestures :step-handler/data-items handler-id :steps]))

(defn get-step
  ; @param (keyword) handler-id
  ; @param (integer) dex
  ;
  ; @usage
  ;  (r gestures/get-step db :my-handler)
  ;
  ; @return (*)
  [db [_ handler-id dex]]
  (nth (r get-steps db handler-id) dex))

(defn get-max-dex
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/get-max-dex db :my-handler)
  ;
  ; @return (integer)
  [db [_ handler-id]]
  (if-let [steps (r get-steps db handler-id)]
          (let [step-count (count steps)]
               (dec step-count))
          (return 0)))

(defn get-current-dex
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/get-current-dex db :my-handler)
  ;
  ; @return (integer)
  [db [_ handler-id]]
  (or (get-in db [:gestures :step-handler/data-items handler-id :current-dex])
      (return 0)))

(defn max-dex-reached?
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/max-dex-reached? db :my-handler)
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (= (r get-max-dex     db handler-id)
     (r get-current-dex db handler-id)))

(defn get-prev-dex
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/get-prev-dex db :my-handler)
  ;
  ; @return (integer)
  [db [_ handler-id]]
  (let [steps       (r get-steps       db handler-id)
        current-dex (r get-current-dex db handler-id)]
       (if (get-in db [:gestures :step-handler/data-items handler-id :infinite-stepping?])
           (vector/prev-dex steps current-dex)
           (vector/dec-dex  steps current-dex))))

(defn get-next-dex
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/get-next-dex db :my-handler)
  ;
  ; @return (integer)
  [db [_ handler-id]]
  (let [steps       (r get-steps       db handler-id)
        current-dex (r get-current-dex db handler-id)]
       (if (get-in db [:gestures :step-handler/data-items handler-id :infinite-stepping?])
           (vector/next-dex steps current-dex)
           (if (r max-dex-reached? db handler-id)
               (return current-dex)
               (vector/inc-dex steps current-dex)))))

(defn get-current-step
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/get-current-step db :my-handler)
  ;
  ; @return (*)
  [db [_ handler-id]]
  (let [current-dex (r get-current-dex db handler-id)]
       (r get-step db handler-id current-dex)))

(defn get-prev-step
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/get-prev-step db :my-handler)
  ;
  ; @return (*)
  [db [_ handler-id]]
  (let [prev-dex (r get-prev-dex db handler-id)]
       (r get-step db handler-id prev-dex)))

(defn get-next-step
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/get-next-step db :my-handler)
  ;
  ; @return (*)
  [db [_ handler-id]]
  (let [next-dex (r get-next-dex db handler-id)]
       (r get-step db handler-id next-dex)))

(defn stepping-paused?
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/stepping-paused? db :my-handler)
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (get-in db [:gestures :step-handler/data-items handler-id :paused?]))

(defn get-step-count
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/get-step-count db :my-handler)
  ;
  ; @return (integer)
  [db [_ handler-id]]
  (count (r get-steps db handler-id)))

(defn step-in-progress?
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/step-in-progress? db :my-handler)
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (some? (get-in db [:gestures :step-handler/data-items handler-id :step-direction])))
