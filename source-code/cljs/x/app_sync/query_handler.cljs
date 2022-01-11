
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.09
; Description:
; Version: v0.4.4
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.query-handler
    (:require [mid-fruits.candy  :refer [param]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [x.app-sync.response-handler :as response-handler]))



;; -- Names -------------------------------------------------------------------
;; -- XXX#5569 ----------------------------------------------------------------

;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DEFAULT-URI "/query")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn query-props->request-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) query-id
  ; @param (map) query-props
  ;  {:body (map)(opt)
  ;   {:query (string or vector)}
  ;   :query (string or vector)(opt)}
  ;
  ; @example
  ;  (query-props->request-props {:query [:all-users]})
  ;  =>
  ;  {:method :post :query "[:all-users]"}
  ;
  ; @example
  ;  (query-props->request-props {:body {:query [:all-users] :my-body-param "My value"}})
  ;  =>
  ;  {:method :post :body {:query "[:all-users]" :my-body-param "My value"}}
  ;
  ; @return (map)
  ;  {:body (map)
  ;    {:query (string)}
  ;   :method (keyword)
  ;   :params (map)
  ;    {:query (string)}}
  [query-id {:keys [body query] :as query-props}]
  (let [request-props (select-keys query-props [:body :idle-timeout :on-failure :on-sent :on-success
                                                :on-stalled :target-path :target-paths :uri])]
       (merge request-props {:method :post}
                            (if query {:params {:query query}}))))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- query-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) query-props
  ;
  ; @return (map)
  ;  {:uri (string)}
  [query-props]
  (merge {:uri DEFAULT-URI}
         (param query-props)))



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



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-query-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) query-id
  ; @param (map) query-props
  ;
  ; @return (map)
  [db [_ query-id query-props]]
  (assoc-in db (db/path :sync/queries query-id) query-props))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :sync/send-query!
  ; @param (keyword)(opt) query-id
  ; @param (map) query-props
  ;  {:body (map)(opt)
  ;    {:query (vector)}
  ;    Only w/o {:query ...}
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
  ;   :target-paths (map)(opt)
  ;   :uri (string)
  ;    Default: DEFAULT-URI}
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
  ;
  ; @usage
  ;  [:sync/send-query! {:query [:all-users]}]
  ;                      :target-paths {:my-data-item   [:db :my   :data :item :path]
  ;                                     :your-data-item [:db :your :data :item :path]
  ;                                     :my-item {:my-nested-item [:db :my :nested :item]}}
  ;
  ; @usage
  ;  [:sync/send-query! {:query [:all-users]}]
  ;                      :target-path [:my :response :path]}
  (fn [{:keys [db]} event-vector]
      (let [query-id      (a/event-vector->second-id   event-vector)
            query-props   (a/event-vector->first-props event-vector)
            query-props   (query-props-prototype       query-props)
            request-props (query-props->request-props  query-id query-props)

            ; BUG#5011
            ; A query vektorba feltételesen – if, when, ... függvény használatával –
            ; írt query-question elemek helyett a feltétel nem teljesülésekor nil
            ; érték kerül, ami a szerver-oldali Pathom rendszerben hibához vezetne.
            ; Emiatt szükséges eltávolítani a query vektorból a nil értékeket,
            ; miután a {:query [...]} és a {:body {:query [...]}} tulajdonságok
            ; összevonása megtörtént.
            request-props (update-in request-props [:params :query] vector/remove-item nil)]

           {:db       (r store-query-props! db query-id query-props)
            :dispatch [:sync/send-request!     query-id request-props]})))
