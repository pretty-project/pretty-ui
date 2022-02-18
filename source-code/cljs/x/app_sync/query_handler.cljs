
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.09
; Description:
; Version: v0.6.2
; Compatibility: x4.6.1



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.query-handler
    (:require [mid-fruits.candy  :refer [param]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [x.app-sync.response-handler :as response-handler]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DEFAULT-URI "/query")



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- query-props-prototype
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
  (merge {:uri DEFAULT-URI}
         (param query-props)
         (if query {:params {:query query}})
         {:method :post}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-query-response
  ; @param (keyword) query-id
  ;
  ; @usage
  ;  (r sync/get-query-response db :my-query)
  ;
  ; @return (map)
  [db [_ query-id]]
  (r response-handler/get-request-response db query-id))

(defn get-query-answer
  ; @param (keyword) query-id
  ; @param (*) query-question
  ;
  ; @example
  ;  (r sync/get-query-answer db :my-query :all-users)
  ;  =>
  ;  [{...} {...}]
  ;
  ; @return (*)
  [db [_ query-id query-question]]
  (let [query-response (r get-query-response db query-id)]
       (get query-response query-question)))



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
