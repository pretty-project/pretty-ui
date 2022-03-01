
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.error-handler.side-effects
    (:require [x.app-details :as details]
              [x.server-core.event-handler        :as event-handler]
              [x.server-core.error-handler.engine :as error-handler.engine]))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-catched
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) error-props
  [error-props]
  (println details/app-codename error-handler.engine/DEFAULT-SERVER-ERROR))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-fx :core/error-catched error-catched)
