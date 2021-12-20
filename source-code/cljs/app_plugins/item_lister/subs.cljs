
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.6.0
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.subs
    (:require [mid-fruits.candy      :refer [param return]]
              [mid-fruits.map        :as map]
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
  (get-in db [extension-id :lister-data]))

(defn get-meta-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ extension-id _ item-key]]
  (get-in db [extension-id :lister-meta item-key]))

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

(defn get-selected-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (r get-meta-value db extension-id item-namespace :selected-items))

(defn get-selected-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (let [selected-items (r get-selected-items db extension-id item-namespace)]
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
  (let [selected-items (r get-selected-items db extension-id item-namespace)]
       (vector/contains-item? selected-items item-dex)))

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
  (let [selected-items (r get-selected-items db extension-id item-namespace)]
       (vector/nonempty? selected-items)))

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

(defn get-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:downloaded-item-count (integer)
  ;   :download-limit (integer)
  ;   :filter (keyword)
  ;   :order-by (keyword)
  ;   :search-term (string)}
  [db [_ extension-id item-namespace]]
  {:downloaded-item-count (r get-downloaded-item-count db extension-id)
   :download-limit        (r get-meta-value            db extension-id item-namespace :download-limit)
   :filter                (r get-meta-value            db extension-id item-namespace :filter)
   :order-by              (r get-meta-value            db extension-id item-namespace :order-by)
   :search-term           (r get-search-term           db extension-id item-namespace)})

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
  ;   :filter-mode? (boolean)
  ;   :handle-archived-items? (boolean)
  ;   :handle-favorite-items? (boolean)
  ;   :no-items-to-show? (boolean)
  ;   :order-changed? (boolean)
  ;   :reorder-mode? (boolean)
  ;   :search-mode? (boolean)
  ;   :select-mode? (boolean)
  ;   :viewport-small? (boolean)}
  [db [_ extension-id item-namespace]]
        ; If select-mode is enabled ...
  (cond (r get-meta-value db extension-id item-namespace :select-mode?)
        {:select-mode?           true
         :all-items-selected?    (r all-items-selected? db extension-id item-namespace)
         :any-item-selected?     (r any-item-selected?  db extension-id item-namespace)
         :filter                 (r get-meta-value      db extension-id item-namespace :filter)
         :filter-mode?           (r get-meta-value      db extension-id item-namespace :filter-mode?)
         :handle-archived-items? (r get-meta-value      db extension-id item-namespace :handle-archived-items?)
         :handle-favorite-items? (r get-meta-value      db extension-id item-namespace :handle-favorite-items?)}
        ; If search-mode is enabled ...
        (r get-meta-value db extension-id item-namespace :search-mode?)
        {:search-mode? true}
        ; If reorder-mode is enabled ...
        (r get-meta-value      db extension-id item-namespace :reorder-mode?)
        {:reorder-mode?  true
         :order-changed? false}
        ; Use actions-mode as default ...
        :default
        {:actions-mode?          true
         :filter                 (r get-meta-value    db extension-id item-namespace :filter)
         :filter-mode?           (r get-meta-value    db extension-id item-namespace :filter-mode?)
         :handle-archived-items? (r get-meta-value    db extension-id item-namespace :handle-archived-items?)
         :handle-favorite-items? (r get-meta-value    db extension-id item-namespace :handle-favorite-items?)
         :no-items-to-show?      (r no-items-to-show? db extension-id)
         :viewport-small?        (r environment/viewport-small? db)}))

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
  ;   :disabled-items (integers in vector)
  ;   :no-items-to-show? (boolean)
  ;   :reorder-mode? (boolean)
  ;   :search-mode? (boolean)
  ;   :select-mode? (boolean)
  ;   :synchronized? (boolean)
  ;   :synchronizing? (boolean)}
  [db [_ extension-id item-namespace]]
        ; If select-mode is enabled ...
  (cond (r get-meta-value db extension-id item-namespace :select-mode?)
        {:select-mode?      true
         :downloaded-items  (r get-downloaded-items db extension-id)
         :no-items-to-show? (r no-items-to-show?    db extension-id)
         :synchronized?     (r synchronized?        db extension-id item-namespace)
         :synchronizing?    (r synchronizing?       db extension-id item-namespace)}
        ; If search-mode is enabled ...
        (r get-meta-value db extension-id item-namespace :search-mode?)
        {:search-mode?      true
         :downloaded-items  (r get-downloaded-items db extension-id)
         :no-items-to-show? (r no-items-to-show?    db extension-id)
         :synchronized?     (r synchronized?        db extension-id item-namespace)
         :synchronizing?    (r synchronizing?       db extension-id item-namespace)}
        ; If reorder-mode is enabled ...
        (r get-meta-value db extension-id item-namespace :reorder-mode?)
        {:reorder-mode?     true
         :downloaded-items  (r get-downloaded-items db extension-id)
         :no-items-to-show? (r no-items-to-show?    db extension-id)
         :synchronized?     (r synchronized?        db extension-id item-namespace)
         :synchronizing?    (r synchronizing?       db extension-id item-namespace)}
        ; Use actions-mode as default ...
        :default
        {:actions-mode?     true
         :downloaded-items  (r get-downloaded-items db extension-id)
         :no-items-to-show? (r no-items-to-show?    db extension-id)
         :synchronized?     (r synchronized?        db extension-id item-namespace)
         :synchronizing?    (r synchronizing?       db extension-id item-namespace)}))

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
