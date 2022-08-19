
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.query-handler.effects
    (:require [mid-fruits.candy                    :refer [return]]
              [mid-fruits.vector                   :as vector]
              [x.app-core.api                      :as a :refer [r]]
              [x.app-sync.query-handler.prototypes :as query-handler.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :sync/send-query!
  ; @param (keyword)(opt) query-id
  ; @param (map) query-props
  ;  {:body (map)(opt)
  ;    {:query (vector)}
  ;    Only w/o {:query ...}
  ;   :debug? (boolean)(opt)
  ;    Default: false
  ;   :display-progress? (boolean)(opt)
  ;    Default: false
  ;   :idle-timeout (ms)(opt)
  ;    Default: x.app-sync/request-handler/DEFAULT-IDLE-TIMEOUT
  ;   :on-failure (metamorphic-event)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a szerver-válasz értékét.
  ;   :on-sent (metamorphic-event)(opt)
  ;   :on-stalled (metamorphic-event)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a szerver-válasz értékét.
  ;   :on-success (metamorphic-event)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a szerver-válasz értékét.
  ;   :query (string or vector)(opt)
  ;    Only w/o {:body {...}}
  ;   :target-path (vector)(opt)
  ;   :uri (string)
  ;    Default: DEFAULT-URI
  ;   :validator-f (function)(opt)}
  ;
  ; @usage
  ;  [:sync/send-query! {...}]
  ;
  ; @usage
  ;  [:sync/send-query! :my-query {...}]
  ;
  ; @usage
  ;  [:sync/send-query! {:query [:all-users]}]
  ;
  ; @usage
  ;  [:sync/send-query! {:body {:query [:all-users] :my-body-param "My value"}}]
  [a/event-vector<-id]
  (fn [{:keys [db]} [_ query-id query-props]]
      (let [debug-mode? (r a/debug-mode-detected? db)
            query-props (query-handler.prototypes/query-props-prototype query-props)

            ; A {:query [...]} és a {:body {:query [...]}} tulajdonságok összevonása után ...
            ; ... eltávolítja a query vektorból a nil értékeket mert a query vektorba feltételesen
            ;     – if, when, ... függvény használatával – írt query-question elemek helyett a feltétel
            ;     nem teljesülésekor nil érték kerülhet, ami a szerver-oldali Pathom rendszerben hibához vezetne.
            ; ... {:debug-mode? true} beállítás esetén hozzáadja a :debug resolver-id azonosítót a query vektorhoz.
            query-props (cond-> query-props :remove-nil (update-in [:params :query] vector/remove-item nil)
                                            debug-mode? (update-in [:params :query] vector/cons-item :debug))]

           [:sync/send-request! query-id query-props])))
