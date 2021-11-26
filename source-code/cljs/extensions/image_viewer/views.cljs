
(ns extensions.image-viewer.views
    (:require [x.app-core.api :as a]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  [])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :extensions.image-viewer/render!
  [:ui/set-surface! ::view
                    {:content #'view
                     :layout :unboxed}])
