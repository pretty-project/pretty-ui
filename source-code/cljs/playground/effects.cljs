
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns playground.effects
    (:require [x.app-core.api :as a]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :playground/test!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch [:developer/test!]}))