
(ns x.app-developer.engine
    (:require [x.app-core.api :as a]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :developer.test!
  [:x.app-ui/blow-bubble! {:content "It works!"}])
