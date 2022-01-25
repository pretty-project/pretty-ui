
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.10
; Description:
; Version: v0.4.2
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.visible
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.map       :refer [dissoc-in]]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-elements.engine.element :as element]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- visible-items->first-content-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (maps in vector) visible-items
  ;  [{:content-id (keyword)}]
  ;
  ; @example
  ;  (engine/visible-items->first-content-id [{:foo1 :bar1} {:foo2 :bar2 :content-id :baz2}])
  ;  =>
  ;  :baz2
  ;
  ; @return (keyword)
  [visible-items]
  (vector/get-first-match-item visible-items #(when-let [content-id (:content-id %1)]
                                                        (return content-id))))

(defn on-hide-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (function)
  [surface-id]
  #(a/dispatch [:elements/empty-surface! surface-id]))

(defn on-show-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (keyword) content-id
  ;
  ; @return (function)
  [surface-id content-id]
  #(a/dispatch [:elements/set-content! surface-id content-id]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-content-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (keyword)
  [db [_ surface-id]]
  (get-in db (db/path :elements/primary surface-id :selected-content-id)))

(defn content-visible?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (keyword) content-id
  ;
  ; @return (boolean)
  [db [_ surface-id content-id]]
  (let [selected-content-id (r get-selected-content-id db surface-id)]
       (= selected-content-id content-id)))

(defn surface-empty?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (keyword)
  [db [_ surface-id]]
  (nil? (r get-selected-content-id db surface-id)))

(defn get-visible-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (map)
  ;  {:selected-content-id (keyword)}
  [db [_ surface-id]]
  ; XXX#4339
  ; Az elem {:selected-content-id ...} tulajdonságát base-props paraméterként
  ; is át lehet adni (ameddig a tulajdonság az adatbázisban nincs jelen)
  (when-let [selected-content-id (r get-selected-content-id db surface-id)]
            {:selected-content-id selected-content-id}))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- set-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (keyword) content-id
  ;
  ; @return (map)
  [db [_ surface-id content-id]]
  (assoc-in db (db/path :elements/primary surface-id :selected-content-id) content-id))

(a/reg-event-db :elements/set-content! set-content!)

(defn- remove-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (map)
  [db [_ surface-id]]
  (dissoc-in db (db/path :elements/primary surface-id :selected-content-id)))

(a/reg-event-db :elements/remove-content! remove-content!)
(a/reg-event-db :elements/empty-surface!  remove-content!)
