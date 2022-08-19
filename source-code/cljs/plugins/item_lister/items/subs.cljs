

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.items.subs
    (:require [mid-fruits.candy                  :refer [return]]
              [mid-fruits.loop                   :refer [some-indexed]]
              [mid-fruits.vector                 :as vector]
              [plugins.item-lister.body.subs     :as body.subs]
              [plugins.item-lister.core.subs     :as core.subs]
              [plugins.plugin-handler.items.subs :as items.subs]
              [x.app-core.api                    :as a :refer [r]]
              [x.app-environment.api             :as environment]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.items.subs
(def get-item         items.subs/get-item)
(def export-item      items.subs/export-item)
(def item-downloaded? items.subs/item-downloaded?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (string) item-id
  ;
  ; @return (integer)
  [db [_ lister-id item-id]]
  ; A get-item-dex függvény visszatérési értéke az item-id paraméterként átadott azonosítójú elem indexe.
  (letfn [(f [item-dex {:keys [id]}] (if (= id item-id) item-dex))]
         (let [downloaded-items (r core.subs/get-downloaded-items db lister-id)]
              (some-indexed f downloaded-items))))

(defn get-item-dexes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (string) item-ids
  ;
  ; @return (integers in vector)
  [db [_ lister-id item-ids]]
  ; A get-item-dexes függvény visszatérési értéke az item-ids paraméterként átadott vektorban felsorolt
  ; azonosítójú elemek indexei egy vektorban felsorolva.
  (let [downloaded-items (r core.subs/get-downloaded-items db lister-id)]
       (letfn [(f [item-dexes item-ids dex]
                  (let [item-id (get-in downloaded-items [dex :id])]
                       (cond ; Ha a vizsgált index magasabb, mint az utolsó listaelem indexe, ...
                             (= dex (count downloaded-items))
                             ; ... akkor a vizsgálat a lista végéhez ért.
                             (return item-dexes)
                             ; Ha a vizsgált indexű elem azonosítója szerepel az item-ids vektorban, ...
                             (vector/contains-item? item-ids item-id)
                             ; ... akkor az item-dexes vektorhoz adja a vizsgált indexet,
                             ;     eltávolítja az elem azonosítóját az item-ids vektorból és folytaja a keresést.
                             (f (conj item-dexes dex)
                                (vector/remove-item item-ids item-id)
                                (inc dex))
                             ; Ha a vizsgált indexű elem azonosítója NEM szerepel az item-ids vektorban, ...
                             ; ... akkor folytaja a keresést.
                             :else (f item-dexes item-ids (inc dex)))))]
              (f [] item-ids 0))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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

(defn item-last?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r items.subs/item-last? db :my-lister 0)
  ;
  ; @return (boolean)
  [db [_ lister-id item-dex]]
  (let [downloaded-item-count (r core.subs/get-downloaded-item-count db lister-id)]
       (= downloaded-item-count (inc item-dex))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-item-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (strings in vector)
  [db [_ lister-id]]
  ; A get-selected-item-ids függvény visszatérési értéke a kijelölt listaelemek azonosítói egy
  ; vektorban felsorolva.
  (let [items-path     (r body.subs/get-body-prop db lister-id :items-path)
        selected-items (r core.subs/get-meta-item db lister-id :selected-items)]
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
  ; A elemre kattintva az elem hozzáadódik a kijelölt elemek listájához, ha ...
  ; ... az item-lister plugin {:select-mode? true} állapotban van.
  ; ... és/vagy a kattintás ideje alatt a SHIFT billentyű le van nyomva, és a body
  ;     komponens megkapja az {:item-actions [...]} paramétert.
  (and (or (r core.subs/get-meta-item  db lister-id :select-mode?)
           ; B)
           (and (r environment/key-pressed? db 16)
                (let [item-actions (r body.subs/get-body-prop db lister-id :item-actions)]
                     (vector/nonempty? item-actions))))
       (not (r core.subs/lister-disabled? db lister-id))))



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
;  [:item-lister/item-last? :my-lister 0]
(a/reg-sub :item-lister/item-last? item-last?)

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
