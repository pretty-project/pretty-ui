
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns pathom.effects
    (:require [mid-fruits.candy  :refer [return]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [pathom.prototypes :as prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :pathom/send-query!
  ; @param (keyword)(opt) query-id
  ; @param (map) query-props
  ;  {:body (map)(opt)
  ;    {:query (vector)}
  ;    Only w/o {:query ...}
  ;   :display-progress? (boolean)(opt)
  ;    Default: false
  ;   :idle-timeout (ms)(opt)
  ;    Default: pathom.config/DEFAULT-IDLE-TIMEOUT
  ;   :on-failure (metamorphic-event)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a szerver-válasz értékét.
  ;   :on-sent (metamorphic-event)(opt)
  ;   :on-stalled (metamorphic-event)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a szerver-válasz értékét.
  ;   :on-success (metamorphic-event)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a szerver-válasz értékét.
  ;   :query (string or vector)(opt)
  ;    Only w/o {:body {...}}
  ;   :uri (string)
  ;    Default: pathom.config/DEFAULT-URI
  ;   :validator-f (function)(opt)}
  ;
  ; @usage
  ;  [:pathom/send-query! {...}]
  ;
  ; @usage
  ;  [:pathom/send-query! :my-query {...}]
  ;
  ; @usage
  ;  [:pathom/send-query! {:query [:all-users]}]
  ;
  ; @usage
  ;  [:pathom/send-query! {:body {:query [:all-users] :my-body-param "My value"}}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ query-id query-props]]
      (let [debug-mode? (r a/debug-mode-detected?           db)
            query-props (r prototypes/query-props-prototype db query-props)

            ; A {:query [...]} és a {:body {:query [...]}} tulajdonságok összevonása után ...
            ; ... eltávolítja a query vektorból a nil értékeket mert a query vektorba feltételesen
            ;     – if, when, ... függvény használatával – írt query-question elemek helyett a feltétel
            ;     nem teljesülésekor nil érték kerülhet, ami a szerver-oldali Pathom rendszerben hibához vezetne.
            ; ... {:debug-mode? true} beállítás esetén hozzáadja a :pathom/debug resolver-id azonosítót a query vektorhoz.
            query-props (cond-> query-props :remove-nil (update-in [:params :query] vector/remove-item nil)
                                            debug-mode? (update-in [:params :query] vector/cons-item :pathom/debug))]

           [:sync/send-request! query-id query-props])))
