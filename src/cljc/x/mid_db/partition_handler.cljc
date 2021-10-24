
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v1.0.2
; Compatibility: x4.1.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-db.partition-handler
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :as map]
              [mid-fruits.vector  :as vector]
              [x.mid-core.api     :refer [r]]
              [x.mid-db.engine    :as engine]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name partition
;  A partíciók a kollekciókkal ellentétben, nem vektorban tárolnak {:id "xyz"}
;  tulajdonsággal azonosított adatokat, hanem térképben tárolnak :xyz {...}
;  kulccsal azonosított adatokat,
;
;  {:data-items {:my-data {:my-prop "my-value"} ...}}
;
;
;
; @name partition-id
;  A partíciók azonosítói, névtérrel rendelkező kulcsszavak lehetnek.
;
;  (def db {:my-partition/primary    {:data-items {...}}
;           :user-handler/users      {:data-items {...}}
;           :shop-handler/cart-items {:data-items {...}}
;           ...})
;
;
;
; @name ordered-partition
;  A partíciók a kollekciókkal ellentétben, nem az adatban tárolják annak sorrendjét
;  {:id "my-data" :order 7}, hanem a partíció :data-order azonosítójú vektorában.
;  A rendezetlen partíciók nem tartalmaznak {:data-order []} vektort.
;
;  (def db {:my-partition/primary {:data-items {:my-data    {:my-prop    "my-value"}}
;                                               :your-data  {:your-prop  "your value"}
;                                               :their-data {:their-prop "their value"}}
;                                  :data-order [:my-data :their-data :your-data]})
;
;
;
; @name data-item
;  A partíciók elemeinek elnevezése a kollekciók elemeivel ellentétben nem document,
;  hanem data-item.
;
;
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
;  => [:my-partition/primary :data-items :my-data]
;
;  (db/path ::primary :my-data :my-prop)
;  => [:my-partition/primary :data-items :my-data :my-prop]
;
;  (db/path ::primary :my-data :my-prop :my-subprop)
;  => [:my-partition/primary :data-items :my-data :my-prop :my-subprop]
;
;  ...
;
;
;
; @name meta-item
;  A partíciók a működésükkel, valamint a névtér működésével kapcsolatos
;  meta adatokat is tárolhatnak.
;
; (def db {:app-ui.menu-bar/menu-items {:data-items {:menu-item-1 {...} ...}
;                                       :meta-items {:menu-visible? true}}})
;
;
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
;  => [:my-partition/primary :meta-items :my-meta]
;
;  ...



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.engine
(def apply! engine/apply!)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn data-item-path
  ; @param (namespaced keyword) partition-id
  ; @param (list of keywords) n
  ;
  ; @example
  ;  (db/data-item-path ::my-partition :a :b :c)
  ;  => [::my-partition :data-items :a :b :c]
  ;
  ; @return (data-item-path vector)
  [partition-id & n]
  (vector/concat-items [partition-id :data-items] n))

(def path data-item-path)

(defn data-item-cofx-path
  ; @param (namespaced keyword) partition-id
  ; @param (list of keywords) n
  ;
  ; @example
  ;  (db/data-item-path ::my-partition :a :b :c)
  ;  => [:db ::my-partition :data-items :a :b :c]
  ;
  ; @return (data-item-path vector)
  [partition-id & n]
  (vector/concat-items [:db partition-id :data-items] n))

(def cofx-path data-item-cofx-path)

(defn meta-item-path
  ; @param (namespaced keyword) partition-id
  ; @param (list of keywords) xyz
  ;
  ; @example
  ;  (db/meta-item-path ::my-partition :a :b :c)
  ;  [::my-partition :meta-items :a :b :c]
  ;
  ; @return (meta-item-path vector)
  [partition-id & xyz]
  (vector/concat-items [partition-id :meta-items] xyz))

(defn meta-item-cofx-path
  ; @param (namespaced keyword) partition-id
  ; @param (list of keywords) n
  ;
  ; @example
  ;  (db/meta-item-path ::my-partition :a :b :c)
  ;  [:db ::my-partition :meta-items :a :b :c]
  ;
  ; @return (meta-item-path vector)
  [partition-id & n]
  (vector/concat-items [:db partition-id :meta-items] n))

(defn data-index-path
  ; @param (namespaced keyword) partition-id
  ; @param (?) _
  ;
  ; @return (data-index-path vector)
  [partition-id _])
  ; TODO ...



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn partition->data-items
  ; @param (map) partition
  ;  {:data-items (map)}
  ;
  ; @usage
  ;  (db/partition->data-items {:data-items {...}})
  ;
  ; @return (map)
  [partition]
  (:data-items partition))

(defn partition->data-item
  ; @param (map) partition
  ;  {:data-items (map)}
  ;
  ; @usage
  ;  (db/partition->data-item {:data-items {...}} :my-item-id)
  ;
  ; @return (map)
  [partition data-item-id]
  (get-in partition [:data-items data-item-id]))

(defn partition->data-order
  ; @param (map) partition
  ;  {:data-order (vector)}
  ;
  ; @usage
  ;  (db/partition->data-order {:data-items {...} :data-order [...]})
  ;
  ; @return (vector)
  [{:keys [data-order]}]
  (if (vector? data-order)
      (return  data-order)
      (return  [])))

(defn partition->partition-ordered?
  ; @param (map) partition
  ;  {:data-order (vector)}
  ;
  ; @usage
  ;  (db/partition->partition-ordered? {:data-items {...} :data-order [...]})
  ;
  ; @return (boolean)
  [{:keys [data-order]}]
  (vector? data-order))

(defn partition->meta-items
  ; @param (map) partition
  ;  {:meta-items (map)}
  ;
  ; @usage
  ;  (db/partition->meta-items {:meta-items {...})
  ;
  ; @return (map)
  [{:keys [meta-items]}]
  (return meta-items))

(defn partition->meta-item
  ; @param (map) partition
  ;  {:meta-items (map)}
  ; @param (keyword) meta-item-id
  ;
  ; @usage
  ;  (db/partition->meta-item {:meta-items {:my-meta-item "..."} :my-meta-item)
  ;
  ; @return (*)
  [{:keys [meta-items]} meta-item-id]
  (get meta-items meta-item-id))

(defn partition->partition-empty?
  ; @param (map) partition
  ;
  ; @usage
  ;  (db/partition->partition-empty? {:data-items {...})
  ;
  ; @return (boolean)
  [partition]
  (empty? (partition->data-items partition)))

(defn partition->partition-nonempty?
  ; @param (map) partition
  ;
  ; @usage
  ;  (db/partition->partition-nonempty? {:data-items {...})
  ;
  ; @return (boolean)
  [partition]
  (not (partition->partition-empty? partition)))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn indexed-partition-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) partition-props
  ;  {:indexed? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:data-index (map)}
  [{:keys [indexed?]}]
  (if indexed? {:data-index {}}))

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
  (cond-> (param {})
          ; Empty data-order
          (and (or ordered? ranged?)
               (not (vector? data-order)))
          (assoc :data-order (map/get-keys data-items))
          ; Default data-order
          (vector? data-order)
          (assoc :data-order data-order)))

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
  ;   :data-index (map)
  ;   :data-order (vector)
  ;   :meta-items (map)}
  [{:keys [data-items meta-items] :as partition-props}]
  (merge {:data-items (or    data-items {})
          :meta-items (param meta-items)}
         (indexed-partition-prototype partition-props)
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

(defn partition-exists?
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (r db/partition-exists? ::my-partition)
  ;
  ; @return (boolean)
  [db [_ partition-id]]
  (map/nonempty? (r get-partition db partition-id)))

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

(defn get-filtered-data-items
  ; @param (namespaced keyword) partition-id
  ; @param (function) filter-f
  ;
  ; @example
  ;  (def db {::my-partition :data-items {:a "Foo" :b "Bar"}})
  ;  (r db/get-filtered-data-items #(= %2 "Foo"))
  ;  => {:a "Foo"}
  ;
  ;  (def db {::my-partition :data-items {:a "Foo" :b "Bar"}})
  ;  (r db/get-filtered-data-items #(= %1 :b))
  ;  => {:b "Bar"}
  ;
  ; @return (map)
  [db [_ partition-id filter-f]]
  (reduce-kv #(if (filter-f %2 %3)
                  (assoc %1 %2 %3)
                  (return %1))
              (param {})
              (r get-data-items db partition-id)))

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

(defn get-data-index
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (r db/get-data-index ::my-partition)
  ;
  ; @return (map)
  [db [_ partition-id]]
  (get-in db [partition-id :data-index]))

(defn partition-indexed?
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (r db/partition-indexed? ::my-partition)
  ;
  ; @return (boolean)
  [db [_ partition-id]]
  (map? (r get-data-index db partition-id)))

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

(defn update-data-order!
  ; @param (namespaced keyword) partition-id
  ; @param (vector) data-order
  ;
  ; @example
  ;  (def db {:my-partition/primary {:data-items {...}
  ;                                  :data-order [:a :b :c]}})
  ;  (r db/update-data-order! db :my-partition/primary [:d :n :c])
  ;  => {:my-partition/primary {:data-items {...}
  ;                             :data-order [:a :b :d :n :c]}})
  ;
  ; @return (map)
  [db [_ partition-id data-order]]
  (let [expired-data-order (r get-data-order db partition-id)
        updated-data-order (vector/concat-once expired-data-order data-order)]
       (assoc-in db [partition-id :data-order] updated-data-order)))

(defn reg-partition!
  ; @param (namespaced keyword) partition-id
  ; @param (map)(opt) partition-props
  ;  {:data-cursor-high (integer)(opt)
  ;    Default: 0
  ;    Only w/ {:ranged? true}
  ;   :data-cursor-low (integer)(opt)
  ;    Default: 0
  ;    Only w/ {:ranged? true}
  ;   :data-items (map)(opt)
  ;    Initial data items
  ;   :data-order (vector)(opt)
  ;    Initial data order
  ;    Only w/ {:ordered? true}
  ;   :indexed? (boolean)(opt)
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

(defn remove-partition!
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (r db/remove-partition! ::my-partition)
  ;
  ; @return (map)
  [db [_ partition-id]]
  (dissoc db partition-id))

(defn merge-partitions!
  ; @param (namespaced keyword) base-partition-id
  ; @param (namespaced keyword) source-partition-id
  ; @param (namespaced keyword) target-partition-id
  ;
  ; @usage
  ;  (r db/merge-partitions! db ::base-partition ::source-partition ::target-partition)
  ;
  ; @return (map)
  [db [_ secondary-partition-id primary-partition-id]])
  ; TODO ...
  ;  A ::source-partition particiot merge-eli a ::base-partition particiora
  ;  es elmenti ::target-partition neven, majd a ::base-partition es ::source-partition
  ;  particiokat torli

(defn add-data-items!
  ; @param (namespaced keyword) partition-id
  ; @param (map) data-items
  ; @param (vector)(opt) data-order
  ;
  ; @usage
  ;  (r db/add-data-items! db ::my-partition {:my-item "my-value"})
  ;
  ; @usage
  ;  (r db/add-data-items! db ::my-partition {:my-item "my-value"} [:my-item])
  ;
  ; @return (map)
  [db [event-id partition-id data-items data-order]]
  (cond-> (param db)
          (vector? data-order)
          (update-data-order! [event-id partition-id data-order])
          :always
          (apply! [event-id [partition-id :data-items] merge data-items])))

(defn set-meta-item!
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) meta-item-id
  ; @param (*) meta-item
  ;
  ; @usage
  ;  (r db/set-meta-item! ::my-partition :my-meta-item "my-value")
  ;
  ; @return (map)
  [db [_ partition-id meta-item-id meta-item]]
  (assoc-in db [partition-id :meta-items meta-item-id] meta-item))
