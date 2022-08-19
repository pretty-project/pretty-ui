
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.items.events
    (:require [mid-fruits.map                 :refer [dissoc-in]]
              [mid-fruits.vector              :as vector]
              [plugins.item-lister.body.subs  :as body.subs]
              [plugins.item-lister.core.subs  :as core.subs]
              [plugins.item-lister.items.subs :as items.subs]
              [x.app-core.api                 :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-all-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (let [downloaded-items (r core.subs/get-downloaded-items db lister-id)
        item-selections  (vector/dex-range downloaded-items)]
       (assoc-in db [:plugins :plugin-handler/meta-items lister-id :selected-items] item-selections)))

(defn toggle-item-selection!
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r item-lister/toggle-item-selection! :my-lister 42)
  ;
  ; @return (map)
  [db [_ lister-id item-dex]]
  ; A toggle-item-selection! függvény az egyes listaelemek kiválasztásakor ...
  ; ... ha a body komponens megkapja az :item-actions paramétert, akkor {:actions-mode? true}
  ;     állapotba lépteti a plugint, így a kiválasztott listaelemeken végrehajtható műveleteket
  ;     tartalmazó menü jelenik meg header komponensen.
  ;
  ; ... minden esetben {:select-mode? true} állapotba lépteti a plugint, mert a listaelemek
  ;     kiválasztása {:select-mode? true} állapotban történik.
  (let [item-actions (r body.subs/get-body-prop db lister-id :item-actions)]
       (cond-> db item-actions            (assoc-in  [:plugins :plugin-handler/meta-items lister-id :actions-mode?] true)
                  :set-select-mode!       (assoc-in  [:plugins :plugin-handler/meta-items lister-id :select-mode?]  true)
                  :toggle-item-selection! (update-in [:plugins :plugin-handler/meta-items lister-id :selected-items]
                                                     vector/toggle-item item-dex))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn disable-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integers in vector) item-dexes
  ;
  ; @usage
  ;  (r items.events/disable-items! db :my-lister [0 1 4])
  ;
  ; @return (map)
  [db [_ lister-id item-dexes]]
  (update-in db [:plugins :plugin-handler/meta-items lister-id :disabled-items] vector/concat-items item-dexes))

(defn enable-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (integers in vector) item-dexes
  ;
  ; @usage
  ;  (r items.events/enable-items! db :my-lister [0 1 4])
  ;
  ; @return (map)
  [db [_ lister-id item-dexes]]
  (update-in db [:plugins :plugin-handler/meta-items lister-id :disabled-items] vector/remove-items item-dexes))

(defn enable-all-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (dissoc-in db [:plugins :plugin-handler/meta-items lister-id :disabled-items]))

(defn disable-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (let [selected-items (r core.subs/get-meta-item db lister-id :selected-items)]
       (r disable-items! db lister-id selected-items)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/select-all-items! select-all-items!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/toggle-item-selection! toggle-item-selection!)
