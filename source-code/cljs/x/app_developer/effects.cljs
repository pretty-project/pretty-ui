
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.effects
    (:require [x.app-core.api :as a]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :developer/test!
  [:ui/blow-bubble! {:body "It works!"}])