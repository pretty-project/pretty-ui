
(ns server-extensions.trader.scheduler
    (:require [mid-fruits.candy  :refer [param return]]
              [tea-time.core     :as tt]
              [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (s)
(def DEFAULT-BOOT-DELAY 120)

; @constant (s)
(def DEFAULT-INTERVAL 60)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- initialize-tasks!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) options
  ;  {:boot-delay (s)
  ;   :interval (s)}
  [{:keys [boot-delay interval]}]
  (let [boot-delay-a (param boot-delay)
        boot-delay-b (+     boot-delay (/ interval 2))
        task-a       (bound-fn [] (a/dispatch [:trader/update-market-data!]))
        task-b       (bound-fn [] (a/dispatch [:trader/resolve-market-data!]))]
      (tt/every! interval boot-delay-a task-a)
      (tt/every! interval boot-delay-b task-b)))

(defn- initialize-scheduler!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) options
  ;  {:boot-delay (s)
  ;   :interval (s)}
  [options]
  (if-let [scheduler-initialized? (a/subscribed [:db/get-item [:trader :scheduler :initialized?]])]
          ; wrap-reload ...
          (return false)
          (do (println "[:trader] initializing scheduler ...")
              (tt/start!)
              (initialize-tasks! options)
              (a/dispatch [:db/set-item! [:trader :scheduler :initialized?] true])
              (a/dispatch [:trader/log! :trader/scheduler "Initializing scheduler ..."]))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(initialize-scheduler! {:boot-delay DEFAULT-BOOT-DELAY
                        :interval   DEFAULT-INTERVAL})
