
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.items.events
    (:require [mid-fruits.map                 :refer [dissoc-in]]
              [mid-fruits.vector              :as vector]
              [plugins.item-lister.core.subs  :as core.subs]
              [plugins.item-lister.items.subs :as items.subs]
              [x.app-core.api                 :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-selections!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (dissoc-in db [:plugins :item-lister/meta-items extension-id :selected-items]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-all-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [downloaded-items (r core.subs/get-downloaded-items db extension-id item-namespace)
        item-selections  (vector/dex-range downloaded-items)]
       (assoc-in db [:plugins :item-lister/meta-items extension-id :selected-items] item-selections)))

(defn toggle-item-selection!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r item-lister/toggle-item-selection! :my-extension :my-type 42)
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-dex]]
  (if (r items.subs/item-selected? db extension-id item-namespace item-dex)
      (-> db (assoc-in  [:plugins :item-lister/meta-items extension-id :select-mode?] true)
             (update-in [:plugins :item-lister/meta-items extension-id :selected-items] vector/remove-item item-dex))
      (-> db (assoc-in  [:plugins :item-lister/meta-items extension-id :select-mode?] true)
             (update-in [:plugins :item-lister/meta-items extension-id :selected-items] vector/conj-item   item-dex))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn disable-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integers in vector) item-dexes
  ;
  ; @usage
  ;  (r events/disable-items! db :my-extension [0 1 4])
  ;
  ; @return (map)
  [db [_ extension-id _ item-dexes]]
  (update-in db [:plugins :item-lister/meta-items extension-id :disabled-items] vector/concat-items item-dexes))

(defn enable-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integers in vector) item-dexes
  ;
  ; @usage
  ;  (r events/enable-items! db :my-extension [0 1 4])
  ;
  ; @return (map)
  [db [_ extension-id _ item-dexes]]
  (update-in db [:plugins :item-lister/meta-items extension-id :disabled-items] vector/remove-items item-dexes))

(defn enable-all-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id _]]
  (dissoc-in db [:plugins :item-lister/meta-items extension-id :disabled-items]))

(defn disable-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (let [selected-items (r core.subs/get-meta-item db extension-id item-namespace :selected-items)]
       (r disable-items! db extension-id item-namespace selected-items)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/select-all-items! select-all-items!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/toggle-item-selection! toggle-item-selection!)
