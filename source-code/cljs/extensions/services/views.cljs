
(ns extensions.services.views
    (:require [x.app-core.api :as a]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; @return ()
  []
  [:div "zZzzzZZzzz"])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :extensions.services/render!
  [:x.app-ui/set-surface!
   ::view
   {:content #'view
    :content-label :services
    :initializer [:extensions.services/download-services!]}])
