
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-browser.sample
    (:require [x.server-core.api :as a]
              [server-plugins.item-browser.api :as item-browser]))



;; -- Példa dokumentum --------------------------------------------------------
;; ----------------------------------------------------------------------------

; - A dokumentoknak tartalmazniuk kell a {:label-key ...} tulajdonságként átadott kulcsot!
; - A dokumentoknak tartalmazniuk kell a {:path-key ...} tulajdonságként átadott kulcsot!
; - A böngészhető dokumentoknak tartalmazniuk kell az {:items-key ...} tulajdonságként átadott kulcsot!
(def SAMPLE-DOCUMENT {:my-type/id    "..."
                      :my-type/name  "My document"
                      :my-type/path  [{:my-type/id "..."} {:my-type/id "..."}]
                      :my-type/items [{:my-type/id "..."}]})



;; -- A plugin beállítása alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

; - Az item-browser plugin az item-lister plugint alkalmazza az elemek listázához,
;   a browser-props térképben található beállítások egy része az item-lister plugin beállításához
;   szükséges és leírásukat annak dokumentációjában találod!
; - Az [:item-browser/init-browser! ...] esemény {:routed? true} beállítással használva,
;   hozzáadja a "/@app-home/my-extension" és "/@app-home/my-extension/:my-type-id" útvonalakat
;   a rendszerhez, amely útvonalak használatával betöltődik a kliens-oldalon az item-browser plugin.
(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-browser/init-browser! :my-extension :my-type]})



;; -- A plugin beállítása -----------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:item-browser/init-browser! :my-extension :my-type
                                                {:items-key :items
                                                 :label-key :name
                                                 :path-key  :path
                                                 :root-item-id "my-item"}]})
