
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v0.9.4
; Compatibility: x4.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-db.data-order-handler
    (:require [mid-fruits.map    :refer [dissoc-in]]
              [mid-fruits.vector :as vector]
              [x.mid-core.api    :refer [r]]
              [x.mid-db.partition-handler :as partition-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.partition-handler
(def data-item-exists?  partition-handler/data-item-exists?)
(def get-data-order     partition-handler/get-data-order)
(def partition-ordered? partition-handler/partition-ordered?)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn data-item-last?
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ;
  ; @usage
  ;  (r db/data-item-last? db ::my-partition :my-item-id)
  ;
  ; @return (boolean)
  [db [_ partition-id data-item-id]]
  (let [data-order (r get-data-order db partition-id)]
       (vector/item-last? data-order data-item-id)))

(defn data-item-first?
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ;
  ; @usage
  ;  (r db/data-item-first? db ::my-partition :my-item-id)
  ;
  ; @return (boolean)
  [db [_ partition-id data-item-id]]
  (let [data-order (r get-data-order db partition-id)]
       (vector/item-first? data-order data-item-id)))

(defn get-data-item-position
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ;
  ; @usage
  ;  (r db/get-data-item-position db ::my-partition :my-item-id)
  ;
  ; @return (integer)
  [db [_ partition-id data-item-id]]
  (let [data-order (r get-data-order db partition-id)]
       (vector/item-first-dex data-order data-item-id)))



;; -- Data-item DB events -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn move-data-item-to-last!
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ;
  ; @usage
  ;  (r db/move-data-item-to-last! db ::my-partition :my-item-id)
  ;
  ; @return (map)
  [db [_ partition-id data-item-id]]
  (let [data-order         (r get-data-order db partition-id)
        updated-data-order (vector/move-item-to-last data-order data-item-id)]
       (assoc-in db [partition-id :data-order] updated-data-order)))

(defn move-data-item-to-first!
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ;
  ; @usage
  ;  (r db/move-data-item-to-first! db ::my-partition :my-item-id)
  ;
  ; @return (map)
  [db [_ partition-id data-item-id]]
  (let [data-order         (r get-data-order db partition-id)
        updated-data-order (vector/move-item-to-first data-order data-item-id)]
       (assoc-in db [partition-id :data-order] updated-data-order)))

(defn move-data-item!
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ;
  ; @usage
  ;  (r db/move-data-item! db ::my-partition :my-item-id 3)
  ;
  ; @return (map)
  [db [_ partition-id data-item-id dex]]
  (let [data-order         (r get-data-order db partition-id)
        updated-data-order (vector/move-first-occurence data-order data-item-id dex)]
       (assoc-in db [partition-id :data-order] updated-data-order)))

(defn remove-ordered-data-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ;
  ; @return (map)
  [db [_ partition-id data-item-id]]
  (-> db (dissoc-in [partition-id :data-items data-item-id])
         (update-in [partition-id :data-order] vector/remove-item data-item-id)))

(defn remove-unordered-data-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ;
  ; @return (map)
  [db [_ partition-id data-item-id]]
  (dissoc-in db [partition-id :data-items data-item-id]))

(defn remove-data-item!
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ;
  ; @usage
  ;  (r db/remove-data-item! db ::my-partition :my-item-id)
  ;
  ; @return (map)
  [db [_ partition-id data-item-id]]
  (if (r partition-ordered?          db partition-id)
      (r remove-ordered-data-item!   db partition-id data-item-id)
      (r remove-unordered-data-item! db partition-id data-item-id)))

(defn- add-ordered-data-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ; @param (*) data-item
  ;
  ; @return (map)
  [db [_ partition-id data-item-id data-item]]
  (as-> db % (assoc-in % [partition-id :data-items data-item-id] data-item)
             (r move-data-item-to-last! % partition-id data-item-id)))

(defn- add-unordered-data-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ; @param (*) data-item
  ;
  ; @return (map)
  [db [_ partition-id data-item-id data-item]]
  (assoc-in db [partition-id :data-items data-item-id] data-item))

(defn add-data-item!
  ; Ha az elem létezik, akkor felülírja és az utolsó helyre teszi.
  ; Ha az elem nem létezik, akkor hozzáadja a partícióhoz és az utolsó helyre teszi.
  ;
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ; @param (*) data-item
  ;
  ; @usage
  ;  (r db/add-data-item! db ::my-partition :my-item-id "my value")
  ;
  ; @return (map)
  [db [_ partition-id data-item-id data-item]]
  (if (r partition-ordered?       db partition-id)
      (r add-ordered-data-item!   db partition-id data-item-id data-item)
      (r add-unordered-data-item! db partition-id data-item-id data-item)))

(defn- update-ordered-data-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ; @param (*) data-item
  ;
  ; @return (map)
  [db [_ partition-id data-item-id data-item]]
  (if (r data-item-exists? db partition-id data-item-id)
      (assoc-in db [partition-id :data-items data-item-id] data-item)
      (r add-data-item! db partition-id data-item-id data-item)))

(defn- update-unordered-data-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ; @param (*) data-item
  ;
  ; @return (map)
  [db [_ partition-id data-item-id data-item]]
  (assoc-in db [partition-id :data-items data-item-id] data-item))

(defn update-data-item!
  ; XXX#5550
  ;  Ha az elem létezik, akkor felülírja.
  ;  Ha az elem nem létezik, akkor hozzáadja a partícióhoz és az utolsó helyre teszi.
  ;
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ; @param (*) data-item
  ;
  ; @usage
  ;  (r db/update-data-item! db ::my-partition :my-item-id "my value")
  ;
  ; @return (map)
  [db [_ partition-id data-item-id data-item]]
  (if (r partition-ordered?          db partition-id)
      (r update-ordered-data-item!   db partition-id data-item-id data-item)
      (r update-unordered-data-item! db partition-id data-item-id data-item)))

(defn apply-data-item!
  ; XXX#5550
  ;  Ha az elem létezik, akkor felülírja.
  ;  Ha az elem nem létezik, akkor hozzáadja a partícióhoz.
  ;
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ; @param (function) f
  ; @param (list of *) params
  ;
  ; @usage
  ;  (r db/apply-data-item! db ::my-partition :my-item-id merge {...})
  ;
  ; @return (map)
  [db [_ partition-id data-item-id f & params]]
  (let [data-item         (get-in db [partition-id :data-items data-item-id])
        params            (cons data-item params)
        updated-data-item (apply f params)]
       (r update-data-item! db partition-id data-item-id updated-data-item)))

(defn apply-data-items!
  ; @param (namespaced keyword) partition-id
  ; @param (function) f
  ; @param (list of *) params
  ;
  ; @usage
  ;  (r db/apply-data-items! db ::my-partition assoc :my-key "My value")
  ;
  ; @return (map)
  [db [_ partition-id f & params]]
  (letfn [(f [db data-item-id data-item]
             (let [params            (cons data-item params)
                   updated-data-item (apply f params)]
                  (assoc-in db [partition-id :data-items data-item-id] updated-data-item)))]
         (reduce-kv f db (get-in db [partition-id :data-items]))))

(defn copy-item-to-partition!
  ; A partition-id azonosítójú partícióba data-item-id azonosítóval másolja
  ; az adatbázis item-path pontján található elemet
  ;
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ; @param (item-path vector) item-path
  ;
  ; @usage
  ;  (r db/copy-item-to-partition! db ::my-partition :my-item-id [:my-item])
  ;
  ; @return (map)
  [db [_ partition-id data-item-id item-path]]
  (let [data-item (get-in db item-path)]
       (r update-data-item! db partition-id data-item-id data-item)))

(defn move-item-to-partition!
  ; A partition-id azonosítójú partícióba data-item-id azonosítóval helyezi
  ; az adatbázis item-path pontján található elemet
  ;
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ; @param (item-path vector) item-path
  ;
  ; @usage
  ;  (r db/move-item-to-partition! db ::my-partition :my-item-id [:my-item])
  ;
  ; @return (map)
  [db [_ partition-id data-item-id item-path]]
  (as-> db % (r copy-item-to-partition! % partition-id data-item-id item-path)
             (dissoc-in % item-path)))



;; -- Partition DB events -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn empty-ordered-partition!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced keyword) partition-id
  ;
  ; @return (map)
  [db [_ partition-id]]
  (-> db (assoc-in [partition-id :data-items] {})
         (assoc-in [partition-id :data-order] [])))

(defn empty-unordered-partition!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced keyword) partition-id
  ;
  ; @return (map)
  [db [_ partition-id]]
  (assoc-in db [partition-id :data-items] {}))

(defn empty-partition!
  ; @param (namespaced keyword) partition-id
  ;
  ; @usage
  ;  (r db/empty-partition! db ::my-partition)
  ;
  ; @return (map)
  [db [_ partition-id]]
  (if (r partition-ordered?         db partition-id)
      (r empty-ordered-partition!   db partition-id)
      (r empty-unordered-partition! db partition-id)))
