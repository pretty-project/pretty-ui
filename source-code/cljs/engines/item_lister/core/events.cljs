
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.core.events
    (:require [candy.api                          :refer [return]]
              [engines.engine-handler.core.events :as core.events]
              [engines.item-lister.body.subs      :as body.subs]
              [engines.item-lister.core.subs      :as core.subs]
              [map.api                            :refer [dissoc-in]]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.core.events
(def set-meta-item!     core.events/set-meta-item!)
(def remove-meta-item!  core.events/remove-meta-item!)
(def remove-meta-items! core.events/remove-meta-items!)
(def set-mode!          core.events/set-mode!)
(def set-engine-error!  core.events/set-engine-error!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-reload-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (r set-mode! db lister-id :reload-mode?))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-meta-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (dissoc-in db [:engines :engine-handler/meta-items lister-id]))

(defn reset-selections!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (dissoc-in db [:engines :engine-handler/meta-items lister-id :selected-items]))

(defn reset-downloads!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (let [items-path (r body.subs/get-body-prop db lister-id :items-path)]
       (-> db ;(dissoc-in items-path)
              (dissoc-in [:engines :engine-handler/meta-items lister-id :all-item-count])
              (dissoc-in [:engines :engine-handler/meta-items lister-id :received-item-count])
              (dissoc-in [:engines :engine-handler/meta-items lister-id :data-received?])
              (dissoc-in [:engines :engine-handler/meta-items lister-id :item-order]))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-default-order-by!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (let [default-order-by (r body.subs/get-body-prop db lister-id :default-order-by)]
       (assoc-in db [:engines :engine-handler/meta-items lister-id :order-by] default-order-by)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn filter-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) filter-pattern
  ;
  ; @return (map)
  [db [_ lister-id filter-pattern]]
  (as-> db % (r reset-downloads!  % lister-id)
             (r reset-selections! % lister-id)
             (assoc-in % [:engines :engine-handler/meta-items lister-id :active-filter] filter-pattern)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn search-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) search-props
  ; {:search-keys (keywords in vector)}
  ; @param (string) search-term
  ;
  ; @return (map)
  [db [_ lister-id {:keys [search-keys]} search-term]]
  (as-> db % (r reset-downloads! % lister-id)
             (r set-meta-item!   % lister-id :search-keys search-keys)
             (r set-meta-item!   % lister-id :search-term search-term)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn order-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (namespaced keyword) order-by
  ;
  ; @return (map)
  [db [_ lister-id order-by]]
  (as-> db % (r reset-downloads! % lister-id)
             (r set-meta-item!   % lister-id :order-by order-by)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :item-lister/set-engine-error! set-engine-error!)

; @param (keyword) lister-id
;
; @usage
; [:item-lister/reset-selections! :my-lister]
(r/reg-event-db :item-lister/reset-selections! reset-selections!)
