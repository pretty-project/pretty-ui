
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-activities.channel-handler.side-effects
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn notifiy-client!
  ; @param (keyword) channel-id
  ; @param (map) ...
  ;  {...}
  [channel-id])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-fx :activities/notifiy-client! notifiy-client!)