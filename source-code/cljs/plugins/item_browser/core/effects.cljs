
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.effects
    (:require [plugins.item-browser.core.events :as core.events]
              [plugins.item-browser.core.subs   :as core.subs]
              [x.app-core.api                   :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/load-browser!

; NEM PUBLIC!

  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) browser-props
  ;  {:item-id (string)(opt)}
  ;
  ; @usage
  ;  [:item-browser/load-browser! :my-extension :my-type]
  ;
  ; @usage
  ;  [:item-browser/load-browser! :my-extension :my-type {...}]
  ;
  ; @usage
  ;  [:item-browser/load-browser! :my-extension :my-type {:item-id "my-item"}]
  (fn [{:keys [db]} [_ extension-id item-namespace browser-props]]
      ; XXX#4579
      {:db (r core.events/load-browser! db extension-id item-namespace browser-props)
       :dispatch-n [; XXX#5660
                    [:environment/reg-keypress-listener! :item-browser/keypress-listener]
                    [:item-browser/request-item! extension-id item-namespace]
                    ;(engine/load-extension-event extension-id item-namespace)
                    ; Ha az [:item-browser/load-browser! ...] esemény megtörténése előtt is
                    ; meg volt jelenítve az item-browser/body komponens és az infinite-loader
                    ; komponens a viewport területén volt, akkor szükséges az infinite-loader
                    ; komponenst újratölteni, hogy a megváltozott beállításokkal újratöltse
                    ; az adatokat.
                    [:tools/reload-infinite-loader! extension-id]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/browse-item!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  [:item-browser/browse-item! :my-extension :my-type "my-item"]
  (fn [{:keys [db]} [_ extension-id item-namespace item-id]]))
      ; - Az [:item-browser/browse-item! ...] esemény nem vizsglja, hogy az item-browser plugin
      ;   útvonala létezik-e.
      ; - Ha az aktuális útvonal az item-browser plugin útvonala, akkor átirányít a böngészendő
      ;   elem útvonalára.
      ; - Ha az aktuális útvonal NEM az item-browser plugin útvonala, akkor útvonal használata
      ;   nélkül indítja el az item-browser plugint, ezért lehetséges a plugint útvonalak
      ;   használata nélkül is elindítani, akkor is ha az item-browser plugin útvonalai léteznek.
      ;   Pl.: A plugin popup elemen való megjelenítése, útvonalak használata nélkül ...
;      (if (r subs/route-handled? db extension-id item-namespace)
          ; If handled by route ...
          ;(let [browser-uri (engine/browser-uri extension-id item-namespace item-id)]
          ;     [:router/go-to! browser-uri])
          ; If NOT handled by route ...
;          [:item-browser/load-browser! extension-id item-namespace {:item-id item-id}]]))

(a/reg-event-fx
  :item-browser/go-home!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-browser/go-home! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [root-item-id (r core.subs/get-root-item-id db extension-id item-namespace)]
           [:item-browser/browse-item! extension-id item-namespace root-item-id])))

(a/reg-event-fx
  :item-browser/go-up!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-browser/go-up! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [parent-item-id (r core.subs/get-parent-item-id db extension-id item-namespace)]
           [:item-browser/browse-item! extension-id item-namespace parent-item-id])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/use-filter!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) filter-pattern
  ;
  ; @usage
  ;  [:item-browser/use-filter! :my-extension :my-type {...}]
  (fn [{:keys [db]} [_ extension-id item-namespace filter-pattern]]
      {:db (r core.events/use-filter! db extension-id item-namespace filter-pattern)
       :dispatch [:tools/reload-infinite-loader! extension-id]}))
