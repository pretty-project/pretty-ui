
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.infinite-loader.events
    (:require [x.app-core.api :as a :refer [r]]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn hide-infinite-observer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ;
  ; @return (map)
  [db [_ loader-id]]
  (assoc-in db [:tools :infinite-loader/data-items loader-id :observer-visible?] false))

(defn show-infinite-observer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ;
  ; @return (map)
  [db [_ loader-id]]
  (assoc-in db [:tools :infinite-loader/data-items loader-id :observer-visible?] true))

(defn pause-infinite-loader!
  ; @param (keyword) loader-id
  ;
  ; @usage
  ;  (r tools/pause-infinite-loader! db :my-loader)
  ;
  ; @return (map)
  [db [_ loader-id]]
  (r hide-infinite-observer! db loader-id))

(defn restart-infinite-loader!
  ; @param (keyword) loader-id
  ;
  ; @usage
  ;  (r tools/restart-infinite-loader! db :my-loader)
  ;
  ; @return (map)
  [db [_ loader-id]]
  (r show-infinite-observer! db loader-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :tools/hide-infinite-observer! hide-infinite-observer!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :tools/show-infinite-observer! show-infinite-observer!)

; @usage
;  [:tools/pause-infinite-loader! :my-loader]
(a/reg-event-db :tools/pause-infinite-loader! pause-infinite-loader!)

; @usage
;  [:tools/restart-infinite-loader! :my-loader]
(a/reg-event-db :tools/restart-infinite-loader! restart-infinite-loader!)
