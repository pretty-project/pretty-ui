
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.engine.effects
    (:require [plugins.item-browser.engine.events     :as engine.events]
              [plugins.item-browser.engine.queries    :as engine.queries]
              [plugins.item-browser.engine.subs       :as engine.subs]
              [plugins.item-browser.engine.validators :as engine.validators]
              [x.app-core.api                         :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/load-browser!
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
      {:db (r engine.events/load-browser! db extension-id item-namespace browser-props)
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
      (let [root-item-id (r engine.subs/get-root-item-id db extension-id item-namespace)]
           [:item-browser/browse-item! extension-id item-namespace root-item-id])))

(a/reg-event-fx
  :item-browser/go-up!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-browser/go-up! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [parent-item-id (r engine.subs/get-parent-item-id db extension-id item-namespace)]
           [:item-browser/browse-item! extension-id item-namespace parent-item-id])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/reload-items!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-browser/reload-items! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:dispatch-n [[:item-lister/reload-items!  extension-id item-namespace]
                    [:item-browser/request-item! extension-id item-namespace]]}))



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
      {:db (r engine.events/use-filter! db extension-id item-namespace filter-pattern)
       :dispatch [:tools/reload-infinite-loader! extension-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [request-id   (r engine.subs/get-request-id                       db extension-id item-namespace)
            query        (r download.queries/get-request-item-query          db extension-id item-namespace)
            validator-f #(r download.validators/request-item-response-valid? db extension-id item-namespace %)]
           [:sync/send-query! request-id
                              {:on-failure [:item-browser/set-error-mode! extension-id item-namespace]
                               :on-success [:item-browser/receive-item!   extension-id item-namespace]
                               :query query :validator-f validator-f}])))

(a/reg-event-fx
  :item-browser/receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]))
;      (let [db (r events/receive-item! db extension-id item-namespace server-response)]
;           (if-let [item-label (r subs/get-item-label db extension-id item-namespace)]
;                   {:db db :dispatch-if [(r subs/set-title? db extension-id item-namespace)
;                                         [:ui/set-title! item-label]]
;                   {:db db}]))
