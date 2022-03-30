
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.items.subs
    (:require [mid-fruits.candy                  :refer [return]]
              [mid-fruits.loop                   :refer [some-indexed]]
              [mid-fruits.vector                 :as vector]
              [plugins.item-lister.core.subs     :as core.subs]
              [plugins.item-lister.mount.subs    :as mount.subs]
              [plugins.item-lister.transfer.subs :as transfer.subs]
              [x.app-core.api                    :as a :refer [r]]
              [x.app-db.api                      :as db]
              [x.app-environment.api             :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ lister-id item-id]]
  (letfn [(f [{:keys [id] :as item}] (if (= id item-id) item))]
         (let [downloaded-items (r core.subs/get-downloaded-items db lister-id)]
              (some f downloaded-items))))

(defn get-item-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (string) item-id
  ;
  ; @return (integer)
  [db [_ lister-id item-id]]
  (letfn [(f [item-dex {:keys [id]}] (if (= id item-id) item-dex))]
         (let [downloaded-items (r core.subs/get-downloaded-items db lister-id)]
              (some-indexed f downloaded-items))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (string) item-id
  ;
  ; @return (namespaced map)
  [db [_ lister-id item-id]]
  (let [item-namespace (r transfer.subs/get-transfer-item db lister-id :item-namespace)
        item           (r get-item                        db lister-id item-id)]
       (db/document->namespaced-document item item-namespace)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-downloaded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (string) item-id
  ;
  ; @return (boolean)
  [db [_ lister-id item-id]]
  (let [item (r get-item db lister-id item-id)]
       (boolean item)))

(defn item-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r items.subs/item-disabled? db :my-lister 0)
  ;
  ; @return (boolean)
  [db [_ lister-id item-dex]]
  (let [disabled-items (r core.subs/get-meta-item db lister-id :disabled-items)]
       (vector/contains-item? disabled-items item-dex)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-item-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (strings in vector)
  [db [_ lister-id]]
  (let [items-path     (r mount.subs/get-body-prop db lister-id :items-path)
        selected-items (r core.subs/get-meta-item  db lister-id :selected-items)]
       (letfn [(f [result item-dex]
                  (let [item-id (get-in db (vector/concat-items items-path [item-dex :id]))]
                       (conj result item-id)))]
              (reduce f [] selected-items))))

(defn get-selected-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (integer)
  [db [_ lister-id]]
  (let [selected-items (r core.subs/get-meta-item db lister-id :selected-items)]
       (count selected-items)))

(defn item-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r engine/item-selected? db :my-lister 0)
  ;
  ; @return (boolean)
  [db [_ lister-id item-dex]]
  (let [selected-items (r core.subs/get-meta-item db lister-id :selected-items)]
       (vector/contains-item? selected-items item-dex)))

(defn all-items-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (let [selected-items-count  (r get-selected-item-count             db lister-id)
        downloaded-item-count (r core.subs/get-downloaded-item-count db lister-id)]
       (and (not= downloaded-item-count 0)
            (= selected-items-count downloaded-item-count))))

(defn any-item-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (let [selected-items (r core.subs/get-meta-item db lister-id :selected-items)]
       (vector/nonempty? selected-items)))

(defn no-items-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (let [selected-items (r core.subs/get-meta-item db lister-id :selected-items)]
       (-> selected-items vector/nonempty? not)))

(defn toggle-item-selection?
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r item-lister/toggle-item-selection? db :my-lister 42)
  [db [_ lister-id item-dex]]
  (and ; A SHIFT billentyű lenyomása közben az elemre kattintva az elem, hozzáadódik a kijelölt elemek listájához.
            (r core.subs/items-selectable? db lister-id)
            (r environment/key-pressed?    db 16)
       (not (r core.subs/lister-disabled?  db lister-id))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn order-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (return false))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/all-items-selected? :my-lister]
(a/reg-sub :item-lister/all-items-selected? all-items-selected?)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/any-item-selected? :my-lister]
(a/reg-sub :item-lister/any-item-selected? any-item-selected?)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/no-items-selected? :my-lister]
(a/reg-sub :item-lister/no-items-selected? no-items-selected?)

; @param (keyword) lister-id
; @param (integer) item-dex
;
; @usage
;  [:item-lister/item-disabled? :my-lister 0]
(a/reg-sub :item-lister/item-disabled? item-disabled?)

; @param (keyword) lister-id
; @param (integer) item-dex
;
; @usage
;  [:item-lister/item-selected? :my-lister 0]
(a/reg-sub :item-lister/item-selected? item-selected?)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/order-changed? :my-lister]
(a/reg-sub :item-lister/order-changed? order-changed?)
