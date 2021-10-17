
(ns extensions.webshops.views
    (:require [x.app-core.api :as a]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  [])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :extensions.webshops/render!
  [:x.app-ui/set-surface!
   ::view
   {:content #'view
    :layout :unboxed}])
