
(ns extensions.notifications.views
    (:require [x.app-core.api :as a]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  [])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :extensions.notifications/render!
  [:x.app-ui/set-surface!
   ::view
   {:content #'view
    :layout :unboxed}])
