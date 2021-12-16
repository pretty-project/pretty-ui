
(ns app-extensions.trader.listeners
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [app-plugins.item-lister.api :as item-lister]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-listeners-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {})

(a/reg-sub :trader/get-listeners-props get-listeners-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [item-lister/body :trader :listener {}])
