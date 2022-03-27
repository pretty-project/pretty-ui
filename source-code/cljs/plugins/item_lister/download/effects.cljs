
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.download.effects
    (:require [plugins.item-lister.core.events         :as core.events]
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
  ;
  ; @usage
  ;  [:item-lister/reload-items! :my-lister]
  (fn [{:keys [db]} [_ lister-id]]
      ; - Az [:item-lister/reload-items! ...] esemény újra letölti a listában található elemeket.
      ; - Ha a szerver-oldalon az elemeket tartalmazó kollekció megváltozott, akkor nem feltétlenül
      ;   ugyanazok az elemek töltődnek le!
      ; - Ha pl. a kliens-oldalon az újratöltés előtt 42 elem van letöltve és a {:download-limit ...}
      ;   értéke 20, akkor az esemény az 1. - 60. elemeket kéri le a szerverről.
      (let [; A {:reload-mode? true} beállítás a query elkészítéséhez szükséges, utána nincs szükség
            ; rá, hogy érvényben maradjon, ezért nincs eltárolva!
            db           (r core.events/toggle-reload-mode!                   db lister-id)
            query        (r download.queries/get-request-items-query          db lister-id)
            validator-f #(r download.validators/request-items-response-valid? db lister-id %)]
           [:sync/send-query! (r core.subs/get-request-id db lister-id)
                              {:display-progress? true
                               ; XXX#4057
                               :on-stalled [:item-lister/receive-reloaded-items! lister-id]
                               :on-failure [:item-lister/set-error-mode!         lister-id]
                               :query query :validator-f validator-f}])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/request-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  (fn [{:keys [db]} [_ lister-id]]
      (if ; Ha az infinite-loader komponens ismételten megjelenik a viewport területén, csak abban
          ; az esetben próbáljon újabb elemeket letölteni, ha még nincs az összes letöltve.
          (r core.subs/request-items? db lister-id)
          (let [query        (r download.queries/get-request-items-query          db lister-id)
                validator-f #(r download.validators/request-items-response-valid? db lister-id %)]
               [:sync/send-query! (r core.subs/get-request-id db lister-id)
                                  {:display-progress? true
                                   ; XXX#4057
                                   ; A letöltött dokumentumok on-success helyett on-stalled időpontban
                                   ; kerülnek tárolásra a Re-Frame adatbázisba, így elkerülhető,
                                   ; hogy a request idle-timeout ideje alatt az újonnan letöltött
                                   ; dokumentumok már kirenderelésre kerüljenek, amíg a letöltést jelző
                                   ; felirat még megjelenik a lista végén.
                                   :on-stalled [:item-lister/receive-items!  lister-id]
                                   :on-failure [:item-lister/set-error-mode! lister-id]
                                   :query query :validator-f validator-f}]))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/receive-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ lister-id server-response]]
      {:db (r download.events/receive-items! db lister-id server-response)
       ; Az elemek letöltődése után újratölti az infinite-loader komponenst, hogy megállapítsa,
       ; hogy az a viewport területén van-e még és szükséges-e további elemeket letölteni.
       :dispatch [:tools/reload-infinite-loader! lister-id]}))
