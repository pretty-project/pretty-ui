
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.09
; Description:
; Version: v0.2.2
; Compatibility: x4.2.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.query-handler
    (:require [mid-fruits.candy            :refer [param]]
              [mid-fruits.map              :as map]
              [x.app-core.api              :as a :refer [r]]
              [x.app-db.api                :as db]
              [x.app-sync.response-handler :as response-handler]))



;; -- Names -------------------------------------------------------------------
;; -- XXX#5569 ----------------------------------------------------------------

;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DEFAULT-URI "/db/query")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-response->query-answer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request-response
  ; @param (keyword, vector or string) query-key
  ;
  ; @example (resolver-id as keyword)
  ;  (request-response->query-answer {:all-users [{...} {...}]} :all-users)
  ;  => [{...} {...}]
  ;
  ; @example (entity as vector)
  ;  (request-response->query-answer {[:directory/id "my-directory"] {:directory/id "my-directory"}}
  ;                                  [:directory/id "my-directory"])
  ;  => {:directory/id "my-directory"}
  ;
  ; @example (mutation-f-name as string)
  ;  (request-response->query-answer {media/update-item! "*"} "media/update-item!")
  ;  => "*"
  ;
  ; @return (*)
  [request-response query-key]
  (if (string? query-key)
      (get request-response (symbol query-key))
      (get request-response (param  query-key))))

(defn query-props->request-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) query-id
  ; @param (map) query-props
  ;  {:query (string or vector)}
  ;
  ; @return (map)
  ;  {:method (keyword)
  ;   :params (map)
  ;    {:query (string)}}
  [query-id {:keys [query] :as query-props}]
  (let [request-props (map/inherit query-props [:modifier :on-failure :on-sent :on-success
                                                :target-path :target-paths :uri])]
       (merge request-props {:method :post
                             :params {:query (str query)}})))



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

(defn- get-query-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) query-id
  ; @param (keyword) prop-id
  ;
  ; @return (*)
  [db [_ query-id prop-id]]
  (get-in db (db/path ::queries query-id prop-id)))

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
  ; @param (keyword, string or vector) query-key
  ;
  ; @example
  ;  (r sync/get-query-answer db :my-query :all-users)
  ;  => [{...} {...}]
  ;
  ; @return (*)
  [db [_ query-id query-key]]
  (let [query-response (r get-query-response db query-id)]
       (get query-response query-key)))



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
  (assoc-in db (db/path ::queries query-id)
               (param query-props)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-sync/send-query!
  ; @param (keyword)(opt) query-id
  ; @param (map) query-props
  ;  {:modifier (function)(opt)
  ;    A szerver-válasz értéket eltárolása előtt módosító függvény.
  ;   :on-failure (metamorphic-event)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a szerver-válasz értékét.
  ;   :on-sent (metamorphic-event)(opt)
  ;   :on-success (metamorphic-event)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a szerver-válasz értékét.
  ;   :query (string or vector)
  ;   :target-path (item-path vector)(opt)
  ;   :target-paths (map)(opt)
  ;   :uri (string)
  ;    Default: DEFAULT-URI}
  ;
  ; @usage
  ;  [:x.app-sync/send-query! {...}]
  ;
  ; @usage
  ;  [:x.app-sync/send-query! :my-query {...}]
  ;
  ; @usage
  ;  [:x.app-sync/send-query! {:query "[:all-users]" :uri "/users"}]
  ;
  ; @usage
  ;  [:x.app-sync/send-query! {:query [:all-users] :uri "/users"}]
  ;
  ; @usage
  ;  [:x.app-sync/send-query! {:query [:all-users] :uri "/users"}]
  ;                            :target-paths {:my-data-item   [:db :my   :data :item :path]
  ;                                           :your-data-item [:db :your :data :item :path]
  ;                                           :my-item {:my-nested-item [:db :my :nested :item]}}
  ;
  ; @usage
  ;  [:x.app-sync/send-query! {:query [:all-users] :uri "/users"}]
  ;                            :target-path [:my :response :path]}
  (fn [{:keys [db]} event-vector]
      (let [query-id      (a/event-vector->second-id   event-vector)
            query-props   (a/event-vector->first-props event-vector)
            query-props   (a/prot query-props query-props-prototype)
            request-props (query-props->request-props  query-id query-props)]
           {:db       (r store-query-props!   db query-id query-props)
            :dispatch [:x.app-sync/send-request! query-id request-props]})))
