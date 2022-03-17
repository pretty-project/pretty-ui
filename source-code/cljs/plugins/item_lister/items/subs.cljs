
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.items.subs
    (:require [mid-fruits.candy               :refer [return]]
              [mid-fruits.vector              :as vector]
              [plugins.item-lister.core.subs  :as core.subs]
              [plugins.item-lister.mount.subs :as mount.subs]
              [x.app-core.api                 :as a :refer [r]]
              [x.app-environment.api          :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r items.subs/item-disabled? db :my-extension :my-type 0)
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace item-dex]]
  (let [disabled-items (r core.subs/get-meta-item db extension-id item-namespace :disabled-items)]
       (vector/contains-item? disabled-items item-dex)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-item-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (strings in vector)
  [db [_ extension-id item-namespace]]
  (let [items-path     (r mount.subs/get-body-prop db extension-id item-namespace :items-path)
        selected-items (r core.subs/get-meta-item  db extension-id item-namespace :selected-items)]
       (letfn [(f [result item-dex]
                  (let [item-id (get-in db (vector/concat-items items-path [item-dex :id]))]
                       (conj result item-id)))]
              (reduce f [] selected-items))))

(defn get-selected-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (integer)
  [db [_ extension-id item-namespace]]
  (let [selected-items (r core.subs/get-meta-item db extension-id item-namespace :selected-items)]
       (count selected-items)))

(defn item-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r engine/item-selected? db :my-extension :my-type 0)
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace item-dex]]
  (let [selected-items (r core.subs/get-meta-item db extension-id item-namespace :selected-items)]
       (vector/contains-item? selected-items item-dex)))

(defn all-items-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [selected-items-count  (r get-selected-item-count             db extension-id item-namespace)
        downloaded-item-count (r core.subs/get-downloaded-item-count db extension-id item-namespace)]
       (and (not= downloaded-item-count 0)
            (= selected-items-count downloaded-item-count))))

(defn any-item-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [selected-items (r core.subs/get-meta-item db extension-id item-namespace :selected-items)]
       (vector/nonempty? selected-items)))

(defn no-items-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [selected-items (r core.subs/get-meta-item db extension-id item-namespace :selected-items)]
       (-> selected-items vector/nonempty? not)))

(defn toggle-item-selection?
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r item-lister/toggle-item-selection? db :my-extension :my-type 42)
  [db [_ extension-id item-namespace item-dex]]
  (and ; A SHIFT billentyű lenyomása közben az elemre kattintva az elem, hozzáadódik a kijelölt elemek listájához.
            (r core.subs/items-selectable? db extension-id item-namespace)
            (r environment/key-pressed?    db 16)
       (not (r core.subs/lister-disabled?  db extension-id item-namespace))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn order-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (return false))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/all-items-selected? :my-extension :my-type]
(a/reg-sub :item-lister/all-items-selected? all-items-selected?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/any-item-selected? :my-extension :my-type]
(a/reg-sub :item-lister/any-item-selected? any-item-selected?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/no-items-selected? :my-extension :my-type]
(a/reg-sub :item-lister/no-items-selected? no-items-selected?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
; @param (integer) item-dex
;
; @usage
;  [:item-lister/item-disabled? :my-extension :my-type 0]
(a/reg-sub :item-lister/item-disabled? item-disabled?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
; @param (integer) item-dex
;
; @usage
;  [:item-lister/item-selected? :my-extension :my-type 0]
(a/reg-sub :item-lister/item-selected? item-selected?)
