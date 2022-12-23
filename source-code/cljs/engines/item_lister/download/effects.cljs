
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.download.effects
    (:require [engines.item-lister.body.subs           :as body.subs]
              [engines.item-lister.core.events         :as core.events]
              [engines.item-lister.core.subs           :as core.subs]
              [engines.item-lister.download.events     :as download.events]
              [engines.item-lister.download.queries    :as download.queries]
              [engines.item-lister.download.subs       :as download.subs]
              [engines.item-lister.download.validators :as download.validators]
              [re-frame.api                            :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/reload-items!
  ; @param (keyword) lister-id
  ; @param (map)(opt) reload-props
  ; {:display-progress? (boolean)(opt)
  ;   Default: false
  ;  :on-failure (metamorphic-event)(opt)
  ;  :on-success (metamorphic-event)(opt)
  ;  :on-stalled (metamorphic-event)(opt)
  ;  :progress-behaviour (keyword)(opt)
  ;   :keep-faked, :normal
  ;   Default: :normal
  ;   W/ {:display-progress? true}}
  ;  :progress-max (percent)(opt)
  ;   Default: 100
  ;   W/ {:display-progress? true}}
  ;
  ; @usage
  ; [:item-lister/reload-items! :my-lister]
  ;
  ; @usage
  ; [:item-lister/reload-items! :my-lister {...}]
  (fn [{:keys [db]} [_ lister-id {:keys [on-failure on-stalled] :as reload-props}]]
      ; XXX#4057 (source-code/cljs/engines/item_handler/download/effects.cljs)
      ;
      ; Az [:item-lister/reload-items! ...] esemény újra letölti a listában található elemeket.
      ;
      ; Ha a szerver-oldalon az elemeket tartalmazó kollekció megváltozott, akkor nem feltétlenül
      ; ugyanazok az elemek töltődnek le!
      ;
      ; Ha pl. a kliens-oldalon az újratöltés előtt 42 elem van letöltve és a {:download-limit ...}
      ; értéke 20, akkor az esemény az 1. - 60. elemeket kéri le a szerverről.
      ;
      ; A {:reload-mode? true} beállítás csak a query elkészítéséhez szükséges, utána már nincs
      ; szükség rá, hogy érvényben maradjon, ezért a set-reload-mode! függvénnyel megváltoztatott
      ; db értéke nem kerül eltárolásra!
      ;
      ; Ha az esemény megtörténésekor a body komponens már nincs a React-fába csatolva,
      ; akkor NEM tölti le újra az elemeket!
      (if (r body.subs/body-did-mount? db lister-id)
          (let [db           (r core.events/set-reload-mode!                      db lister-id)
                query        (r download.queries/get-request-items-query          db lister-id)
                validator-f #(r download.validators/request-items-response-valid? db lister-id %)
                on-failure   {:dispatch-n [on-failure [:item-lister/set-engine-error!       lister-id :failed-to-reload-items]]}
                on-stalled   {:dispatch-n [on-stalled [:item-lister/receive-reloaded-items! lister-id reload-props]]}]
               [:pathom/send-query! (r core.subs/get-request-id db lister-id)
                                    (assoc reload-props :query query :validator-f validator-f :on-failure on-failure :on-stalled on-stalled)]))))

(r/reg-event-fx :item-lister/receive-reloaded-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) reload-props
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id _ server-response]]
      ; Az {:on-reload ...} tulajdonságként átadott esemény használatával megoldható,
      ; hogy a listaelemeken végzett műveletek záró-eseményei a listaelemek sikeres újratöltése
      ; után történjenek meg.
      ;
      ; Lassú internetkapcsolat esetén zavaró lenne, ha a listaelemeken végzett műveletek végét
      ; jelző értesítések, a műveletek után, még a listaelemek újratöltése előtt jelennének meg.
      ;
      ; Pl.: Ha a kiválasztott listaelemek sikeres törlése után azonnal jelenne meg
      ;      a "Törölt elemek visszaállítása" értesítés, akkor a felhasználó még a listaelemek
      ;      újratöltése közben elindíthatná a "Törölt elemek visszaállítása" folyamatot,
      ;      ami azonban nem indítaná el a lekérést, mivel a listaelemek újratöltése még folyamatban
      ;      van és az engine egyes lekérései megegyező azonosítóval rendelkeznek
      ;      (XXX#5476 - source-code/cljs/engines/engine_handler/core/subs.cljs),
      ;      ami megakadályozza, hogy párhuzamosan több lekérés történjen (x4.6.8).
      ;
      ; Ha az esemény megtörténésekor a body komponens már nincs a React-fába csatolva,
      ; akkor nem tárolja el a letöltött elemeket!
      (if (r body.subs/body-did-mount? db lister-id)
          {:db (r download.events/receive-reloaded-items! db lister-id server-response)})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-lister/request-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  (fn [{:keys [db]} [_ lister-id]]
      ; Ha az infinite-loader komponens ismételten megjelenik a viewport területén, csak abban
      ; az esetben próbál újabb elemeket letölteni, ha még nincs az összes letöltve.
      ;
      ; XXX#4057 (source-code/cljs/engines/item_handler/download/effects.cljs)
      ; A letöltött dokumentumok on-success helyett on-stalled időpontban kerülnek tárolásra
      ; a Re-Frame adatbázisba, így elkerülhető, hogy a request idle-timeout ideje alatt
      ; az újonnan letöltött dokumentumok már kirenderelésre kerüljenek, amíg a letöltést jelző
      ; felirat még megjelenik a lista végén.
      (if (r core.subs/request-items? db lister-id)
          (let [display-progress? (r body.subs/get-body-prop                           db lister-id :display-progress?)
                query             (r download.queries/get-request-items-query          db lister-id)
                validator-f      #(r download.validators/request-items-response-valid? db lister-id %)]
               [:pathom/send-query! (r core.subs/get-request-id db lister-id)
                                    {:display-progress? display-progress?
                                     :on-stalled [:item-lister/receive-items!    lister-id]
                                     :on-failure [:item-lister/set-engine-error! lister-id :failed-to-request-items]
                                     :query query :validator-f validator-f}]))))

(r/reg-event-fx :item-lister/receive-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id server-response]]
      ; Az elemek letöltődése után újratölti az infinite-loader komponenst, hogy megállapítsa,
      ; hogy az a viewport területén van-e még és szükséges-e további elemeket letölteni.
      ;
      ; Ha az [:item-lister/receive-items! ...] esemény megtörténésekor a body komponens már
      ; nincs a React-fába csatolva, akkor az esemény nem végez műveletet!
      (if (r body.subs/body-did-mount? db lister-id)
          {:db (r download.events/receive-items! db lister-id server-response)
           :fx [:infinite-loader/reload-loader! lister-id]})))
