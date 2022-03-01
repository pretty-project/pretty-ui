
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.boot-loader.subs
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-restart-target
  ; @return (string)
  [db _]
  (if-let [restart-target (get-in db [:boot-loader :restart-handler/meta-items :restart-target])]
          (return restart-target)
          (r a/get-app-config-item db :app-home)))
