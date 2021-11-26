
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.08.11
; Description:
; Version: v0.2.6
; Compatibility: x4.3.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.expandable
    (:require [x.app-core.api                :as a :refer [r]]
              [x.app-elements.engine.element :as element]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-expanded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ element-id]]
  (let [element-expanded? (r element/get-element-prop db element-id :expanded?)]
       (boolean element-expanded?)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expand-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ element-id]]
  (r element/set-element-prop! db element-id :expanded? true))

(a/reg-event-db :elements/expand-element! expand-element!)

(defn compress-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ element-id]]
  (r element/set-element-prop! db element-id :expanded? false))

(a/reg-event-db :elements/compress-element! compress-element!)

(defn toggle-element-expansion!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ element-id]]
  (if (r element-expanded? db element-id)
      (r compress-element! db element-id)
      (r expand-element!   db element-id)))

(a/reg-event-db :elements/toggle-element-expansion! toggle-element-expansion!)
