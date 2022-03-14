
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.progress-bar.subs
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-process-progress
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (integer)
  [db _]
  (let [fake-progress (get-in db [:ui :progress-bar/meta-items :fake-progress] 0)]
       (if-let [process-id (get-in db [:ui :progress-bar/meta-items :process-id])]
               (let [process-progress (r a/get-process-progress db process-id)]
                    (max fake-progress process-progress))
               (return fake-progress))))

(defn process-failured?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (if-let [process-id (get-in db [:ui :progress-bar/meta-items :process-id])]
          (r a/process-failured? db process-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :ui/get-process-progress get-process-progress)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :ui/process-failured? process-failured?)
