
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.developer-tools.subs
    (:require [x.app-core.api :as a :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn print-events?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [debug-mode (r a/get-debug-mode db)]
       (= debug-mode "pineapple-juice")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :developer-tools/print-events? print-events?)
