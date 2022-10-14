
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v1.0.2
; Compatibility: x4.1.5



;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-db.partition-handler
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :as map]
              [mid-fruits.vector  :as vector]
              [x.mid-core.api     :refer [r]]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name partition
;  A partíciók a kollekciókkal ellentétben, nem vektorban tárolnak {:id "xyz"}
;  tulajdonsággal azonosított adatokat, hanem térképben tárolnak :xyz {...}
;  kulccsal azonosított adatokat.
;  Pl. {:data-items {:my-data {:my-prop "my-value"} ...}}
;
; @name partition-id
;  A partíciók azonosítói, névtérrel rendelkező kulcsszavak lehetnek.
;  Pl. (def db {:my-partition/primary    {:data-items {...}}
;                :user-handler/users      {:data-items {...}}
;                :shop-handler/cart-items {:data-items {...}}
;                ...})
;
; @name ordered-partition
;  A partíciók a kollekciókkal ellentétben, nem az adatban tárolják annak sorrendjét
;  {:id "my-data" :order 7}, hanem a partíció :data-order azonosítójú vektorában.
;  A rendezetlen partíciók nem tartalmaznak {:data-order []} vektort.
;  Pl. (def db {:my-partition/primary {:data-items {:my-data    {:my-prop    "my-value"}}
;                                                    :your-data  {:your-prop  "your value"}
;                                                    :their-data {:their-prop "their value"}}
;                                       :data-order [:my-data :their-data :your-data]})
;
; @name data-item
;  A partíciók elemeinek elnevezése data-item.
;
; @name data-item-path
;  Egy szabvány data-item elem adatbázis útvonala a következőképpen nézhet ki:
;
;  [:my-partition/primary :data-items :my-data]
;  [:my-partition/primary :data-items :my-data :my-prop]
;  [:my-partition/primary :data-items :my-data :my-prop :my-subprop]
;  ...
;
;  Ezek az útvonalak a (db/data-item-path ...) függvény használatával egyszerűen
;  megadhatók. A (db/data-item-path ...) függvény rövidített formulája a (db/path ...)
;  függvény.
;
;  (db/path ::primary :my-data)
;  =>
;  [:my-partition/primary :data-items :my-data]
;
;  (db/path ::primary :my-data :my-prop)
;  =>
;  [:my-partition/primary :data-items :my-data :my-prop]
;
;  (db/path ::primary :my-data :my-prop :my-subprop)
;  =>
;  [:my-partition/primary :data-items :my-data :my-prop :my-subprop]
;
; @name meta-item
;  A partíciók a működésükkel, valamint a névtér működésével kapcsolatos
;  meta adatokat is tárolhatnak.
;  Pl. (def db {:app-ui.menu-bar/menu-items {:data-items {:menu-item-1 {...} ...}
;                                             :meta-items {:menu-visible? true}}})
;
; @name meta-item-path
;  Egy szabvány meta adat adatbázis útvonala a következőképpen nézhet ki:
;
;  [:my-partition/primary :meta-items :my-meta]
;  [:my-partition/primary :data-items :my-meta :my-prop]
;  [:my-partition/primary :data-items :my-meta :my-prop :my-subprop]
;  ...
;
;  Ezek az útvonalak a (db/meta-item-path ...) függvény használatával egyszerűen
;  megadhatók.
;
;  (db/meta-item-path ::primary :my-meta)
;  =>
;  [:my-partition/primary :meta-items :my-meta]
;
;  ...



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn data-item-path
  ; @param (namespaced keyword) partition-id
  ; @param (list of keywords) xyz
  ;
  ; @example
  ;  (db/data-item-path ::my-partition :a :b :c)
  ;  =>
  ;  [::my-partition :data-items :a :b :c]
  ;
  ; @return (data-item-path vector)
  [partition-id & xyz]
  (vector/concat-items [partition-id :data-items] xyz))

(def path data-item-path)

(defn meta-item-path
  ; @param (namespaced keyword) partition-id
  ; @param (list of keywords) xyz
  ;
  ; @example
  ;  (db/meta-item-path ::my-partition :a :b :c)
  ;  =>
  ;  [::my-partition :meta-items :a :b :c]
  ;
  ; @return (meta-item-path vector)
  [partition-id & xyz]
  (vector/concat-items [partition-id :meta-items] xyz))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ordered-partition-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) partition-props
  ;  {:data-items (map)(opt)
  ;   :data-order (vector)(opt)
  ;   :ordered? (boolean)(opt)
  ;   :ranged? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:data-order (vector)}
  [{:keys [data-items data-order ordered? ranged?]}]
             ; Default data-order
  (cond-> {} (vector? data-order)
             (assoc  :data-order data-order)
             ; Empty data-order
             (and (or ordered? ranged?))
             (assoc :data-order (map/get-keys data-items))))

(defn ranged-partition-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) partition-props
  ;  {:data-cursor-high (integer)(opt)
  ;   :data-cursor-low (integer)(opt)
  ;   :ranged? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:data-order (vector)}
  [{:keys [data-cursor-high data-cursor-low ranged?]}]
  (if ranged? {:data-cursor-high (or data-cursor-high 0)
               :data-cursor-low  (or data-cursor-low  0)}))

(defn partition-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) partition-props
  ;  {:data-items (map)(opt)
  ;   :meta-items (map)(opt)}
  ;
  ; @return (map)
  ;  {:data-cursor-high (integer)
  ;   :data-cursor-low (integer)
  ;   :data-items (map)
  ;   :data-order (vector)
  ;   :meta-items (map)}
  [{:keys [data-items meta-items] :as partition-props}]
  (merge {:data-items (or    data-items {})
          :meta-items (param meta-items)}
         (ordered-partition-prototype partition-props)
         (ranged-partition-prototype  partition-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-partition
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (r db/get-partition ::my-partition)
  ;
  ; @return (map)
  [db [_ partition-id]]
  (partition-id db))

(defn get-data-items
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (r db/get-data-items ::my-partition)
  ;
  ; @return (map)
  [db [_ partition-id]]
  (get-in db [partition-id :data-items]))

(defn get-data-item
  ; @param (namespaced keyword) partition-id
  ; @param (list of keywords) n
  ;
  ; @usage
  ;  (r db/get-data-item ::my-partition :my-data-item-id)
  ;
  ; @usage
  ;  (r db/get-data-item ::my-partition :my-data-item-id :my-subitem-id)
  ;
  ; @return (*)
  [db [_ partition-id & n]]
  (let [data-item-path (vector/concat-items [partition-id :data-items] n)]
       (get-in db data-item-path)))

(defn data-item-exists?
  ; @param (namespaced keyword) partition-id
  ; @param (list of keywords) n
  ;
  ; @usage
  ;  (r db/data-item-exists? ::my-partition :my-data-item-id)
  ;
  ; @usage
  ;  (r db/data-item-exists? ::my-partition :my-data-item-id :my-subitem-id)
  ;
  ; @return (boolean)
  [db [_ partition-id & n]]
  (let [data-item-path (vector/concat-items [partition-id :data-items] n)]
       (some? (get-in db data-item-path))))

(defn get-data-item-count
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (r db/get-data-item-count ::my-partition)
  ;
  ; @return (integer)
  [db [_ partition-id]]
  (count (r get-data-items db partition-id)))

(defn get-meta-items
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (r db/get-meta-items ::my-partition)
  ;
  ; @return (map)
  [db [_ partition-id]]
  (get-in db [partition-id :meta-items]))

(defn get-meta-item
  ; @param (namespaced keyword) partition-id
  ; @param (list of keywords) n
  ;
  ; @usage
  ;  (r db/get-meta-item ::my-partition :my-meta-item-id)
  ;
  ; @usage
  ;  (r db/get-meta-item ::my-partition :my-meta-item-id :my-meta-subitem-id)
  ;
  ; @return (*)
  [db [_ partition-id & n]]
  (let [meta-item-path (vector/concat-items [partition-id :meta-items] n)]
       (get-in db meta-item-path)))

(defn get-data-order
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (r db/get-data-order ::my-partition)
  ;
  ; @return (vector)
  [db [_ partition-id]]
  (get-in db [partition-id :data-order]))

(defn partition-ordered?
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (r db/partition-ordered? ::my-partition)
  ;
  ; @return (boolean)
  [db [_ partition-id]]
  (vector? (r get-data-order db partition-id)))

(defn partition-empty?
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (r db/partition-empty? ::my-partition)
  ;
  ; @return (boolean)
  [db [_ partition-id]]
  (empty? (r get-data-items db partition-id)))



;; -- DB events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-partition!
  ; @param (namespaced keyword) partition-id
  ; @param (map)(opt) partition-props
  ;  {:data-cursor-high (integer)(opt)
  ;    Default: 0
  ;    W/ {:ranged? true}
  ;   :data-cursor-low (integer)(opt)
  ;    Default: 0
  ;    W/ {:ranged? true}
  ;   :data-items (map)(opt)
  ;    Initial data items
  ;   :data-order (vector)(opt)
  ;    Initial data order
  ;    W/ {:ordered? true}
  ;   :meta-items (map)(opt)
  ;   :ordered? (boolean)(opt)
  ;   :ranged? (boolean)(opt)}
  ;
  ; @usage
  ;  (r db/reg-partition! ::my-partition)
  ;
  ; @usage
  ;  (r db/reg-partition! ::my-partition {:data-items {...}})
  ;
  ; @return (map)
  [db [_ partition-id partition-props]]
  (let [partition-id (keyword/namespaced! partition-id)]
       (assoc db partition-id (partition-prototype partition-props))))
