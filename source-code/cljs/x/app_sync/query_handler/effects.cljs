
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.query-handler.effects
    (:require [mid-fruits.candy                :refer [param]]
              [mid-fruits.vector               :as vector]
              [x.app-core.api                  :as a]
              [x.app-sync.query-handler.engine :as query-handler.engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn query-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) query-props
  ;
  ; @return (map)
  ;  {:method (keyword)
  ;   :params (map)
  ;   :query (vector)
  ;   :uri (string)}
  [{:keys [query] :as query-props}]
  (merge {:uri query-handler.engine/DEFAULT-URI}
         (param query-props)
         (if query {:params {:query query}})
         {:method :post}))



;; -- Effect events -----------------------------------------------------------
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
  ;   :target-path (item-path vector)(opt)
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
      (let [query-props (query-props-prototype query-props)

            ; BUG#5011
            ; A query vektorba feltételesen – if, when, ... függvény használatával –
            ; írt query-question elemek helyett a feltétel nem teljesülésekor nil
            ; érték kerül, ami a szerver-oldali Pathom rendszerben hibához vezetne.
            ; Emiatt szükséges eltávolítani a query vektorból a nil értékeket,
            ; miután a {:query [...]} és a {:body {:query [...]}} tulajdonságok
            ; összevonása megtörtént.
            query-props (update-in query-props [:params :query] vector/remove-item nil)]

           [:sync/send-request! query-id query-props])))
