
(ns server-plugins.item-browser.sample
    (:require [x.server-core.api :as a]
              [server-plugins.item-browser.api :as item-browser]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SAMPLE-DOCUMENT {:my-type/id "..."
                      ; A dokumentoknak tartalmazniuk kell a {:label-key ...} tulajdonságként átadott kulcsot!
                      :my-type/name "My document"
                      ; A dokumentoknak tartalmazniuk kell a {:path-key ...} tulajdonságként átadott kulcsot!
                      :my-type/path [{:my-type/id "..."} {:my-type/id "..."}]})



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  ; Az [:item-browser/initialize! ...] esemény hozzáadja a "/@app-home/my-extension"
  ; és "/@app-home/my-extension/:my-type-id" útvonalakat a rendszerhez, amely útvonalak
  ; használatával betöltődik a kliens-oldalon az item-browser plugin.
  {:on-server-boot [:item-browser/initialize! :my-extension :my-type
                                              {:default-item-id "my-item"
                                               :label-key :name
                                               :path-key  :path}]})
  ; Az item-browser plugin az item-lister plugint alkalmazza az elemek listázához,
  ; a browser-props térképben található beállítások egy része az item-lister plugin beállításához
  ; szükséges és leírásukat annak dokumentációjában találod!
