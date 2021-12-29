
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.6.8
; Compatibility: x4.5.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.subs
    (:require [mid-fruits.candy      :refer [param return]]
              [mid-fruits.vector     :as vector]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-db.api          :as db]
              [x.app-environment.api :as environment]
              [x.app-sync.api        :as sync]
              [app-plugins.item-lister.engine :as engine]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-downloaded-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (maps in vector)
  [db [_ extension-id]]
  (get-in db [extension-id :item-lister/data-items]))

(defn get-meta-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ extension-id _ item-key]]
  (get-in db [extension-id :item-lister/meta-items item-key]))

(defn synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [request-id (engine/request-id extension-id item-namespace)]
       (r sync/listening-to-request? db request-id)))

(defn synchronized?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  ; A szerverrel való első kommunkáció megtörténtét, nem lehetséges az (r sync/request-sent? db ...)
  ; függvénnyel vizsgálni, mert ha az item-lister már meg volt jelenítve, akkor az újbóli
  ; megjelenítéskor (r sync/request-sent? db ...) függvény visszatérési értéke true lenne!
  (boolean (r get-meta-value db extension-id item-namespace :synchronized?)))

(defn no-items-to-show?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (boolean)
  [db [_ extension-id]]
  (let [downloaded-items (r get-downloaded-items db extension-id)]
       (empty? downloaded-items)))

(defn any-item-to-show?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (boolean)
  [db [_ extension-id]]
  (let [downloaded-items (r get-downloaded-items db extension-id)]
       (vector/nonempty? downloaded-items)))

(defn get-downloaded-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (integer)
  [db [_ extension-id]]
  (let [downloaded-items (r get-downloaded-items db extension-id)]
       (count downloaded-items)))

(defn get-all-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (integer)
  [db [_ extension-id item-namespace]]
  ; XXX#0791
  ; - Ha a tárolt érték nil, akkor a visszatérési érték 0
  ; - Ha a szerver hibásan nil értéket küld le, akkor a 0 visszatérési érték miatt
  ;   az all-items-downloaded? függvény visszatérési értéke true lesz ezért megáll
  ;   az újabb elemek letöltése.
  ; - Hibás szerver-működés esetén szükséges, hogy az infinite-loader komponens
  ;   ne próbálja újra és újra letölteni a további feltételezett elemeket.
  ; - Ha még nem történt meg az első kommunikáció a szerverrel, akkor a get-all-item-count
  ;   függvény visszatérési értéke nem tekinthető mérvadónak!
  ;   Ezért az első kommunikáció megtörténtét szükséges külön vizsgálni!
  (let [all-item-count (r get-meta-value db extension-id item-namespace :document-count)]
       (if (integer? all-item-count)
           (return   all-item-count)
           (return   0))))

(defn get-disabled-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (r get-meta-value db extension-id item-namespace :disabled-items))

(defn item-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r engine/item-disabled? db :my-extension :my-type 0)
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace item-dex]]
  (let [disabled-items (r get-disabled-items db extension-id item-namespace)]
       (vector/contains-item? disabled-items item-dex)))

; @usage
;  [:item-lister/item-disabled? :my-extension :my-type 0]
(a/reg-sub :item-lister/item-disabled? item-disabled?)

(defn get-selected-item-dexes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (integers in vector)
  [db [_ extension-id item-namespace]]
  (r get-meta-value db extension-id item-namespace :selected-items))

(defn get-selected-item-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (strings in vector)
  [db [_ extension-id item-namespace]]
  (let [selected-items (r get-meta-value db extension-id item-namespace :selected-items)]
       (vec (reduce (fn [result item-dex]
                        (let [item-id (get-in db [extension-id :item-lister/data-items item-dex :id])]
                             (conj result item-id)))
                    [] selected-items))))

(defn get-selected-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (integer)
  [db [_ extension-id item-namespace]]
  (let [selected-items (r get-selected-item-dexes db extension-id item-namespace)]
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
  (let [selected-item-dexes (r get-selected-item-dexes db extension-id item-namespace)]
       (vector/contains-item? selected-item-dexes item-dex)))

; @usage
;  [:item-lister/item-selected? :my-extension :my-type 0]
(a/reg-sub :item-lister/item-selected? item-selected?)

(defn all-items-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [selected-items-count  (r get-selected-item-count   db extension-id item-namespace)
        downloaded-item-count (r get-downloaded-item-count db extension-id)]
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
  (let [selected-item-dexes (r get-selected-item-dexes db extension-id item-namespace)]
       (vector/nonempty? selected-item-dexes)))

(defn all-items-downloaded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [all-item-count        (r get-all-item-count        db extension-id item-namespace)
        downloaded-item-count (r get-downloaded-item-count db extension-id)]
       ; XXX#0791
       ; - = vizsgálat helyett szükséges >= vizsgálatot alkalmazni, hogy ha hibásan
       ;   nagyobb a downloaded-item-count értéke, mint az all-item-count értéke,
       ;   akkor ne próbáljon további feltételezett elemeket letölteni.
       ; - Ha még nem történt meg az első kommunikáció a szerverrel, akkor az all-items-downloaded?
       ;   függvény visszatérési értéke nem tekinthető mérvadónak!
       ;   Ezért az első kommunikáció megtörténtét szükséges külön vizsgálni!
       (>= downloaded-item-count all-item-count)))

(defn get-search-term
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (let [search-term  (r get-meta-value db extension-id item-namespace :search-term)]
       (str search-term)))

(defn some-items-received?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [received-count (r get-meta-value db extension-id item-namespace :received-count)]
       (not= received-count 0)))

(defn download-more-items?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
       ; XXX#0791
       ; Ha még nem történt meg az első kommunikáció a szerverrel, akkor
       ; az all-items-downloaded? függvény visszatérési értéke nem tekinthető mérvadónak!
  (and (or (not (r synchronized?         db extension-id item-namespace))
           (not (r all-items-downloaded? db extension-id item-namespace)))
       ; BUG#7009
       (r some-items-received? db extension-id item-namespace)))

(defn downloading-items?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  ; A kiválasztott elemeken végzett műveletek is {:synchronizing? true} állapotba hozzák
  ; az item-lister plugint, ezért szükséges megkülönböztetni az elemek letöltése szinkronizációt,
  ; az elemeken végzett műveletek szinkronizációval.
  (and (r download-more-items? db extension-id item-namespace)
       (r synchronizing?       db extension-id item-namespace)))

(defn export-backup-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (namespaced maps in vector)
  [db [_ extension-id item-namespace item-ids]]
  (vector/->items item-ids #(let [backup-item (get-in db [extension-id :item-lister/backup-items %])]
                                 (db/document->namespaced-document backup-item item-namespace))))

(defn export-marked-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) mark-props
  ;  {:marker-key (keyword)
  ;   :toggle-f (function)}
  ;
  ; @example
  ;  (r subs/export-marked-items db :my-extension :my-type {:marker-key :archived? :toggle-f not}
  ;                                                        ["my-item" "your-item"])
  ;  =>
  ;  [{:my-type/id "my-item"   :my-type/archived? true}
  ;   {:my-type/id "your-item" :my-type/archived? true}]
  ;
  ; @return (namespaced maps in vector)
  [db [_ extension-id item-namespace {:keys [marker-key toggle-f]}]]
  (let [item-dexes  (r get-selected-item-dexes db extension-id item-namespace)
        item-id-key (keyword item-namespace :id)
        marker-key  (keyword item-namespace marker-key)]
       (vector/->items item-dexes #(let [item-id      (get-in db [extension-id :item-lister/data-items % :id])
                                         marker-value (get-in db [extension-id :item-lister/data-items % marker-key])]
                                        {item-id-key item-id marker-key (toggle-f marker-value)}))))

(defn export-unmarked-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) mark-props
  ;  {:marker-key (keyword)
  ;   :toggle-f (function)}
  ; @param (strings in vector) item-ids
  ;
  ; @example
  ;  (r subs/export-unmarked-items db :my-extension :my-type {:marker-key :archived? :toggle-f not}
  ;                                                          ["my-item" "your-item"])
  ;  =>
  ;  [{:my-type/id "my-item"   :my-type/archived? false}
  ;   {:my-type/id "your-item" :my-type/archived? false}]
  ;
  ; @return (namespaced maps in vector)
  [db [_ extension-id item-namespace {:keys [marker-key toggle-f]} item-ids]]
  ; XXX#7810
  ; Az elemek megjelőlésének visszavonásához szükséges azokról másolatot készíteni,
  ; mivel az item-lister plugin a megjelölt elemeket eltávolítja a letöltött elemek listájából.
  (let [item-id-key (keyword item-namespace :id)
        marker-key  (keyword item-namespace marker-key)]
       (vector/->items item-ids #(let [marker-value (get-in db [extension-id :item-lister/backup-items % marker-key])]
                                      {item-id-key % marker-key (toggle-f marker-value)}))))

(defn get-description
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (metamorphic-content)
  [db [_ extension-id item-namespace]]
  (let [downloaded-item-count (r get-downloaded-item-count db extension-id)
        all-item-count        (r get-all-item-count        db extension-id item-namespace)
        synchronized?         (r synchronized?             db extension-id item-namespace)]
       (if synchronized? (components/content {:content      :npn-items-downloaded
                                              :replacements [downloaded-item-count all-item-count]}))))

(defn get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:actions-mode? (boolean)
  ;   :all-items-selected? (boolean)
  ;   :any-item-selected? (boolean)
  ;   :no-items-to-show? (boolean)
  ;   :order-changed? (boolean)
  ;   :reorder-mode? (boolean)
  ;   :search-mode? (boolean)
  ;   :select-mode? (boolean)
  ;   :synchronizing? (boolean)
  ;   :viewport-small? (boolean)}
  [db [_ extension-id item-namespace]]
  (cond ; If select-mode is enabled ...
        (r get-meta-value db extension-id item-namespace :select-mode?)
        {:select-mode?        true
         :all-items-selected? (r all-items-selected? db extension-id item-namespace)
         :any-item-selected?  (r any-item-selected?  db extension-id item-namespace)
         :synchronizing?      (r synchronizing?      db extension-id item-namespace)}
        ; If search-mode is enabled ...
        (r get-meta-value db extension-id item-namespace :search-mode?)
        {:search-mode?   true
         :synchronizing? (r synchronizing? db extension-id item-namespace)}
        ; If reorder-mode is enabled ...
        (r get-meta-value db extension-id item-namespace :reorder-mode?)
        {:reorder-mode?  true
         :order-changed? false
         :synchronizing? (r synchronizing? db extension-id item-namespace)}
        ; Use actions-mode as default ...
        :default
        {:actions-mode?     true
         :no-items-to-show? (r no-items-to-show?           db extension-id)
         :synchronizing?    (r synchronizing?              db extension-id item-namespace)
         :viewport-small?   (r environment/viewport-small? db)}))

(a/reg-sub :item-lister/get-header-props get-header-props)

(defn get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:actions-mode? (boolean)
  ;   :downloaded-items (vector)
  ;   :downloading-items? (boolean)
  ;   :disabled-items (integers in vector)
  ;   :no-items-to-show? (boolean)
  ;   :reorder-mode? (boolean)
  ;   :search-mode? (boolean)
  ;   :select-mode? (boolean)
  ;   :synchronized? (boolean)
  ;   :synchronizing? (boolean)}
  [db [_ extension-id item-namespace]]
  (cond ; If select-mode is enabled ...
        (r get-meta-value db extension-id item-namespace :select-mode?)
        {:select-mode?      true
         :downloaded-items   (r get-downloaded-items db extension-id)
         :downloading-items? (r downloading-items?   db extension-id item-namespace)
         :no-items-to-show?  (r no-items-to-show?    db extension-id)
         :synchronized?      (r synchronized?        db extension-id item-namespace)
         :synchronizing?     (r synchronizing?       db extension-id item-namespace)}
        ; If search-mode is enabled ...
        (r get-meta-value db extension-id item-namespace :search-mode?)
        {:search-mode?      true
         :downloaded-items   (r get-downloaded-items db extension-id)
         :downloading-items? (r downloading-items?   db extension-id item-namespace)
         :no-items-to-show?  (r no-items-to-show?    db extension-id)
         :synchronized?      (r synchronized?        db extension-id item-namespace)
         :synchronizing?     (r synchronizing?       db extension-id item-namespace)}
        ; If reorder-mode is enabled ...
        (r get-meta-value db extension-id item-namespace :reorder-mode?)
        {:reorder-mode?     true
         :downloaded-items   (r get-downloaded-items db extension-id)
         :downloading-items? (r downloading-items?   db extension-id item-namespace)
         :no-items-to-show?  (r no-items-to-show?    db extension-id)
         :synchronized?      (r synchronized?        db extension-id item-namespace)
         :synchronizing?     (r synchronizing?       db extension-id item-namespace)}
        ; Use actions-mode as default ...
        :default
        {:actions-mode?     true
         :downloaded-items   (r get-downloaded-items db extension-id)
         :downloading-items? (r downloading-items?   db extension-id item-namespace)
         :no-items-to-show?  (r no-items-to-show?    db extension-id)
         :synchronized?      (r synchronized?        db extension-id item-namespace)
         :synchronizing?     (r synchronizing?       db extension-id item-namespace)}))

(a/reg-sub :item-lister/get-body-props get-body-props)

(defn get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:description (metamorphic-content)}
  [db [_ extension-id item-namespace]]
  {:description (r get-description db extension-id item-namespace)})

(a/reg-sub :item-lister/get-view-props get-view-props)