
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.30
; Description:
; Version: v0.2.8
; Compatibility: x4.3.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.surface
    (:require [mid-fruits.candy              :refer [param return]]
              [x.app-core.api                :as a :refer [r]]
              [x.app-elements.engine.element :as element]))



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  Bizonyos elemek (Pl.: text-field) rendelkeznek {:surface {...}} tulajdonsággal,
;  aminek a használatával lehetséges az elemhez tartozó kiegészítő felületen
;  tartalmat megjeleníteni.



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-visible?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ element-id]]
  (r element/get-element-prop db element-id :surface-visible?))

(defn surface-hidden?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ element-id]]
  (let [surface-visible? (r surface-visible? db element-id)]
       (not surface-visible?)))

(defn get-surface-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (map)
  ;  {:surface-visible? (boolean)}
  [db [_ element-id]]
  (if-let [surface-visible? (r surface-visible? db element-id)]
          {:surface-visible? surface-visible?}))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn hide-surface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ element-id]]
  (if (r surface-visible? db element-id)
      (as-> db % (r element/set-element-prop!    % element-id :surface-visible? false)
                 (r element/remove-element-prop! % element-id :surface-props))
      (return db)))

(a/reg-event-db :elements/hide-surface! hide-surface!)

(defn show-surface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ element-id]]
  (r element/set-element-prop! db element-id :surface-visible? true))

(a/reg-event-db :elements/show-surface! show-surface!)
