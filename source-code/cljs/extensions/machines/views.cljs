
(ns extensions.machines.views
    (:require [x.app-core.api :as a]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  [])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :extensions.machines/render!
  [:ui/set-surface! ::view
                    {:content #'view
                     :layout :unboxed}])
