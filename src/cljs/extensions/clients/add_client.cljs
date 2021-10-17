
(ns extensions.clients.add-client
    (:require [extensions.clients.client-form :rename {view client-form}]
              [x.app-core.api     :as a]
              [x.app-elements.api :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- ak8700
  []
  [:div#clients-add-client
    [client-form]])

(defn- view
  []
  [elements/box {:content #'ak8700
                 :horizontal-align :left}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::render!
  [:x.app-ui/set-surface!
   ::view
   {:content #'view
    :content-label "New client"}])
