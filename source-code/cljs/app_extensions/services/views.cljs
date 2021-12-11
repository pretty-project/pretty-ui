
(ns app-extensions.services.views
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
  :services/render!
  [:ui/set-surface! ::view
                    {:view {:content #'view}}])
