
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.download.effects
    (:require [plugins.item-lister.body.subs           :as body.subs]
              [plugins.item-lister.core.events         :as core.events]
              [plugins.item-lister.core.subs           :as core.subs]
              [plugins.item-lister.download.events     :as download.events]
              [plugins.item-lister.download.queries    :as download.queries]
              [plugins.item-lister.download.subs       :as download.subs]
              [plugins.item-lister.download.validators :as download.validators]
              [x.app-core.api                          :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/reload-items!
  ; @param (keyword) lister-id
  ; @param (map)(opt) reload-props
  ;  {:on-reload (metamorphic-event)(opt)}
  ;
  ; @usage
  ;  [:item-lister/reload-items! :my-lister]
  ;
  ; @usage
  ;  [:item-lister/reload-items! :my-lister {...}]
  (fn [{:keys [db]} [_ lister-id reload-props]]
      ; - Az [:item-lister/reload-items! ...] esemény újra letölti a listában található elemeket.
      ;
      ; - Ha a szerver-oldalon az elemeket tartalmazó kollekció megváltozott, akkor nem feltétlenül
      ;   ugyanazok az elemek töltődnek le!
      ;
      ; - Ha pl. a kliens-oldalon az újratöltés előtt 42 elem van letöltve és a {:download-limit ...}
      ;   értéke 20, akkor az esemény az 1. - 60. elemeket kéri le a szerverről.
      (let [; A {:reload-mode? true} beállítás a query elkészítéséhez szükséges, utána nincs szükség
            ; rá, hogy érvényben maradjon, ezért nincs eltárolva!
            db           (r core.events/set-reload-mode!                      db lister-id)
            query        (r download.queries/get-request-items-query          db lister-id)
            validator-f #(r download.validators/request-items-response-valid? db lister-id %)]
           [:sync/send-query! (r core.subs/get-request-id db lister-id)
                              {:display-progress? true
                               ; XXX#4057
                               :on-stalled [:item-lister/receive-reloaded-items! lister-id reload-props]
                               :on-failure [:item-lister/set-error-mode!         lister-id]
                               :query query :validator-f validator-f}])))

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) reload-props
  ;  {:on-reload (metamorphic-event)(opt)}
  ; @param (map) server-response
  :item-lister/receive-reloaded-items!
  ; - Az {:on-reload ...} tulajdonságként átadott esemény használatával megoldható,
  ;   hogy a listaelemeken végzett műveletek záró-eseményei a listaelemek sikeres újratöltése
  ;   után történjenek meg.
  ;
  ; - Lassú internetkapcsolat esetén zavaró lenne, ha a listaelemeken végzett műveletek végét
  ;   jelző értesítések, a műveletek után, még a listaelemek újratöltése előtt jelennének meg.
  ;
  ; Pl.: Ha a kiválasztott listaelemek sikeres törlése után azonnal jelenne meg
  ;      a "Törölt elemek visszaállítása" értesítés, akkor a felhasználó még a listaelemek
  ;      újratöltése közben elindíthatná a "Törölt elemek visszaállítása" folyamatot,
  ;      ami azonban nem indítaná el a lekérést, mivel a listaelemek újratöltése még folyamatban
  ;      van és a plugin egyes lekérései megegyező azonosítóval rendelkeznek (XXX#5476),
  ;      ami megakadályozza, hogy párhuzamosan több lekérés történjen (x4.6.8).
  ;
  ; - Ha az [:item-lister/receive-reloaded-items! ...] esemény megtörténésekor a body komponens
  ;   már nincs a React-fába csatolva (pl. a felhasználó kilépett a pluginból), akkor
  ;   nem tárolja el a letöltött elemeket.
  (fn [{:keys [db]} [_ lister-id {:keys [on-reload]} server-response]]
      (if (r body.subs/body-did-mount? db lister-id)
          {:db (r download.events/receive-reloaded-items! db lister-id server-response)
           :dispatch on-reload}
          {:dispatch on-reload})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/request-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  (fn [{:keys [db]} [_ lister-id]]
      ; - Ha az infinite-loader komponens ismételten megjelenik a viewport területén, csak abban
      ;   az esetben próbál újabb elemeket letölteni, ha még nincs az összes letöltve.
      ;
      ; - XXX#4057
      ;   A letöltött dokumentumok on-success helyett on-stalled időpontban kerülnek tárolásra
      ;   a Re-Frame adatbázisba, így elkerülhető, hogy a request idle-timeout ideje alatt
      ;   az újonnan letöltött dokumentumok már kirenderelésre kerüljenek, amíg a letöltést jelző
      ;   felirat még megjelenik a lista végén.
      (if (r core.subs/request-items? db lister-id)
          (let [query        (r download.queries/get-request-items-query          db lister-id)
                validator-f #(r download.validators/request-items-response-valid? db lister-id %)]
               [:sync/send-query! (r core.subs/get-request-id db lister-id)
                                  {:display-progress? true
                                   :on-stalled [:item-lister/receive-items!  lister-id]
                                   :on-failure [:item-lister/set-error-mode! lister-id]
                                   :query query :validator-f validator-f}]))))

(a/reg-event-fx
  :item-lister/receive-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id server-response]]
      ; - Az elemek letöltődése után újratölti az infinite-loader komponenst, hogy megállapítsa,
      ;   hogy az a viewport területén van-e még és szükséges-e további elemeket letölteni.
      ;
      ; - Ha az [:item-lister/receive-items! ...] esemény megtörténésekor a body komponens már
      ;   nincs a React-fába csatolva, akkor az esemény nem végez műveletet!
      (if (r body.subs/body-did-mount? db lister-id)
          {:db       (r download.events/receive-items! db lister-id server-response)
           :dispatch [:tools/reload-infinite-loader! lister-id]})))
