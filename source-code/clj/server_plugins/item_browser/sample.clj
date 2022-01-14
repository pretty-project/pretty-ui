
(ns server-plugins.item-browser.sample
    (:require [x.server-core.api :as a]
              [server-plugins.item-browser.api :as item-browser]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  ; Az [:item-browser/initialize! ...] esemény hozzáadja a "/@app-home/my-extension"
  ; és "/@app-home/my-extension/:my-type-id" útvonalakat a rendszerhez, amely útvonalak
  ; használatával betöltődik a kliens-oldalon az item-browser plugin.
  {:on-server-boot [:item-browser/initialize! :my-extension :my-type {:default-item-id "my-item"
                                                                      :search-keys     [:name :email-address]}]})
