
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns pathom.register.helpers
    (:require [com.wsscode.pathom3.connect.indexes :as pathom.ci]
              [pathom.register.state               :as register.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-environment!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [handlers    (-> register.state/HANDLERS deref vals)
        registry    [handlers]
        environment (pathom.ci/register registry)]
       (reset! register.state/ENVIRONMENT environment)))