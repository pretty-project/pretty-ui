
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.lifecycle-handler.subs
    (:require [mid-fruits.candy         :refer [param return]]
              [x.mid-core.event-handler :as event-handler :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-lifes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (get-in db [:core :lifecycle-handler/data-items]))

(defn get-period-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) period-id
  ;
  ; @return (vector)
  [db [_ period-id]]
  (letfn [(f [period-events dex life] (if-let [period (get life period-id)]
                                              (conj   period-events period)
                                              (return period-events)))]
         (reduce-kv f [] (r get-lifes db))))
